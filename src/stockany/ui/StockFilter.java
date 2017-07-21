/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tools.utilities.Logs;
import stockany.datamodel.SingleStock;

/**
 *
 * @author JianGe
 */
public class StockFilter extends Application {

    private final String title = "StockFilter......";
    private StockFilterController controller;
    private ObservableList<SingleStock> stockList;
    public static EventHandler closeEvent = new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent event) {
            //do nothing
            Logs.e(event.toString());
        }
    };

    public StockFilter(ObservableList list) {
        stockList = list;
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StockFilter.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        
        controller.getStockListTableView().setTableViewItems(stockList, false);

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
//                JianGeStockAns.toolBarController.getTgBtnShowLog().setSelected(false);
            }
        });

//        stage.alwaysOnTopProperty();
        stage.show();
    }

    public String getSelectedStock() {
        return controller.getStockListTableView().getSelectedStock();
    }
    
}
