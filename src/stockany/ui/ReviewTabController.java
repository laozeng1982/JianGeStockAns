/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import stockany.datamodel.GlobalParameters;
import stockany.datamodel.SingleStock;
import stockany.datamodel.records.ReviewRecords;
import stockany.tools.AlertDialog;
import tools.utilities.DateUtils;
import stockany.tools.Common;
import tools.sqlite.SQLiteJDBC;
import tools.utilities.Logs;

/**
 * FXML Controller class
 *
 * @author JianGe
 */
public class ReviewTabController extends Tab implements Initializable {

    @FXML
    private TableView<ReviewRecords> reviewTableView;
    @FXML
    private TableColumn<ReviewRecords, String> idCol;
    @FXML
    private TableColumn<ReviewRecords, String> stockCodeCol;
    @FXML
    private TableColumn<ReviewRecords, String> stockNameCol;
    @FXML
    private TableColumn<ReviewRecords, String> referenceCol;
    @FXML
    private TableColumn<ReviewRecords, ComboBox> methodCol;
    @FXML
    private TableColumn<ReviewRecords, DatePicker> startDateCol;
    @FXML
    private TableColumn<ReviewRecords, String> startPriceCol;
    @FXML
    private TableColumn<ReviewRecords, DatePicker> endDateCol;
    @FXML
    private TableColumn<ReviewRecords, String> endPriceCol;
    @FXML
    private TableColumn<ReviewRecords, String> profitCol;
    @FXML
    private TableColumn<ReviewRecords, String> efficencyCol;
    @FXML
    private TableColumn<ReviewRecords, String> commentsCol;
    @FXML
    private TableColumn<ReviewRecords, Button> comfirmCol;

    @FXML
    private Button btnSaveReviews;
    @FXML
    private Button btnAddReview;
    @FXML
    private Button btnRefreshReviews;

    private ArrayList<ReviewRecords> recordIterms = new ArrayList<>();
    private ObservableList<ReviewRecords> reviewRecordsList = FXCollections.observableArrayList();

    //For profit and efficency show color
    private Callback<TableColumn<ReviewRecords, String>, TableCell<ReviewRecords, String>> ratiolCellCallback;

    private String[] colNames = new String[6];

    StockFilter stockFilter;
    Stage flterStage = new Stage();

    public ReviewTabController() {
        this.ratiolCellCallback = (TableColumn<ReviewRecords, String> column) -> new TableCell<ReviewRecords, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (Float.valueOf(item) > 0) {
                        setTextFill(Color.RED);
                    } else {
                        setTextFill(Color.GREEN);
                    }
                }
            }
        };

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ReviewTab.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.setClosable(false);

        // Column names to make columns and hold data
        colNames[0] = "StockCode";
        colNames[1] = "StockName";
        colNames[2] = "Reference";
        colNames[3] = "Methods";
        colNames[4] = "ObStartDate";
        colNames[5] = "Comments";

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

        //make iterms
        // add 20 more rows
        Thread readDataThread = new Thread(() -> {
            Logs.e("wait for refreshing");
            while (GlobalParameters.isRefreshingStockList) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ReviewTabController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            recordIterms = readRecords();

            stockFilter = new StockFilter(GlobalParameters.getStockInfoObservableList());

            int size = recordIterms.size();

            for (int i = 1; i <= 30; i++) {
                recordIterms.add(new ReviewRecords(i + size));
            }

            reviewRecordsList = FXCollections.observableArrayList(recordIterms);
            reviewTableView.setItems(reviewRecordsList);
        });

        readDataThread.start();

        reviewTableView.setEditable(true);

        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        stockCodeCol.setCellValueFactory(cellData -> cellData.getValue().stockCodeProperty());
        stockNameCol.setCellValueFactory(cellData -> cellData.getValue().stockNameProperty());
        referenceCol.setCellValueFactory(cellData -> cellData.getValue().obReferenceProperty());
        methodCol.setCellValueFactory(cellData -> cellData.getValue().obMethodPropertys().getComboBox());
        startDateCol.setCellValueFactory(cellData -> cellData.getValue().obStartDatePicker().getObserveDatePicker());
        startPriceCol.setCellValueFactory(cellData -> cellData.getValue().obStartClosePriceProperty());
        endDateCol.setCellValueFactory(cellData -> cellData.getValue().obEndDatePicker().getObserveDatePicker());
        endPriceCol.setCellValueFactory(cellData -> cellData.getValue().obEndClosePriceProperty());
        profitCol.setCellValueFactory(cellData -> cellData.getValue().obProfitProperty());
        efficencyCol.setCellValueFactory(cellData -> cellData.getValue().obEfficiencyProperty());
        commentsCol.setCellValueFactory(cellData -> cellData.getValue().obCommentsProperty());

        stockCodeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        referenceCol.setCellFactory(TextFieldTableCell.forTableColumn());
        commentsCol.setCellFactory(TextFieldTableCell.forTableColumn());
        profitCol.setCellFactory(ratiolCellCallback);
        efficencyCol.setCellFactory(ratiolCellCallback);

        //set column editable
        for (int i = 0; i < reviewTableView.getColumns().size(); i++) {
            reviewTableView.getColumns().get(i).setEditable(true);

        }

//        comfirmCol.setCellValueFactory(cellData -> cellData.getValue().obEfficiencyProperty());
    }

    @FXML
    private void onBtnSaveReviews(ActionEvent event) {
        saveRecords();
    }

    @FXML
    private void onBtnAddReview(ActionEvent event) {
        // make a new row
        reviewRecordsList.add(new ReviewRecords(reviewRecordsList.size()));
    }

    @FXML
    private void onBtnRefreshReviews(ActionEvent event) {
        readRecords();

    }

    @FXML
    private void editStockCode(TableColumn.CellEditEvent<ReviewRecords, String> event) {
        try {
            stockFilter.start(flterStage);
            Thread waitThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (stockFilter.getSelectedStock() == null) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ReviewTabController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        Logs.e(stockFilter.getSelectedStock());
//                        stockCodeCol.getTableView().getSelectionModel().getSelectedItem().setStockCode(stockFilter.getSelectedStock());

                    }
                    onEditConfirm(stockFilter.getSelectedStock());
                }
            });

            waitThread.start();

        } catch (Exception ex) {
            Logger.getLogger(ReviewTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void commitStockCode(TableColumn.CellEditEvent<ReviewRecords, String> event) {
        if (GlobalParameters.isRefreshingStockList) {
            AlertDialog.showErrorDialog(new Stage(), "Please waitting for refreshing stocklist!");
            return;
        }
        onEditConfirm(event.getNewValue());
    }

    private void onEditConfirm(String stockCode) {
        String editingStockCode = stockCode;
        String editingStockName = "";
        String editingClosePrice = "";

//            Logs.e("Old value: " + event.getOldValue() + "  ----> New value: " + event.getNewValue());
        boolean hasThisStock = false;
        LocalDate lastDate = LocalDate.now();

        for (int idx = 0; idx < GlobalParameters.AllStockSetsForCalc.getStocksList().size(); idx++) {
            SingleStock singleStock = GlobalParameters.AllStockSetsForCalc.getStocksList().get(idx);
            int lastPosition = singleStock.getDateList(Common.Stock.DateTypes.Daliy).size() - 1;
            if (singleStock.getStockCode().toLowerCase().equals(editingStockCode.toLowerCase())) {
                editingStockName = singleStock.getStockName();
                editingClosePrice = singleStock.getStockDaliyData().get(singleStock.getStockDaliyData().size() - 1).getClosePrice() + "";
                lastDate = singleStock.getDateList(Common.Stock.DateTypes.Daliy).get(lastPosition).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                hasThisStock = true;
                break;
            }

        }

        if (!hasThisStock) {
            AlertDialog.showErrorDialog(new Stage(), "Don't have this stock code!");
            reviewTableView.getSelectionModel().getSelectedItem().setStockCode("");
            return;
        }

        int row = reviewTableView.getSelectionModel().getSelectedIndex();
        int id = row + 1;

        ReviewRecords newRecord = new ReviewRecords(id, editingStockCode, editingStockName, new Date(), null);

        newRecord.setObStartDate(lastDate);
        newRecord.setObStartClosePrice(editingClosePrice);

        reviewRecordsList.remove(row);

        reviewRecordsList.add(row, newRecord);

        reviewTableView.setItems(reviewRecordsList);

        reviewTableView.getSelectionModel().select(newRecord);

    }

    private ArrayList<ReviewRecords> readRecords() {
        SQLiteJDBC sqliteJDBC = new SQLiteJDBC(GlobalParameters.WORKING_REVIEW_DB_PATH);
        ArrayList<ReviewRecords> outputArrayList = new ArrayList<>();

        ArrayList<ArrayList<String>> reviewList = sqliteJDBC.selectAllColsValue("Review", colNames);
        if (reviewList == null) {
            return null;
        }
        for (int row = 0; row < reviewList.size(); row++) {
//            Logs.e(reviewList.get(row).toString());
            outputArrayList.add(new ReviewRecords(row + 1));
            //from database
//            outputArrayList.get(row).setID(String.valueOf(row + 1));
            outputArrayList.get(row).setStockCode(reviewList.get(row).get(0));
            outputArrayList.get(row).setStockName(reviewList.get(row).get(1));
            outputArrayList.get(row).setObReference(reviewList.get(row).get(2));
            outputArrayList.get(row).setObMethod(reviewList.get(row).get(3));
            outputArrayList.get(row).setObStartDate(DateUtils.stringToLocalDate(reviewList.get(row).get(4), "yyyy-MM-dd"));
            outputArrayList.get(row).setObComments(reviewList.get(row).get(5));

            // dynamic create other items 
            String stockCode = reviewList.get(row).get(0);
            SingleStock singleStock;
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = DateUtils.stringToLocalDate(reviewList.get(row).get(4), "yyyy-MM-dd");
            float startClosePrice = 0;
            float endClosePrice = 0;
            for (int index = 0; index < GlobalParameters.AllStockSetsForCalc.getStocksList().size(); index++) {
                if (GlobalParameters.AllStockSetsForCalc.getStocksList().get(index).getStockCode().toLowerCase().equals(stockCode.toLowerCase())) {
                    singleStock = GlobalParameters.AllStockSetsForCalc.getStocksList().get(index);

                    int lastPosition = singleStock.getDateList(Common.Stock.DateTypes.Daliy).size() - 1;
                    LocalDate lastDate = singleStock.getDateList(Common.Stock.DateTypes.Daliy).get(lastPosition).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    if (startDate.toEpochDay() > lastDate.toEpochDay()) {
                        startClosePrice = Float.valueOf(singleStock.getDataList(Common.Stock.DateTypes.Daliy, Common.Stock.DataTypes.CLOSE_PRICE).get(lastPosition));
                        startDate = lastDate;
                    }

                    if (endDate.toEpochDay() > lastDate.toEpochDay()) {
                        endClosePrice = Float.valueOf(singleStock.getDataList(Common.Stock.DateTypes.Daliy, Common.Stock.DataTypes.CLOSE_PRICE).get(lastPosition));
                        endDate = lastDate;
                    }

                    for (int idx = 0; idx < singleStock.getDateList(Common.Stock.DateTypes.Daliy).size(); idx++) {
                        if (startDate.toEpochDay() == singleStock.getDateList(Common.Stock.DateTypes.Daliy).get(idx).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toEpochDay()) {
                            startClosePrice = Float.valueOf(singleStock.getDataList(Common.Stock.DateTypes.Daliy, Common.Stock.DataTypes.CLOSE_PRICE).get(idx));
                        }

                        if (endDate.toEpochDay() == singleStock.getDateList(Common.Stock.DateTypes.Daliy).get(idx).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toEpochDay()) {
                            endClosePrice = Float.valueOf(singleStock.getDataList(Common.Stock.DateTypes.Daliy, Common.Stock.DataTypes.CLOSE_PRICE).get(idx));
                        }

                    }

                    break;
                }
            }

            float profit = 0;
            float efficency = 0;
            if (startClosePrice > 0 && endClosePrice > 0) {
                profit = (endClosePrice - startClosePrice) * 100 / startClosePrice;

                long days = endDate.toEpochDay() - startDate.toEpochDay();

                if (days > 0) {
                    efficency = profit / days;

                }
            }
            outputArrayList.get(row).setObStartDate(startDate);
            outputArrayList.get(row).setObStartClosePrice(startClosePrice + "");
            outputArrayList.get(row).setObEndDate(endDate);
            outputArrayList.get(row).setObEndClosePrice(endClosePrice + "");
            outputArrayList.get(row).setObProfit(profit + "");
            outputArrayList.get(row).setObEfficiency(efficency + "");
        }

        return outputArrayList;
    }

    /**
     * Save records to SQLite database
     */
    private void saveRecords() {
//        for (int i = 0; i < reviewRecords.size(); i++) {
//            ReviewRecords get = reviewRecords.get(i);
//            if (!get.toString().startsWith("null")) {
//                Logs.e(get.toString());
//            }
//
//        }

        SQLiteJDBC sqliteJDBC = new SQLiteJDBC(GlobalParameters.WORKING_REVIEW_DB_PATH);
        StringBuilder sqlTableItems = new StringBuilder();

        sqlTableItems.append(" (").append("StockCode").append(" TEXT PRIMARY KEY NOT NULL,")
                .append("StockName").append(" TEXT, ")
                .append("Reference").append(" TEXT, ")
                .append("Methods").append(" TEXT, ")
                .append("ObStartDate").append(" TEXT, ")
                .append("Comments").append(" TEXT)");

//        sqlTableItems.append(" (").append("StockCode").append(" TEXT PRIMARY KEY NOT NULL,")
//                .append("StockName").append(" TEXT, ")
//                .append("Reference").append(" TEXT, ")
//                .append("Methods").append(" TEXT, ")
//                .append("ObStartDate").append(" TEXT, ")
//                .append("ObStartPrice").append(" TEXT, ")
//                .append("ObEndDate").append(" TEXT, ")
//                .append("ObEndPrice").append(" TEXT, ")
//                .append("Profit").append(" TEXT, ")
//                .append("Efficency").append(" TEXT, ")
//                .append("Comments").append(" TEXT)");
        String tableName = "Review";
        sqliteJDBC.createTable(tableName, sqlTableItems.toString());
        sqliteJDBC.insertColumns(tableName, colNames, getListData());

    }

    private Map<String, ArrayList<String>> getMapData() {
        Map<String, ArrayList<String>> mapData = new HashMap<>();

        return mapData;
    }

    private ArrayList< ArrayList<String>> getListData() {
        ArrayList< ArrayList<String>> listData = new ArrayList<>();

        for (int i = 0; i < reviewRecordsList.size(); i++) {
            if (reviewRecordsList.get(i).toStringArrayList() != null) {
                listData.add(reviewRecordsList.get(i).toStringArrayList());
            }

        }

        return listData;
    }

    public TableView<ReviewRecords> getReviewTableView() {
        return reviewTableView;
    }

    public void setReviewTableView(TableView<ReviewRecords> reviewTableView) {
        this.reviewTableView = reviewTableView;
    }

    public ObservableList<ReviewRecords> getReviewRecords() {
        return reviewRecordsList;
    }

    public void setReviewRecords(ObservableList<ReviewRecords> reviewRecords) {
        this.reviewRecordsList = reviewRecords;
    }

    public TableColumn<ReviewRecords, DatePicker> getStartDateCol() {
        return startDateCol;
    }

    public void setStartDateCol(TableColumn<ReviewRecords, DatePicker> startDateCol) {
        this.startDateCol = startDateCol;
    }

    public TableColumn<ReviewRecords, DatePicker> getEndDateCol() {
        return endDateCol;
    }

    public void setEndDateCol(TableColumn<ReviewRecords, DatePicker> endDateCol) {
        this.endDateCol = endDateCol;
    }

}
