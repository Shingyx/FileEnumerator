package fileenumerator.gui;

import javafx.fxml.FXML;
import javafx.scene.Parent;
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
        System.out.println(targetFolder);
        if (targetFolder.exists()) {
            System.out.println("Target folder already exists");
        } else {
            boolean result = targetFolder.mkdir();
            if (!result) {
                System.out.println("Could not create folder due to security settings");
            }
        }
    }
}
