import swim.model.hbm.DeskUser;
import swim.network.utils.AbstractServer;
import swim.network.utils.ServerException;
import swim.network.utils.SwimRpcConcurrentServer;
import swim.persistance.db.ContestantDBRepository;
import swim.persistance.db.DeskUserDBRepository;
import swim.persistance.db.RegistrationDBRepository;
import swim.persistance.db.SwimmingEventDBRepository;
import swim.persistance.interfaces.ContestantRepository;
import swim.persistance.interfaces.DeskUserRepository;
import swim.persistance.interfaces.RegistrationRepository;
import swim.persistance.interfaces.SwimmingEventRepository;
import swim.persistance.orm.DeskUserORMRepository;
import swim.persistance.orm.DeskUserORMRepository2;
import swim.server.SwimServicesImpl;
import swim.services.ISwimServices;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort = 55555;

    public static void main(String[] args) {
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/swimserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find swimserver.properties " + e);
            return;
        }

        //DeskUserRepository deskUserORMRepository = new DeskUserORMRepository();


        DeskUserRepository deskUserORMRepository = new DeskUserORMRepository2();

//        ADD
//        DeskUser d = new DeskUser("test@yahoo.com", "test");
//        deskUserORMRepository.add(d);
//
//        UPDATE
//        deskUserORMRepository.update(new DeskUser("test2","test"),4);

//        DELETE
//        d.setID(4);
//        deskUserORMRepository.delete(d);

//        FINDALL()
//        List<DeskUser> lista = (List<DeskUser>) deskUserORMRepository.findAll();
//        for (DeskUser elem : lista) {
//            System.out.println(elem.toString());
//        }

//        FIND BY EMAIL
//        System.out.println(deskUserORMRepository.findByEmail("radu@yahoo.com").toString());

//        FIND BY ID
//        System.out.println(deskUserORMRepository.findById(1).toString());

        ContestantRepository contestantRepository = new ContestantDBRepository(serverProps);
        //DeskUserRepository deskUserRepository = new DeskUserDBRepository(serverProps);
        RegistrationRepository registrationRepository = new RegistrationDBRepository(serverProps);
        SwimmingEventRepository swimmingEventRepository = new SwimmingEventDBRepository(serverProps);
        ISwimServices swimServerImpl = new SwimServicesImpl(contestantRepository, deskUserORMRepository, registrationRepository, swimmingEventRepository);
        int swimServerPort = defaultPort;

        try {
            swimServerPort = Integer.parseInt(serverProps.getProperty("swim.server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong  Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + swimServerPort);
        AbstractServer server = new SwimRpcConcurrentServer(swimServerPort, swimServerImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        } finally {
            try {
                server.stop();
            } catch (ServerException e) {
                System.err.println("Error stopping server " + e.getMessage());
            }
        }
    }
}
