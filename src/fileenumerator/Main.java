package fileenumerator;

import fileenumerator.gui.MainPaneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application to run. Load the view and pass over control to the controller.
 */
public class Main extends Application {

  /**
   * Main method to launch application.
   * @param args Command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Launch the application.
   * @param primaryStage The primary stage
   * @throws Exception Something went wrong or something?
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/MainPane.fxml"));
    Parent root = loader.load();
    MainPaneController mainPaneController = loader.getController();

    primaryStage.setTitle("File Enumerator");
    primaryStage.setScene(new Scene(root, 300, 275));

    mainPaneController.setupController(primaryStage);

    primaryStage.show();
  }
}
