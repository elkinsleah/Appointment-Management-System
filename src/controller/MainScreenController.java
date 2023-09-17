package controller;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Node;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for Main Screen
 * */
public class MainScreenController implements Initializable {

        public Button customersButton;
        public Button deleteButton;
        public RadioButton allRadio;
        public TableColumn<Appointment, Integer> apptIdCol;
        public TableColumn<Appointment, String> contactCol;
        public TableColumn<Appointment, Integer> custIdCol;
        public TableColumn<Appointment, String> descriptionCol;
        public TableColumn<Appointment, String> endDateTimeCol;
        public TableColumn<Appointment, String> locationCol;
        public Button logoutButton;
        public TableView<Appointment> mainCalendarTableView;
        public RadioButton monthlyRadio;
        public Button reportsButton;
        public TableColumn<Appointment, String> startDateTimeCol;
        public TableColumn<Appointment, String> titleCol;
        public TableColumn<Appointment, String> typeCol;
        public Button updateAppointmentButton;
        public TableColumn<Appointment, Integer> userIdCol;
        public RadioButton weeklyRadio;
        public ToggleGroup calendarRadio;
        private ObservableList<model.Appointment> displayAppointments = FXCollections.observableArrayList();
        private AppointmentCalendar appointmentCalendar = null;

        /**
         * Takes user to the Add Appointment Screen.
         * Adds a new appointment to the Appointment Calendar Table.
         * @param event action event when the Add button is clicked.
         * Error message is displayed if appointment could not be added.
         * */
        public void onActionAddAppointment(ActionEvent event) {

                try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddAppointment.fxml"));
                        Parent mainScreenParent;
                        mainScreenParent = loader.load();
                        Scene mainScreenScene = new Scene(mainScreenParent);
                        Stage mainStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        mainStage.setScene(mainScreenScene);
                        mainStage.show();

                        AddAppointmentController controller = loader.getController();
                        controller.addMode();

                } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Add Appointment Error");
                        alert.setHeaderText("Add Appointment Failed");
                        alert.setContentText("Could not add appointment. Please check database connection and restart the program.");
                        alert.showAndWait();
                }
        }

        /**
         * Takes the user to the Customer Screen.
         * @param event action event when the Customers button is clicked.
         * */
        public void onActionCustomersButton(ActionEvent event) throws IOException {

                Parent parent = FXMLLoader.load(getClass().getResource("/view/Customers.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
        }

        /**
         * Deletes the selected appointment from the database.
         * @param event action event when the delete button is clicked.
         * Displays an error message if no appointment is selected to delete.
         * Popup including the appointment ID and type confirms before deletion.
         * Error message is displayed if appointment could not be deleted.
         * */
        public void onActionDeleteButton(ActionEvent event) throws IOException {

                Appointment selectedAppointment = mainCalendarTableView.getSelectionModel().getSelectedItem();
                if (mainCalendarTableView.getSelectionModel().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.initModality(Modality.NONE);
                        alert.setTitle("Delete Error");
                        alert.setHeaderText("No appointment selected");
                        alert.setContentText("No appointment was selected to delete");
                        alert.showAndWait();
                } else {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Delete Appointment");
                        alert.setHeaderText("ID: " + selectedAppointment.getAppointmentID() + "\nType: " + selectedAppointment.getType());
                        alert.setContentText("Are you sure you want to delete this appointment?");

                        Optional<ButtonType> result = alert.showAndWait();
                        if(result.get() == ButtonType.OK) {
                                if (appointmentCalendar.deleteAppointment(selectedAppointment)) {
                                        if (calendarRadio.getSelectedToggle() == allRadio) {
                                                onActionAllRadio(event);
                                        } else if (calendarRadio.getSelectedToggle() == monthlyRadio) {
                                                onActionMonthlyRadio(event);
                                        } else if (calendarRadio.getSelectedToggle() == weeklyRadio) {
                                                onActionWeeklyRadio(event);
                                        }
                                } else {
                                       alert = new Alert(Alert.AlertType.ERROR);
                                       alert.setTitle("Delete Appointment Error");
                                       alert.setHeaderText("Delete Appointment Failed");
                                       alert.setContentText("Could not delete appointment. Please check database connection and restart the program.");
                                }
                        }
                }
        }

        /**
         * Displays the appointments in the database by month.
         * @param event action event when the view by month radio button is clicked.
         * */
        public void onActionMonthlyRadio(ActionEvent event) {

                displayAppointments.clear();

                ObservableList<Appointment> allAppointments = appointmentCalendar.getAllAppointments();
                Month thisMonth = LocalDate.now().getMonth();
                allAppointments.stream().filter(n -> n.getStartDateTimeLocal().getMonth() == thisMonth)
                               .forEach(n -> displayAppointments.add(n));
        }

        /**
         * Displays the appointments in the database by week.
         * @param event action event when the view by week radio button is clicked.
         * */
        public void onActionWeeklyRadio(ActionEvent event) {
                displayAppointments.clear();
                int dayOfWeek = LocalDate.now().getDayOfWeek().getValue();
                LocalDate monday = LocalDate.now().minusDays(dayOfWeek - 1);
                LocalDate sunday = monday.plusDays(6);

                for (Appointment appointment : appointmentCalendar.getAllAppointments()) {
                        LocalDate appointmentStartDate = appointment.getStartDateTimeLocal().toLocalDate();
                        LocalDate appointmentEndDate = appointment.getEndDateTimeLocal().toLocalDate();
                        if (appointmentStartDate.compareTo(monday) * appointmentStartDate.compareTo(sunday) <= 0) {
                                displayAppointments.add(appointment);
                        } else if (appointmentEndDate.compareTo(monday) * appointmentEndDate.compareTo(sunday) <= 0) {
                                displayAppointments.add(appointment);
                        }
                }
        }

        /**
         * Displays all the appointments in the database.
         * @param event action event when the view all appointments radio button is clicked.
         * */
        public void onActionAllRadio(ActionEvent event) {
                displayAppointments = appointmentCalendar.getAllAppointments();
                mainCalendarTableView.setItems(displayAppointments);
        }

        /**
         * Logs the user out of the application.
         * @param event action event when the logout button is clicked.
         * */
        public void onActionLogoutButton(ActionEvent event) throws IOException {

                JDBC.closeConnection();
                Parent parent = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
        }

        /**
         * Takes the user to the Reports Screen.
         * @param event action event when the reports button is clicked.
         * */
        public void onActionReports(ActionEvent event) throws IOException {

                Parent parent = FXMLLoader.load(getClass().getResource("/view/Reports.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
        }

        /**
         * Takes user to the Update Appointment Screen.
         * Transfers the content of the selected appointment to the Update Appointment Screen.
         * @param event action event when the update button is clicked.
         * Error message is displayed if no appointment is selected.
         * */
        public void onActionUpdateAppointment(ActionEvent event) throws IOException {

                Appointment selectedAppointment = mainCalendarTableView.getSelectionModel().getSelectedItem();
                if (mainCalendarTableView.getSelectionModel().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.initModality(Modality.NONE);
                        alert.setTitle("Update Error");
                        alert.setHeaderText("No appointment selected");
                        alert.setContentText("No appointment was selected to update");
                        alert.showAndWait();

                } else {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateAppointment.fxml"));
                        Parent mainScreenParent;
                        mainScreenParent = loader.load();
                        Scene mainScreenScene = new Scene(mainScreenParent);
                        Stage mainStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        mainStage.setScene(mainScreenScene);
                        mainStage.show();

                        UpdateAppointmentController controller = loader.getController();
                        controller.updateMode(selectedAppointment);
                }
        }

        /**
         * Initializes the Appointment Main Calendar Table View.
         * Appointments are retrieved from the database by a new Appointment Calendar.
         * The radio button for displaying all appointments is automatically selected.
         * */
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

                calendarRadio = new ToggleGroup();
                this.weeklyRadio.setToggleGroup(calendarRadio);
                this.monthlyRadio.setToggleGroup(calendarRadio);
                this.allRadio.setToggleGroup(calendarRadio);
                this.allRadio.setSelected(true);

                appointmentCalendar = new AppointmentCalendar();

                displayAppointments = appointmentCalendar.getAllAppointments();
                mainCalendarTableView.setItems(displayAppointments);

                apptIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
                titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
                descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
                locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
                contactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
                typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
                startDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
                endDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
                custIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
                userIdCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        }
}


