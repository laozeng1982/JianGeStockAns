/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import stockany.datamodel.records.HoldRecords;
import stockany.datamodel.records.ReviewRecords;
import stockany.datamodel.records.TreadRecords;

/**
 * FXML Controller class
 *
 * @author JianGe
 */
public class RecordsTabController extends Tab implements Initializable {

    @FXML
    private Button btnAddHold;
    @FXML
    private Button btnAddHoldSave;
    @FXML
    private TableView<HoldRecords> holdsTableView;
    @FXML
    private TableColumn<TreadRecords, String> holdIdCol;
    @FXML
    private TableColumn<HoldRecords, DatePicker> holdDateCol;
    @FXML
    private TableColumn<HoldRecords, String> holdStockCodeCol;
    @FXML
    private TableColumn<HoldRecords, String> holdStockNameCol;
    @FXML
    private TableColumn<HoldRecords, String> holdHoldCol;
    @FXML
    private TableColumn<HoldRecords, String> holdCostPriceCol;
    @FXML
    private TableColumn<HoldRecords, String> holdCurrentPriceCol;
    @FXML
    private TableColumn<HoldRecords, String> holdTotalCostCol;
    @FXML
    private TableColumn<HoldRecords, String> holdMarketValueCol;
    @FXML
    private TableColumn<HoldRecords, String> holdPorfitsCol;
    @FXML
    private TableColumn<HoldRecords, String> holdPercentageCol;
    @FXML
    private TableView<TreadRecords> tradeRecordstableView;
    @FXML
    private TableColumn<TreadRecords, String> tradeIdCol;
    @FXML
    private TableColumn<TreadRecords, DatePicker> tradeDateCol;
    @FXML
    private TableColumn<TreadRecords, String> tradeTimeCol;
    @FXML
    private TableColumn<TreadRecords, String> tradeHourCol;
    @FXML
    private TableColumn<TreadRecords, String> tradeMinuteCol;
    @FXML
    private TableColumn<TreadRecords, String> tradeStockCodeCol;
    @FXML
    private TableColumn<TreadRecords, String> tradeStockNameCol;
    @FXML
    private TableColumn<TreadRecords, String> tradeFlagCol;
    @FXML
    private TableColumn<TreadRecords, String> tradePriceCol;
    @FXML
    private TableColumn<TreadRecords, String> tradeAmountCol;
    @FXML
    private TableColumn<TreadRecords, String> tradeTransferFeeCol;
    @FXML
    private TableColumn<TreadRecords, String> tradeCommissionFeeCol;
    @FXML
    private TableColumn<TreadRecords, String> tradeTaxCol;
    @FXML
    private TableColumn<TreadRecords, String> tradeTotalCostCol;
    @FXML
    private TableColumn<TreadRecords, String> commentsCol;
    @FXML
    private Button btnSaveRecords;
    @FXML
    private Button btnAddTreadRecords;

    private ArrayList<TreadRecords> tradeRecordIterms = new ArrayList<>();
    private ObservableList<ReviewRecords> tradeRecordsList = FXCollections.observableArrayList();

    private ArrayList<TreadRecords> holdRecordIterms = new ArrayList<>();
    private ObservableList<ReviewRecords> holdRecordsList = FXCollections.observableArrayList();

    //For profit and efficency
    private Callback<TableColumn<ReviewRecords, String>, TableCell<ReviewRecords, String>> ratiolCellCallback;

    private String[] colNames = new String[6];

    public RecordsTabController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RecordsTab.fxml"));
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
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void onBtnAddHold(ActionEvent event) {
    }

    @FXML
    private void onBtnAddHoldSave(ActionEvent event) {
    }

    @FXML
    private void onBtnSaveRecords(ActionEvent event) {
    }

    @FXML
    private void onBtnAddTreadRecords(ActionEvent event) {
    }

}
