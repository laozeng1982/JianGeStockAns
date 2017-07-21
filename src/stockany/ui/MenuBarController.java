/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuBar;

/**
 * FXML Controller class
 *
 * @author JianGe
 */
public class MenuBarController extends MenuBar implements Initializable {

    @FXML
    private MenuItem exitMenuItem;

    public MenuBarController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MenuBar.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void onExitMenuItem(ActionEvent event) {
        Platform.exit();
    }

}
