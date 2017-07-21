/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.maths;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import stockany.datamodel.CalculationParameters;
import stockany.datamodel.GlobalParameters;
import stockany.datamodel.LogInformation;
import stockany.datamodel.SingleStock;
import stockany.datamodel.StockSets;
import stockany.tools.StockDataParser;
import stockany.tools.Common;
import stockany.tools.Common.Stock;
import tools.utilities.StringUtils;
import tools.files.FileHelper;
import tools.files.IOHelper;
import tools.sqlite.SQLiteJDBC;
import tools.utilities.Logs;

/**
 *
 * @author JianGe
 */
public class CalcCorrelationTables implements Runnable {

    private volatile Thread blinker = new Thread();
    private Thread currentThread = new Thread();
    private volatile boolean ThreadDone = false;

    private final int CorrLength;
    private final int CurrentThreadID;
    private final String CurrentThreadName;
    private final StockSets StockSetsForCalc;

    private CalculationParameters calculationParameters = new CalculationParameters();

//    private static final DecimalFormat DF = new DecimalFormat("#.####");
    public static void main(String[] args) {
        int threadID = Integer.valueOf(args[0]);
        CalcCorrelationTables calc = new CalcCorrelationTables(threadID);
        calc.start();
    }

    public CalcCorrelationTables(int threadID) {
        this.StockSetsForCalc = new StockSets();

        this.calculationParameters = GlobalParameters.CalculationParameters;

        this.CurrentThreadID = threadID;
        this.CurrentThreadName = "Thread " + CurrentThreadID;

        this.CorrLength = 60;

    }

    public Thread getCurrThread() {
        return currentThread;
    }

    public String getCurrentThreadName() {
        return CurrentThreadName;
    }

    public boolean isThreadDone() {
        return ThreadDone;
    }

    /**
     * In this method don't create any logs in loop, it will slow down process
     *
     * @param sourceStockCode
     */
    public void runBacthSingleStockCorrelation(String sourceStockCode) {
        long startTime = System.currentTimeMillis();

        // Prepare source data
        final SingleStock sourceStock = StockSetsForCalc.getSingleStockByCode(sourceStockCode);

        if (sourceStock.getStockDaliyData().size() < GlobalParameters.defaultLength) {
            return;
        }

        // Init Source
        for (int i = 0; i < calculationParameters.getProcessDateTypeList().size(); i++) {
            switch (calculationParameters.getProcessDateTypeList().get(i)) {
                case Min5:
                    sourceStock.read5MinData();
                    break;
                case Min15:
                    sourceStock.make15MinData();
                    break;
                case Min30:
                    sourceStock.make30MinData();
                    break;
                case Min60:
                    sourceStock.make60MinData();
                case Daliy:
                    break;
                default:
                    break;
            }
        }
        // Prepare parted source data
        ArrayList<SingleStock> partedSingleStockList = new ArrayList<>();
        for (int lengthIdx = 0; lengthIdx < calculationParameters.getProcessLengthList().size(); lengthIdx++) {
            // Prepare parted source data
            partedSingleStockList.add(sourceStock.getPartSingleStock(calculationParameters.getStartDate(),
                    calculationParameters.getProcessLengthList().get(lengthIdx),
                    calculationParameters.getEndDate()));

            for (int dateIdx = 0; dateIdx < calculationParameters.getProcessDateTypeList().size(); dateIdx++) {
                Stock.DateTypes dateTypes = calculationParameters.getProcessDateTypeList().get(dateIdx);
                partedSingleStockList.get(lengthIdx).calculateSub(dateTypes);
            }
        }

        //out put data
        ArrayList<String> singleBestCorrList = new ArrayList<>();
        //out put content per line
        StringBuilder output = new StringBuilder();

        //prepare header
        output.append(StringUtils.formatLeftS("StockCode")).append(StringUtils.formatLeftS("StockName"));
        for (int dateIdx = 0; dateIdx < calculationParameters.getProcessDateTypeList().size(); dateIdx++) {
            for (int lengthIdx = 0; lengthIdx < calculationParameters.getProcessLengthList().size(); lengthIdx++) {
                for (int patternIdx = 0; patternIdx < calculationParameters.getProcessPatternList().size(); patternIdx++) {

                    output.append(StringUtils.formatLeftS("" + calculationParameters.getProcessDateTypeList().get(dateIdx)
                            + calculationParameters.getProcessPatternList().get(patternIdx)
                            + calculationParameters.getProcessLengthList().get(lengthIdx) + "Corr"));
                    output.append(StringUtils.formatLeftS("" + calculationParameters.getProcessDateTypeList().get(dateIdx)
                            + calculationParameters.getProcessPatternList().get(patternIdx)
                            + calculationParameters.getProcessLengthList().get(lengthIdx) + "Date"));
                }
            }
        }

        singleBestCorrList.add(output.toString());

        //Core Runing
        for (int idx = 0; idx < GlobalParameters.AllStockSetsForCalc.getStocksList().size(); idx++) {
            long start = System.currentTimeMillis();
            // Prepare target data
            SingleStock targetStock = GlobalParameters.AllStockSetsForCalc.getStocksList().get(idx);

            String targetStockCode = targetStock.getStockCode();
            String targetStockName = targetStock.getStockName();

            if (GlobalParameters.stockSizeCheckFailed(targetStockCode, GlobalParameters.defaultLength)) {
                continue;
            }

            for (int i = 0; i < calculationParameters.getProcessDateTypeList().size(); i++) {
                targetStock.calculateSub(calculationParameters.getProcessDateTypeList().get(i));

            }
            // Init target
//            for (int i = 0; i < calculationParameters.getProcessDateTypeList().size(); i++) {
//                switch (calculationParameters.getProcessDateTypeList().get(i)) {
//                    case Min5:
//                        targetStock.read5MinData();
//                        break;
//                    case Min15:
//                        targetStock.make15MinData();
//                        break;
//                    case Min30:
//                        targetStock.make30MinData();
//                        break;
//                    case Min60:
//                        targetStock.make60MinData();
//                    case Daliy:
//                        break;
//                    default:
//                        break;
//                }
//            }

//            Logs.e("init Minutes  " + (System.currentTimeMillis() - start));
            //Clear line contents
            output.delete(0, output.length());
            output.append(StringUtils.formatLeftS(targetStockCode)).append(StringUtils.formatLeftS(targetStockName));
//            Logs.e("be running");
            // Running 
            for (int dateIdx = 0; dateIdx < calculationParameters.getProcessDateTypeList().size(); dateIdx++) {
                Stock.DateTypes dateTypes = calculationParameters.getProcessDateTypeList().get(dateIdx);
                for (int lengthIdx = 0; lengthIdx < calculationParameters.getProcessLengthList().size(); lengthIdx++) {

                    for (int patternIdx = 0; patternIdx < calculationParameters.getProcessPatternList().size(); patternIdx++) {

                        CorrelationsPearson correlationsPearson;

                        switch (calculationParameters.getProcessPatternList().get(patternIdx)) {
                            case Price:
                                correlationsPearson = new CorrelationsPearson(partedSingleStockList.get(lengthIdx).getDataArray(dateTypes, Stock.DataTypes.CLOSE_PRICE),
                                        targetStock.getDataArray(dateTypes, Stock.DataTypes.CLOSE_PRICE));
                                break;
                            case OCHL:
                                Logs.e("not yet");
                                correlationsPearson = null;
                                break;
                            case Ampl:
                                correlationsPearson = new CorrelationsPearson(partedSingleStockList.get(lengthIdx).getDataArray(dateTypes, Stock.DataTypes.RATE_OF_AMPLITUDE),
                                        targetStock.getDataArray(dateTypes, Stock.DataTypes.RATE_OF_AMPLITUDE));
                                break;
                            case Volate:
                                correlationsPearson = new CorrelationsPearson(partedSingleStockList.get(lengthIdx).getDataArray(dateTypes, Stock.DataTypes.RATE_OF_VOLATILITY),
                                        targetStock.getDataArray(dateTypes, Stock.DataTypes.RATE_OF_VOLATILITY));
                                break;
                            case Volume:
                                correlationsPearson = new CorrelationsPearson(partedSingleStockList.get(lengthIdx).getDataArray(dateTypes, Stock.DataTypes.VOL),
                                        targetStock.getDataArray(dateTypes, Stock.DataTypes.VOL));
                                break;
                            case Turnover:
                                Logs.e("not yet");
                                correlationsPearson = null;
                                break;
                            default:
                                correlationsPearson = null;
                                Logs.e("erro");
                                break;
                        }

                        if (correlationsPearson == null) {
                            continue;
                        }

                        // run correlation
                        correlationsPearson.runMaxNormalizedCorrelation();
                        // get the max correlation
                        output.append(StringUtils.formatLeftS(String.valueOf(correlationsPearson.getMaxCorrelation())));
                        // get the date of the max correlation
                        if (targetStock.getDateList(dateTypes).isEmpty()) {
                            output.append(StringUtils.formatLeftS("null"));
                        } else {
                            output.append(StringUtils.formatLeftS(Common.DATE_OF_OUTPUT_MIN_SDF.format(targetStock.getDateList(dateTypes).get(correlationsPearson.getMaxPosition()))));
                        }
                    }
//                output.append(StringUtils.formatLeftS("null"));
                }
            }

            singleBestCorrList.add(output.toString());
        }

        saveSingleToFile(sourceStockCode, singleBestCorrList);
        long endTime = System.currentTimeMillis();
        GlobalParameters.logInfo(LogInformation.Category.CALC, CurrentThreadName + " Total " + sourceStockCode + " Spend " + (endTime - startTime) / 1000 + " seconds!");
        Logs.e(CurrentThreadName + " Total " + sourceStockCode + " Spend " + (endTime - startTime) / 1000 + " seconds!");
//        return singleBestCorrList;

    }

    public void saveAllToDatabase() {
        long startTime = System.currentTimeMillis();
        SQLiteJDBC sqliteJDBC = new SQLiteJDBC(GlobalParameters.WORKING_CORR_DB_PATH);

        long endTime = System.currentTimeMillis();
        Logs.e("Full Search Spend " + (endTime - startTime) / 1000 + " seconds!");

    }

    private void saveSingleToFile(String stockName, ArrayList<String> values) {
        //Just a Test
        IOHelper.saveArrayListToFile(GlobalParameters.WORKING_CORR_ASC_PATH + File.separator + stockName + ".dat", values);

    }

    private void saveSingleToDatabase(String stockName, ArrayList<ArrayList<String>> values) {
        SQLiteJDBC sqliteJDBC = new SQLiteJDBC(GlobalParameters.WORKING_CORR_DB_PATH);

        //Just a Test
        StringBuffer sqlTableItems = new StringBuffer();

//                    + "StockName TEXT,"
//                    + "Best60Correlations Real,"
        Map<String, ArrayList<String>> output = new LinkedHashMap<>();
        for (int idx = 0; idx < values.size(); idx++) {
            output.put(values.get(idx).get(0), values.get(idx));
            if (idx == 0) {
                sqlTableItems.append(" (StockCode TEXT PRIMARY KEY NOT NULL,");
            } else if (idx == values.size() - 1) {
                sqlTableItems.append(values.get(idx).get(0)).append(" TEXT)");
            } else {
                sqlTableItems.append(values.get(idx).get(0)).append(" TEXT, ");
            }
        }

        Logs.e(sqlTableItems);
        sqliteJDBC.createTable(stockName, sqlTableItems.toString());
        sqliteJDBC.insertColumns(stockName, output);
        sqlTableItems.delete(0, sqlTableItems.length());

    }

    public void start() {
        blinker = new Thread(this);
        currentThread = blinker;
        blinker.start();
    }

    public void stop() {
        this.blinker = null;
        Logs.e("Calculation Thread " + CurrentThreadID + " is stopping!");
        GlobalParameters.logWarn(LogInformation.Category.CALC, CurrentThreadName + " terminalted at " + Common.LOG_DATE_SDF.format(System.currentTimeMillis()));
    }

    @Override
    public void run() {
        Thread thisThread = Thread.currentThread();

        try {
            long startTime = System.currentTimeMillis();
            Logs.e(CurrentThreadName + " start at " + Common.LOG_DATE_SDF.format(startTime));

            // For mulitple threads
            int Length;
            int last = 0;
            if (calculationParameters.getThreadNumber() < calculationParameters.getSourceStocksCodeList().size()) {
                Length = (int) calculationParameters.getSourceStocksCodeList().size() / calculationParameters.getThreadNumber();
                last = calculationParameters.getSourceStocksCodeList().size() % Length;
//                Logs.e("Total: " + calculationParameters.getSourceStocksCodeList().size());
//                Logs.e("Length: " + Length);
//                Logs.e("Last: " + last);
            } else {
                Length = 1; // Use one Thread
            }

            // Prepare data
            StockSetsForCalc.getStocksList().clear();
            File directory = new File(GlobalParameters.WORKING_DALIY_DATA_ASC_PATH);
            if (directory.exists()) {
                String fileName;
                String fileAbsPath;
                StockDataParser dataParser = null;
                for (int idx = 0 + CurrentThreadID * Length; idx < (CurrentThreadID + 1) * Length; idx++) {
                    fileName = calculationParameters.getSourceStocksCodeList().get(idx) + ".txt";
                    fileAbsPath = GlobalParameters.WORKING_DALIY_DATA_ASC_PATH + File.separator + fileName;
//                    Logs.e(CurrentThreadName + "  :  " + fileAbsPath);
                    dataParser = new StockDataParser(IOHelper.readAsciiFile(fileAbsPath, Common.AsciiUnicode.GB2312), null);
                    StockSetsForCalc.add(dataParser.getSingleStock());

                    if (calculationParameters.getThreadNumber() * Length == idx + 1) {
                        // Get the last few
                        for (int lastFew = idx + 1; lastFew < calculationParameters.getSourceStocksCodeList().size(); lastFew++) {
                            fileName = calculationParameters.getSourceStocksCodeList().get(lastFew) + ".txt";
                            fileAbsPath = GlobalParameters.WORKING_DALIY_DATA_ASC_PATH + File.separator + fileName;
//                            Logs.e(CurrentThreadName + "  :  " + fileAbsPath);
                            dataParser = new StockDataParser(IOHelper.readAsciiFile(fileAbsPath, Common.AsciiUnicode.GB2312), null);
                            StockSetsForCalc.add(dataParser.getSingleStock());

                        }
                    }
                }

            } else {
                GlobalParameters.logErro(LogInformation.Category.FILES, "The source file is wrong,  Please check directory set. ");
                Logs.e("The source file is wrong,  Please check directory set. ");
            }

            for (int idx = 0; idx < calculationParameters.getProcessDateTypeList().size(); idx++) {
                StockSetsForCalc.initAllCurves(calculationParameters.getProcessDateTypeList().get(idx));

            }
//            Logs.e(CurrentThreadName +"  ,   "+StockSetsForCalc.toString());

            // Runing calculating
            for (int idx = 0; idx < StockSetsForCalc.getStocksList().size(); idx++) {
                if (blinker != thisThread) {
                    // This is safe to terminate Threads 
                    Logs.e(CurrentThreadName + " terminalted at " + Common.LOG_DATE_SDF.format(System.currentTimeMillis()));
                    GlobalParameters.logErro(LogInformation.Category.CALC, CurrentThreadName + " terminalted at " + Common.LOG_DATE_SDF.format(System.currentTimeMillis()));
                    break;
                }
                // Core runing
                runBacthSingleStockCorrelation(StockSetsForCalc.getStocksList().get(idx).getStockCode());

            }

            ThreadDone = true;
            long endTime = System.currentTimeMillis();
//            GlobalParameters.isCalculating = false;
            Logs.e(CurrentThreadName + " end at " + Common.LOG_DATE_SDF.format(endTime));
            GlobalParameters.logInfo(LogInformation.Category.CALC, CurrentThreadName + " end at " + Common.LOG_DATE_SDF.format(endTime));
//            PatternTabController.showConfirmDialog("Calculation is done!");
        } catch (Exception e) {
            e.printStackTrace();
            ThreadDone = true;
            GlobalParameters.logErro(LogInformation.Category.CALC, CurrentThreadName + " crashed at " + Common.LOG_DATE_SDF.format(System.currentTimeMillis()));
            Logs.e(CurrentThreadName + " crashed at " + Common.LOG_DATE_SDF.format(System.currentTimeMillis()));
//            GlobalParameters.isCalculating = false;
        }

    }
}
