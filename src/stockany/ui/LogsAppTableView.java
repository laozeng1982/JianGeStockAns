/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui;

import stockany.datamodel.LogInformation;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tools.utilities.Logs;
import jiangestockans.JianGeStockAns;

/**
 *
 * @author JianGe
 */
public class LogsAppTableView extends Application {

    private final String title = "Logs......";
    private LogsAppTableViewController controller;
    public static EventHandler closeEvent = new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent event) {
            //do nothing
            Logs.e(event.toString());
        }
    };

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LogsAppTableView.fxml"));
        Parent root = loader.load();
        controller = loader.getController();

        Scene scene = new Scene(root);

        stage.hide();
        stage.setResizable(false);
        stage.setMaximized(false);
        stage.setTitle(title);
        stage.setScene(scene);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //do nothing
                JianGeStockAns.toolBarController.getTgBtnShowLog().setSelected(false);
            }
        });

//        stage.alwaysOnTopProperty();
        stage.show();
    }

    public void updateInfo(final ObservableList<LogInformation> info) {
        controller.updateInfo(info);
    }

    public void updateInfo(final String info) {
        controller.updateInfo(info);
    }

    public void updateInfo(final ArrayList<String> info) {
        controller.updateInfo(info);
    }
}
