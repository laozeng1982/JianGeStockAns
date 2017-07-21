/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

/**
 * FXML Controller class
 *
 * @author JianGe
 */
public class HelpsTabController extends Tab implements Initializable {

    public HelpsTabController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HelpsTab.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.setClosable(false);

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

}
