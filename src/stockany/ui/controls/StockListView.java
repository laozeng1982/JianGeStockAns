/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui.controls;

import java.util.ArrayList;
import java.util.function.Predicate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import stockany.datamodel.SingleStock;

/**
 *
 * @author JianGe
 */
public class StockListView extends VBox {

    private final Label infoLabel;
    private final HBox subHBox;
    private final Label label;
    private final TextField txtF_Filter;

    private TableView<SingleStock> stockTableView;
    private TableColumn<SingleStock, String> stockIdCol;
    private TableColumn<SingleStock, CheckBox> stockSelectedCol;
    private TableColumn<SingleStock, String> stockItermCountCol;
    private TableColumn<SingleStock, String> stockStockCodeCol;
    private TableColumn<SingleStock, String> stockStockNameCol;

    public StockListView(ObservableList list, boolean hasChkBox) {
        infoLabel = new Label("Stock sets: ");
        subHBox = new HBox();
        label = new Label("Filter: ");
        txtF_Filter = new TextField();

        stockTableView = new TableView<>(getSortedListByNameOrCode(list));

        stockIdCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        stockItermCountCol.setCellValueFactory(cellData -> cellData.getValue().stockSizeProperty());
        stockStockCodeCol.setCellValueFactory(cellData -> cellData.getValue().stockCodeProperty());
        stockStockNameCol.setCellValueFactory(cellData -> cellData.getValue().stockNameProperty());
        stockSelectedCol.setCellValueFactory(cellData -> cellData.getValue().getChkBox().getCheckBox());
        
        

        if (hasChkBox) {
            stockTableView.getColumns().addAll(stockIdCol, stockSelectedCol, stockItermCountCol, stockStockCodeCol, stockStockNameCol);
            stockTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            stockTableView.getSelectionModel().selectAll();
        } else {
            stockTableView.getColumns().addAll(stockIdCol, stockItermCountCol, stockStockCodeCol, stockStockNameCol);
            stockTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        }

        pack();

    }

    private void pack() {
        subHBox.getChildren().add(label);
        subHBox.getChildren().add(txtF_Filter);
        txtF_Filter.setDisable(true);
        this.getChildren().addAll(infoLabel, subHBox, stockTableView);
    }

    private SortedList<SingleStock> getSortedListByNameOrCode(ObservableList list) {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<SingleStock> filteredData;
        filteredData = new FilteredList<>(list, (SingleStock stock) -> true);
        // 2. Set the filter Predicate whenever the filter changes.
        txtF_Filter.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filteredData.setPredicate(new Predicate<SingleStock>() {
                    //this will test the whole list
                    @Override
                    public boolean test(SingleStock stock) {
                        // If filter text is empty, display all stocks.
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        // Compare stock code with filter text.
                        String lowerCaseFilter = newValue.toLowerCase();

                        if (stock.getStockNameHead().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                            return true; // Filter matches stockcode.
                        } else if (stock.getStockCode().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                            return true;
                        }
                        return false; // Does not match.
                    }
                });
            }
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<SingleStock> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
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

    public void setTableViewIterms(ObservableList list) {
        stockTableView.setItems(getSortedListByNameOrCode(list));
    }

    public void clearAllTableViewData() {
        stockTableView.getItems().clear();
        stockIdCol.getColumns().clear();
        stockStockCodeCol.getColumns().clear();
        stockStockNameCol.getColumns().clear();
        stockSelectedCol.getColumns().clear();
    }

    public TableView<SingleStock> getStockTableView() {
        return stockTableView;
    }

}
