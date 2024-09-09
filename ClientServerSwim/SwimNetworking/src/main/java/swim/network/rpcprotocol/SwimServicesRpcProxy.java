package swim.network.rpcprotocol;

import swim.model.Contestant;
import swim.model.hbm.DeskUser;
import swim.model.Registration;
import swim.model.SwimmingEvent;
import swim.services.ISwimObserver;
import swim.services.ISwimServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class SwimServicesRpcProxy implements ISwimServices {
    private String host;
    private int port;

    private ISwimObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public SwimServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }

    public void login(DeskUser user, ISwimObserver client) throws Exception {
        initializeConnection();
        Request req = new Request.Builder().type(RequestType.LOGIN).data(user).build();
        sendRequest(req);
        System.out.println(req.toString());
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            this.client = client;
            return;
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new Exception(err);
        }
    }

    @Override
    public void logout(DeskUser user, ISwimObserver client) throws Exception {
        Request req=new Request.Builder().type(RequestType.LOGOUT).data(user).build();
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new Exception(err);
        }
    }

    @Override
    public DeskUser getDeskUserByEmail(String email) {
        return null;
    }


    public List<SwimmingEvent> findAllSwimmingEvents() throws Exception {
        Request req = new Request.Builder().type(RequestType.GET_EVENTS).data("null").build();
        System.out.println(req.toString());
        sendRequest(req);
        Response response = readResponse();
        System.out.println(response.toString());
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new Exception(err);
        }
        System.out.println(response.toString());
        List<SwimmingEvent> swimmingEvents = (List<SwimmingEvent>) response.data();
        return swimmingEvents;
    }

    public List<Contestant> findAllContestants() throws Exception {
        Request req = new Request.Builder().type(RequestType.GET_CONTESTANTS).data("null").build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new Exception(err);
        }
        List<Contestant> contestants = (List<Contestant>) response.data();
        return contestants;
    }

    public void addRegistration(Registration registration) throws Exception {
        Request req = new Request.Builder().type(RequestType.ADD_REGISTRATION).data(registration).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new Exception(err);
        }
    }

    public int getNumberOfContestants(SwimmingEvent swimmingEvent) throws Exception {
        Request req = new Request.Builder().type(RequestType.GET_CONTESTANTS_NUMBER).data(swimmingEvent).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new Exception(err);
        }
        int nr = (int) response.data();
        return nr;
    }

    public List<Contestant> getSubmittedContestants(SwimmingEvent swimmingEvent) throws Exception{
        Request req = new Request.Builder().type(RequestType.GET_SUBMITTED_CONTESTANTS).data(swimmingEvent).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new Exception(err);
        }
        List<Contestant> contestants = (List<Contestant>) response.data();
        return contestants;
    }

    public List<SwimmingEvent> getSubmittedEvents(Contestant contestant) throws Exception{
        Request req = new Request.Builder().type(RequestType.GET_SUBMITTED_EVENTS).data(contestant).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new Exception(err);
        }
        List<SwimmingEvent> events = (List<SwimmingEvent>) response.data();
        return events;
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request) throws Exception {
        try {
            System.out.println("voi trimite");
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new Exception("Error sending object " + e);
        }

    }

    private Response readResponse() throws Exception {
        System.out.println("RASPUNS");
        Response response = null;
        try {

            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws Exception {
        try {
            connection = new Socket(host, port);
            System.out.println(connection.getOutputStream());
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(Response response) {
        if (response.type() == ResponseType.UPDATE_REGISTRATIONS) {

            Registration registration = (Registration) response.data();
            System.out.println("New registration " + registration.toString());
            try {
                client.registrationAdded(registration);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response) {
        return response.type() == ResponseType.UPDATE_REGISTRATIONS;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    if (isUpdate((Response) response)) {
                        handleUpdate((Response) response);
                    } else {

                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
