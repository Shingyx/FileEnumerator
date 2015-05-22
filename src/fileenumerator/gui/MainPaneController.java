package fileenumerator.gui;

import java.io.File;

import javafx.scene.Scene;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

/**
 * Main controller for the GUI.
 */
public class MainPaneController {

  /**
   * Set up the GUI to have a minimum size and behaviour on file drag.
   * @param stage The primary stage
   */
  public void setupController(Stage stage) {

    stage.setMinHeight(480);
    stage.setMinWidth(640);

    Scene scene = stage.getScene();

    scene.setOnDragOver(event -> {
      Dragboard dragboard = event.getDragboard();
      if (dragboard.hasFiles()) {
        event.acceptTransferModes(TransferMode.COPY);
      } else {
        event.consume();
      }
    });

    scene.setOnDragDropped(event -> {
      Dragboard dragboard = event.getDragboard();
      if (dragboard.hasFiles()) {
        String filePath = null;
        for (File file : dragboard.getFiles()) {
          filePath = file.getAbsolutePath();
          System.out.println(filePath);
        }
        event.setDropCompleted(true);
        event.consume();
      }
    });
  }
}
