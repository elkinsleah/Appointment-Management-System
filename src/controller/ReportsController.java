package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for Reports Screen
 * */
public class ReportsController implements Initializable {

        public ComboBox<String> contactComboBox;
        public TableColumn<Customer, String> custCol;
        public TableColumn<Customer, Integer> custIdCol;
        public TableColumn<Appointment, String> descriptionCol;
        public TableColumn<Appointment, String> endDateTimeCol;
        public TableColumn<Appointment, Integer> hoursCol;
        public TableColumn<Appointment, Integer> idCol;
        public TableColumn<Appointment, String> monthCol;
        public TableView reportOneTableView;
        public TableView reportThreeTableView;
        public TableView reportTwoTableView;

        public Button returnButton;
        public TableColumn<Appointment, String> startDateTimeCol;
        public TableColumn<Appointment, String> titleCol;
        public TableColumn<Appointment, Integer> totalCol;
        public TableColumn<Appointment, String> typeApptCol;
        public TableColumn<Appointment, String> typeContactCol;
        private ObservableList<FirstReport> allFirstReport = FXCollections.observableArrayList();
        private ObservableList<Appointment> displayAppointments = FXCollections.observableArrayList();
        private ObservableList<ThirdReport> allThirdReport = FXCollections.observableArrayList();
        private AppointmentCalendar appointmentCalendar = null;
        private Directory directory = null;

        /**
         * Takes the user back to the Main Screen.
         * @param event action event when the return button is clicked.
         * */
        public void onActionReturnButton(ActionEvent event) throws IOException {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("Main Screen");
                alert.setHeaderText("Return to Main Screen?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                        Parent parent = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                        Scene scene = new Scene(parent);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                }
        }

        /**
         * Populates the table data for the First Report.
         * First Report is the number of appointments by type and month.
         * */
        public void populateFirstReport() {

                ObservableList<Appointment> allAppointments = appointmentCalendar.getAllAppointments();

                Appointment appointment = allAppointments.get(0);
                int month = appointment.getStartDateTimeLocal().getMonth().getValue();
                int year = appointment.getStartDateTimeLocal().getYear();
                String type = appointment.getType();
                allFirstReport.add(new FirstReport(month, year, type));

                for (int i = 1; i < allAppointments.size(); ++i) {
                        appointment = allAppointments.get(i);
                        month = appointment.getStartDateTimeLocal().getMonth().getValue();
                        year = appointment.getStartDateTimeLocal().getYear();
                        type = appointment.getType();

                        boolean counted = false;
                        for (FirstReport firstReport : allFirstReport) {
                                if (firstReport.getMonth() == month && firstReport.getYear() == year && firstReport.getType().equals(type)) {
                                        firstReport.incrementCount();
                                        counted = true;
                                        break;
                                }
                        }
                        if (!counted) {
                                allFirstReport.add(new FirstReport(month, year, type));
                        }
                }
        }

        /**
         * Populates the table data for the Third Report.
         * Third Report is the total appointment hours per Customer.
         * */
        public void populateThirdReport() {

                ObservableList<Appointment> allAppointments = appointmentCalendar.getAllAppointments();

                Appointment appointment = allAppointments.get(0);
                LocalDateTime start = appointment.getStartDateTimeLocal();
                LocalDateTime end = appointment.getEndDateTimeLocal();

                int customerID = appointment.getCustomerID();
                String customerName = directory.getCustomer(customerID).getName();
                double hours = Duration.between(start, end).toMinutes() / 60.0;

                allThirdReport.add(new ThirdReport(customerID, customerName, hours));
                for (int i = 1; i < allAppointments.size(); ++i) {
                        appointment = allAppointments.get(i);
                        start = appointment.getStartDateTimeLocal();
                        end = appointment.getEndDateTimeLocal();

                        customerID = appointment.getCustomerID();
                        hours = Duration.between(start, end).toMinutes() / 60.0;

                        boolean counted = false;
                        for (ThirdReport thirdReport : allThirdReport) {
                               if (thirdReport.getId() == customerID) {
                                        thirdReport.addHours(hours);
                                        counted = true;
                                        break;
                                }
                        }
                        if (!counted) {
                                customerName = directory.getCustomer(customerID).getName();
                                allThirdReport.add(new ThirdReport(customerID, customerName, hours));
                        }
                }

                for (Customer customer : directory.getAllCustomers()) {
                        boolean counted = false;
                        for (ThirdReport thirdReport : allThirdReport) {
                                if (customer.getId() == thirdReport.getId()) {
                                        counted = true;
                                        break;
                                }
                        }
                        if (!counted) {
                                allThirdReport.add(new ThirdReport(customer.getId(), customer.getName(), 0));
                        }
                }
        }

        /**
         * Populates the table data for the Second Report depending on the selected Contact.
         * Second Report is the appointment schedule for the selected Contact.
         * */
        public void onActionSelectContact(ActionEvent event) {

                int contactIndex = contactComboBox.getItems().indexOf(contactComboBox.getValue());
                int contactID = directory.getAllContacts().get(contactIndex).getId();
                displayAppointments.clear();

                appointmentCalendar.getAllAppointments().forEach((n) -> {if (n.getContactID() == contactID) displayAppointments.add(n);});
        }

        /**
         * Initializes the Reports Table Views.
         * Appointments are retrieved from the database by a new Appointment Calendar.
         * Contacts, Customers, Users, and Countries are retrieved from the database by a new Directory.
         * <p>
         * Lambda Expression is used to populate the Contact Combo Box. ForEach is used on allContacts to populate the combo box.
         * </p>
         * */
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

                appointmentCalendar = new AppointmentCalendar();
                directory = new Directory();

                ObservableList<String> contactNames = FXCollections.observableArrayList();
                directory.getAllContacts().forEach((n) -> contactNames.add(n.getName()));
                contactComboBox.getItems().addAll(contactNames);

                populateFirstReport();
                reportOneTableView.setItems(allFirstReport);
                monthCol.setCellValueFactory(new PropertyValueFactory<>("monthYear"));
                typeApptCol.setCellValueFactory(new PropertyValueFactory<>("type"));
                totalCol.setCellValueFactory(new PropertyValueFactory<>("count"));

                appointmentCalendar = new AppointmentCalendar();
                displayAppointments = appointmentCalendar.getAllAppointments();

                reportTwoTableView.setItems(displayAppointments);
                idCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
                titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
                descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
                typeContactCol.setCellValueFactory(new PropertyValueFactory<>("type"));
                startDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
                endDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
                custIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));

                populateThirdReport();
                reportThreeTableView.setItems(allThirdReport);
                custCol.setCellValueFactory(new PropertyValueFactory<>("name"));
                hoursCol.setCellValueFactory(new PropertyValueFactory<>("hours"));

                reportThreeTableView.getSortOrder().add(custCol);
                reportThreeTableView.getSortOrder().clear();
        }
}