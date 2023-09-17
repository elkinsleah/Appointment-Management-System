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
import model.Customer;
import model.Directory;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for Customers Screen
 * */
public class CustomersController implements Initializable {

    public Button addCustomerButton;
    public TableColumn<Customer, String> custAddressCol;
    public TableColumn<Customer, String> custCountryCol;
    public TableColumn<Customer, String> custDivisionCol;
    public TableColumn<Customer, Integer> custIdCol;
    public TableColumn<Customer, String> custNameCol;
    public TableColumn<Customer, String> custPhoneCol;
    public TableColumn<Customer, String> custPostalCol;
    public TableView<Customer> customersTableView;
    public Button deleteButton;
    public Button returnButton;
    public Button updateCustomerButton;
    private ObservableList<model.Customer> displayCustomers = FXCollections.observableArrayList();
    private Directory directory = null;

    /**
     * Adds a new Customer to the Customer Table.
     * @param event action event when the Add button is clicked.
     * Error message is displayed if customer could not be added.
     * */
    public void onActionAddCustomer(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddCustomer.fxml"));
            Parent mainScreenParent;
            mainScreenParent = loader.load();
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();

            AddCustomerController controller = loader.getController();
            controller.addMode();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add Customer Error");
            alert.setHeaderText("Add Customer Failed");
            alert.setContentText("Could not add customer. Please check database connection and restart the program.");
            alert.showAndWait();
        }
    }

    /**
     * Deletes the selected customer from the database, including all associated appointments.
     * @param event action event when the delete button is clicked.
     * Displays an error message if no customer is selected to delete.
     * Popup confirms before deletion.
     * Error message is displayed if customer could not be deleted.
     * */
    public void onActionDeleteButton(ActionEvent event) {

        Customer selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();
        if (customersTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Delete Error");
            alert.setHeaderText("No customer selected");
            alert.setContentText("No customer was selected to delete");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Customer");
            alert.setHeaderText(selectedCustomer.getName());
            alert.setContentText("Are you sure you want to delete this customer and associated appointments?");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK) {
                if (directory.deleteCustomer(selectedCustomer)) {
                    displayCustomers = directory.getAllCustomers();
                    customersTableView.setItems(displayCustomers);
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Delete Customer Error");
                    alert.setHeaderText("Delete Customer Failed");
                    alert.setContentText("Could not delete customer. Please check database connection and restart the program.");
                }
            }
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
     * Takes the user to the Update Customer Screen.
     * Transfers the content of the selected customer to the Update Customer Screen.
     * @param event action event when the update button is clicked.
     * Error message is displayed if no customer is selected.
     * */
    public void onActionUpdateCustomer(ActionEvent event) throws IOException {

        Customer selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();
        if (customersTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Update Error");
            alert.setHeaderText("No customer selected");
            alert.setContentText("No customer was selected to update");
            alert.showAndWait();

        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateCustomer.fxml"));
            Parent mainScreenParent;
            mainScreenParent = loader.load();
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();

            UpdateCustomerController controller = loader.getController();
            controller.updateMode(selectedCustomer);
        }
    }

    /**
     * Initializes the Customer Table View.
     * Customers, Contacts, Users, and Countries are retrieved from the database by a new Directory.
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        directory = new Directory();

        displayCustomers = directory.getAllCustomers();
        customersTableView.setItems(displayCustomers);

        custIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        custAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        custPostalCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        custDivisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        custCountryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }
}
