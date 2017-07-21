/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.datamodel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import stockany.ui.SearchResultsPlots;
import stockany.tools.Common;
import stockany.ui.LogsAppTableView;

/**
 *
 * @author JianGe
 */
public class GlobalParameters {

    //Load Data Path 
    public static final String tableItems = " (Date TEXT,"
            + "Open TEXT,"
            + "Highest TEXT,"
            + "Lowest TEXT,"
            + "Close TEXT,"
            + "Capital TEXT,"
            + "Volume TEXT,"
            + "Reserved TEXT) ";

    public static volatile String WORKING_DATA_BIN_PATH = ".\\";
    public static volatile String WORKING_5MIN_DATA_ASC_PATH = ".\\";
    public static volatile String WORKING_DALIY_DATA_ASC_PATH = ".\\";
    public static volatile String WORKING_DATA_DB_PATH = ".\\";
    public static volatile String WORKING_CORR_DB_PATH = ".\\";
    public static volatile String WORKING_CORR_ASC_PATH = ".\\";
    public static volatile String WORKING_REVIEW_DB_PATH = ".\\";
    public static volatile String WORKING_LOG_PATH = ".\\";

    private static final String DEFAULT_PROPERTY_FILE = ".\\mainpage.properties";
    public static PropertyHelper AppProperty = new PropertyHelper(DEFAULT_PROPERTY_FILE);

    public static final StockSets AllStockSetsForCalc = new StockSets();
    public static final StockSets AllStockSetsForSearch = new StockSets();

    public static void setWorkingPaths(String type, String path) {
        switch (type) {
            case Common.Keys.STOCKDATA_BIN_DEF_FILE_LOC:
                WORKING_DATA_BIN_PATH = path;
                break;
            case Common.Keys.STOCKDATA_5MIN_ASC_DEF_FILE_LOC:
                WORKING_5MIN_DATA_ASC_PATH = path;
                break;
            case Common.Keys.STOCKDATA_DALIY_ASC_DEF_FILE_LOC:
                WORKING_DALIY_DATA_ASC_PATH = path;
                break;
            case Common.Keys.STOCKDATA_DB_DEF_FILE_LOC:
                WORKING_DATA_DB_PATH = path;
                break;
            case Common.Keys.STOCKCORR_DB_DEF_FILE_LOC:
                WORKING_CORR_DB_PATH = path;
                break;
            case Common.Keys.STOCKCORR_ASC_DEF_FILE_LOC:
                WORKING_CORR_ASC_PATH = path;
                break;
            case Common.Keys.STOCKREVIEW_DB_DEF_FILE_LOC:
                WORKING_REVIEW_DB_PATH = path;
                break;
            case Common.Keys.LOG_DEF_FILE_LOC:
                WORKING_LOG_PATH = path;
                break;
            case "":
                setDefWorkingPaths();
                break;
            default:
                break;
        }
    }

    public static void setDefWorkingPaths() {
        if (AppProperty != null) {

        } else {

        }
        WORKING_DATA_BIN_PATH = PropertyHelper.getKeyValue(Common.Keys.STOCKDATA_BIN_DEF_FILE_LOC);
        WORKING_5MIN_DATA_ASC_PATH = PropertyHelper.getKeyValue(Common.Keys.STOCKDATA_5MIN_ASC_DEF_FILE_LOC);
        WORKING_DALIY_DATA_ASC_PATH = PropertyHelper.getKeyValue(Common.Keys.STOCKDATA_DALIY_ASC_DEF_FILE_LOC);
        WORKING_DATA_DB_PATH = PropertyHelper.getKeyValue(Common.Keys.STOCKDATA_DB_DEF_FILE_LOC);
        WORKING_CORR_DB_PATH = PropertyHelper.getKeyValue(Common.Keys.STOCKCORR_DB_DEF_FILE_LOC);
        WORKING_CORR_ASC_PATH = PropertyHelper.getKeyValue(Common.Keys.STOCKCORR_ASC_DEF_FILE_LOC);
        WORKING_REVIEW_DB_PATH = PropertyHelper.getKeyValue(Common.Keys.STOCKREVIEW_DB_DEF_FILE_LOC);
        WORKING_LOG_PATH = PropertyHelper.getKeyValue(Common.Keys.LOG_DEF_FILE_LOC);
    }

    //Com Resources
    public static volatile boolean isCalculating = false;
    public static volatile boolean isRefreshingStockList = false;
    public static volatile boolean isRefreshing5MinData = false;
    public static volatile boolean isRefresh5MinDataDone = false;

    public static volatile int defaultLength = 45;

    public static List<File> AscDataFilesList = new ArrayList<>();
    public static List<File> BinDataFilesList = new ArrayList<>();
    public static List<File> CorASCFilesList = new ArrayList<>();

    public static ObservableList<String> codeList;
    public static ObservableList<String> nameList;
    public static ObservableList<String> codeNameList;

    public static CalculationParameters CalculationParameters = new CalculationParameters();

    public static final LogsAppTableView logsAppTableView = new LogsAppTableView();
    public static final SearchResultsPlots searchResultsPlots = new SearchResultsPlots();
    public static final Stage logsStage = new Stage();
    public static final Stage resultPlotStage = new Stage();

    public static ObservableList<SingleStock> getStockInfoObservableList() {
        ObservableList<SingleStock> stocksTableItems = FXCollections.<SingleStock>observableArrayList();

        if (GlobalParameters.AllStockSetsForCalc.getStocksList().size() > 0) {
            for (int idx = 0; idx < GlobalParameters.AllStockSetsForCalc.getStocksList().size(); idx++) {
                stocksTableItems.add(GlobalParameters.AllStockSetsForCalc.getStocksList().get(idx));
            }
        }

        return stocksTableItems;

    }
//      public void onLogsWinClosed() {
//        tgBtnShowLog.setSelected(false);
//    }

    public static boolean stockSizeCheckFailed(String stockCode, int size) {
        boolean isFailed;
        isFailed = AllStockSetsForCalc.getStocksList().isEmpty()
                || (AllStockSetsForCalc.getSingleStockByCode(stockCode) == null)
                || AllStockSetsForCalc.getSingleStockByCode(stockCode).getStockDaliyData().size() < size;

        return isFailed;
    }

    //Log
    private static final ObservableList<LogInformation> logInformations = FXCollections.observableArrayList();

    public static void logShow() {
        logsAppTableView.updateInfo(logInformations);
    }

    public static void logInfo(String category, String msg) {
        logInformations.add(LogInformation.createInfoLog(category, msg));
        logsAppTableView.updateInfo(logInformations);
    }

    public static void logWarn(String category, String msg) {
        logInformations.add(LogInformation.createWarnLog(category, msg));
        logsAppTableView.updateInfo(logInformations);
    }

    public static void logErro(String category, String msg) {
        logInformations.add(LogInformation.createErroLog(category, msg));
        logsAppTableView.updateInfo(logInformations);
    }

}
