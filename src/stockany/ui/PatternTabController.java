/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import stockany.tools.AlertDialog;
import stockany.tools.Common;
import stockany.datamodel.GlobalParameters;
import stockany.maths.CalcCorrelationTables;
import stockany.datamodel.CalculationParameters;
import stockany.datamodel.LogInformation;
import stockany.tools.StockDataParser;
import tools.files.FileHelper;
import tools.utilities.Logs;
import jiangestockans.JianGeStockAns;
import stockany.tools.Common.Stock;
import stockany.ui.controls.StocksTableViewController;
import tools.files.IOHelper;

/**
 * FXML Controller class
 *
 * @author JianGe
 */
public class PatternTabController extends Tab implements Initializable {

    private static final ArrayList<CalcCorrelationTables> threadList = new ArrayList<>();
    private ArrayList<Integer> batchJobList = new ArrayList<>();

    @FXML
    private GridPane gridPanePatternController;
    @FXML
    private AnchorPane stockPlotAnchPane;
    @FXML
    private FinanceChartPaneController stockChart;
    @FXML
    private AnchorPane stockListAnchPane;
    @FXML
    private StocksTableViewController stockListTableView;
//    @FXML
    private VBox chartPaneVbox = new VBox();
    @FXML
    private AnchorPane patternAnchorPane;
    @FXML
    private DatePicker datePicker_Start;
    @FXML
    private DatePicker datePicker_End;
    @FXML
    private CheckBox chkBx_PatternPrice;
    @FXML
    private CheckBox chkBx_PatternVolume;
    @FXML
    private CheckBox chkBx_PatternOCHL;
    @FXML
    private CheckBox chkBx_PatternAmp;
    @FXML
    private CheckBox chkBx_PatternUpDown;
    @FXML
    private CheckBox chkBx_PatternTurnover;
    @FXML
    private Button btnFullSearch;
    @FXML
    private RadioButton rdBtn_AscFileData;
    @FXML
    private RadioButton rdBtn_BinFileData;
    @FXML
    private RadioButton rdBtn_DataBase;
    @FXML
    private ComboBox cmBx_ThreadNumber;
    @FXML
    private Button btnTerminateCalc;
    @FXML
    private ComboBox cmBx_CorrelationLength;
    @FXML
    private Button btnAddJob;
    @FXML
    private TextField txtF_JobList;
    @FXML
    private RadioButton rdBtn_SelectedStocks;
    @FXML
    private RadioButton rdBtn_AllStocks;
    @FXML
    private CheckBox chkBxFromStart;
    @FXML
    private Button btnClearJob;
    @FXML
    private Button btnRefreshAllData;
    @FXML
    private Button btn_Classic;
    @FXML
    private CheckBox chkBx_PatternDaily;
    @FXML
    private CheckBox chkBx_Pattern5Min;
    @FXML
    private CheckBox chkBx_Pattern15Min;
    @FXML
    private CheckBox chkBx_Pattern30Min;
    @FXML
    private CheckBox chkBx_Pattern60Min;
    @FXML
    private Button btnLoad5Minutes;
    @FXML
    private Button btnMaek15Minutes;
    @FXML
    private Button btnMake30Minutes;
    @FXML
    private Button btnMake60Minutes;

    public PatternTabController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PatternTab.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.setClosable(false);
        this.chartPaneVbox.autosize();

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void onBtnClassic(ActionEvent event) {
        batchJobList.clear();
        batchJobList.add(60);
        batchJobList.add(90);
        batchJobList.add(120);
        batchJobList.add(150);
        chkBx_PatternPrice.setSelected(true);
        chkBx_PatternAmp.setSelected(true);
        chkBx_PatternUpDown.setSelected(true);
        chkBx_PatternVolume.setSelected(true);
        txtF_JobList.setText(batchJobList.toString().replace("[", "").replace("]", ""));
    }

    @FXML
    private void onBtnLoad5Minutes(ActionEvent event) {

        Thread load5Min = new Thread(new ReadMinutesDataRunable(event));

        load5Min.start();

    }

    @FXML
    private void onBtnMakeSubMinutes(ActionEvent event) {
        Thread makeSubMinutes = new Thread(new ReadMinutesDataRunable(event));

        makeSubMinutes.start();
    }

    private class ReadMinutesDataRunable implements Runnable {

        private ActionEvent Event;

        public ReadMinutesDataRunable(ActionEvent event) {
            this.Event = event;
        }

        @Override
        public void run() {
            try {
                if (checkRefreshing() || checkRuning()) {
                    return;
                }

                if (GlobalParameters.AllStockSetsForCalc.getStocksList().isEmpty()) {
                    AlertDialog.showErrorDialog(new Stage(), "StockList is Empty!");
                    GlobalParameters.isRefreshing5MinData = false;
                    return;
                } else {
                    long start = System.currentTimeMillis();
                    String messageHeader;
                    GlobalParameters.isRefreshing5MinData = true;

                    if (Event.getSource() == btnLoad5Minutes) {

                        messageHeader = "Loading 5";
                        Logs.e(messageHeader + " minutes data.....");
                        GlobalParameters.logInfo(LogInformation.Category.FILES, "Loading 5 minutes data.....");
                        for (int idx = 0; idx < GlobalParameters.AllStockSetsForCalc.getStocksList().size(); idx++) {
                            GlobalParameters.AllStockSetsForCalc.getStocksList().get(idx).read5MinData();
                        }

                        Logs.e("Loading 5 minutes data, done!");
                        Logs.e("Loading 5 spends: " + (System.currentTimeMillis() - start) / 1000 + " seconds!");
                        GlobalParameters.logInfo(LogInformation.Category.FILES, "Load 5 minutes data, done! And spends: " + (System.currentTimeMillis() - start) / 1000 + " seconds!");
                        Platform.runLater(new AlertDialog(AlertDialog.DialogType.Info, null, "Load 5 minutes data, done! "));

                    } else if (Event.getSource() == btnMaek15Minutes) {
                        messageHeader = "Making 15";
                        Logs.e(messageHeader + " minutes data.....");
                        GlobalParameters.logInfo(LogInformation.Category.FILES, "Loading 5 minutes data.....");
                        for (int idx = 0; idx < GlobalParameters.AllStockSetsForCalc.getStocksList().size(); idx++) {
                            GlobalParameters.AllStockSetsForCalc.getStocksList().get(idx).make15MinData();
                        }

                        Logs.e("Making 15 minutes data, done!");
                        Logs.e("Making 15 spends: " + (System.currentTimeMillis() - start) / 1000 + " seconds!");
                        GlobalParameters.logInfo(LogInformation.Category.FILES, "Making 15 minutes data, done! And spends: " + (System.currentTimeMillis() - start) / 1000 + " seconds!");
                        Platform.runLater(new AlertDialog(AlertDialog.DialogType.Info, null, "Making 5 minutes data, done! "));
                    } else if (Event.getSource() == btnMake30Minutes) {
                        messageHeader = "Making 30";
                        Logs.e(messageHeader + " minutes data.....");
                        GlobalParameters.logInfo(LogInformation.Category.FILES, "Making 30 minutes data.....");
                        for (int idx = 0; idx < GlobalParameters.AllStockSetsForCalc.getStocksList().size(); idx++) {
                            GlobalParameters.AllStockSetsForCalc.getStocksList().get(idx).make30MinData();

                        }

                        Logs.e("Making 30 minutes data, done!");
                        Logs.e("Making 30 spends: " + (System.currentTimeMillis() - start) / 1000 + " seconds!");
                        GlobalParameters.logInfo(LogInformation.Category.FILES, "Making 30 minutes data, done! And spends: " + (System.currentTimeMillis() - start) / 1000 + " seconds!");
                        Platform.runLater(new AlertDialog(AlertDialog.DialogType.Info, null, "Making 30 minutes data, done! "));
                    } else if (Event.getSource() == btnMake60Minutes) {
                        messageHeader = "Making 60";
                        Logs.e(messageHeader + " minutes data.....");
                        GlobalParameters.logInfo(LogInformation.Category.FILES, "Making 60 minutes data.....");
                        for (int idx = 0; idx < GlobalParameters.AllStockSetsForCalc.getStocksList().size(); idx++) {
                            GlobalParameters.AllStockSetsForCalc.getStocksList().get(idx).make60MinData();
                        }

                        Logs.e("Making 60 minutes data, done!");
                        Logs.e("Making 60 spends: " + (System.currentTimeMillis() - start) / 1000 + " seconds!");
                        GlobalParameters.logInfo(LogInformation.Category.FILES, "Making 60 minutes data, done! And spends: " + (System.currentTimeMillis() - start) / 1000 + " seconds!");
                        Platform.runLater(new AlertDialog(AlertDialog.DialogType.Info, null, "Making 60 minutes data, done! "));
                    }
                    GlobalParameters.isRefreshing5MinData = false;
                }

            } catch (Exception e) {
                GlobalParameters.isRefreshing5MinData = false;
            }
        }
    }

    private class ReadAllDailyDataRunable implements Runnable {

        @Override
        public void run() {
            try {
                refreshStockDataAndView();

            } catch (Exception e) {
            }
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
        //Make tooltips
        chkBx_PatternPrice.setTooltip(Common.Tips.TREND_TOOLTIP);
        chkBx_PatternVolume.setTooltip(Common.Tips.VOLUME_TOOLTIP);
        rdBtn_AscFileData.setTooltip(Common.Tips.ASCFILEDATA_TOOLTIP);
        rdBtn_BinFileData.setTooltip(Common.Tips.BINFILEDATA_TOOLTIP);
        rdBtn_DataBase.setTooltip(Common.Tips.DATABASE_TOOLTIP);

        datePicker_Start.setShowWeekNumbers(true);
        datePicker_Start.setValue(LocalDate.now());
        datePicker_Start.setValue(LocalDate.ofEpochDay(tools.utilities.DateUtils.getEpochDay() - 90));

        datePicker_End.setShowWeekNumbers(true);
        datePicker_End.setValue(LocalDate.now());

        final Callback<DatePicker, DateCell> dayCellFactory
                = (final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(
                        datePicker_End.getValue().plusDays(1))) {
//                            setDisable(true);
//                            setStyle("-fx-background-color: #ffc0cb;");
                }
                long p = ChronoUnit.DAYS.between(item, datePicker_End.getValue());
                setTooltip(new Tooltip("You're about choose " + p + " days for calculate!"));
            }
        } //Set auto tips
                ;

        datePicker_Start.setDayCellFactory(dayCellFactory);

        //Load the default stock code and name
        ArrayList<String> threadNumbers = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            threadNumbers.add(String.valueOf((int) Math.pow(2.0, Double.valueOf(i))));
        }

        cmBx_ThreadNumber.setItems(FXCollections.observableArrayList(threadNumbers));
        cmBx_ThreadNumber.getSelectionModel().select(4);

        stockListTableView.setFilterDisable(true);

        Thread readThread = new Thread(new ReadAllDailyDataRunable());
        readThread.start();
        //Taget Stocks TableView
        //Select model
        final ToggleGroup dataGroup = new ToggleGroup();
        rdBtn_AscFileData.setToggleGroup(dataGroup);
        rdBtn_BinFileData.setToggleGroup(dataGroup);
        rdBtn_DataBase.setToggleGroup(dataGroup);

        final ToggleGroup stocksGroup = new ToggleGroup();
        rdBtn_AllStocks.setToggleGroup(stocksGroup);
        rdBtn_SelectedStocks.setToggleGroup(stocksGroup);

        ArrayList<String> cmBxParameterList = new ArrayList<>();
        cmBxParameterList.add(String.valueOf(60));
        cmBxParameterList.add(String.valueOf(90));
        cmBxParameterList.add(String.valueOf(120));
        cmBxParameterList.add(String.valueOf(150));
        cmBx_CorrelationLength.setItems(FXCollections.observableArrayList(cmBxParameterList));
        cmBx_CorrelationLength.getSelectionModel().select(0);
        txtF_JobList.setText(cmBx_CorrelationLength.getSelectionModel().getSelectedItem().toString());
        batchJobList.add(Integer.valueOf(cmBx_CorrelationLength.getSelectionModel().getSelectedItem().toString()));

    }

    @FXML
    private void onCmBxCodeScrolled(ScrollEvent event) {
        Logs.e("CmbxCode Scorlled");
    }

    @FXML
    private void onBtnFullSearch(ActionEvent event) {

        if (checkRefreshing() || checkRuning()) {
            return;
        }

        CalculationParameters parameters = getCalculationParameters();

        if (parameters == null) {
            Logs.e("Parameters has error!");
            return;
        }

        if (!checkParameter()) {
            return;
        }
        GlobalParameters.isCalculating = true;

        GlobalParameters.logInfo(LogInformation.Category.CALC, "Runing Full Search.......");
        Logs.e("Runing Full Search.......");

        long startTime = System.currentTimeMillis();
        Logs.e("Start Full Search at " + Common.LOG_DATE_SDF.format(startTime));

        //for recalculation, clear threads list and easy to terminate.
        threadList.clear();

        for (int idx = 0; idx < parameters.getThreadNumber(); idx++) {
            CalcCorrelationTables correlationTables = new CalcCorrelationTables(idx);
            threadList.add(correlationTables);

            correlationTables.start();
        }
    }

    @FXML
    private void onAscFileDataMode(ActionEvent event
    ) {
        if (rdBtn_AscFileData.isSelected()) {
            GlobalParameters.logInfo(LogInformation.Category.FILES, "Using data in Ascii files!");
        }
    }

    @FXML
    private void onBinFileDataMode(ActionEvent event
    ) {
        if (rdBtn_BinFileData.isSelected()) {
            GlobalParameters.logInfo(LogInformation.Category.FILES, "Using data in Binary files!");
        }
    }

    @FXML
    private void onDataBaseMode(ActionEvent event
    ) {
        if (rdBtn_DataBase.isSelected()) {
            GlobalParameters.logInfo(LogInformation.Category.FILES, "Using data in DataBase!");
        }
    }

    @FXML
    private void onCmBxThreadNumber(ActionEvent event
    ) {
        int ThreadNumber = 0;
        try {
            ThreadNumber = Integer.valueOf(cmBx_ThreadNumber.getSelectionModel().getSelectedItem().toString());
        } catch (NumberFormatException e) {
            Logs.e(e.toString());
            String[] erroInfo = e.toString().split(" ");
            String erro = erroInfo[erroInfo.length - 1];
            AlertDialog.showErrorDialog(new Stage(), "The parameter " + erro + " is wrong!");
        }

        GlobalParameters.logInfo(LogInformation.Category.CALC, "Set thread number to " + ThreadNumber);
        Logs.e("Thread Number is: " + ThreadNumber);
    }

    @FXML
    private void onBtnTerminateCalc(ActionEvent event) {
        stopAllThreads();
    }

    public static void stopAllThreads() {
        GlobalParameters.logWarn(LogInformation.Category.CALC, "User just cancelled the calculation job, it will teriminate after the current calculation!");
        Thread stopThread = new Thread(() -> {
            try {
                for (int i = 0; i < threadList.size(); i++) {
                    threadList.get(i).stop();

                }

                for (int i = 0; i < threadList.size(); i++) {
                    while (true) {
                        if (threadList.get(i).isThreadDone()) {
                            Logs.e(threadList.get(i).getCurrentThreadName() + " is terminated at " + Common.LOG_DATE_SDF.format(System.currentTimeMillis()));
                            GlobalParameters.logWarn(LogInformation.Category.CALC, threadList.get(i).getCurrentThreadName() + " is terminated at " + Common.LOG_DATE_SDF.format(System.currentTimeMillis()));
                            break;
                        }
                    }
                }
                GlobalParameters.isCalculating = false;

            } catch (Exception e) {
            }
        });

        stopThread.start();

    }

    @FXML
    private void onBtnAddJob(ActionEvent event) {

        try {
            Integer selected = Integer.valueOf(cmBx_CorrelationLength.getSelectionModel().getSelectedItem().toString());
            if (!batchJobList.contains(selected) && selected > 0) {
                batchJobList.add(selected);
            } else if (batchJobList.contains(selected)) {
                AlertDialog.showErrorDialog(new Stage(), "Areadly add this one!");
            } else {
                AlertDialog.showErrorDialog(new Stage(), "The parameter " + selected + " is wrong!");
            }

        } catch (NumberFormatException e) {
            Logs.e(e.toString());
            String[] erroInfo = e.toString().split(" ");
            String erro = erroInfo[erroInfo.length - 1];
            AlertDialog.showErrorDialog(new Stage(), "The parameter " + erro + " is wrong!");

        }

        Collections.sort(batchJobList, new Common.NumberAscendSort());
        txtF_JobList.setText(batchJobList.toString().replace("[", "").replace("]", ""));
    }

    @FXML
    private void onSelectedMode(ActionEvent event) {
        if (rdBtn_SelectedStocks.isSelected()) {
            stockListTableView.setAllSelected(false);
            GlobalParameters.logInfo(LogInformation.Category.STOCK, "Selected stocks model selected!");

        }
    }

    @FXML
    private void onAllSelectedMode(ActionEvent event) {
        if (rdBtn_AllStocks.isSelected()) {
            stockListTableView.setAllSelected(true);

            GlobalParameters.logInfo(LogInformation.Category.STOCK, "All stocks model selected!");
        }
    }

    private CalculationParameters getCalculationParameters() {
        CalculationParameters parameters = new CalculationParameters();
        parameters.clearAllParameters();

        if (stockListTableView.getSelectedStocksCodeList().isEmpty() || stockListTableView.getSelectedStocksNameList().isEmpty()) {
            AlertDialog.showErrorDialog(new Stage(), "Please selected stocks to calculate!!");
            GlobalParameters.logErro(LogInformation.Category.CALC, "Please selected stocks to calculate!!");
            Logs.e("Please selected stocks to calculate!!");
            return null;
        }

        parameters.setSourceStocksCodeList(stockListTableView.getSelectedStocksCodeList());
        parameters.setSourceStocksNameList(stockListTableView.getSelectedStocksNameList());

        if (chkBx_PatternPrice.isSelected()) {
            parameters.addProcessPattern(CalculationParameters.PatternType.Price);
        }
        if (chkBx_PatternOCHL.isSelected()) {
            parameters.addProcessPattern(CalculationParameters.PatternType.OCHL);
        }
        if (chkBx_PatternAmp.isSelected()) {
            parameters.addProcessPattern(CalculationParameters.PatternType.Ampl);
        }
        if (chkBx_PatternUpDown.isSelected()) {
            parameters.addProcessPattern(CalculationParameters.PatternType.Volate);
        }
        if (chkBx_PatternVolume.isSelected()) {
            parameters.addProcessPattern(CalculationParameters.PatternType.Volume);
        }
        if (chkBx_PatternTurnover.isSelected()) {
            parameters.addProcessPattern(CalculationParameters.PatternType.Turnover);
        }

        if (parameters.getProcessPatternList().size() < 1) {
            AlertDialog.showErrorDialog(new Stage(), "Please selected a search pattern to calculate!!");
            GlobalParameters.logErro(LogInformation.Category.CALC, "Please selected a search pattern to calculate!!");
            Logs.e("Please selected a search pattern to calculate!!");
            return null;
        }

        if (chkBx_PatternDaily.isSelected()) {
            parameters.addProcessDateType(Stock.DateTypes.Daliy);
        }
        if (chkBx_Pattern5Min.isSelected()) {
            parameters.addProcessDateType(Stock.DateTypes.Min5);
        }
        if (chkBx_Pattern15Min.isSelected()) {
            parameters.addProcessDateType(Stock.DateTypes.Min15);
        }
        if (chkBx_Pattern30Min.isSelected()) {
            parameters.addProcessDateType(Stock.DateTypes.Min30);
        }
        if (chkBx_Pattern60Min.isSelected()) {
            parameters.addProcessDateType(Stock.DateTypes.Min60);
        }

        if (parameters.getProcessDateTypeList().size() < 1) {
            AlertDialog.showErrorDialog(new Stage(), "Please selected a date type to calculate!!");
            GlobalParameters.logErro(LogInformation.Category.CALC, "Please selected a date type to calculate!!");
            Logs.e("Please selected a date type to calculate!!");
            return null;
        }

        if (batchJobList.isEmpty()) {
            AlertDialog.showErrorDialog(new Stage(), "Please selected a search length to calculate!!");
            GlobalParameters.logErro(LogInformation.Category.CALC, "Please selected a search length to calculate!!");
            Logs.e("Please selected a search length to calculate!!");
            return null;
        }

        parameters.setProcessLengthList(batchJobList);

        int ThreadNumber = Integer.valueOf(cmBx_ThreadNumber.getSelectionModel().getSelectedItem().toString());

        if (ThreadNumber < 1) {
            AlertDialog.showErrorDialog(new Stage(), "Please input a valid int number to calculate!!");
            GlobalParameters.logErro(LogInformation.Category.CALC, "Please input a valid int number to calculate!!");
            Logs.e("Please input a valid int number to calculate!!");
            return null;
        }

        if (ThreadNumber >= parameters.getSourceStocksCodeList().size()) {
            if (parameters.getSourceStocksCodeList().size() <= 16) {
                ThreadNumber = parameters.getSourceStocksCodeList().size();
            } else {
                ThreadNumber = 16;

            }
            cmBx_ThreadNumber.getSelectionModel().select(ThreadNumber + "");
        }

        parameters.setThreadNumber(ThreadNumber);

        try {
            Date startDate;
            Date endDate;

            if (chkBxFromStart.isSelected()) {
                startDate = Common.DATAPICKER_DAY_SDF.parse(datePicker_Start.getValue().toString());
                endDate = null;
            } else {
                startDate = null;
                endDate = Common.DATAPICKER_DAY_SDF.parse(datePicker_End.getValue().toString());
            }

            parameters.setStartDate(startDate);
            parameters.setEndDate(endDate);

        } catch (ParseException e) {
        }

//        Logs.e("here");
        parameters.printParameter(false);
        GlobalParameters.CalculationParameters = parameters;
        return parameters;
    }

    @FXML
    private void onBtnClearJob(ActionEvent event) {
        chkBx_PatternPrice.setSelected(false);
        chkBx_PatternAmp.setSelected(false);
        chkBx_PatternUpDown.setSelected(false);
        chkBx_PatternVolume.setSelected(false);
        batchJobList.clear();
        txtF_JobList.clear();
    }

    @FXML
    private void onJobListConfirmed(ActionEvent event) {
        setJobList();
    }

    private void setJobList() {
        ArrayList<Integer> tempList = new ArrayList<>();
        try {
            String[] corrLengthArray = txtF_JobList.getText().replaceAll(" +", "").split(",");

            for (String corrLength : corrLengthArray) {
                int len = Integer.valueOf(corrLength);
                if (Integer.valueOf(corrLength) < 1 || tempList.contains(len)) {
                    continue;
                }

                tempList.add(len);
            }
        } catch (NumberFormatException e) {
            String[] erroInfo = e.toString().split(" ");
            String erro = erroInfo[erroInfo.length - 1];
            AlertDialog.showErrorDialog(new Stage(), "The parameter " + erro + " is wrong!");
            Logs.e(erro.replaceAll("\"", ""));
        }

        Collections.sort(tempList, new Common.NumberAscendSort());

        batchJobList = tempList;
        txtF_JobList.setText(batchJobList.toString().replace("[", "").replace("]", ""));
    }

    private boolean checkRuning() {
        boolean running = true;
        if (!threadList.isEmpty()) {
            for (int i = 0; i < threadList.size(); i++) {
                if (!threadList.get(i).isThreadDone()) {
                    Platform.runLater(new AlertDialog(AlertDialog.DialogType.Erro, null, "Calculation is already running!"));
                    GlobalParameters.logErro(LogInformation.Category.CALC, "Calculation is already running!");
                    Logs.e("Calculation is already running!");
                    running = true;
                    break;
                }
            }
        } else {
            running = false;
        }
        return running;

    }

    private boolean checkRefreshing() {
        if (GlobalParameters.isRefreshingStockList) {
            Platform.runLater(new AlertDialog(AlertDialog.DialogType.Erro, null, "Refreshing Daily data is running, Please wait!"));
            GlobalParameters.logErro(LogInformation.Category.CALC, "Refreshing Daily data is running, Please wait!");
            Logs.e("Refreshing Daily data is running, Please wait!");
            return true;
        } else if (GlobalParameters.isRefreshing5MinData) {
            Platform.runLater(new AlertDialog(AlertDialog.DialogType.Erro, null, "Refreshing 5 minutes data is running, Please wait!"));
            GlobalParameters.logErro(LogInformation.Category.CALC, "Refreshing 5 minutes data is running, Please wait!");
            Logs.e("Refreshing 5 minutes data is running, Please wait!");
            return true;
        } else {
            return false;
        }
    }

    private boolean checkParameter() {
        GlobalParameters.logsStage.toFront();
        if (GlobalParameters.logsStage.isShowing()) {
            GlobalParameters.logsStage.toFront();
        } else {
            GlobalParameters.logsStage.showAndWait();
        }

        Common.Response response = AlertDialog.showConfirmDialog(Modality.NONE, new Stage(), "Please Check the Logs...... \r\n Are these parameter right?");
        boolean isParameterRight = false;
        switch (response) {
            case YES:
                Logs.e("Yes");
                GlobalParameters.logInfo(LogInformation.Category.FILES, "Parameter are right, continue calculate......");
                isParameterRight = true;
                break;
            case CANCEL:
                Logs.e("Cancelled");
                GlobalParameters.logInfo(LogInformation.Category.FILES, "Not sure, calculate cancelled.");
                isParameterRight = false;
                break;
            case NO:
                Logs.e("No");
                GlobalParameters.logInfo(LogInformation.Category.FILES, "Parameters are wrong, calculate cancelled!");
                isParameterRight = false;
                break;
            default:
                Logs.e("Default");
                GlobalParameters.logInfo(LogInformation.Category.FILES, "Default.");
                break;
        }
        return isParameterRight;
    }

    @FXML
    private void onBtnRefreshAllData(ActionEvent event) {
        Thread readThread = new Thread(new ReadAllDailyDataRunable());
        readThread.start();
    }

    private void refreshStockDataAndView() {

        if (GlobalParameters.isCalculating) {
            AlertDialog.showErrorDialog(new Stage(), "Calculation is running, Please wait!");
            GlobalParameters.logErro(LogInformation.Category.CALC, "Calculation is running, Please wait!");
            Logs.e("Calculation is running, Please wait!");
            return;
        }
        try {

            GlobalParameters.logInfo(LogInformation.Category.FILES,
                    "Refresh all data started. Please wait.... ");
            Logs.e("Refresh start at: " + Common.LOG_DATE_SDF.format(System.currentTimeMillis()));

            GlobalParameters.isRefreshingStockList = true;
            //Clear
            GlobalParameters.AllStockSetsForCalc.getStocksList().clear();

            if (stockListTableView.getStockTableView().getItems().size() > 0) {
                Logs.e("clear");
                stockListTableView.clearAllTableViewData();
            }

            //Read new data
            File daliyDirectory = new File(GlobalParameters.WORKING_DALIY_DATA_ASC_PATH);

            if (daliyDirectory.exists()) {
                for (File daliyFile : daliyDirectory.listFiles()) {
                    StockDataParser dataParser = new StockDataParser(IOHelper.readAsciiFile(daliyFile, Common.AsciiUnicode.GB2312), null);
                    if (dataParser != null) {
                        GlobalParameters.AllStockSetsForCalc.add(dataParser.getSingleStock());
                    }

                }

//                GlobalParameters.AllStockSetsForCalc.initAllCurves(Stock.DateTypes.Daliy);
            } else {
                GlobalParameters.logErro(LogInformation.Category.FILES, "The source file is wrong,  Please check directory set. ");
                Logs.e("The source file is wrong,  Please check directory set. ");
            }

            if (GlobalParameters.AllStockSetsForCalc.getStocksList().size() > 0) {

                stockListTableView.setTableViewItems(GlobalParameters.getStockInfoObservableList(), true);

                //make auto hit
                GlobalParameters.codeList = FXCollections.observableArrayList(GlobalParameters.AllStockSetsForCalc.getStockCodeList());
                GlobalParameters.nameList = FXCollections.observableArrayList(GlobalParameters.AllStockSetsForCalc.getStockNameList());
                GlobalParameters.codeNameList = FXCollections.observableArrayList(GlobalParameters.AllStockSetsForCalc.getStockCodeNameList());

                stockChart.setDefaultWidth(1300);
                stockChart.setDefaultMainHeight(400);
                stockChart.setDefaultIndicatorHeight(75);
                stockChart.setCmBxSourceStockCodeItems(GlobalParameters.codeNameList);

                stockChart.setCmBxSourceStockSelected(0);
                stockChart.showChart();
            }

            JianGeStockAns.searchResultsPlotsController.RefreshTableView();

            GlobalParameters.logInfo(LogInformation.Category.FILES,
                    "Refresh all data end. Total load daily data of " + GlobalParameters.AllStockSetsForCalc.getStockCodeList().size() + " stocks. Please go ahead.... ");
            Logs.e("Refresh end at: " + Common.LOG_DATE_SDF.format(System.currentTimeMillis()));
            rdBtn_AllStocks.setSelected(true);
            rdBtn_AscFileData.setSelected(true);
            GlobalParameters.isRefreshingStockList = false;

            // Run later in JavaFX thread
            Platform.runLater(new AlertDialog(AlertDialog.DialogType.Info, null, "Refresh all data is done!"));

        } catch (Exception e) {
            e.printStackTrace();
            Logs.e("Erro!");
        }

    }

    @FXML
    private void onChkBxFromStart(ActionEvent event) {
        if (chkBxFromStart.isSelected()) {
            datePicker_Start.setDisable(false);
            datePicker_End.setDisable(true);
//            cmBx_CorrelationLength.setDisable(false);
//            batchJobList.clear();
        } else {
            datePicker_Start.setDisable(true);
            datePicker_End.setDisable(false);
//            cmBx_CorrelationLength.setDisable(true);
//            batchJobList.clear();
//            batchJobList.add(0);
            txtF_JobList.setText(batchJobList.toString().replace("[", "").replace("]", ""));
        }

    }

}
