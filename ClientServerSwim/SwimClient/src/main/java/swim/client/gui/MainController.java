package swim.client.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import swim.client.gui.dto.ContestantDTO;
import swim.client.gui.dto.SwimmingEventDTO;
import swim.model.Contestant;
import swim.model.hbm.DeskUser;
import swim.model.Registration;
import swim.model.SwimmingEvent;
import swim.services.ISwimObserver;
import swim.services.ISwimServices;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements ISwimObserver, Initializable {

    private ISwimServices server;
    private DeskUser user;
    ObservableList<SwimmingEventDTO> modelEventsTab1 = FXCollections.observableArrayList();
    ObservableList<SwimmingEvent> modelEventsTab2 = FXCollections.observableArrayList();
    ObservableList<Contestant> modelContestantsTab3 = FXCollections.observableArrayList();
    ObservableList<SwimmingEvent> modelEventsTab3 = FXCollections.observableArrayList();

    public MainController() {
        System.out.println("Constructor MainController");
    }

    public MainController(ISwimServices server) {
        this.server = server;
        System.out.println("Constructor MainController cu server param");

    }

    private List<SwimmingEventDTO> getAllEventsDto() {
        try {
            List<SwimmingEvent> events = server.findAllSwimmingEvents();
            List<SwimmingEventDTO> swList = new ArrayList<>();
            for (SwimmingEvent e : events) {
                swList.add(new SwimmingEventDTO(e.getID(), e.getDistance(), e.getStyle(), server.getNumberOfContestants(e)));
            }
            return swList;
        } catch (Exception ex) {

        }
        return null;
    }

    private List<ContestantDTO> getAllContestantsDto(SwimmingEvent sw) {
        try {
            List<Contestant> contestants = server.getSubmittedContestants(sw);
            List<ContestantDTO> contestantList = new ArrayList<>();
            for (Contestant c : contestants) {
                contestantList.add(new ContestantDTO(c.getID(), c.getName(), c.getAge(), server.getSubmittedEvents(c)));
            }
            return contestantList;
        } catch (Exception ex) {

        }
        return null;
    }

    public void populateTable() {
        //tab1
        modelEventsTab1.setAll(getAllEventsDto());
        tableViewEventsTab1.setItems(modelEventsTab1);

        //tab2
        try {
            List<SwimmingEvent> allEvents = server.findAllSwimmingEvents();
            modelEventsTab2.setAll(allEvents);
            tableViewEventsTab2.setItems(modelEventsTab2);
        } catch (Exception ex) {

        }

        //tab3
        try {
            List<SwimmingEvent> allEvents = server.findAllSwimmingEvents();
            modelEventsTab3.setAll(allEvents);
            tableViewEventsTab3.setItems(modelEventsTab3);
            List<Contestant> allContestants = server.findAllContestants();
            modelContestantsTab3.setAll(allContestants);
            tableViewContestantsTab3.setItems(modelContestantsTab3);
        } catch (Exception ex) {

        }

    }

    public void setServer(ISwimServices s) {
        server = s;
    }

    public void setUser(DeskUser s) {
        user = s;
    }

    //tab1
    @FXML
    TableColumn<SwimmingEventDTO, String> tableColumnDistanceTab1;
    @FXML
    TableColumn<SwimmingEventDTO, String> tableColumnStyleTab1;
    @FXML
    TableColumn<SwimmingEventDTO, Integer> tableColumnNumberTab1;
    @FXML
    TableView<SwimmingEventDTO> tableViewEventsTab1;

    //tab2
    @FXML
    TableColumn<SwimmingEvent, String> tableColumnDistanceTab2;
    @FXML
    TableColumn<SwimmingEvent, String> tableColumnStyleTab2;
    @FXML
    TableView<SwimmingEvent> tableViewEventsTab2;

    @FXML
    TableColumn<ContestantDTO, Integer> tableColumnIdTab2;
    @FXML
    TableColumn<ContestantDTO, String> tableColumnNameTab2;
    @FXML
    TableColumn<ContestantDTO, Integer> tableColumnAgeTab2;
    @FXML
    TableColumn<ContestantDTO, String> tableColumnRegistrationsTab2;
    @FXML
    TableView<ContestantDTO> tableViewContestantsTab2;

    //tab3
    @FXML
    TableColumn<Contestant, Integer> tableColumnIdTab3;
    @FXML
    TableColumn<Contestant, String> tableColumnNameTab3;
    @FXML
    TableColumn<Contestant, Integer> tableColumnAgeTab3;
    @FXML
    TableView<Contestant> tableViewContestantsTab3;

    @FXML
    TableColumn<SwimmingEvent, String> tableColumnDistanceTab3;
    @FXML
    TableColumn<SwimmingEvent, String> tableColumnStyleTab3;
    @FXML
    TableView<SwimmingEvent> tableViewEventsTab3;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //tab1
        tableColumnDistanceTab1.setCellValueFactory(new PropertyValueFactory<SwimmingEventDTO, String>("distance"));
        tableColumnStyleTab1.setCellValueFactory(new PropertyValueFactory<SwimmingEventDTO, String>("style"));
        tableColumnNumberTab1.setCellValueFactory(new PropertyValueFactory<SwimmingEventDTO, Integer>("noContestants"));

        //tab2
        tableColumnDistanceTab2.setCellValueFactory(new PropertyValueFactory<SwimmingEvent, String>("distance"));
        tableColumnStyleTab2.setCellValueFactory(new PropertyValueFactory<SwimmingEvent, String>("style"));
        tableColumnIdTab2.setCellValueFactory(new PropertyValueFactory<ContestantDTO, Integer>("id"));
        tableColumnNameTab2.setCellValueFactory(new PropertyValueFactory<ContestantDTO, String>("name"));
        tableColumnAgeTab2.setCellValueFactory(new PropertyValueFactory<ContestantDTO, Integer>("age"));
        tableColumnRegistrationsTab2.setCellValueFactory(new PropertyValueFactory<ContestantDTO, String>("registrations"));

        //tab3
        tableColumnIdTab3.setCellValueFactory(new PropertyValueFactory<Contestant, Integer>("id"));
        tableColumnNameTab3.setCellValueFactory(new PropertyValueFactory<Contestant, String>("name"));
        tableColumnAgeTab3.setCellValueFactory(new PropertyValueFactory<Contestant, Integer>("age"));
        tableColumnDistanceTab3.setCellValueFactory(new PropertyValueFactory<SwimmingEvent, String>("distance"));
        tableColumnStyleTab3.setCellValueFactory(new PropertyValueFactory<SwimmingEvent, String>("style"));

    }

    //tab2
    @FXML
    public void onSelectionRow() {
        List<ContestantDTO> result = this.getAllContestantsDto(tableViewEventsTab2.getSelectionModel().getSelectedItem());
        tableViewContestantsTab2.getItems().clear();
        tableViewContestantsTab2.getItems().setAll(result);
    }

    //tab3
    private SwimmingEvent getSelectedSwimEvent() {
        return tableViewEventsTab3.getSelectionModel().getSelectedItem();
    }

    private Contestant getSelectedContestant() {
        return tableViewContestantsTab3.getSelectionModel().getSelectedItem();
    }

    public void clickOnRegister(ActionEvent event) {
        try {
            server.addRegistration(new Registration(getSelectedContestant(), getSelectedSwimEvent()));
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Registration added!");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(e.getMessage());
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
    }


    @Override
    public void registrationAdded(Registration registration) throws Exception {
        Platform.runLater(() -> {
            try {
                modelEventsTab1.setAll(getAllEventsDto());
                tableViewEventsTab1.setItems(modelEventsTab1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    //logout
    public void clickOnLogout(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Do you want to continue?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                server.logout(user, this);
                ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
            }
            catch (Exception ex){

            }
        }

    }



}
