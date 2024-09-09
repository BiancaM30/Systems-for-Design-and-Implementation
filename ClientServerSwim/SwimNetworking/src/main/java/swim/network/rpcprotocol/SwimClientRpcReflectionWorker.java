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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;


public class SwimClientRpcReflectionWorker implements Runnable, ISwimObserver {
    private ISwimServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();
    //  private static Response errorResponse=new Response.Builder().type(ResponseType.ERROR).build();

    public SwimClientRpcReflectionWorker(ISwimServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    public void registrationAdded(Registration registration) {
        Response resp = new Response.Builder().type(ResponseType.UPDATE_REGISTRATIONS).data(registration).build();
        System.out.println("Added Registration " + registration.toString());
        try {
            sendResponse(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response handleRequest(Request request) {
        Response response = null;
        String handlerName = "handle" + (request).type();
        System.out.println("HandlerName " + handlerName);
        try {
            Method method = this.getClass().getDeclaredMethod(handlerName, Request.class);
            response = (Response) method.invoke(this, request);
            System.out.println("Method " + handlerName + " invoked");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return response;
    }

    private Response handleLOGIN(Request request) {
        System.out.println("Login request ..." + request.type());
        DeskUser user = (DeskUser) request.data();
        try {
            server.login(user, this);
            return okResponse;
        } catch (Exception e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleLOGOUT(Request request) {
        System.out.println("Logout request...");
        DeskUser user = (DeskUser) request.data();
        try {
            server.logout(user, this);
            connected = false;
            return okResponse;

        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_DESKUSER_BY_EMAIL(Request request) {
        System.out.println("Get deskUser by email Request ...");
        String email = (String) request.data();
        try {
            DeskUser user = server.getDeskUserByEmail(email);
            return new Response.Builder().type(ResponseType.GET_DESKUSER_BY_EMAIL).data(user).build();
        } catch (Exception e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_EVENTS(Request request) {
        System.out.println("Get SwimmingEvents Request ...");
        try {
            List<SwimmingEvent> events = server.findAllSwimmingEvents();
            return new Response.Builder().type(ResponseType.GET_EVENTS).data(events).build();
        } catch (Exception e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_CONTESTANTS(Request request) {
        System.out.println("Get Contestants Request ...");
        try {
            List<Contestant> contestants = server.findAllContestants();
            return new Response.Builder().type(ResponseType.GET_CONTESTANTS).data(contestants).build();
        } catch (Exception e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_SUBMITTED_CONTESTANTS(Request request) {
        System.out.println("Get contestants submitted for a specific swimEvent Request ...");
        SwimmingEvent swEvent = (SwimmingEvent) request.data();
        try {
            List<Contestant> contestants = server.getSubmittedContestants(swEvent);
            return new Response.Builder().type(ResponseType.GET_SUBMITTED_CONTESTANTS).data(contestants).build();
        } catch (Exception e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_SUBMITTED_EVENTS(Request request) {
        System.out.println("Get all submissions of a specific contestants Request ...");
        Contestant contestant = (Contestant) request.data();
        try {
            List<SwimmingEvent> swimmingEvents = server.getSubmittedEvents(contestant);
            return new Response.Builder().type(ResponseType.GET_SUBMITTED_EVENTS).data(swimmingEvents).build();
        } catch (Exception e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleADD_REGISTRATION(Request request) {
        System.out.println("Add registration Request ...");
        Registration registration = (Registration) request.data();
        try {
            server.addRegistration(registration);
            return okResponse;
        } catch (Exception e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_CONTESTANTS_NUMBER(Request request) {
        System.out.println("Get contestants number on a specific swimmingEvent Request ...");
        SwimmingEvent event = (SwimmingEvent) request.data();
        try {
            int nr = server.getNumberOfContestants(event);
            return new Response.Builder().type(ResponseType.GET_CONTESTANTS_NUMBER).data(nr).build();
        } catch (Exception e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }


    private void sendResponse(Response response) throws IOException {
        System.out.println("sending response " + response);
        output.writeObject(response);
        output.flush();
    }


}
