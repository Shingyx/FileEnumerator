package fileenumerator.gui;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Main controller for the GUI.
 */
public class MainPaneController {

  private final ObservableList<File> files = FXCollections.observableArrayList();

  @FXML ListView<File> fileListView;

  /**
   * Set up the GUI to have a minimum size and behaviour on file drag.
   *
   * @param stage The primary stage
   */
  public void setupController(Stage stage) {
    // Set min window size
    stage.setMinHeight(480);
    stage.setMinWidth(640);

    // Set up file dragging
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
        for (File file : dragboard.getFiles()) {
          if (file.isDirectory()) {
            // Store all files in dragged directory
            File[] filesInDir = file.listFiles();
            if (filesInDir != null) {
              for (File fileInDir : filesInDir) {
                if (!fileInDir.isDirectory()) {
                  files.add(fileInDir);
                }
              }
            }
          } else {
            files.add(file);
          }
        }
        event.setDropCompleted(true);
        event.consume();
      }
    });

    // Set up the list view to display only filename
    fileListView.setCellFactory(new Callback<ListView<File>, ListCell<File>>() {
      @Override
      public ListCell<File> call(ListView<File> param) {
        return new ListCell<File>() {
          protected void updateItem(File file, boolean empty) {
            super.updateItem(file, empty);
            if (file != null) {
              setText(file.getName());
            }
          }
        };
      }
    });
    fileListView.setItems(files);
  }
}
