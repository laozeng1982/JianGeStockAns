/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import stockany.tools.AlertDialog;
import stockany.tools.StockDataParser;
import stockany.tools.Common;
import stockany.datamodel.GlobalParameters;
import stockany.datamodel.LogInformation;
import stockany.datamodel.PropertyHelper;
import stockany.ui.controls.FileTableViewController;
import tools.files.FileHelper;
import tools.sqlite.SQLiteJDBC;
import tools.utilities.Logs;

/**
 * FXML Controller class
 *
 * @author JianGe
 */
public class LoadDataTabController extends Tab implements Initializable {

    @FXML
    private AnchorPane loadAnchorPane;
    @FXML
    private TextField txtF_DataBinFilesPath;
    @FXML
    private TextField txtF_5MinDataAscFilesPath;
    @FXML
    private TextField txtF_DataAscFilesPath;
    @FXML
    private TextField txtF_DataDbFilePath;
    @FXML
    private TextField txtF_CorrDbFilePath;
    @FXML
    private TextField txtF_CorrAscFilesPath;
    @FXML
    private TextField txtF_ReviewDbFilePath;
    @FXML
    private Button btnBrowsDataBinFiles;
    @FXML
    private Button btnBrows5MinDataAscFiles;
    @FXML
    private Button btnBrowsDataAscFiles;
    @FXML
    private Button btnBrowsDataDbFile;
    @FXML
    private Button btnBrowsCorrDbFile;
    @FXML
    private Button btnBrowsCorrAscFiles;
    @FXML
    private Button btnBrowsReviewDbFile;
    @FXML
    private Button btn_ReadBin;
    @FXML
    private Button btn_ReadAsc;

    @FXML
    private CheckBox chkBx_UpdateList;
    @FXML
    private ComboBox cmBx_AscCode;
    @FXML
    private CheckBox chkBx_SaveAsDef;
    @FXML
    private Button btn_UpdateList;
    @FXML
    private HBox fileReviewVBox;

    public LoadDataTabController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoadDataTab.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.setClosable(false);

        fileReviewVBox = new HBox(15);

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

        chkBx_UpdateList.setTooltip(Common.Tips.UPDATESTOCKLIST_TOOLTIP);

        setWorkingLocations();

        //Make Ascii code list
        ArrayList<String> unicode = new ArrayList<>();
        unicode.add("UTF-8");
        unicode.add(Common.AsciiUnicode.GB2312);

        cmBx_AscCode.setItems(FXCollections.observableArrayList(unicode));
        cmBx_AscCode.getSelectionModel().selectFirst();

        refreshAllFolder();

    }

    @FXML
    private void onBtnBrowsDataBinFiles(ActionEvent event) {
        Common.selectMultiFiles(txtF_DataBinFilesPath, txtF_DataBinFilesPath.getText(), "day", "dat");
        GlobalParameters.setWorkingPaths(Common.Keys.STOCKDATA_BIN_DEF_FILE_LOC, txtF_DataBinFilesPath.getText());
        GlobalParameters.logInfo(LogInformation.Category.FILES, "Selected binary data files directory: " + txtF_DataBinFilesPath.getText());
    }

    @FXML
    private void onBtnBrows5MinDataAscFiles(ActionEvent event) {
        Common.selectMultiFiles(txtF_5MinDataAscFilesPath, txtF_5MinDataAscFilesPath.getText(), "day", "dat");
        GlobalParameters.setWorkingPaths(Common.Keys.STOCKDATA_5MIN_ASC_DEF_FILE_LOC, txtF_5MinDataAscFilesPath.getText());
        GlobalParameters.logInfo(LogInformation.Category.FILES, "Selected binary data files directory: " + txtF_5MinDataAscFilesPath.getText());
    }

    @FXML
    private void onBtnBrowsDataAscFiles(ActionEvent event) {
        GlobalParameters.AscDataFilesList = Common.selectMultiFiles(txtF_DataAscFilesPath, txtF_DataAscFilesPath.getText(), "txt", "dat");
        GlobalParameters.setWorkingPaths(Common.Keys.STOCKDATA_DALIY_ASC_DEF_FILE_LOC, txtF_DataAscFilesPath.getText());
        GlobalParameters.logInfo(LogInformation.Category.FILES, "Selected ascii data files directory: " + txtF_DataAscFilesPath.getText());
    }

    @FXML
    private void onBtnBrowsDataDbFile(ActionEvent event) {
        if (txtF_DataDbFilePath.getText() == null || txtF_DataDbFilePath.getText().isEmpty()) {
            Common.selectSingleFile(txtF_DataDbFilePath, System.getProperty("user.home"), jiangestockans.JianGeStockAns.primaryStage, Common.Hints.DB_HINT, "db");
        } else {
            Common.selectSingleFile(txtF_DataDbFilePath, txtF_DataDbFilePath.getText(),  jiangestockans.JianGeStockAns.primaryStage, Common.Hints.DB_HINT, "db");
        }
        GlobalParameters.setWorkingPaths(Common.Keys.STOCKDATA_DB_DEF_FILE_LOC, txtF_DataDbFilePath.getText());
        GlobalParameters.logInfo(LogInformation.Category.FILES, "Selected stock data database file: " + txtF_DataDbFilePath.getText());
    }

    @FXML
    private void onBtnBrowsCorrDbFile(ActionEvent event) {
        if (txtF_CorrDbFilePath.getText() == null || txtF_CorrDbFilePath.getText().isEmpty()) {
            Common.selectSingleFile(txtF_CorrDbFilePath, System.getProperty("user.home"),  jiangestockans.JianGeStockAns.primaryStage, Common.Hints.DB_HINT, "db");
        } else {
            Logs.e(txtF_CorrDbFilePath.getText());
            Common.selectSingleFile(txtF_CorrDbFilePath, txtF_CorrDbFilePath.getText(),  jiangestockans.JianGeStockAns.primaryStage, Common.Hints.DB_HINT, "db");
        }
        GlobalParameters.setWorkingPaths(Common.Keys.STOCKCORR_DB_DEF_FILE_LOC, txtF_CorrDbFilePath.getText());
        GlobalParameters.logInfo(LogInformation.Category.FILES, "Selected stock correlation database file: " + txtF_CorrDbFilePath.getText());
    }

    @FXML
    private void onBtnBrowsCorrAscFiles(ActionEvent event) {
        Common.selectMultiFiles(txtF_CorrAscFilesPath, txtF_CorrAscFilesPath.getText(), "day", "dat");
        GlobalParameters.setWorkingPaths(Common.Keys.STOCKCORR_ASC_DEF_FILE_LOC, txtF_CorrAscFilesPath.getText());
        GlobalParameters.logInfo(LogInformation.Category.FILES, "Selected ascii correlation directory: " + GlobalParameters.WORKING_CORR_ASC_PATH);
    }

    @FXML
    private void onBtnBrowsReviewDbFile(ActionEvent event) {
        if (txtF_ReviewDbFilePath.getText() == null || txtF_ReviewDbFilePath.getText().isEmpty()) {
            Logs.e(System.getProperty("user.home"));
            Common.selectSingleFile(txtF_ReviewDbFilePath, System.getProperty("user.home"), jiangestockans.JianGeStockAns.primaryStage,  Common.Hints.DB_HINT, "db");
        } else {
            Logs.e(txtF_ReviewDbFilePath.getText());
            Common.selectSingleFile(txtF_ReviewDbFilePath, txtF_ReviewDbFilePath.getText(),  jiangestockans.JianGeStockAns.primaryStage, Common.Hints.DB_HINT, "db");
        }
        GlobalParameters.setWorkingPaths(Common.Keys.STOCKREVIEW_DB_DEF_FILE_LOC, txtF_ReviewDbFilePath.getText());
        GlobalParameters.logInfo(LogInformation.Category.FILES, "Selected stock review database file: " + txtF_ReviewDbFilePath.getText());
    }

    @FXML
    private void onBtnReadBinFiles(ActionEvent event) {
        txtF_DataBinFilesPath.setText("not yet!");

    }

    @FXML
    private void onBtnReadAscFiles(ActionEvent event) {
        readAscFilesToDB(event);
    }

    @FXML
    private void onSaveNewPath(ActionEvent event) {
        if (chkBx_SaveAsDef.isSelected()) {
            saveSetting();
//            setDefaultLocations();
        }
    }

    @FXML
    private void onBtnRefreshAllFolder(ActionEvent event) {
        refreshAllFolder();
    }

    private void refreshAllFolder() {
        fileReviewVBox.getChildren().clear();
        FileTableViewController binDataFileTableView = new FileTableViewController(GlobalParameters.WORKING_DATA_BIN_PATH);
        FileTableViewController min5AscDataFileTableView = new FileTableViewController(GlobalParameters.WORKING_5MIN_DATA_ASC_PATH);
        FileTableViewController ascDataFileTableView = new FileTableViewController(GlobalParameters.WORKING_DALIY_DATA_ASC_PATH);
        FileTableViewController dbDataFileTableView = new FileTableViewController(GlobalParameters.WORKING_DATA_DB_PATH);
        FileTableViewController dbCorrFileTableView = new FileTableViewController(GlobalParameters.WORKING_CORR_DB_PATH);
        FileTableViewController ascCorrTableView = new FileTableViewController(GlobalParameters.WORKING_CORR_ASC_PATH);
        fileReviewVBox.getChildren().add(binDataFileTableView);
        fileReviewVBox.getChildren().add(min5AscDataFileTableView);
        fileReviewVBox.getChildren().add(ascDataFileTableView);
        fileReviewVBox.getChildren().add(dbDataFileTableView);
        fileReviewVBox.getChildren().add(dbCorrFileTableView);
        fileReviewVBox.getChildren().add(ascCorrTableView);
    }

    /**
     * Save settings
     *
     * @param isDefault , If isDefault is true, means save from Settings Panel,
     * else from Load Data Panel
     */
    private void saveSetting() {
        Common.Response response = AlertDialog.showConfirmDialog(Modality.WINDOW_MODAL, new Stage(), "Will you overwrite the configs?");
        switch (response) {
            case YES:
                Logs.e("Yes");

                GlobalParameters.AppProperty.saveProperty(Common.Keys.STOCKDATA_BIN_DEF_FILE_LOC, txtF_DataBinFilesPath.getText());
                GlobalParameters.AppProperty.saveProperty(Common.Keys.STOCKDATA_5MIN_ASC_DEF_FILE_LOC, txtF_5MinDataAscFilesPath.getText());
                GlobalParameters.AppProperty.saveProperty(Common.Keys.STOCKDATA_DALIY_ASC_DEF_FILE_LOC, txtF_DataAscFilesPath.getText());
                GlobalParameters.AppProperty.saveProperty(Common.Keys.STOCKDATA_DB_DEF_FILE_LOC, txtF_DataDbFilePath.getText());
                GlobalParameters.AppProperty.saveProperty(Common.Keys.STOCKCORR_DB_DEF_FILE_LOC, txtF_CorrDbFilePath.getText());
                GlobalParameters.AppProperty.saveProperty(Common.Keys.STOCKCORR_ASC_DEF_FILE_LOC, txtF_CorrAscFilesPath.getText());
                GlobalParameters.AppProperty.saveProperty(Common.Keys.STOCKREVIEW_DB_DEF_FILE_LOC, txtF_ReviewDbFilePath.getText());
//                    GeneralParameters.mAppProperty.saveProperty(Utils.Keys.LOG_DEF_FILE_LOC, txtF_LogFileDefPath.getText());
                GlobalParameters.logInfo(LogInformation.Category.FILES, "New directories have been saved.");
                break;
            case CANCEL:
                Logs.e("Cancelled");
                GlobalParameters.logInfo(LogInformation.Category.FILES, "Save cancelled.");
                break;
            case NO:
                Logs.e("No");
                GlobalParameters.logInfo(LogInformation.Category.FILES, "Save discard.");
                break;
            default:
                break;
        }
    }

    /**
     * Read data in ASCII file to Database
     *
     * @param evt
     */
    private void readAscFilesToDB(ActionEvent evt) {
        // TODO add your handling code here:

        long startTime = System.currentTimeMillis();
        if (tools.utilities.Utils.isFileSelected(txtF_DataAscFilesPath, "ASCII") && tools.utilities.Utils.isFileSelected(txtF_DataDbFilePath, "Database")) {

//            if (chkBx_UpdateList.isSelected()) {
//                GeneralParameters.StockCodeNameMap.clear();
//            }
            Thread readThread = new Thread(() -> {
                try {
                    //Add erro process when the file is not a data file!
                    GlobalParameters.AscDataFilesList = FileHelper.getAllFilesInDirectory(txtF_DataAscFilesPath, txtF_DataAscFilesPath.getText(), "txt", "dat");
                    SQLiteJDBC sqliteJDBC = new SQLiteJDBC(txtF_DataDbFilePath.getText());
                    for (int i = 0; i < GlobalParameters.AscDataFilesList.size(); i++) {
                        StockDataParser dataParser = new StockDataParser(FileHelper.readAsciiFile(GlobalParameters.AscDataFilesList.get(i).getAbsolutePath(), Common.AsciiUnicode.GB2312), null);

                        String tableName = GlobalParameters.AscDataFilesList.get(i).getName().split("\\.")[0];

                        sqliteJDBC.createTable(tableName, GlobalParameters.tableItems);

                        sqliteJDBC.insertColumns(tableName, dataParser.getSingleStock().toMapData());
                        GlobalParameters.logInfo(LogInformation.Category.FILES, dataParser.getSingleStock().getStockCode() + " Processed.");

                        //Save Stock code and name list!
//                        if (chkBx_UpdateList.isSelected()) {
//                            GeneralParameters.StockCodeNameMap.
//                                    put(dataParser.getSingleStock().getStockCode(), dataParser.getSingleStock().getStockName());
//                        }
                    }
//                    if (GeneralParameters.StockCodeNameMap.size() > 0) {
//                        FileHelper.saveMapDataToFile(".\\Stock_CodeName.list", GeneralParameters.StockCodeNameMap);
//                        Logs.e("saved!");
//                    }

                    long endTime = System.currentTimeMillis();

                    GlobalParameters.logInfo(LogInformation.Category.FILES, "Total process " + GlobalParameters.AscDataFilesList.size() + " stocks cost time is: " + (endTime - startTime) / 1000 + " Seconds!");
                    Logs.e("Total process time is: " + (endTime - startTime) / 1000 + " Seconds!");
                } catch (Exception e) {
                }
            });

            readThread.start();
        } else {
        }
    }

    /**
     * Set working location on Load Data Table and Setting Table on main page
     */
    private void setWorkingLocations() {
        txtF_DataBinFilesPath.setText(PropertyHelper.getKeyValue(Common.Keys.STOCKDATA_BIN_DEF_FILE_LOC));
        txtF_5MinDataAscFilesPath.setText(PropertyHelper.getKeyValue(Common.Keys.STOCKDATA_5MIN_ASC_DEF_FILE_LOC));
        txtF_DataAscFilesPath.setText(PropertyHelper.getKeyValue(Common.Keys.STOCKDATA_DALIY_ASC_DEF_FILE_LOC));
        txtF_DataDbFilePath.setText(PropertyHelper.getKeyValue(Common.Keys.STOCKDATA_DB_DEF_FILE_LOC));
        txtF_CorrDbFilePath.setText(PropertyHelper.getKeyValue(Common.Keys.STOCKCORR_DB_DEF_FILE_LOC));
        txtF_CorrAscFilesPath.setText(PropertyHelper.getKeyValue(Common.Keys.STOCKCORR_ASC_DEF_FILE_LOC));
        txtF_ReviewDbFilePath.setText(PropertyHelper.getKeyValue(Common.Keys.STOCKREVIEW_DB_DEF_FILE_LOC));
    }

    @FXML
    private void onTextFiledsChanged(ActionEvent event) {
        changePath(event);
    }

    private void changePath(ActionEvent event) {
        TextField path = (TextField) event.getSource();
        boolean isFileExists = (new File(path.getText())).exists();

        if (!(new File(path.getText())).exists()) {
            Logs.e(path.getText() + " does not exist!");
            GlobalParameters.logErro(LogInformation.Category.FILES, path.getText() + " does not exist!");
            return;
        }

        if (event.getSource().equals(txtF_DataBinFilesPath)) {
            if (!isFileExists) {
                Logs.e(path.getText() + " does not exist!");
                GlobalParameters.logWarn(LogInformation.Category.FILES, path.getText() + " does not exist!");
            }
            GlobalParameters.setWorkingPaths(Common.Keys.STOCKDATA_BIN_DEF_FILE_LOC, txtF_DataBinFilesPath.getText());
            GlobalParameters.logInfo(LogInformation.Category.FILES, "Change data binary files directory to: " + txtF_DataBinFilesPath.getText());
            Logs.e(GlobalParameters.WORKING_DATA_BIN_PATH);

        } else if (event.getSource().equals(txtF_5MinDataAscFilesPath)) {
            if (!isFileExists) {
                Logs.e(path.getText() + " does not exist!");
                GlobalParameters.logErro(LogInformation.Category.FILES, path.getText() + " does not exist!");
                return;
            }
            GlobalParameters.setWorkingPaths(Common.Keys.STOCKDATA_5MIN_ASC_DEF_FILE_LOC, txtF_5MinDataAscFilesPath.getText());
            GlobalParameters.logInfo(LogInformation.Category.FILES, "Change 5 minutes data acsii files directory to: " + txtF_5MinDataAscFilesPath.getText());
            Logs.e(GlobalParameters.WORKING_5MIN_DATA_ASC_PATH);

        } else if (event.getSource().equals(txtF_DataAscFilesPath)) {
            if (!isFileExists) {
                Logs.e(path.getText() + " does not exist!");
                GlobalParameters.logErro(LogInformation.Category.FILES, path.getText() + " does not exist!");
                return;
            }
            GlobalParameters.setWorkingPaths(Common.Keys.STOCKDATA_DALIY_ASC_DEF_FILE_LOC, txtF_DataAscFilesPath.getText());
            GlobalParameters.logInfo(LogInformation.Category.FILES, "Change daliy data acsii files directory to: " + txtF_DataAscFilesPath.getText());
            Logs.e(GlobalParameters.WORKING_DALIY_DATA_ASC_PATH);

        } else if (event.getSource().equals(txtF_DataDbFilePath)) {
            if (!isFileExists) {
                Logs.e(path.getText() + " does not exist!");
                GlobalParameters.logWarn(LogInformation.Category.FILES, path.getText() + " does not exist!");
            }
            GlobalParameters.setWorkingPaths(Common.Keys.STOCKDATA_DB_DEF_FILE_LOC, txtF_DataDbFilePath.getText());
            GlobalParameters.logInfo(LogInformation.Category.FILES, "Change data database file to: " + txtF_DataDbFilePath.getText());
            Logs.e(GlobalParameters.WORKING_DATA_DB_PATH);

        } else if (event.getSource().equals(txtF_CorrDbFilePath)) {
            if (!isFileExists) {
                Logs.e(path.getText() + " does not exist!");
                GlobalParameters.logWarn(LogInformation.Category.FILES, path.getText() + " does not exist!");
            }
            GlobalParameters.setWorkingPaths(Common.Keys.STOCKCORR_DB_DEF_FILE_LOC, txtF_CorrDbFilePath.getText());
            GlobalParameters.logInfo(LogInformation.Category.FILES, "Change correlation database file to: " + txtF_CorrDbFilePath.getText());
            Logs.e(GlobalParameters.WORKING_CORR_DB_PATH);

        } else if (event.getSource().equals(txtF_CorrAscFilesPath)) {
            if (!isFileExists) {
                Logs.e(path.getText() + " does not exist!");
                GlobalParameters.logWarn(LogInformation.Category.FILES, path.getText() + " does not exist!");
            }
            GlobalParameters.setWorkingPaths(Common.Keys.STOCKCORR_ASC_DEF_FILE_LOC, txtF_CorrAscFilesPath.getText());
            GlobalParameters.logInfo(LogInformation.Category.FILES, "Change correlation acsii files directory to: " + txtF_DataAscFilesPath.getText());
            Logs.e(GlobalParameters.WORKING_CORR_ASC_PATH);
        } else if (event.getSource().equals(txtF_ReviewDbFilePath)) {
            if (!isFileExists) {
                Logs.e(path.getText() + " does not exist!");
                GlobalParameters.logWarn(LogInformation.Category.FILES, path.getText() + " does not exist!");
            }
            GlobalParameters.setWorkingPaths(Common.Keys.STOCKREVIEW_DB_DEF_FILE_LOC, txtF_ReviewDbFilePath.getText());
            GlobalParameters.logInfo(LogInformation.Category.FILES, "Change correlation acsii files directory to: " + txtF_ReviewDbFilePath.getText());
            Logs.e(GlobalParameters.WORKING_REVIEW_DB_PATH);
        }

    }

    @FXML
    private void onBtnUpdateList(ActionEvent event) {
    }

}
