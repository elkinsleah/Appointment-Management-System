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
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Customer;
import model.Directory;
import model.Division;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for Add Customer Screen
 * */
public class AddCustomerController implements Initializable {

        public Button saveButton;
        public TextField addressField;
        public Button cancelButton;
        public ComboBox<String> countryComboBox;
        public TextField custIdField;
        public ComboBox<String> divisionComboBox;
        public TextField nameField;
        public TextField phoneField;
        public TextField postalField;
        public Button returnButton;
        public Label blankFieldErrorLabel;
        private enum MODE {ADD, UPDATE}
        private MODE mode;
        private Directory directory = null;

        /**
         * Add Mode is used to create a new Customer and add it to the database.
         * */
        public void addMode() {
                mode = MODE.ADD;
        }

        /**
         * Customer is updated in the database.
         * All fields are set to the values of updateCustomer.
         * @param updateCustomer the selected customer to be updated.
         * */
        public void updateMode(Customer updateCustomer) {
                mode = MODE.UPDATE;
                custIdField.setText(String.valueOf(updateCustomer.getId()));
                nameField.setText(updateCustomer.getName());
                addressField.setText(updateCustomer.getAddress());
                postalField.setText(updateCustomer.getPostalCode());
                phoneField.setText(updateCustomer.getPhone());

                countryComboBox.setValue(updateCustomer.getCountry());
                onActionSelectCountry(new ActionEvent());
                divisionComboBox.setValue(updateCustomer.getDivision());
        }

        /**
         * Saves the new Added Customer.
         * Add Mode is used to create a new Customer and add it to the database.
         * checkErrors is used to make sure fields are not empty.
         * @param event action event when the save button is clicked.
         * */
        public void onActionSaveButton(ActionEvent event) throws IOException {

                String name = nameField.getText();
                String address = addressField.getText();
                String postal = postalField.getText();
                String phone = phoneField.getText();

                boolean checkErrors = false;

                if (name.isEmpty()) {
                        checkErrors = true;
                        nameField.setStyle("-fx-border-color: rgb(255, 0, 0);");
                } else {
                        nameField.setStyle(null);
                }
                if (address.isEmpty()) {
                        checkErrors = true;
                        addressField.setStyle("-fx-border-color: rgb(255, 0, 0);");
                } else {
                        addressField.setStyle(null);
                }
                if (postal.isEmpty()) {
                        checkErrors = true;
                        postalField.setStyle("-fx-border-color: rgb(255, 0, 0);");
                } else {
                        postalField.setStyle(null);
                }
                if (phone.isEmpty()) {
                        checkErrors = true;
                        phoneField.setStyle("-fx-border-color: rgb(255, 0, 0);");
                } else {
                        phoneField.setStyle(null);
                }
                if (countryComboBox.getValue() == null) {
                        checkErrors = true;
                        countryComboBox.setStyle("-fx-border-color: rgb(255, 0, 0);");
                } else {
                        countryComboBox.setStyle(null);
                }
                if (divisionComboBox.getValue() == null) {
                        checkErrors = true;
                        divisionComboBox.setStyle("-fx-border-color: rgb(255, 0, 0);");
                } else {
                        divisionComboBox.setStyle(null);
                }

                if (!checkErrors) {
                        Customer newCustomer = new Customer();
                        newCustomer.setName(name);
                        newCustomer.setAddress(address);
                        newCustomer.setPostalCode(postal);
                        newCustomer.setPhone(phone);

                        int countryIndex = countryComboBox.getItems().indexOf(countryComboBox.getValue());
                        int divisionIndex = divisionComboBox.getItems().indexOf(divisionComboBox.getValue());
                        int divisionID = directory.getAllCountries().get(countryIndex).getAllDivisions().get(divisionIndex).getDivisionID();
                        newCustomer.setDivisionID(divisionID);

                        if (mode == MODE.ADD) {
                                if (!directory.addCustomer(newCustomer)) {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Add Customer Error");
                                        alert.setHeaderText("Add Customer Failed");
                                        alert.setContentText("Could not add customer. Please check database connection and restart the program.");
                                        alert.showAndWait();
                                }
                        } else if (mode == MODE.UPDATE) {
                                newCustomer.setId(Integer.parseInt(custIdField.getText()));
                                if (!directory.updateCustomer(newCustomer)) {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Update Customer Error");
                                        alert.setHeaderText("Update Customer Failed");
                                        alert.setContentText("Could not update customer. Please check database connection and restart the program.");
                                        alert.showAndWait();
                                }
                        }
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.initModality(Modality.NONE);
                        alert.setTitle("Add Customer");
                        alert.setHeaderText("Customer successfully Added!");
                        Optional<ButtonType> result = alert.showAndWait();

                        if (result.get() == ButtonType.OK) {
                                Parent parent = FXMLLoader.load(getClass().getResource("/view/Customers.fxml"));
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
         * Takes the user back to the Customers Screen.
         * Cancels the Add Customer.
         * @param event action event when the cancel button is clicked.
         * Confirmation is displayed before cancelling.
         * */
        public void onActionCancelButton(ActionEvent event) throws IOException {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("Cancel");
                alert.setHeaderText("Return to Customers");
                alert.setContentText("Are you sure you want to cancel?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                        Parent parent = FXMLLoader.load(getClass().getResource("/view/Customers.fxml"));
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
         * Populates the Division Combo Box based on the selected Country.
         * */
        public void onActionSelectCountry(ActionEvent event) {

                int countryIndex = countryComboBox.getItems().indexOf(countryComboBox.getValue());
                ObservableList<String> divisionNames = FXCollections.observableArrayList();
                for (Division division : directory.getAllCountries().get(countryIndex).getAllDivisions()) {
                        divisionNames.add(division.getDivision());
                }
                divisionComboBox.getItems().clear();
                divisionComboBox.getItems().addAll(divisionNames);
                divisionComboBox.setValue(null);
        }

        /**
         * Initializes the Add Customer Screen.
         * Customers, Contacts, Users, and Countries are retrieved from the database from a new Directory.
         * <p>
         * Lambda Expression is used to populate the Country Combo Box.
         * </p>
         * */
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

                directory = new Directory();

                ObservableList<String> countryNames = FXCollections.observableArrayList();

                directory.getAllCountries().forEach((n) -> countryNames.add(n.getCountry()));
                countryComboBox.getItems().addAll(countryNames);

                blankFieldErrorLabel.setVisible(false);
        }
}

