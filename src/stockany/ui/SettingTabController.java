/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author JianGe
 */
public class SettingTabController extends Tab implements Initializable {

    @FXML
    private AnchorPane settingAnchorPane;
    @FXML
    private Button btnBrowsDataAscFilesDef;
    @FXML
    private Button btnBrowsDataDbFileDef;
    @FXML
    private TextField txtF_DataBinFilesDefPath;
    @FXML
    private TextField txtF_DataAscFilesDefPath;
    @FXML
    private TextField txtF_DataDbFileDefPath;
    @FXML
    private Button btn_SaveSettings;
    @FXML
    private Button btnBrowsDataBinFilesDef;
    @FXML
    private TextField txtF_LogFileDefPath;
    @FXML
    private Button btnBrowsLogFileDef;
    @FXML
    private TextField txtF_CorrDbFileDefPath;
    @FXML
    private Button btnBrowsCorrDbFileDef;
    @FXML
    private TextField txtF_CorrAscFilesDefPath;
    @FXML
    private Button btnBrowsCorrAscFilesDef;
    @FXML
    private TextField txtF_DefSample;
    
     public SettingTabController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SettingTab.fxml"));
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

    @FXML
    private void onBtnBrowsDataAscFilesDef(ActionEvent event) {
    }

    @FXML
    private void onBtnBrowsDataDbFileDef(ActionEvent event) {
    }

    @FXML
    private void onBtnSaveSetting(ActionEvent event) {
    }

    @FXML
    private void onBtnBrowsDataBinFilesDef(ActionEvent event) {
    }

    @FXML
    private void onBtnBrowsLogFileDef(ActionEvent event) {
    }

    @FXML
    private void onBtnBrowsCorrDbFileDef(ActionEvent event) {
    }

    @FXML
    private void onBtnBrowsCorrAscFilesDef(ActionEvent event) {
    }

    @FXML
    private void onContentChanged(InputMethodEvent event) {
    }
    
}
