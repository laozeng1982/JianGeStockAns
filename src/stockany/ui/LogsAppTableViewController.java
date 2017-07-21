/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui;

import stockany.datamodel.LogInformation;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import tools.files.IOHelper;
import tools.utilities.Logs;

/**
 * FXML Controller class
 *
 * @author JianGe
 */
public class LogsAppTableViewController implements Initializable {

    private static final StringBuilder logsInformation = new StringBuilder();
    private static final String LOG_FILE_LOCATION = ".\\OperationLogs.log";
    private static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat SDF = new SimpleDateFormat(dateTimePattern);

    @FXML
    private Button btnClear;
    @FXML
    private CheckBox chkBx_AutoSave;
    @FXML
    private Button btnSaveLog;
    @FXML
    private TableView<LogInformation> tableViewInfo;
    @FXML
    private TableColumn<LogInformation, String> IdCol;
    @FXML
    private TableColumn<LogInformation, String> timeCol;
    @FXML
    private TableColumn<LogInformation, String> typeCol;
    @FXML
    private TableColumn<LogInformation, String> categoryCol;
    @FXML
    private TableColumn<LogInformation, String> infoCol;
    @FXML
    private Button btnBrowsLogs;
    @FXML
    private Label lbl_SelectRowCount;
    @FXML
    private TextField txtF_TimeFilter;
    @FXML
    private TextField txtF_TypeFilter;
    @FXML
    private TextField txtF_CategoryFilter;
    @FXML
    private TextField txtF_InfoFilter;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        tableViewInfo.setTableMenuButtonVisible(true);
        tableViewInfo.setEditable(true);
        IdCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        timeCol.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        typeCol.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        categoryCol.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        infoCol.setCellValueFactory(cellData -> cellData.getValue().messageProperty());

        IdCol.setCellFactory(TextFieldTableCell.forTableColumn());
        timeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        categoryCol.setCellFactory(TextFieldTableCell.forTableColumn());
        infoCol.setCellFactory(TextFieldTableCell.forTableColumn());

        // Custom rendering of the table cell.
        typeCol.setCellFactory((TableColumn<LogInformation, String> column) -> new TableCell<LogInformation, String>() {
            
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if (item != null && !empty) {
                    // Format log type.
                    // Style all dates in March with a different color.
                    setText(item);
                    switch (item) {
                        case LogInformation.Type.ERRO:
                            setTextFill(Color.RED);
                            break;
                        case LogInformation.Type.WARN:
                            setTextFill(Color.BLUE);
                            break;
                        default:
                            setTextFill(Color.BLACK);
                            break;
                    }
                } else {
                    setText(null);
                }
            }
        });

        tableViewInfo.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    @FXML
    private void onBtnClear(ActionEvent event) {
        logsInformation.setLength(0);
        tableViewInfo.getItems().clear();
    }

    @FXML
    private void onAutoSave(ActionEvent event) {
        if (chkBx_AutoSave.isSelected()) {
            btnSaveLog.setDisable(true);
        } else {
            btnSaveLog.setDisable(false);
        }
    }

    @FXML
    private void onBtnSaveLog(ActionEvent event) {
        Logs.e("still need coding here");   //how to append?
        IOHelper.saveAsciiToFile(LOG_FILE_LOCATION, logsInformation.toString());
    }

    /**
     *
     * @param infomations
     */
    public void updateInfo(final ObservableList<LogInformation> infomations) {
//        logsInformation.delete(0, logsInformation.length());
//        infomations.forEach((infomation) -> {
//            logsInformation.append(infomation.toString()).append("\n");
//        });

        tableViewInfo.setItems(infomations);
//        tableViewInfo.setItems(getSortedListByNameOrCode(infomations));

        if (chkBx_AutoSave.isSelected()) {
            IOHelper.saveAsciiToFile(LOG_FILE_LOCATION, logsInformation.toString());
        }

    }

    @FXML
    private void onBtnBrowsLogs(ActionEvent event) {
    }

    public void updateInfo(final String info) {
        StringBuilder msg = new StringBuilder();
        msg.append(SDF.format(System.currentTimeMillis())).append("    ").append(info).append("\n");

        if (chkBx_AutoSave.isSelected()) {
            IOHelper.saveAsciiToFile(LOG_FILE_LOCATION, logsInformation.toString());
        }

    }

    public void updateInfo(final ArrayList<String> info) {
        for (int i = 0; i < info.size(); i++) {
            logsInformation.append(SDF.format(System.currentTimeMillis())).append("    ").append(info.get(i)).append("\n");
        }

        if (chkBx_AutoSave.isSelected()) {
            IOHelper.saveAsciiToFile(LOG_FILE_LOCATION, logsInformation.toString());
        }
    }

    @FXML
    private void onSelected(MouseEvent event) {
        lbl_SelectRowCount.setText(String.valueOf(tableViewInfo.getSelectionModel().getSelectedCells().size()));

    }

    private class TextFiledChangeListener implements ChangeListener<String> {

        FilteredList filteredData;
        Columns columns;

        public TextFiledChangeListener(FilteredList filter, Columns columns) {
            filteredData = filter;
            this.columns = columns;
        }

        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            // make a listener, Set the filter Predicate whenever the filter changes.
            filteredData.setPredicate(new Predicate<LogInformation>() {
                //this will test the whole list
                @Override
                public boolean test(LogInformation information) {
                    // If filter text is empty, display all stocks.
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    // Compare stock code with filter text.
                    String lowerCaseFilter = newValue.toLowerCase();

                    switch (columns) {
                        case Time:
                            if (information.getTime().toLowerCase().contains(lowerCaseFilter)) {
                                return true; // Filter matches stockcode.
                            }
                            break;
                        case Type:
                            if (information.getType().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        case Category:
                            if (information.getCategory().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        case Message:
                            if (information.getMessage().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            break;
                        default:
                            return false;
                    }

                    return false; // Does not match.
                }
            });

        }
    }

    private SortedList<LogInformation> getSortedListByNameOrCode(ObservableList list) {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<LogInformation> filteredData;
        filteredData = new FilteredList<>(list, (LogInformation info) -> true);
        // 2. Set the filter Predicate whenever the filter changes.
        txtF_TimeFilter.textProperty().addListener(new TextFiledChangeListener(filteredData, Columns.Time));
        txtF_TypeFilter.textProperty().addListener(new TextFiledChangeListener(filteredData, Columns.Type));
        txtF_CategoryFilter.textProperty().addListener(new TextFiledChangeListener(filteredData, Columns.Category));
        txtF_InfoFilter.textProperty().addListener(new TextFiledChangeListener(filteredData, Columns.Message));

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<LogInformation> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tableViewInfo.comparatorProperty());

        return sortedData;
    }

    private static enum Columns {
        ID,
        Time,
        Type,
        Category,
        Message
    }

}
