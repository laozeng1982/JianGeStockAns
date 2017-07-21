/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import stockany.datamodel.GlobalParameters;
import stockany.tools.Common;
import stockany.datamodel.LogInformation;
import tools.utilities.StringUtils;
import stockany.ui.controls.StocksTableViewController;
import tools.sqlite.SQLiteJDBC;
import tools.utilities.Logs;

/**
 * FXML Controller class
 *
 * @author JianGe
 */
public class SearchResultsTabController extends Tab implements Initializable {

    private String dataSourcePath = GlobalParameters.WORKING_DALIY_DATA_ASC_PATH;
    private String resultsPath = GlobalParameters.WORKING_CORR_ASC_PATH;

    private String matchedStockCode;
    private String matchedStockName;
    private Date matchedStartDate;
    private Date matchedEndDate;

    private String sourceStockCode;
    private String sourceStockName;
    private Date sourceStartDate;
    private Date sourceEndDate;
    private int showLength = 0;
    private boolean isSelectCorrect = false;
    private static TextFieldTableCell<Task, String> lastCell = new TextFieldTableCell<>();

    private ArrayList<String> columnNameList = new ArrayList<>();

    @FXML
    private TableView searchResultsTableView;
    @FXML
    private StocksTableViewController stockListTableView;
    @FXML
    private ComboBox cmBx_SearchStockCode;
    @FXML
    private ComboBox cmBx_SearchStockName;
    @FXML
    private ImageView imageIcon;
    @FXML
    private RadioButton rdBtn_AscFileData;
    @FXML
    private RadioButton rdBtn_BinFileData;
    @FXML
    private RadioButton rdBtn_DataBase;
    @FXML
    private MenuItem muItm_AddToReview;
    @FXML
    private CheckBox chkBx_FilterSameDate;

    public SearchResultsTabController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SearchResultsTab.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.setClosable(false);
        this.dataSourcePath = GlobalParameters.WORKING_DALIY_DATA_ASC_PATH;
        this.resultsPath = GlobalParameters.WORKING_CORR_ASC_PATH;

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

    public void RefreshTableView() {

        if (GlobalParameters.AllStockSetsForCalc.getStocksList().size() > 0) {
            stockListTableView.setTableViewItems(GlobalParameters.getStockInfoObservableList(), false);

            ToggleGroup toggleGroup = new ToggleGroup();
            rdBtn_AscFileData.setToggleGroup(toggleGroup);
            rdBtn_BinFileData.setToggleGroup(toggleGroup);
            rdBtn_DataBase.setToggleGroup(toggleGroup);

            cmBx_SearchStockCode.setItems(FXCollections.observableArrayList(GlobalParameters.AllStockSetsForCalc.getStockCodeList()));
            cmBx_SearchStockName.setItems(FXCollections.observableArrayList(GlobalParameters.AllStockSetsForCalc.getStockNameList()));

        }
    }

    @FXML
    private void onSearchTableClicked(MouseEvent event) {
        //clear status

        TableView thisTableView = (TableView) event.getSource();
        int matchedRowNunmber = thisTableView.getSelectionModel().getSelectedIndex();
//        Logs.e("Selected row: " + rowNunmber + " Selected column: " + colNumber);
        //Fisrt Sort
        if (matchedRowNunmber != -1 && isSelectCorrect) {
            Map rowDataMap = (HashMap) thisTableView.getItems().get(matchedRowNunmber);
            matchedStockCode = rowDataMap.get("StockCode").toString();
            matchedStockName = rowDataMap.get("StockName").toString();
            Logs.e("Matched StockCode: " + matchedStockCode);
            Logs.e("Matched StockName: " + matchedStockName);

            sourceStockCode = cmBx_SearchStockCode.getSelectionModel().getSelectedItem().toString();
            sourceStockName = cmBx_SearchStockName.getSelectionModel().getSelectedItem().toString();

            if (showLength == 0) {
                Logs.e("show length erro");
                return;
            }

            //prepare matched data
            matchedEndDate = getEndDate(matchedStockCode, matchedStartDate, showLength);
            sourceEndDate = getEndDate(sourceStockCode, sourceStartDate, showLength);
            Logs.e("Matched EndDate: " + Common.DATE_OF_READIN_DALIY_SDF.format(matchedEndDate));
            GlobalParameters.searchResultsPlots.updateSelectedSourcePlots(sourceStockCode, sourceStockCode, sourceStockName, sourceStartDate, showLength, sourceEndDate, true);
            GlobalParameters.searchResultsPlots.updateSelectedMatchedPlots(sourceStockCode, matchedStockCode, matchedStockName, matchedStartDate, showLength, matchedEndDate, true);

        }
        isSelectCorrect = false;
    }

    private Date getEndDate(String stockCode, Date startDate, int length) {
        Date endDate = null;
        ArrayList<Date> dateList = GlobalParameters.AllStockSetsForCalc.getSingleStockByCode(stockCode).getDateList(Common.Stock.DateTypes.Daliy);
        for (int idx = 0; idx < dateList.size(); idx++) {
            if (dateList.get(idx).equals(startDate)) {
                if ((idx + length) < dateList.size()) {
                    endDate = dateList.get(idx + length);
                } else {
                    endDate = dateList.get(dateList.size() - 1);
                }
            }

        }

        return endDate;
    }

    public void onStockListTableClicked(String stockCode) {

        if (stockCode != null && !stockCode.isEmpty()) {
            cmBx_SearchStockCode.getSelectionModel().select(stockCode);
        } else {
        }

    }

    @FXML
    private void onCmBxCodeScrolled(ScrollEvent event) {
    }

    @FXML
    private void onCmBxNameScrolled(ScrollEvent event) {
    }

    @FXML
    private void onCmBxSearchStockCode(ActionEvent event) {
        String stockCode = cmBx_SearchStockCode.getSelectionModel().getSelectedItem().toString();
        String stockName = GlobalParameters.AllStockSetsForCalc.getSingleStockByCode(stockCode).getStockName();
        Common.onCmBxCodeChanged(cmBx_SearchStockCode, cmBx_SearchStockName);

        comboxActionOnResultsPage(stockCode, stockName);
    }

    @FXML
    private void onCmBxSearchStockName(ActionEvent event) {
        Common.onCmBxNameChanged(cmBx_SearchStockCode, cmBx_SearchStockName);
    }

    /**
     * This method only for the Combo Box was selected or the StockCode List was
     * clicked and item was selected.
     *
     * @param stockCode
     * @param stockName
     */
    private void comboxActionOnResultsPage(String stockCode, String stockName) {

        //Prepare for 
        searchResultsTableView.getItems().clear();
        ObservableList<Map<String, String>> tableItems = getMapObservableList(stockCode, chkBx_FilterSameDate.isSelected());

        if (tableItems == null) {
            GlobalParameters.logErro(LogInformation.Category.FILES, "There is no correlation curve about " + stockCode + ",  " + stockName);
            Logs.e("There is no correlation curve about " + stockCode + ",  " + stockName);
            return;

        } else {
            int colNumber = tableItems.get(0).size();
            searchResultsTableView.getItems().clear();
            searchResultsTableView.getItems().addAll(tableItems);
            searchResultsTableView.getColumns().clear();

            for (int idx = 0; idx < colNumber; idx++) {
                TableColumn tableColumn = new TableColumn(columnNameList.get(idx));
                if (idx == 0) {
                    tableColumn.setMinWidth(50);
                }
                tableColumn.setCellValueFactory(new MapValueFactory<>(columnNameList.get(idx)));
                //if it's not correlation column, don't let it add listener
                if (tableColumn.getText().contains("Corr")) {
                    tableColumn.setCellFactory(new TaskCellFactory());

                } else {
                    tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

                }

                searchResultsTableView.getColumns().add(tableColumn);
            }
//            searchResultsTableView.setEditable(true);
        }

        searchResultsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //Plot original stock data        
        GlobalParameters.searchResultsPlots.updateSourcePlots(stockCode, stockName, null, 5000, null, true);
    }

    private ObservableList<Map<String, String>> getMapObservableList(String stockCode, boolean isFilter) {
        ObservableList<Map<String, String>> items
                = FXCollections.<Map<String, String>>observableArrayList();
        // Extract the stock data, add the data to a Map, and add the Map to
        // the items list
        if (rdBtn_DataBase.isSelected()) {
            SQLiteJDBC sqliteJDBC = new SQLiteJDBC(resultsPath);
            ArrayList<String> columnNames = new ArrayList<>();

            Map<String, ArrayList<String>> values = sqliteJDBC.selectColsValueByNameList(stockCode, columnNames);

            if (!sqliteJDBC.isOperationSucessed()) {
                GlobalParameters.logErro(LogInformation.Category.FILES, "Get SQLite Erro!");
                Logs.e("Get SQLite Erro!");
                return null;
            }

            for (int idx = 0; idx < values.get(Common.Stock.Infos.STOCKCODE).size(); idx++) {

                Map<String, String> map = new HashMap<>();
                map.put(Common.Stock.Infos.ID, String.valueOf(idx + 1));
                map.put(Common.Stock.Infos.STOCKCODE, values.get(Common.Stock.Infos.STOCKCODE).get(idx));
                map.put(Common.Stock.Infos.STOCKNAME, values.get(Common.Stock.Infos.STOCKNAME).get(idx));
                map.put(Common.Stock.Infos.BEST_CORR, values.get(Common.Stock.Infos.BEST_CORR).get(idx));
                map.put(Common.Stock.Infos.BEST_MATCHED_DATE, values.get(Common.Stock.Infos.BEST_MATCHED_DATE).get(idx));
                map.put(Common.Stock.Infos.DETAILS, values.get(Common.Stock.Infos.DETAILS).get(idx));
                items.add(map);
            }
            GlobalParameters.logWarn(LogInformation.Category.FILES, "Sorry, This version may not support database files");
            Logs.e("Sorry, This version may not support database files");
            rdBtn_AscFileData.setSelected(true);
        } else if (rdBtn_BinFileData.isSelected()) {
            GlobalParameters.logWarn(LogInformation.Category.FILES, "Sorry, This version may not support Binary files");
            Logs.e("Sorry, This version may not support Binary files");
            rdBtn_AscFileData.setSelected(true);
            return null;
        } else if (rdBtn_AscFileData.isSelected()) {
            // read data from file
            ArrayList<ArrayList<String>> curvesList = Common.readAsciiFile(resultsPath + "\\" + stockCode + ".dat", "GB2312");
            ArrayList<ArrayList<String>> curvesFilteredList = new ArrayList<>();
            columnNameList.clear();

            // if read successful
            if (curvesList != null) {

                if (isFilter) {
                    int stockCodeRowIndex;
                    int stockCodeColumIndex;

                    // Init filtered list
                    for (int i = 0; i < curvesList.size(); i++) {
                        ArrayList<String> colum = new ArrayList<>();
                        colum.add(curvesList.get(i).get(0));
                        curvesFilteredList.add(colum);
//                        Logs.e(colum.get(0));
                    }

                    // prepare source stock end date and it's index
                    ArrayList<MapKey> sourceStockEndDateList = new ArrayList<>();
                    // stock code is at colum 1, index is 0
                    for (int index = 0; index < curvesList.get(0).size(); index++) {
                        if (curvesList.get(0).get(index).equals(stockCode)) {
                            for (int idx = 0; idx < curvesList.size(); idx++) {
                                // get the colum contains "Date"
                                if (curvesList.get(idx).get(0).contains("Date")) {
                                    MapKey map = new MapKey(idx, curvesList.get(idx).get(index));

                                    sourceStockEndDateList.add(map);

                                }
                            }
                            break;
                        }
                    }

//                    sourceStockEndDateList.forEach((MapKey t) -> {
//                        Logs.e(t.toString());
//                    });
                    for (int index = 0; index < sourceStockEndDateList.size(); index++) {
                        int columIndex = sourceStockEndDateList.get(index).getIndex();
                        String date = sourceStockEndDateList.get(index).getDate();
                        try {
                            Date sourceDate = Common.DATE_OF_READIN_DALIY_SDF.parse(date);
                            for (int row = 1; row < curvesList.get(columIndex).size(); row++) {

                                Date compareDate = Common.DATE_OF_READIN_DALIY_SDF.parse(curvesList.get(columIndex).get(row));
                                // If it's the stock, skip
                                if (curvesList.get(0).get(row).equals(stockCode)) {
                                    continue;
                                }
                                if (compareDate.getTime() >= sourceDate.getTime()) {
//                                    Logs.e(row + "," + curvesList.get(0).get(row) + ", " + curvesList.get(columIndex).get(row));

                                    for (int i = 0; i < curvesList.size(); i++) {
                                        curvesList.get(i).remove(row);
//                                        curvesFilteredList.get(i).add(curvesList.get(i).get(row));

                                    }
                                }

                            }
                        } catch (ParseException e) {
                        }

                    }
                    curvesFilteredList = curvesList;
                } else {
                    curvesFilteredList = curvesList;
                }

                items = Common.getMapObListFromArrayList(curvesFilteredList);
                for (int i = 0; i < curvesFilteredList.size(); i++) {
                    columnNameList.add(curvesFilteredList.get(i).get(0));
                }
//                items = Utils.getMapObListFromArrayList(curvesList);
//                for (int i = 0; i < curvesList.size(); i++) {
//                    columnNameList.add(curvesList.get(i).get(0));
//                }
            } else {
                GlobalParameters.logErro(LogInformation.Category.FILES, "Read Ascii file erro!");
                Logs.e("Read Ascii file erro!");
                return null;
            }
        }

        return items;
    }

    @FXML
    private void onAscFileDataMode(ActionEvent event) {
        if (rdBtn_AscFileData.isSelected()) {
            resultsPath = GlobalParameters.WORKING_CORR_ASC_PATH;
            GlobalParameters.logInfo(LogInformation.Category.FILES, "Selected result path: " + resultsPath);
        }
    }

    @FXML
    private void onBinFileDataMode(ActionEvent event) {
        if (rdBtn_BinFileData.isSelected()) {
            resultsPath = GlobalParameters.WORKING_CORR_ASC_PATH;
            GlobalParameters.logInfo(LogInformation.Category.FILES, "Selected result path: " + resultsPath);
        }
    }

    @FXML
    private void onDataBaseMode(ActionEvent event) {
        if (rdBtn_DataBase.isSelected()) {
            resultsPath = GlobalParameters.WORKING_CORR_DB_PATH;
            GlobalParameters.logInfo(LogInformation.Category.FILES, "Selected result path: " + resultsPath);
        }
    }

    @FXML
    private void onMuItmAddToReview(ActionEvent event) {
    }

    @FXML
    private void onChkBxFilterSameDate(ActionEvent event) {
        sourceStockCode = cmBx_SearchStockCode.getSelectionModel().getSelectedItem().toString();
        sourceStockName = cmBx_SearchStockName.getSelectionModel().getSelectedItem().toString();

        comboxActionOnResultsPage(sourceStockCode, sourceStockName);

    }

    private class TaskCellFactory implements Callback<TableColumn<Task, String>, TableCell<Task, String>> {

        @Override
        public TableCell<Task, String> call(TableColumn<Task, String> param) {
            TextFieldTableCell<Task, String> cell = new TextFieldTableCell<>();
            cell.setOnMouseClicked((MouseEvent evt) -> {
                showLength = StringUtils.getNumeric(cell.getTableColumn().getText());
                GlobalParameters.logInfo(LogInformation.Category.ANALY,
                        cmBx_SearchStockCode.getSelectionModel().getSelectedItem().toString() + "'s correlation length is: " + showLength);
                Logs.e("ShowDate Length: " + showLength);

                if (lastCell != null) {
                    lastCell.setTextFill(Color.BLACK);
                    lastCell.setStyle("-fx-background-color: white");
                }

                cell.setTextFill(Color.BLACK);
                cell.setStyle("-fx-background-color: red");
                lastCell = cell;

                int colNumber = cell.getTableView().getColumns().size();

                int columnID = 0;
                //Get the searched start date and column ID
                for (int idx = 0; idx < colNumber; idx++) {
                    String columnName = cell.getTableView().getColumns().get(idx).getText();
                    String matchedStartDateString = cell.getTableView().getColumns().get(idx).getCellData(cell.getIndex()).toString();
                    if (columnName.contains(showLength + "Date")) {
                        try {
                            matchedStartDate = Common.DATE_OF_OUTPUT_MIN_SDF.parse(matchedStartDateString);
                            columnID = idx;
//                        Logs.e("Rows are: " + cell.getTableView().getColumns().get(idx).getText());
                            Logs.e("MatchedStartDate: " + matchedStartDateString);
                            break;
                        } catch (ParseException ex) {
                            Logger.getLogger(SearchResultsTabController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
                //Get the source start date using the column ID
                for (int idx = 0; idx < cell.getTableView().getItems().size(); idx++) {
                    Map temp = (HashMap) searchResultsTableView.getItems().get(idx);
                    if (temp.get("StockCode").equals(cmBx_SearchStockCode.getSelectionModel().getSelectedItem().toString())) {
                        String sourceStartDateString = cell.getTableView().getColumns().get(columnID).getCellData(idx).toString();
                        try {
                            sourceStartDate = Common.DATE_OF_OUTPUT_MIN_SDF.parse(sourceStartDateString);
                        } catch (ParseException ex) {
                            Logger.getLogger(SearchResultsTabController.class.getName()).log(Level.SEVERE, null, ex);
                        }

//                        Logs.e("Rows are: " + cell.getTableView().getColumns().get(idx).getText());
                        Logs.e("SourceStartDate: " + sourceStartDateString);
                        break;
                    }

                }
                isSelectCorrect = true;
            });

            return cell;
        }
    }

    class MapKey {

        int index;
        String Date;

        public MapKey(int idx, String date) {
            this.index = idx;
            this.Date = date;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String Date) {
            this.Date = Date;
        }

        @Override
        public String toString() {
            return index + " , " + Date;
        }
    }
}
