/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui;

import java.io.IOException;
import java.util.Date;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jiangestockans.JianGeStockAns;
import tools.utilities.Logs;

/**
 *
 * @author JianGe
 */
public class SearchResultsPlots extends Application {

    private final String header = "Search Results Plots......";
    private SearchResultsPlotsController controller;
    public static EventHandler closeEvent = new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent event) {
            //do nothing
            Logs.e(event.toString());
        }
    };

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchResultsPlots.fxml"));
        Parent root;
        try {
            root = loader.load();
            controller = loader.getController();

            Scene scene = new Scene(root);

//            stage.setResizable(false);
//            stage.setMaximized(false);
            stage.setTitle(header);
            stage.setScene(scene);
        } catch (IOException e) {
        }

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                controller.closeTab();
                JianGeStockAns.toolBarController.getTgBtnShowPlots().setSelected(false);
            }
        });

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
//        Logs.e(primaryScreenBounds.getWidth() + "  " + primaryScreenBounds.getHeight());

        //Set to full screen
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());
        stage.alwaysOnTopProperty();
        stage.show();
    }

    public void updateSourcePlots(final String code, final String name, final Date startDate, final int duration, final Date endDate, boolean onlyDisplayMatched) {
//        Logs.e("update Source Plots");
        controller.newPlotsTab(code, name, startDate, duration, endDate, onlyDisplayMatched);
    }

    public void updateMatchedPlots(final String code, final String name, final Date startDate, final int duration, final Date endDate, boolean onlyDisplayMatched) {
//        Logs.e("update Searched Plots");
        controller.newPlotsTab(code, name, startDate, duration, endDate, onlyDisplayMatched);
    }

    public void updateSelectedSourcePlots(String tabName, final String code, final String name, final Date startDate, final int duration, final Date endDate, boolean isStockChange) {
//        Logs.e("update Source Plots");
        controller.getTabByTabName(tabName).updateSourceChart(code, name, startDate, duration, endDate, isStockChange);

    }

    public void updateSelectedMatchedPlots(String tabName, final String code, final String name, final Date startDate, final int duration, final Date endDate, boolean isStockChange) {
//        Logs.e("update Searched Plots");
        controller.getTabByTabName(tabName).updateMatchedChart(code, name, startDate, duration, endDate, isStockChange);
    }

}
