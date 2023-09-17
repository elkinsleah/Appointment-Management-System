package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.Node;

/**
 * Controller for Add Appointment Screen
 * */
public class AddAppointmentController implements Initializable {

        public Button saveButton;
        public Button cancelButton;
        public ComboBox<String> contactComboBox;
        public ComboBox<String> custComboBox;
        public TextField descriptionField;
        public ComboBox<String> endComboBox;
        public DatePicker endDatePicker;
        public TextField idField;
        public TextField locationField;
        public ComboBox<String> startComboBox;
        public DatePicker startDatePicker;
        public TextField titleField;
        public TextField typeField;
        public ComboBox<String> userComboBox;
        public Button returnButton;
        private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a");
        private AppointmentCalendar appointmentCalendar = null;
        private Directory directory = null;
        public Label timeErrorLabel;
        public Label blankFieldErrorLabel;
        private enum MODE {ADD, UPDATE}
        private MODE mode;

        /**
         * ComboBoxes for start and end times are defaulted to the closest hour for start time and half hour for end time to current time.
         * DatePickers are defaulted to current date.
         * */
        public void addMode() {

                mode = MODE.ADD;
                startDatePicker.setValue(LocalDate.now());
                endDatePicker.setValue(LocalDate.now());

                int hour = LocalTime.now().getHour();
                LocalTime defaultStartTime;
                LocalTime defaultEndTime;

                if (LocalTime.now().getMinute() <30) {
                        defaultStartTime = LocalTime.of(hour, 30);
                        ++hour;
                        defaultEndTime = LocalTime.of(hour, 0);
                } else {
                        ++hour;
                        defaultStartTime = LocalTime.of(hour, 0);
                        defaultEndTime = LocalTime.of(hour, 30);
                }

                startComboBox.setValue(defaultStartTime.format(timeFormat));
                endComboBox.setValue(defaultEndTime.format(timeFormat));
        }

        /**
         * Appointment is updated in the database.
         * All fields are set to the values of updateAppointment.
         * @param updateAppointment the selected appointment to be updated.
         * */
        public void updateMode(Appointment updateAppointment) {

                mode = MODE.UPDATE;

                idField.setText(String.valueOf(updateAppointment.getAppointmentID()));
                titleField.setText(updateAppointment.getTitle());
                descriptionField.setText(updateAppointment.getDescription());
                locationField.setText(updateAppointment.getLocation());
                typeField.setText(updateAppointment.getType());

                startDatePicker.setValue(updateAppointment.getStartDateTimeLocal().toLocalDate());
                endDatePicker.setValue(updateAppointment.getEndDateTimeLocal().toLocalDate());

                startComboBox.setValue(updateAppointment.getStartDateTimeLocal().toLocalTime().format(timeFormat));
                endComboBox.setValue(updateAppointment.getEndDateTimeLocal().toLocalTime().format(timeFormat));

                Customer customer = directory.getCustomer(updateAppointment.getCustomerID());
                User user = directory.getUser(updateAppointment.getUserID());
                Contact contact = directory.getContact(updateAppointment.getContactID());

                custComboBox.setValue(customer.getName());
                userComboBox.setValue(user.getName());
                contactComboBox.setValue(contact.getName());
        }

        /**
         * Saves the new Added Appointment.
         * Add Mode is used to create a new Appointment and add it to the database.
         * checkErrors is used to make sure fields are not empty.
         * Checks to make sure that Appointment times are within business hours.
         * Checks to make sure that Appointments are not overlapping.
         * @param event action event when the save button is clicked.
         * <p>
         * Lambda Expression is used to check if appointments are overlapping with the new Appointment that is created.
         * ForEach is used to search through already scheduled appointments.
         * Error message is displayed containing the Appointment ID, Start Time,
         * and End Time if new Appointment overlaps with an existing appointment.
         * </p>
         * */
        public void onActionSaveButton(ActionEvent event) throws IOException {

                String title = titleField.getText();
                String description = descriptionField.getText();
                String location = locationField.getText();
                String type = typeField.getText();

                LocalDate startDate = startDatePicker.getValue();
                LocalDate endDate = endDatePicker.getValue();
                LocalTime startTime = LocalTime.parse(startComboBox.getValue(), timeFormat);
                LocalTime endTime = LocalTime.parse(endComboBox.getValue(), timeFormat);
                LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
                LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

                boolean checkErrors = false;

                if (title.isEmpty()) {
                        checkErrors = true;
                        titleField.setStyle("-fx-border-color: rgb(255, 0, 0);");
                } else {
                        titleField.setStyle(null);
                }
                if (description.isEmpty()) {
                        checkErrors = true;
                        descriptionField.setStyle("-fx-border-color: rgb(255, 0, 0);");
                } else {
                        descriptionField.setStyle(null);
                }
                if (location.isEmpty()) {
                        checkErrors = true;
                        locationField.setStyle("-fx-border-color: rgb(255, 0, 0);");
                } else {
                        locationField.setStyle(null);
                }
                if (type.isEmpty()) {
                        checkErrors = true;
                        typeField.setStyle("-fx-border-color: rgb(255, 0, 0);");
                } else {
                        typeField.setStyle(null);
                }

                if (endDateTime.compareTo(startDateTime) <= 0) {
                        checkErrors = true;
                        startDatePicker.setStyle("-fx-border-color: rgb(255, 0, 0);");
                        endDatePicker.setStyle("-fx-border-color: rgb(255, 0, 0);");
                        startComboBox.setStyle("-fx-border-color: rgb(255, 0, 0);");
                        endComboBox.setStyle("-fx-border-color: rgb(255, 0, 0);");
                        timeErrorLabel.setVisible(true);
                } else {
                        startDatePicker.setStyle(null);
                        endDatePicker.setStyle(null);
                        startComboBox.setStyle(null);
                        endComboBox.setStyle(null);
                        timeErrorLabel.setVisible(false);
                }

                if (!AppointmentCalendar.insideBusinessHours(startDateTime, endDateTime)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Appointment Time Error");
                        alert.setHeaderText("Appointment is scheduled outside business hours: " +
                                "\nMonday through Friday 8:00 a.m. to 10:00 p.m. Eastern Time.\n" +
                                "\nStart Time: " + AppointmentCalendar.toZONE_ID(startDateTime).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)) +
                                "\nEnd Time: " + AppointmentCalendar.toZONE_ID(endDateTime).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)));
                        alert.showAndWait();
                        startDatePicker.setStyle("-fx-border-color: rgb(255, 0, 0);");
                        endDatePicker.setStyle("-fx-border-color: rgb(255, 0, 0);");
                        startComboBox.setStyle("-fx-border-color: rgb(255, 0, 0);");
                        endComboBox.setStyle("-fx-border-color: rgb(255, 0, 0);");
                        checkErrors = true;
                } else {
                        startDatePicker.setStyle(null);
                        endDatePicker.setStyle(null);
                        startComboBox.setStyle(null);
                        endComboBox.setStyle(null);
                }

                Appointment newAppointment = new Appointment();
                newAppointment.setTitle(title);
                newAppointment.setDescription(description);
                newAppointment.setLocation(location);
                newAppointment.setType(type);
                newAppointment.setStartDateTime(startDateTime);
                newAppointment.setEndDateTime(endDateTime);

                if (userComboBox.getValue() == null) {
                        checkErrors = true;
                        userComboBox.setStyle("-fx-border-color: rgb(255, 0, 0);");
                } else {
                        userComboBox.setStyle(null);
                }
                if (custComboBox.getValue() == null) {
                        checkErrors = true;
                        custComboBox.setStyle("-fx-border-color: rgb(255, 0, 0);");
                } else {
                        custComboBox.setStyle(null);

                        int userIndex = userComboBox.getItems().indexOf(userComboBox.getValue());
                        int userID = directory.getAllUsers().get(userIndex).getId();
                        newAppointment.setUserID(userID);

                        if (mode == MODE.UPDATE) newAppointment.setAppointmentID(Integer.parseInt(idField.getText()));

                        ObservableList<Appointment> overlap = FXCollections.observableArrayList();

                        appointmentCalendar.getAllAppointments().forEach((n) -> {
                                if (n.appointmentOverlap(newAppointment)) overlap.add(n);});
                        if (overlap.size() > 0) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Appointment Overlap Error");
                                StringBuilder headerText = new StringBuilder("Appointment overlaps with existing appointment: ");
                                for (Appointment appointment : overlap) {
                                        headerText.append("\n\nAppointment ID: ").append(appointment.getAppointmentID())
                                                .append("\nStart Time: ").append(appointment.getStartDateTime())
                                                .append("\nEnd Time: ").append(appointment.getEndDateTime());
                                }
                                alert.setHeaderText(headerText.toString());
                                startDatePicker.setStyle("-fx-border-color: rgb(255, 0, 0);");
                                endDatePicker.setStyle("-fx-border-color: rgb(255, 0, 0);");
                                startComboBox.setStyle("-fx-border-color: rgb(255, 0, 0);");
                                endComboBox.setStyle("-fx-border-color: rgb(255, 0, 0);");
                                alert.showAndWait();
                                checkErrors = true;
                        } else {
                                startDatePicker.setStyle(null);
                                endDatePicker.setStyle(null);
                                startComboBox.setStyle(null);
                                endComboBox.setStyle(null);
                        }
                }

                if (contactComboBox.getValue() == null) {
                        checkErrors = true;
                        contactComboBox.setStyle("-fx-border-color: rgb(255, 0, 0);");
                } else {
                        contactComboBox.setStyle(null);
                }

                if (!checkErrors) {

                        int customerIndex = custComboBox.getItems().indexOf(custComboBox.getValue());
                        int customerID = directory.getAllCustomers().get(customerIndex).getId();
                        newAppointment.setCustomerID(customerID);

                        int contactIndex = contactComboBox.getItems().indexOf(contactComboBox.getValue());
                        int contactID = directory.getAllContacts().get(contactIndex).getId();
                        newAppointment.setContactID(contactID);

                        blankFieldErrorLabel.setVisible(false);

                        if (mode == MODE.ADD) {

                                if (!appointmentCalendar.addAppointment(newAppointment)) {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Add Appointment Error");
                                        alert.setHeaderText("Add Appointment Failed");
                                        alert.setContentText("Could not add appointment. Please check database connection and restart the program.");
                                        alert.showAndWait();
                        }
                } else if (mode == MODE.UPDATE) {
                        newAppointment.setAppointmentID(Integer.parseInt(idField.getText()));
                        if (!appointmentCalendar.updateAppointment(newAppointment)) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Update Failed");
                                alert.setContentText("Could not update appointment. Please check database connection and restart the program.");
                                alert.showAndWait();
                        }
                }
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.initModality(Modality.NONE);
                        alert.setTitle("Add Appointment");
                        alert.setHeaderText("Appointment successfully Added!");
                        Optional<ButtonType> result = alert.showAndWait();

                        if (result.get() == ButtonType.OK) {
                                Parent parent = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                                Scene scene = new Scene(parent);
                                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                stage.setScene(scene);
                                stage.show();
                        }
                } else {
                blankFieldErrorLabel.setVisible(true);
                }
       }

       /**
        * Takes the user back to the Main Screen.
        * Cancels the Add Appointment.
        * @param event action event when the cancel button is clicked.
        * Confirmation is displayed before cancelling.
        * */
        public void onActionCancelButton(ActionEvent event) throws IOException {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("Cancel");
                alert.setHeaderText("Return to Main Screen");
                alert.setContentText("Are you sure you want to cancel?");
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
         * Takes the user back to the Main Screen.
         * @param event action event when the return button is clicked.
         * Confirmation is displayed before returning to the Main Screen.
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
         * Customers, Contacts, Users, and Countries are retrieved from the database by a new Directory.
         * Lambda Expression is used to populate the Combo Boxes for Customers, Users, and Contacts.
         * ForEach is used on allContacts, allUsers, and AllCustomers to populate the combo boxes.
         *  */
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

                directory = new Directory();
                appointmentCalendar = new AppointmentCalendar();

                ObservableList<String> customerNames = FXCollections.observableArrayList();
                directory.getAllCustomers().forEach((n) -> customerNames.add(n.getName()));
                custComboBox.getItems().addAll(customerNames);

                ObservableList<String> userNames = FXCollections.observableArrayList();
                directory.getAllUsers().forEach((n) -> userNames.add(n.getName()));
                userComboBox.getItems().addAll(userNames);

                ObservableList<String> contactNames = FXCollections.observableArrayList();
                directory.getAllContacts().forEach((n) -> contactNames.add(n.getName()));
                contactComboBox.getItems().addAll(contactNames);

                startComboBox.getItems().addAll(getAppointmentTimes());
                endComboBox.getItems().addAll(startComboBox.getItems());

                blankFieldErrorLabel.setVisible(false);
                timeErrorLabel.setVisible(false);
        }

        /**
         * Gets the appointment times.
         * Increments the appointment times every 15 minutes.
         * @return the appointment times.
         * */
        private ObservableList<String> getAppointmentTimes() {

                ObservableList<String> appointmentTimes = FXCollections.observableArrayList();

                LocalTime midnight = LocalTime.of(0,0);
                appointmentTimes.add(midnight.format(timeFormat));

                int minutes = 15;
                LocalTime appointmentTime = midnight.plusMinutes(minutes);
                while (!appointmentTime.equals(midnight)) {
                        appointmentTimes.add(appointmentTime.format(timeFormat));
                        appointmentTime = appointmentTime.plusMinutes(minutes);
                }
                return appointmentTimes;
        }
}
