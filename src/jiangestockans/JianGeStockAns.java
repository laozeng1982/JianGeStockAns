/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jiangestockans;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import stockany.datamodel.GlobalParameters;
import stockany.ui.HelpsTabController;
import stockany.ui.LoadDataTabController;
import stockany.ui.MenuBarController;
import stockany.ui.PatternTabController;
import stockany.ui.RecordsTabController;
import stockany.ui.ReviewTabController;
import stockany.ui.SearchResultsTabController;
import stockany.ui.SettingTabController;
import stockany.ui.controls.ToolBarController;
import tools.utilities.Logs;

/**
 * This the entrance of this application
 *
 * @author JianGe
 */
public class JianGeStockAns extends Application {

    public static MenuBarController menuBarController = new MenuBarController();
    public static ToolBarController toolBarController = new ToolBarController();
    public static LoadDataTabController loadDataTab;
    public static PatternTabController patternTabController;
    public static SearchResultsTabController searchResultsPlotsController;
    public static ReviewTabController reviewTabController;
    public static RecordsTabController recordsTabController;
    public static SettingTabController settingTabController;
    public static HelpsTabController helpsTabController;

    public static final VBox rootPane = new VBox();

    public static Stage primaryStage;

    public void initMainFrame() {
        try {

            GlobalParameters.logsAppTableView.start(GlobalParameters.logsStage);
            GlobalParameters.searchResultsPlots.start(GlobalParameters.resultPlotStage);

        } catch (Exception e) {
            Logs.e(e);
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initMainFrame();
        GlobalParameters.setDefWorkingPaths();

        //Top level
        //Second level Containor
        menuBarController = new MenuBarController();
        toolBarController = new ToolBarController();
        TabPane mainTabPane = new TabPane();

        //Controls
        loadDataTab = new LoadDataTabController();
        patternTabController = new PatternTabController();
        searchResultsPlotsController = new SearchResultsTabController();
        reviewTabController = new ReviewTabController();
        recordsTabController = new RecordsTabController();
        settingTabController = new SettingTabController();
        helpsTabController = new HelpsTabController();

        mainTabPane.getTabs().add(loadDataTab);
        mainTabPane.getTabs().add(patternTabController);
        mainTabPane.getTabs().add(searchResultsPlotsController);
        mainTabPane.getTabs().add(reviewTabController);
        mainTabPane.getTabs().add(recordsTabController);
        mainTabPane.getTabs().add(settingTabController);
        mainTabPane.getTabs().add(helpsTabController);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
//        Logs.e(primaryScreenBounds.getWidth() + "  " + primaryScreenBounds.getHeight());

        mainTabPane.setPrefSize(primaryScreenBounds.getWidth() - 10,
                primaryScreenBounds.getHeight() - menuBarController.getPrefHeight() - toolBarController.getPrefHeight());

        rootPane.getChildren().add(menuBarController);
        rootPane.getChildren().add(toolBarController);
        rootPane.getChildren().add(mainTabPane);

        Scene scene = new Scene(rootPane);
        //Set to full screen
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());

        primaryStage.setTitle("JianGe Stock Assitant");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            Logs.e("System closed!");
            PatternTabController.stopAllThreads();
            Platform.exit();
        });
        JianGeStockAns.primaryStage = primaryStage;
    }

    public void onLogsWinClosed() {
//        controller.onLogsWinClosed();
    }

    public static void onExit() {
        try {

        } catch (Exception ex) {
            Logger.getLogger(JianGeStockAns.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
