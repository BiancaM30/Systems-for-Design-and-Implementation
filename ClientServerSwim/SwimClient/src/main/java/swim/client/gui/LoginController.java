package swim.client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import swim.model.hbm.DeskUser;
import swim.services.ISwimServices;

import java.io.IOException;


public class LoginController {

    private ISwimServices server;
    private MainController mainController;
    private DeskUser crtUser;

    @FXML
    TextField emailTextField;

    @FXML
    TextField passwordTextField;

    Parent mainSwimParent;

    public void setServer(ISwimServices s) {
        this.server = s;
    }

    public void setParent(Parent p) {
        mainSwimParent = p;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }



    public void clickOnLogin(ActionEvent actionEvent) throws IOException {
        String userEmail = emailTextField.getText();
        String userPassword = passwordTextField.getText();

        try {
            DeskUser crtUser = new DeskUser(userEmail, userPassword);
            server.login(crtUser, mainController);

            Stage stage = new Stage();
            stage.setTitle("Window for " + crtUser.getEmail());
            stage.setScene(new Scene(mainSwimParent));

            stage.show();
            mainController.setUser(crtUser);
            mainController.populateTable();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();


        } catch (Exception ex) {
            emailTextField.setText("");
            passwordTextField.setText("");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Invalid credentials");
            alert.setHeaderText("Invalid email or password!");
            alert.setContentText("Do you want to try again?");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
    }


}