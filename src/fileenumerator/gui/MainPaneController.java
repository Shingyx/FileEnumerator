package fileenumerator.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Main controller for the GUI.
 */
public class MainPaneController {

  @FXML
  private ListView<File> fileListView;
  @FXML
  private TextField txtFilenameEditor;
  @FXML
  private TextField txtTargetFormat;

  private ObservableList<File> files;
  private int lastCaretPos;

  /**
   * Set up the GUI to have a minimum size, appropriate behaviour on file drag, and drag selection
   * on the file list.
   *
   * @param stage The primary stage
   */
  public void setupController(Stage stage) {
    files = FXCollections.observableArrayList();
    lastCaretPos = -1;

    // Set min window size
    stage.setMinHeight(480);
    stage.setMinWidth(640);

    // Set up file dragging
    setupFileDragging(stage.getScene());

    // temp folder
    File testDir = new File("E:\\Desktop\\fileenumerator test files\\testing");
    File[] testFiles = testDir.listFiles();
    if (testFiles != null) {
      for (File testFile : testFiles) {
        if (!testFile.isDirectory() && !files.contains(testFile)) {
          files.add(testFile);
        }
      }
    }

    // Set up the list view
    setupListView();

    // Set up list to drag-select by default. TODO: Drag and drop mode toggle option
    setupSelectionMode(true);

    txtFilenameEditor.setOnMouseClicked(event -> {
      String filename = txtFilenameEditor.getText();

      int start = txtFilenameEditor.getCaretPosition();
      while (start > 0 && Character.isDigit(filename.charAt(start - 1))) {
        start--;
      }
      if (start >= filename.length() || !Character.isDigit(filename.charAt(start))) {
        return;
      }

      int end = start;
      while (end < filename.length() && Character.isDigit(filename.charAt(end))) {
        end++;
      }

      txtFilenameEditor.positionCaret(start);
      txtFilenameEditor.selectPositionCaret(end);

      lastCaretPos = start;
    });
  }

  /**
   * Set a scene up so that files dragged into the scene will be loaded into the file list.
   *
   * @param scene The scene to change
   */
  private void setupFileDragging(Scene scene) {
    scene.setOnDragOver(event -> {
      Dragboard dragboard = event.getDragboard();
      if (dragboard.hasFiles()) {
        event.acceptTransferModes(TransferMode.COPY);
      }
      event.consume();
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
                if (!fileInDir.isDirectory() && !files.contains(fileInDir)) {
                  files.add(fileInDir);
                }
              }
            }
          } else {
            if (!files.contains(file)) {
              files.add(file);
            }
          }
        }
        event.setDropCompleted(true);
        event.consume();
      }
    });
  }

  /**
   * Set the file list view up to enable drag selection and display only filenames.
   */
  private void setupListView() {
    fileListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    fileListView.setItems(files);
  }

  private void setupSelectionMode(boolean dragSelectionMode) {
    fileListView.setCellFactory(new Callback<ListView<File>, ListCell<File>>() {
      @Override
      public ListCell<File> call(ListView<File> param) {
        final ListCell<File> cell = new ListCell<File>() {
          @Override
          protected void updateItem(File file, boolean empty) {
            super.updateItem(file, empty);
            if (file != null) {
              setText(file.getName());
            }
          }
        };

        cell.selectedProperty().addListener((observable, oldValue, newValue) -> {
          if (newValue) {
            txtFilenameEditor.setText(cell.getText());
            lastCaretPos = -1;
          }
        });

        if (dragSelectionMode) {
          cell.setOnDragDetected(event -> {
            if (cell.getItem() == null) {
              return;
            }
            Dragboard dragboard = cell.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(cell.getText());
            dragboard.setContent(content);

            event.consume();

          });

          cell.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.COPY);
            if (cell.getItem() == null) {
              return;
            }
            if (!event.getDragboard().hasFiles()) {
              event.acceptTransferModes(TransferMode.ANY);
              cell.getListView().getSelectionModel().select(cell.getIndex());
            }
            event.consume();
          });

          cell.setOnDragDropped(event -> {
            event.setDropCompleted(true);
            event.consume();
          });
        }
        return cell;
      }
    });
  }

  /**
   * Reset all the fields and values related to setting filenames.
   */
  private void resetFields() {
    txtFilenameEditor.setText(null);
    txtTargetFormat.setText(null);
    lastCaretPos = -1;
  }

  /**
   * Move all selected items to the top.
   */
  @FXML
  private void moveSelectedToTop() {
    // Put selected items at front of new list
    List<File> newFiles = new ArrayList<>(fileListView.getSelectionModel().getSelectedItems());
    if (files.isEmpty()) {
      System.out.println("No files loaded in application");
      return;
    } else if (newFiles.isEmpty()) {
      System.out.println("No files selected");
      return;
    }
    File first = newFiles.get(0);
    File last = newFiles.get(newFiles.size() - 1);

    // Then add the old files
    newFiles.addAll(files.subList(0, files.indexOf(first)));
    newFiles.addAll(files.subList(files.indexOf(last) + 1, files.size()));

    // Refresh the list
    files.setAll(newFiles);
  }

  /**
   * Apply the default sorting method.
   */
  @FXML
  private void defaultSortFiles() {
    files.sort(Comparator.<File>naturalOrder());
  }

  /**
   * Mark highlighted text for enumeration
   */
  @FXML
  private void markForEnumeration() {
    String targetFilename = txtFilenameEditor.getText();
    if (lastCaretPos >= 0) {
      int start = lastCaretPos;
      int end = start;
      while (end < targetFilename.length() && Character.isDigit(targetFilename.charAt(end))) {
        end++;
      }
      String before = targetFilename.substring(0, start);
      String after = targetFilename.substring(end, targetFilename.length());

      int numLen = (int) Math.log10(files.size()) + 1;

      // Replace int in middle with format including leading zeroes
      String format = String.format("%s%%0%dd%s", before, numLen, after);

      txtTargetFormat.setText(format);
    } else {
      System.out.println("Wot");
    }
  }

  /**
   * Clear the list of all files.
   */
  @FXML
  private void clearList() {
    files.clear();
    resetFields();
  }

  /**
   * Enumerate all files in the list using the string format in whatever order the list is in.
   */
  @FXML
  private void enumerateFiles() {
    if (txtTargetFormat.getText().isEmpty()) {
      System.out.println("No template chosen");
      return;
    }

    String format = txtTargetFormat.getText();
    List<File> newFiles = new ArrayList<>();

    for (int i = 0; i < files.size(); i++) {
      File file = files.get(i);
      String newFilename = String.format(format, i + 1);
      File newFile = new File(file.getParent() + File.separator + newFilename);
      if (!file.renameTo(newFile)) {
        System.out.println("file couldn't be renamed?");
      } else {
        newFiles.add(newFile);
      }
    }

    files.setAll(newFiles);
  }
}
