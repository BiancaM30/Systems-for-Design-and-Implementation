package swim.client;

import javafx.scene.Scene;
import swim.client.gui.*;
import swim.network.rpcprotocol.SwimServicesRpcProxy;
import swim.services.ISwimServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;


public class StartRpcClientFX extends Application {
    private Stage primaryStage;

    private static int defaultSwimPort = 55555;
    private static String defaultServer = "localhost";


    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClientFX.class.getResourceAsStream("/swimclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find swimclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("swim.server.host", defaultServer);
        int serverPort = defaultSwimPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("swim.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultSwimPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        ISwimServices server = new SwimServicesRpcProxy(serverIP, serverPort);


        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("login.fxml"));
        Parent root=loader.load();

        LoginController ctrl =
                loader.<LoginController>getController();
        ctrl.setServer(server);


        FXMLLoader cloader = new FXMLLoader(
                getClass().getClassLoader().getResource("main.fxml"));
        Parent croot=cloader.load();


        MainController swimCtrl =
                cloader.<MainController>getController();
        swimCtrl.setServer(server);


        ctrl.setMainController(swimCtrl);
        ctrl.setParent(croot);

        primaryStage.setTitle("Swimming Contests");
        primaryStage.setWidth(400);
        primaryStage.setHeight(550);
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 300, 130));
        primaryStage.show();


    }


}


