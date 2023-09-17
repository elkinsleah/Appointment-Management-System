package controller;

import helper.JDBC;
import helper.Query;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Node;
import model.Appointment;
import model.AppointmentCalendar;
import model.User;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for Login Screen
 * */
public class LoginScreenController implements Initializable {

        public Label appointmentManagementSystemLabel;
        public Button exitButton;
        public Button loginButton;
        public PasswordField passwordField;
        public Label passwordLabel;
        public Label timeZoneLabel;
        public TextField usernameField;
        public Label usernameLabel;
        private ResourceBundle languageBundle;
        static boolean loggedIn;
        public Button returnButton;

        /**
         * Initializes the Login Screen.
         * Database connection is opened.
         * The language is determined from the system setting.
         * Login Screen is displayed as English or French.
         * */
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

                JDBC.openConnection();
                Locale locale = Locale.getDefault();
                loggedIn = false;

                languageBundle = ResourceBundle.getBundle("LanguageBundle_" + locale.getLanguage());
                usernameLabel.setText(languageBundle.getString("username"));
                passwordLabel.setText(languageBundle.getString("password"));
                loginButton.setText(languageBundle.getString("login"));
                timeZoneLabel.setText(languageBundle.getString("location") + ZoneId.systemDefault());
                exitButton.setText(languageBundle.getString("exit"));
                appointmentManagementSystemLabel.setText(languageBundle.getString("title"));
        }

        /**
         * Exits the application and closes the database connection.
         * @param event action event when exit button is clicked and exits the application.
         * */
        public void onActionExitButton(ActionEvent event) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("Exit");
                alert.setHeaderText("Exit Application");
                alert.setContentText("Are you sure you want to exit?");
                Optional<ButtonType> result = alert.showAndWait();

                if (loggedIn) {
                        JDBC.closeConnection();
                }
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
        }

        /**
         * Retrieves the input User ID and Password and checks if it matches the database.
         * If the login attempt fails, an error pops up in English or French.
         * @param event action event when the log-in button is clicked.
         * */
        public void onActionLoginButton(ActionEvent event) throws IOException {

                if (Query.login(usernameField.getText(), passwordField.getText())) {
                        appointmentReminder();
                        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(languageBundle.getString("error"));
                        alert.setTitle(languageBundle.getString("errorTitle"));
                        alert.setContentText(languageBundle.getString("errorText"));
                        alert.showAndWait();
                }
        }

        /**
         * A reminder for determining if the logged-in User has an appointment within the next 15 minutes.
         * A popup is displayed if there is an appointment within the next 15 minutes.
         * A popup is displayed if there are no upcoming appointments.
         * */
        public void appointmentReminder() {

                AppointmentCalendar appointmentCalendar = new AppointmentCalendar();
                Appointment nextAppointment = null;
                User loginUser = Query.getLoginUser();

                LocalDateTime timeNow = LocalDateTime.now();
                for (Appointment appointment : appointmentCalendar.getAllAppointments()) {
                        LocalDateTime startDateTime = appointment.getStartDateTimeLocal();
                        if (timeNow.toLocalDate().equals(startDateTime.toLocalDate())) {
                                if (appointment.getUserID() == loginUser.getId()) {
                                        long timeToAppointment = Duration.between(timeNow, startDateTime).toMinutes();
                                        if (timeToAppointment <= 15 && timeToAppointment >= 0) {
                                                nextAppointment = appointment;
                                                break;
                                        }
                                }
                        }
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Upcoming Appointment");
                if (nextAppointment != null) {
                        alert.setHeaderText("You have an appointment within the next 15 minutes.");
                        alert.setContentText("Appointment ID: " + nextAppointment.getAppointmentID() + "\nDate and Time: " + nextAppointment.getStartDateTime());
                } else {
                        alert.setHeaderText("You have no upcoming appointments.");
                }
                alert.showAndWait();
        }
}








