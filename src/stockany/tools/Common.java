/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import stockany.datamodel.GlobalParameters;
import stockany.datamodel.LogInformation;
import tools.files.FileHelper;
import tools.files.IOHelper;
import tools.sqlite.SQLiteJDBC;
import tools.utilities.Logs;

/**
 *
 * @author JianGe
 */
public class Common {

    // =================================================
    // Date Formats
    // For Logs date
    public static final SimpleDateFormat LOG_DATE_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // For Datapicker date
    public static final SimpleDateFormat DATAPICKER_DAY_SDF = new SimpleDateFormat("yyyy-MM-dd");
    // For 5 min data 
    public static final SimpleDateFormat DATE_OF_READIN_MIN_SDF = new SimpleDateFormat("yyyy/MM/dd-HHmm");
    public static final SimpleDateFormat DATE_OF_OUTPUT_MIN_SDF = new SimpleDateFormat("yyyy/MM/dd-HH:mm");

    // For day data
    public static final SimpleDateFormat DATE_OF_READIN_DALIY_SDF = new SimpleDateFormat("yyyy/MM/dd");

    public static class AsciiUnicode {

        public static final String GB2312 = "GB2312";
        public static final String ISO_8859_1 = "ISO-8859-1";
        public static final String UTF8 = "UTF-8";
        public static final String GBK = "GBK";
    }

    public static List removeDuplicateWithOrder(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Object element = iter.next();
            if (set.add(element)) {
                newList.add(element);
            }
        }
        return newList;
    }

    public static class Stock {

        public static class Infos {

            public static final String ID = "Id";
            public static final String STOCKCODE = "StockCode";
            public static final String STOCKNAME = "StockName";
            public static final String SELECTED = "Selected";
            public static final String BEST_CORR = "BestCorrelations";
            public static final String BEST_MATCHED_DATE = "BestMatchedDate";
            public static final String DETAILS = "Details";
        }

        public static enum DataTypes {

            OPEN_PRICE,
            HIGHEST_PRICE,
            LOWEST_PRICE,
            CLOSE_PRICE,
            VOL,
            CAPITAL,
            // need to calculate
            RATE_OF_AMPLITUDE,
            RATE_OF_VOLATILITY,
            RATE_OF_TURNOVER
//            public static final String VOL = "Volume";
//            public static final String CLOSE_PRICE = "ClosePrice";
//            public static final String OPEN_PRICE = "OpenPrice";
//            public static final String HIGHEST_PRICE = "HighestPrice";
//            public static final String LOWEST_PRICE = "LowestPrice";

        }

        public static enum DateTypes {
            Min5,
            Min15,
            Min30,
            Min60,
            Daliy
        }
    }

    public static class Tips {

        public static final Tooltip UPDATESTOCKLIST_TOOLTIP = new Tooltip("If you have new stocks, please check it on!");
        public static final Tooltip TREND_TOOLTIP = new Tooltip("Use trend to search!");
        public static final Tooltip VOLUME_TOOLTIP = new Tooltip("Use volume to search!");
        public static final Tooltip MULTISTOCK_TOOLTIP = new Tooltip("Show mulitple stocks!");
        public static final Tooltip SHANGHAI_TOOLTIP = new Tooltip("Show Shanghai indicator!");
        public static final Tooltip SHENZHEN_TOOLTIP = new Tooltip("Show ShenZhen indicator!");
        public static final Tooltip ASCFILEDATA_TOOLTIP = new Tooltip("Use data in Ascii files!");
        public static final Tooltip BINFILEDATA_TOOLTIP = new Tooltip("Use data in Binary files!");
        public static final Tooltip DATABASE_TOOLTIP = new Tooltip("Use data in database!");
        public static final Tooltip ALLSTOCKS_TOOLTIP = new Tooltip("Use all stocks as taget to match!");
        public static final Tooltip SELECTEDSTOCKS_TOOLTIP = new Tooltip("Use selected stocks as taget to match!");
        public static final Tooltip NOSTOCKSCODE_TOOLTIP = new Tooltip("No such a Stock Code!");
        public static final Tooltip NOSTOCKSNAME_TOOLTIP = new Tooltip("No such a Stock Name!");
        public static final Tooltip SHOWLOG_TOOLTIP = new Tooltip("Show Logs!");

    }

    public static class Hints {

        public static final String BINARY_HINT = "Binary Data Files (*.day, *.5)";
        public static final String ASCII_HINT = "Ascii Data Files (*.txt, *.dat, *.csv)";
        public static final String DB_HINT = "DataBase Files (*.db)";
    }

    public static class Keys {

        public static final String STOCKDATA_BIN_DEF_FILE_LOC = "BinFileLocation";
        public static final String STOCKDATA_DALIY_ASC_DEF_FILE_LOC = "DailyAscFilesLocation";
        public static final String STOCKDATA_5MIN_ASC_DEF_FILE_LOC = "5MinAscFilesLocation";
        public static final String STOCKDATA_DB_DEF_FILE_LOC = "StockDataDbFileLocation";
        public static final String STOCKCORR_DB_DEF_FILE_LOC = "StockCorrDbFileLocation";
        public static final String STOCKCORR_ASC_DEF_FILE_LOC = "StockCorrAscFilesLocation";
        public static final String STOCKREVIEW_DB_DEF_FILE_LOC = "StockReviewDbFileLocation";
        public static final String LOG_DEF_FILE_LOC = "LogFileLocation";
        public static final String STOCKLIST_DEF_FILE_LOC = "StockListLocation";
    }

    public static enum Response {
        NO, YES, CANCEL
    };

    public static class NumberAscendSort implements Comparator<Number> {

        @Override
        public int compare(Number o1, Number o2) {
            if (o1 instanceof Integer) {
                Integer s1 = (Integer) o1;
                Integer s2 = (Integer) o2;
                if (Objects.equals(s1, s2)) {
                    return 0;
                }
                return (s1 < s2) ? -1 : 1;

            } else if (o1 instanceof Float) {
                Float s1 = (Float) o1;
                Float s2 = (Float) o2;
                if (Objects.equals(s1, s2)) {
                    return 0;
                }
                return (s1 < s2) ? -1 : 1;

            } else if (o1 instanceof Double) {
                Double s1 = (Double) o1;
                Double s2 = (Double) o2;
                if (Objects.equals(s1, s2)) {
                    return 0;
                }
                return (s1 < s2) ? -1 : 1;

            } else {
                return -1;
            }
        }
    }

    public static ArrayList<ArrayList<String>> readDataFromDatabase(String code, String startDate, String endDate, int length, String dbPath) {
        ArrayList<ArrayList<String>> outputArrayLists = new ArrayList<>();
        ArrayList<String> dates;
        ArrayList<String> values;

        SQLiteJDBC sqliteJDBC = new SQLiteJDBC(dbPath);
        ArrayList<ArrayList<String>> data = sqliteJDBC.selectColumsWith1stByName("Close", code);
        dates = data.get(0);
        values = data.get(1);
//            dates = sqliteJDBC.selectColValueByName("Date", code);
//            values = sqliteJDBC.selectColValueByName("Close", code);

        outputArrayLists.add(dates);
        outputArrayLists.add(values);
        return outputArrayLists;
    }

    public static ArrayList<ArrayList<String>> readDataFromFiles(String code, String startDate, String endDate, int length, String ascDirectory) {
        ArrayList<ArrayList<String>> outputArrayLists = new ArrayList<>();
        ArrayList<String> dates;
        ArrayList<String> values;
        try {

            String fileName = ascDirectory + "\\" + code + ".txt";

            StockDataParser dataParser = new StockDataParser(IOHelper.readAsciiFile(fileName, "GB2312"), null);

//            dates = dataParser.getSingleStock().getDateList();
//            values = dataParser.getSingleStock().getCloseList();
//            outputArrayLists.add(dates);
//            outputArrayLists.add(values);
        } catch (Exception e) {
            return null;
        }

        return outputArrayLists;
    }

    public static void onCmBxCodeChanged(ComboBox cmBxCode, ComboBox cmBxName) {

        String stockCode = cmBxCode.getSelectionModel().getSelectedItem().toString();
        if (stockCode.isEmpty()) {
            cmBxName.getSelectionModel().select("");
            return;
        }

        String stockName = GlobalParameters.AllStockSetsForCalc.getSingleStockByCode(stockCode).getStockName();

        if (stockName.isEmpty()) {
            cmBxCode.setTooltip(Common.Tips.NOSTOCKSCODE_TOOLTIP);
            cmBxCode.getTooltip().setAutoHide(true);

            Logs.e("Don't have this (" + stockCode + ") stock code!");

            return;
        }

        cmBxName.getSelectionModel().select(stockName);
    }

    public static void onCmBxNameChanged(ComboBox cmBxCode, ComboBox cmBxName) {
        String stockCode;
        String stockName = cmBxName.getSelectionModel().getSelectedItem().toString();

        if (stockName.isEmpty()) {
            cmBxCode.getSelectionModel().select("");
            Logs.e("Don't have this stock code!");
            return;
        }

        stockCode = GlobalParameters.AllStockSetsForCalc.getSingleStockByName(stockName).getStockCode();

        if (stockCode.isEmpty()) {
            cmBxCode.setTooltip(Common.Tips.NOSTOCKSCODE_TOOLTIP);
            cmBxCode.getTooltip().setAutoHide(true);

            Logs.e("Don't have this (" + stockName + ") stock name!");

            return;
        }
        cmBxCode.getSelectionModel().select(stockCode);

    }

    /**
     *
     * @param stockCode
     * @param stockName
     * @param startDate
     * @param endDate
     * @param length
     * @param isDataBase
     * @param isAutoScale this parameter just use to depart the same stock in
     * different period, it's not a good way, but it's OK right now.
     * @return
     */
    public static Map<String, XYChart.Series> makeXYSeries(String stockCode, String stockName, String startDate, String endDate, int length, boolean isDataBase, boolean isAutoScale) {

        Map<String, XYChart.Series> seriesMap = new HashMap<>();
        XYChart.Series xyClosePriceSeries = new XYChart.Series();
        XYChart.Series xyVolumSeries = new XYChart.Series();

        ArrayList<ArrayList<String>> dataSource;
        ArrayList<String> dates;
        ArrayList<String> closePrices;
        ArrayList<String> volums;
        ArrayList<Float> meanValues = new ArrayList<>();

//        dataSource = GlobalParameters.AllStockSetsForCalc.getSingleStockByCode(stockCode).getDateCloseVolList(startDate, endDate, length);
//
//        dates = dataSource.get(0);
//        closePrices = dataSource.get(1);
//        volums = dataSource.get(2);
        float minCloseValue = 5000.0f;
        float maxCloseValue = 0.0f;
        float minVolumValue = 5000.0f;
        float maxVolumValue = 0.0f;
//        Logs.e("data set size is: " + closePrices.size());
//        for (int idx = 0; idx < dates.size(); idx++) {
//
//            minCloseValue = (minCloseValue < Float.valueOf(closePrices.get(idx))) ? minCloseValue : Float.valueOf(closePrices.get(idx));
//            maxCloseValue = (maxCloseValue > Float.valueOf(closePrices.get(idx))) ? maxCloseValue : Float.valueOf(closePrices.get(idx));
//
//            minVolumValue = (minVolumValue < Float.valueOf(volums.get(idx))) ? minVolumValue : Float.valueOf(volums.get(idx));
//            maxVolumValue = (maxVolumValue > Float.valueOf(volums.get(idx))) ? maxVolumValue : Float.valueOf(volums.get(idx));
//
//        }
//        Logs.e(stockCode + " minValue is: " + minCloseValue);
//        Logs.e(stockCode + " maxValue is: " + maxCloseValue);

//        if (isAutoScale) {
//            for (String value : values) {
//                meanValues.add((Float.valueOf(value) - minValue) / (maxValue - minValue));
//            }
//            for (int idx = 0; idx < dates.size(); idx++) {
//                series.getData().add(new XYChart.Data(String.valueOf(idx), Float.valueOf(values.get(idx)) / maxValue));
//            }
//        } else {
//            extremDistance = maxValue - minValue;
//            for (int idx = 0; idx < dates.size(); idx++) {
////            series.getData().add(new XYChart.Data(String.valueOf(idx), Float.valueOf(values.get(idx))));
//                series.getData().add(new XYChart.Data(String.valueOf(idx), Float.valueOf(values.get(idx)) / maxValue));
//            }
//        }
//        if (isAutoScale) {
//            for (int index = 0; index < dates.size(); index++) {
//
//                xyClosePriceSeries.getData().add(new XYChart.Data(dates.get(index), (Float.valueOf(closePrices.get(index)) - minCloseValue) / 1.1f));
//                xyVolumSeries.getData().add(new XYChart.Data(dates.get(index), Float.valueOf(volums.get(index)) * 8.0f / maxVolumValue));
//            }
//        } else {
//            for (int index = 0; index < dates.size(); index++) {
//
//                xyClosePriceSeries.getData().add(new XYChart.Data(dates.get(index), (Float.valueOf(closePrices.get(index)) - minCloseValue)));
//                xyVolumSeries.getData().add(new XYChart.Data(dates.get(index), Float.valueOf(volums.get(index)) * 8.0f / maxVolumValue));
//            }
//        }
        //This the series name not the chart title!!!
//        xyVolumSeries.setName(stockCode + ", " + stockName);
        return seriesMap;
    }

    public static ObservableList<Map<String, String>> getCodeNameMap(ArrayList<String> codes, ArrayList<String> names) {
        ObservableList<Map<String, String>> items
                = FXCollections.<Map<String, String>>observableArrayList();
        // Extract the stock list, add the list to a Map, and add the Map to
        // the items list
        for (int i = 0; i < codes.size(); i++) {

            Map<String, String> map = new HashMap<>();
            map.put(Common.Stock.Infos.ID, String.valueOf(i + 1));
            map.put(Common.Stock.Infos.STOCKCODE, codes.get(i));
            map.put(Common.Stock.Infos.STOCKNAME, names.get(i));
            map.put(Common.Stock.Infos.SELECTED, "");
            items.add(map);
        }

        return items;
    }

    public static ArrayList<ArrayList<String>> readAsciiFile(String filePath, String code) {
        BufferedReader reader = null;
        ArrayList<ArrayList<String>> contentsArrayList = new ArrayList<>();

        try {
            if (!(new File(filePath).exists())) {
                GlobalParameters.logErro(LogInformation.Category.FILES, "The file does not exist: " + filePath);
                Logs.e("The file does not exist: " + filePath);
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), code));
            String tempString = null;
            int lineCount = 1;
            int colNumber = 0;
            while ((tempString = reader.readLine()) != null) {
                String[] readString;
                //Replace all "tabs" and "space" to one "space"
//                Logs.e(tempString);
                readString = tempString.replaceAll("\t", " ").replaceAll(" +", " ").split(" ");
                colNumber = readString.length;
                if (lineCount == 1) {

                    for (int i = 0; i < colNumber; i++) {
                        contentsArrayList.add(new ArrayList<>());
                    }
                }

                if (colNumber != contentsArrayList.size()) {
                    GlobalParameters.logErro(LogInformation.Category.FILES, "The content of " + filePath + " has wrong.");
                    Logs.e("The content of " + filePath + " has wrong.");
                    return null;
                }

                for (int col = 0; col < colNumber; col++) {
                    contentsArrayList.get(col).add(readString[col]);

                }

                lineCount++;
            }
            reader.close();
        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return contentsArrayList;
    }

    public static ObservableList<Map<String, String>> getMapObListFromArrayList(ArrayList<ArrayList<String>> values) {
        ObservableList<Map<String, String>> items
                = FXCollections.<Map<String, String>>observableArrayList();
        // Extract the stock data, add the data to a Map, and add the Map to
        // the items list

        ArrayList<String> columnNames = new ArrayList<>();

        ArrayList<String> idList = new ArrayList<>();

        //make an "ID" 
        for (int idx = 0; idx < values.get(0).size(); idx++) {
            if (idx == 0) {
                idList.add("ID");
            } else {
                idList.add(String.valueOf(idx));
            }
        }
        values.add(0, idList);

        for (int col = 0; col < values.size(); col++) {
            columnNames.add(values.get(col).get(0));
        }

        if (values.isEmpty()) {
            return null;
        }
        for (int row = 1; row < values.get(0).size(); row++) {
            Map<String, String> map = new HashMap<>();
            for (int col = 0; col < values.size(); col++) {
                map.put(columnNames.get(col), values.get(col).get(row));

            }
            items.add(map);
        }

//        Map<String, ArrayList<String>> values = sqliteJDBC.selectColsValueByNameList(columnNames, stockCode);
//               Logs.e("DONE!");
        return items;
    }

    public ArrayList<ArrayList<String>> parseAsciiFile(ArrayList<String> SourceData) {
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        String tempString = null;
        int line = 1;
        int col_number = SourceData.get(0).replaceAll(" +", " ").split(" ").length;
        for (int i = 0; i < col_number; i++) {
            output.add(new ArrayList<>());
        }

        for (int row = 0; row < SourceData.size(); row++) {
            tempString = (String) SourceData.get(row);
//            Logs.e("line " + line + ": " + tempString);
            String[] readString = tempString.replaceAll(" +", " ").split(" ");
            for (int col = 0; col < readString.length; col++) {
                String string = readString[col];
                output.get(col).add(string);
            }

        }
        return output;
    }

}
