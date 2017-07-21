/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import stockany.ui.controls.StocksTableViewController;

/**
 * FXML Controller class
 *
 * @author JianGe
 */
public class StockFilterController implements Initializable {

    @FXML
    private StocksTableViewController stockListTableView;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public StocksTableViewController getStockListTableView() {
        return stockListTableView;
    }

    public void setStockListTableView(StocksTableViewController stockListTableView) {
        this.stockListTableView = stockListTableView;
    }
    
}
