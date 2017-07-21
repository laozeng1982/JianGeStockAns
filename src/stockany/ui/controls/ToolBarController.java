/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui.controls;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import stockany.datamodel.GlobalParameters;
import stockany.ui.PatternTabController;

/**
 * FXML Controller class
 *
 * @author JianGe
 */
public class ToolBarController extends ToolBar implements Initializable {

    @FXML
    private ToggleButton tgBtnShowPlots;
    @FXML
    private ToggleButton tgBtnShowLog;
    @FXML
    private Button btnExit;

    public ToolBarController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ToolBar.fxml"));
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
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public ToggleButton getTgBtnShowPlots() {
        return tgBtnShowPlots;
    }

    public void setTgBtnShowPlots(ToggleButton tgBtnShowPlots) {
        this.tgBtnShowPlots = tgBtnShowPlots;
    }

    public ToggleButton getTgBtnShowLog() {
        return tgBtnShowLog;
    }

    public void setTgBtnShowLog(ToggleButton tgBtnShowLog) {
        this.tgBtnShowLog = tgBtnShowLog;
    }
    
    

    @FXML
    private void onTgBtnShowPlots(ActionEvent event) {
        if (tgBtnShowPlots.isSelected()) {

            try {
                GlobalParameters.searchResultsPlots.start(GlobalParameters.resultPlotStage);

            } catch (Exception e) {
            }

//            logInfo("Logs window show up!");
        } else {
//            logInfo("Logs window hide!");
            GlobalParameters.resultPlotStage.close();

        }
    }

    @FXML
    private void onTgBtnShowLogs(ActionEvent event) {
        if (tgBtnShowLog.isSelected()) {
            try {
                GlobalParameters.logsAppTableView.start(GlobalParameters.logsStage);
                GlobalParameters.logShow();
            } catch (Exception e) {
            }
        } else {
            GlobalParameters.logsStage.close();

        }
    }

    @FXML
    private void onBtnExit(ActionEvent event) {
        PatternTabController.stopAllThreads();
        Platform.exit();
    }

}
