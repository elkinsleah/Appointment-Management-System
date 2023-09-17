package main;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Folder containing Javadoc files can be found in /Users/leahelkins/IdeaProjects/AppointmentManagementSystem/Javadoc. The folder is named Javadoc.

/**
 * Main class begins application.
 *
 * @author Leah Elkins
 * WGU C195 Software II Performance Assessment
 */
public class AppointmentManagementSystem extends Application {

    /**
     * Login Screen that is shown when application is started
     */
    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}





