/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui.controls;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import stockany.datamodel.SingleStock;
import stockany.ui.SearchResultsTabController;
import tools.utilities.Logs;

/**
 * FXML Controller class
 *
 * @author JianGe
 */
public class StocksTableViewController extends VBox implements Initializable {

    @FXML
    private TextField txtF_Filter;
    @FXML
    private TableView<SingleStock> stockTableView;
    @FXML
    private TableColumn<SingleStock, String> stockIdCol;
    @FXML
    private TableColumn<SingleStock, CheckBox> stockSelectedCol;
    @FXML
    private TableColumn<SingleStock, String> stockItemCountCol;
    @FXML
    private TableColumn<SingleStock, String> stockCodeCol;
    @FXML
    private TableColumn<SingleStock, String> stockNameCol;

    private String selectedStock = null;

    public StocksTableViewController() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StocksTableView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setTableViewItems(ObservableList<SingleStock> list, boolean hasChkBox) {
        stockTableView.setItems(getSortedListByNameOrCode(list));
        try {
            if (hasChkBox) {
                stockTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                setAllSelected(hasChkBox);
            } else {
                Platform.runLater(() -> {
                    stockTableView.getColumns().remove(1);
                });

                stockTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            }
        } catch (Exception e) {
        }

    }

    public void setAllSelected(boolean allSelected) {
        stockTableView.getSelectionModel().selectAll();
        setFilterDisable(allSelected);
        stockTableView.setDisable(allSelected);

        for (int i = 0; i < stockTableView.getItems().size(); i++) {
            stockTableView.getItems().get(i).getChkBox().setSelected(allSelected);
        }

        if (!allSelected) {
            stockTableView.getSelectionModel().clearSelection();
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
        stockIdCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        stockItemCountCol.setCellValueFactory(cellData -> cellData.getValue().stockSizeProperty());
        stockCodeCol.setCellValueFactory(cellData -> cellData.getValue().stockCodeProperty());
        stockNameCol.setCellValueFactory(cellData -> cellData.getValue().stockNameProperty());
        stockSelectedCol.setCellValueFactory(cellData -> cellData.getValue().getChkBox().getCheckBox());

        stockIdCol.setCellFactory(TextFieldTableCell.forTableColumn());
        stockItemCountCol.setCellFactory(TextFieldTableCell.forTableColumn());
        stockCodeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        stockNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        stockSelectedCol.setCellFactory(new Callback<TableColumn<SingleStock, CheckBox>, TableCell<SingleStock, CheckBox>>() {
//                    public TableCell<SingleStock, CheckBox> call(
//                            TableColumn<SingleStock, Boolean> param) {
//                        CheckBoxTableCell<SingleStock, CheckBox> cell = new CheckBoxTableCell<>();
//                        cell.setAlignment(Pos.CENTER);
//                        return cell;
//                    }
//                });
    }

    @FXML
    public String onStockListTableClicked(MouseEvent event) {
        int selectedRowNumber = stockCodeCol.getTableView().getSelectionModel().getSelectedIndex();
        //Fisrt Sort

        String stockCode = "";

        if (selectedRowNumber != -1) {
            stockCode = stockCodeCol.getCellData(selectedRowNumber);
            selectedStock = stockCodeCol.getCellData(selectedRowNumber);
        }

        // 1. Find the TabPane
        AnchorPane father = (AnchorPane) this.getParent();

        AnchorPane grandpa = (AnchorPane) father.getParent();

        if (grandpa != null && grandpa.getParent() != null) {
            TabPane tabpane = (TabPane) grandpa.getParent().getParent();

            // 2. Get the Tab of the TabPane, then involke the method
            SearchResultsTabController searchResultsTabController = null;

            // 3. Compare with current selected tab, if current selected tab is a SearchResultsTabController, show chart plot
            if (tabpane.getSelectionModel().getSelectedItem() instanceof SearchResultsTabController) {
                searchResultsTabController = (SearchResultsTabController) tabpane.getSelectionModel().getSelectedItem();
            }

            if (searchResultsTabController != null && stockCode != null) {
                searchResultsTabController.onStockListTableClicked(stockCode);
            }
        }

        return stockCode;
    }

    private SortedList<SingleStock> getSortedListByNameOrCode(ObservableList list) {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<SingleStock> filteredData;
        filteredData = new FilteredList<>(list, (SingleStock stock) -> true);
        // 2. Set the filter Predicate whenever the filter changes.
        txtF_Filter.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            filteredData.setPredicate((SingleStock stock) -> {
                // If filter text is empty, display all stocks.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare stock code with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (stock.getStockNameHead().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches stockcode.
                } else if (stock.getStockCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false; // Does not match.
            } //this will test the whole list
            );
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<SingleStock> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator. Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(stockTableView.comparatorProperty());

        return sortedData;
    }

    public ArrayList<String> getSelectedStocksCodeList() {
        ArrayList<String> selectedStocksCodeList = new ArrayList<>();
        for (int i = 0; i < stockTableView.getItems().size(); i++) {
            if (stockTableView.getItems().get(i).getChkBox().isSelected()) {
                selectedStocksCodeList.add(stockTableView.getItems().get(i).getStockCode());
            }

        }
        return selectedStocksCodeList;
    }

    public ArrayList<String> getSelectedStocksNameList() {
        ArrayList<String> selectedStocksNameList = new ArrayList<>();
        for (int i = 0; i < stockTableView.getItems().size(); i++) {
            if (stockTableView.getItems().get(i).getChkBox().isSelected()) {
                selectedStocksNameList.add(stockTableView.getItems().get(i).getStockName());
            }

        }
        return selectedStocksNameList;
    }

    public TableView<SingleStock> getStockTableView() {
        return stockTableView;
    }

    public TableColumn<SingleStock, String> getStockIdCol() {
        return stockIdCol;
    }

    public TableColumn<SingleStock, CheckBox> getStockSelectedCol() {
        return stockSelectedCol;
    }

    public TableColumn<SingleStock, String> getStockItemCountCol() {
        return stockItemCountCol;
    }

    public TableColumn<SingleStock, String> getStockCodeCol() {
        return stockCodeCol;
    }

    public TableColumn<SingleStock, String> getStockNameCol() {
        return stockNameCol;
    }

    public void setFilterDisable(boolean disable) {
        txtF_Filter.setDisable(disable);
    }

    public void clearAllTableViewData() {
//        stockTableView.getItems().clear();
//        stockIdCol.getColumns().clear();
//        stockCodeCol.getColumns().clear();
//        stockNameCol.getColumns().clear();
//        stockSelectedCol.getColumns().clear();
    }

    public String getSelectedStock() {
        return selectedStock;
    }

}
