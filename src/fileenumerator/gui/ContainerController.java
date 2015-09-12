package fileenumerator.gui;

import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Main controller for the main container of the GUI. It contains the tabs and initialises them individually.
 */
public class ContainerController {

    @FXML
    private EnumeratorController enumeratorController;
    @FXML
    private FolderCreationController folderCreationController;

    /**
     * Set up the controller for the main container.
     *
     * @param stage The stage of the main application.
     */
    public void setupController(Stage stage) {
        // Set min window size
        stage.setMinHeight(480);
        stage.setMinWidth(640);
    }
}
