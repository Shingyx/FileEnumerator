package fileenumerator.gui;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.io.File;

/**
 * Main controller for the tab which creates a new folder.
 */
public class FolderCreationController {

    @FXML
    private Parent folderCreationContainer;
    @FXML
    private Label directoryLabel;
    @FXML
    private Label filenameLabel;
    @FXML
    private TextField targetFolderField;

    /**
     * Initialise the tab to have appropriate behaviour on file drag.
     */
    @FXML
    private void initialize() {
        folderCreationContainer.setOnDragOver(event -> {
            final Dragboard dragboard = event.getDragboard();
            if (dragboard.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        folderCreationContainer.setOnDragDropped(event -> {
            final Dragboard dragboard = event.getDragboard();
            if (dragboard.hasFiles()) {
                File file = dragboard.getFiles().get(0);
                String directory = file.getParentFile().getAbsolutePath();
                String filename = file.getName();
                String targetFolderName;
                directoryLabel.setText(directory);
                filenameLabel.setText(filename);
                if (file.isFile()) {
                    targetFolderName = (filename.substring(0, filename.lastIndexOf('.')));
                } else {
                    targetFolderName = filename;
                }
                targetFolderField.setText(targetFolderName);

                event.setDropCompleted(true);
                event.consume();
            }
        });
    }

    /**
     * Handle the action of clicking the create folder button.
     */
    @FXML
    private void createFolder() {
        String directory = directoryLabel.getText();
        String targetFolderName = targetFolderField.getText();
        File targetFolder = new File(directory, targetFolderName);

        Alert.AlertType alertType;
        String message;
        if (targetFolder.exists()) {
            alertType = Alert.AlertType.WARNING;
            message = "Target folder already exists";
        } else {
            boolean result = targetFolder.mkdir();
            if (result) {
                alertType = Alert.AlertType.INFORMATION;
                message = "Folder successfully created";
            } else {
                alertType = Alert.AlertType.ERROR;
                message = "Could not create folder due to security settings";
            }
        }
        Alert alert = new Alert(alertType);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
