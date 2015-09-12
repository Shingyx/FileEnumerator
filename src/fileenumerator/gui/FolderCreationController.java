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
                String targetFolder;
                directoryLabel.setText(directory);
                filenameLabel.setText(filename);
                if (file.isFile()) {
                    targetFolder = (filename.substring(0, filename.lastIndexOf('.')));
                } else {
                    targetFolder = filename;
                }
                targetFolderField.setText(targetFolder);

                event.setDropCompleted(true);
                event.consume();
            }
        });
    }
}
