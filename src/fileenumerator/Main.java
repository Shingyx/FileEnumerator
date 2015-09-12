package fileenumerator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application to run. Load the view and set it up then pass over control to the controllers.
 */
public class Main extends Application {

    /**
     * Main method to launch application.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Launch the application.
     *
     * @param primaryStage The primary stage
     * @throws Exception Something went wrong or something?
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        final Parent root = loader.load();

        primaryStage.setTitle("File Enumerator");
        primaryStage.setScene(new Scene(root));

        // Set min window size
        primaryStage.setMinHeight(480);
        primaryStage.setMinWidth(640);


        primaryStage.show();
    }
}
