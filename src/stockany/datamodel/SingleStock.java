/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.datamodel;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import tools.utilities.Logs;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import tools.utilities.ChineseToEnglish;
import stockany.tools.StockDataParser;
import stockany.tools.Common;
import stockany.tools.Common.Stock;
import stockany.ui.controls.CheckBoxForTableCell;
import tools.files.FileHelper;
import tools.files.IOHelper;

/**
 *
 * @author JianGe
 */
public class SingleStock implements Cloneable {

    private SimpleStringProperty ID;
    private CheckBoxForTableCell chkBox;
    private SimpleStringProperty stockCode;
    private SimpleStringProperty stockName;
    private SimpleStringProperty stockSize; //Daily size
    private ArrayList<StockDataElement> stockDaliyData = new ArrayList<>();
    private ArrayList<StockDataElement> stock5MinData = new ArrayList<>();
    private ArrayList<StockDataElement> stock15MinData = new ArrayList<>();
    private ArrayList<StockDataElement> stock30MinData = new ArrayList<>();
    private ArrayList<StockDataElement> stock60MinData = new ArrayList<>();
    private ArrayList<String> OCHLData = new ArrayList<>();
    private ArrayList<Date> OCHLDate = new ArrayList<>();
    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public SingleStock() {
        this.ID = new SimpleStringProperty();
        this.chkBox = new CheckBoxForTableCell();
        this.stockCode = new SimpleStringProperty();
        this.stockName = new SimpleStringProperty();
        this.stockSize = new SimpleStringProperty();
    }

    public SingleStock(int id, String code, String name, int size) {
        this.ID = new SimpleStringProperty(String.valueOf(id));
        this.chkBox = new CheckBoxForTableCell();
        this.stockCode = new SimpleStringProperty(code);
        this.stockName = new SimpleStringProperty(name);
        this.stockSize = new SimpleStringProperty(String.valueOf(size));
    }

    @Override
    public SingleStock clone() throws CloneNotSupportedException {
        SingleStock clonedSingleStock = null;
        try {
            clonedSingleStock = (SingleStock) super.clone();
            clonedSingleStock.ID = new SimpleStringProperty(this.ID.get());
            clonedSingleStock.chkBox = new CheckBoxForTableCell();
            clonedSingleStock.stockCode = new SimpleStringProperty(this.stockCode.get());
            clonedSingleStock.stockName = new SimpleStringProperty(this.stockName.get());
            clonedSingleStock.stockSize = new SimpleStringProperty(this.stockSize.get());
            clonedSingleStock.OCHLData = (ArrayList<String>) this.OCHLData.clone();
            clonedSingleStock.OCHLDate = (ArrayList<Date>) this.OCHLDate.clone();
            for (int i = 0; i < stockDaliyData.size(); i++) {

                clonedSingleStock.getStockDaliyData().add(stockDaliyData.get(i).clone());

            }
        } catch (CloneNotSupportedException e) {
        }
        return clonedSingleStock;
    }

    //===========================================
    // Getter and Setter
    public ArrayList<StockDataElement> getStockDaliyData() {
        return stockDaliyData;
    }

    public void setStockDailyData(ArrayList<StockDataElement> stockData) {
        this.stockDaliyData = stockData;
    }

    public ArrayList<StockDataElement> getStock5MinData() {
        return stock5MinData;
    }

    public void setStock5MinData(ArrayList<StockDataElement> stock5MinData) {
        this.stock5MinData = stock5MinData;
    }

    public ArrayList<StockDataElement> getStock15MinData() {
        return stock15MinData;

    }

    public void setStock15MinData(ArrayList<StockDataElement> stock15MinData) {
        this.stock15MinData = stock15MinData;
    }

    public ArrayList<StockDataElement> getStock30MinData() {
        return stock30MinData;

    }

    public void setStock30MinData(ArrayList<StockDataElement> stock30MinData) {
        this.stock30MinData = stock30MinData;
    }

    public ArrayList<StockDataElement> getStock60MinData() {
        return stock60MinData;

    }

    public void setStock60MinData(ArrayList<StockDataElement> stock60MinData) {
        this.stock60MinData = stock60MinData;
    }

    public String getID() {
        return ID.get();
    }

    public SimpleStringProperty idProperty() {
        return ID;
    }

    public void setID(SimpleStringProperty ID) {
        this.ID = ID;
    }

    public void setID(String ID) {
        this.ID.set(ID);
    }

    public CheckBoxForTableCell getChkBox() {
        return chkBox;
    }

    public void setChkBox(CheckBoxForTableCell chkBox) {
        this.chkBox = chkBox;
    }

    public void setStockCode(SimpleStringProperty stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockCode() {
        return stockCode.get();
    }

    public SimpleStringProperty stockCodeProperty() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode.set(stockCode);
    }

    public String getStockName() {
        return stockName.get();
    }

    public String getStockNameHead() {
        return ChineseToEnglish.getPinYinHeadChar(stockName.get());
    }

    public SimpleStringProperty stockNameProperty() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName.set(stockName);
    }

    public void setStockName(SimpleStringProperty stockName) {
        this.stockName = stockName;
    }

    public String getStockSize() {
        return stockSize.get();
    }

    public SimpleStringProperty stockSizeProperty() {
        return stockSize;
    }

    public void setStockSize(SimpleStringProperty stockSize) {
        this.stockSize = stockSize;
    }

    public void setStockSize(String stockSize) {
        this.stockSize.set(stockSize);
    }

    // End of Getter and Setter
    //===========================================
    //===========================================
    public boolean read5MinData() {
        long start = System.currentTimeMillis();
        boolean Successful = false;
        File min5Directory = new File(GlobalParameters.WORKING_5MIN_DATA_ASC_PATH);
        if (min5Directory.exists()) {

            for (File min5File : min5Directory.listFiles()) {
                if (min5File.getName().contains(this.stockCode.get())) {
                    StockDataParser dataParser = new StockDataParser(null,
                            IOHelper.readAsciiFile(min5File, Common.AsciiUnicode.GB2312));
                    stock5MinData = dataParser.getSingleStock().getStock5MinData();
                    Successful = true;
                    break;
                }
            }
        } else {
            Logs.e("Read 5 min failed");
            Successful = false;
        }

//        Logs.e("read 5 min: " + (System.currentTimeMillis() - start));
        return Successful;
    }

    public boolean check5MinData() {
        if (stock5MinData.isEmpty() && !read5MinData()) {
            Logs.e("init 5 min data erro!");
            return false;
        } else {
//            Logs.e("checked");
            return true;
        }
    }

    public boolean make15MinData() {
        if (!check5MinData()) {
            return false;
        }

        for (int index = 0; index < stock5MinData.size(); index++) {
            StockDataElement element = stock5MinData.get(index);

            Date timeFlag = element.getTimeFlag();
            if (timeFlag != null) {
                try {
                    if (timeFlag.equals(sdf.parse("09:35")) || timeFlag.equals(sdf.parse("9:50"))
                            || timeFlag.equals(sdf.parse("10:05")) || timeFlag.equals(sdf.parse("10:20"))
                            || timeFlag.equals(sdf.parse("10:35")) || timeFlag.equals(sdf.parse("10:50"))
                            || timeFlag.equals(sdf.parse("11:05")) || timeFlag.equals(sdf.parse("11:20"))
                            || timeFlag.equals(sdf.parse("13:05")) || timeFlag.equals(sdf.parse("13:20"))
                            || timeFlag.equals(sdf.parse("13:35")) || timeFlag.equals(sdf.parse("13:50"))
                            || timeFlag.equals(sdf.parse("14:05")) || timeFlag.equals(sdf.parse("14:20"))
                            || timeFlag.equals(sdf.parse("14:35")) || timeFlag.equals(sdf.parse("14:50"))) {

                        if (getDataElement(Stock.DateTypes.Min15, timeFlag, index, 3) != null) {
                            stock15MinData.add(getDataElement(Stock.DateTypes.Min15, timeFlag, index, 3));
                        }

                    }
                } catch (ParseException e) {
                    return false;
                }

            }
        }
        return true;
    }

    public boolean make30MinData() {
        if (!check5MinData()) {
            return false;
        }

        for (int index = 0; index < stock5MinData.size(); index++) {
            StockDataElement element = stock5MinData.get(index);

            Date timeFlag = element.getTimeFlag();
            if (timeFlag != null) {
                try {
                    if (timeFlag.equals(sdf.parse("09:35")) || timeFlag.equals(sdf.parse("10:05"))
                            || timeFlag.equals(sdf.parse("10:35")) || timeFlag.equals(sdf.parse("11:05"))
                            || timeFlag.equals(sdf.parse("13:05")) || timeFlag.equals(sdf.parse("13:35"))
                            || timeFlag.equals(sdf.parse("14:05")) || timeFlag.equals(sdf.parse("14:35"))) {

                        if (getDataElement(Stock.DateTypes.Min30, timeFlag, index, 6) != null) {
                            stock30MinData.add(getDataElement(Stock.DateTypes.Min30, timeFlag, index, 6));
                        }

                    }
                } catch (ParseException e) {
                    return false;
                }

            }
        }
        return true;
    }

    public boolean make60MinData() {
        if (!check5MinData()) {
            Logs.e("fuck");
            return false;
        }

        for (int index = 0; index < stock5MinData.size(); index++) {
            StockDataElement element = stock5MinData.get(index);

            Date timeFlag = element.getTimeFlag();
            if (timeFlag != null) {
                try {
                    if (timeFlag.equals(sdf.parse("09:35")) || timeFlag.equals(sdf.parse("10:35"))
                            || timeFlag.equals(sdf.parse("13:05")) || timeFlag.equals(sdf.parse("14:05"))) {

                        if (getDataElement(Stock.DateTypes.Min60, timeFlag, index, 12) != null) {
                            stock60MinData.add(getDataElement(Stock.DateTypes.Min60, timeFlag, index, 12));
                        }

                    }
                } catch (ParseException e) {
                    return false;
                }

            }
        }
        return true;
    }

    private StockDataElement getDataElement(Stock.DateTypes dateType, Date flag, int start, int step) {
        StockDataElement dataElement = null;

        for (int idx = 1; idx < step; idx++) {
            if (!stock5MinData.get(start + idx).getTimeFlag().equals(new Date(flag.getTime() + 60 * 5 * 1000 * idx))) {
                Logs.e(idx + "," + stockCode + " , " + Common.LOG_DATE_SDF.format(stock5MinData.get(start + idx).getDate()) + " , " + Common.LOG_DATE_SDF.format(stock5MinData.get(start + idx).getTimeFlag()) + " erro");
                return null;
            }
        }

//            Logs.e(Utils.LOG_DATE_SDF.format(stock5MinData.get(start + 2).getDate()));
        ArrayList<Float> priceList = new ArrayList<>();
        Date date = stock5MinData.get(start + step - 1).getDate();
        float open;
        float close;
        float highest;
        float lowest;
        float capital = 0;
        float vol = 0;
        for (int idx = 0; idx < step; idx++) {
            priceList.add(stock5MinData.get(start + idx).getOpenPrice());
            priceList.add(stock5MinData.get(start + idx).getClosePrice());
            priceList.add(stock5MinData.get(start + idx).getHighestPrice());
            priceList.add(stock5MinData.get(start + idx).getLowestPrice());
            capital = (capital + stock5MinData.get(start + idx).getCapital());
            vol = (vol + stock5MinData.get(start + idx).getVolume());
        }
        open = stock5MinData.get(start).getOpenPrice();
        close = stock5MinData.get(start + step - 1).getClosePrice();

        Collections.sort(priceList, new Common.NumberAscendSort());
        highest = priceList.get(priceList.size() - 1);
        lowest = priceList.get(0);

        dataElement = new StockDataElement(dateType, date, open, close, highest, lowest, capital, vol);

        return dataElement;
    }

    public void makeOCHL() {
        for (StockDataElement thi : stockDaliyData) {
            OCHLData.add(String.valueOf(thi.getOpenPrice()));
            OCHLData.add(String.valueOf(thi.getHighestPrice()));
            OCHLData.add(String.valueOf(thi.getLowestPrice()));
            OCHLData.add(String.valueOf(thi.getClosePrice()));
            OCHLDate.add(thi.getDate());
            OCHLDate.add(thi.getDate());
            OCHLDate.add(thi.getDate());
            OCHLDate.add(thi.getDate());

        }
    }

    /**
     * when single stock load work is done, calculate the rate of stock price
     * Volatility. Use this method before use this data to calculate
     *
     * @param dateType
     */
    public void calculateSub(Stock.DateTypes dateType) {

        setRateOfVolatility(dateType);
        setRateOfAmplitude(dateType);
//        setRateOfTurnover();

    }

    public void setRateOfVolatility(Stock.DateTypes dateType) {
        ArrayList<StockDataElement> stockData = getStockDataList(dateType);
        if (stockData == null) {
            return;
        }
        float rate = 0.0f;
        if (stockData.size() >= 2) {
            stockData.get(0).setRateOfVolatility(rate);
        } else {
            return;
        }
        try {
            for (int i = 1; i < stockData.size(); i++) {
                rate = 100.0f * (stockData.get(i).getClosePrice() - stockData.get(i - 1).getClosePrice()) / stockData.get(i - 1).getClosePrice();
                stockData.get(i).setRateOfVolatility(rate);

            }
        } catch (Exception e) {
        }

    }

    /**
     * when single stock load work is done, calculate the rate of stock price
     * Amplitude. Use this method before use stockData data to calculate
     *
     * @param dateType
     */
    public void setRateOfAmplitude(Stock.DateTypes dateType) {
        ArrayList<StockDataElement> stockData = getStockDataList(dateType);
        if (stockData == null) {
            return;
        }
        float rate = 0.0f;
        if (stockData.size() >= 2) {
            rate = 100.0f * (stockData.get(0).getHighestPrice() - stockData.get(0).getLowestPrice()) / stockData.get(0).getClosePrice();
            stockData.get(0).setRateOfAmplitude(rate);
        } else {
            return;
        }
        try {
            for (int i = 1; i < stockData.size(); i++) {
                rate = 100.0f * (stockData.get(i).getHighestPrice() - stockData.get(i).getLowestPrice()) / stockData.get(i - 1).getClosePrice();
                stockData.get(i).setRateOfAmplitude(rate);

            }
        } catch (Exception e) {
        }

    }

    public void setRateOfTurnover() {
        float rate = 0.0f;

    }

    //============================================================
    /**
     *
     * @param startDate
     * @param length
     * @param endDate
     * @return
     */
    public SingleStock getPartSingleStock(final Date startDate, final int length, final Date endDate) {

        SingleStock singleStock = new SingleStock();
        singleStock.setID(ID);
        singleStock.setStockCode(stockCode);
        singleStock.setStockName(stockName);

        singleStock.setStock5MinData(getPartDataList(Stock.DateTypes.Min5, startDate, length * 48, endDate));
        singleStock.setStock15MinData(getPartDataList(Stock.DateTypes.Min15, startDate, length * 16, endDate));
        singleStock.setStock30MinData(getPartDataList(Stock.DateTypes.Min30, startDate, length * 8, endDate));
        singleStock.setStock60MinData(getPartDataList(Stock.DateTypes.Min60, startDate, length * 4, endDate));
        singleStock.setStockDailyData(getPartDataList(Stock.DateTypes.Daliy, startDate, length, endDate));
//        if (singleStock.getStock5MinData() != null) {
//            Logs.e("5 MIN: " + singleStock.getStock5MinData().get(singleStock.getStock5MinData().size() - 1).toLongString());
//
//        }
//
//        if (singleStock.getStock15MinData() != null) {
//            Logs.e("15 MIN: " + singleStock.getStock15MinData().get(singleStock.getStock15MinData().size() - 1).toLongString());
//
//        }
//
//        if (singleStock.getStock30MinData() != null) {
//            Logs.e("30 MIN: " + singleStock.getStock30MinData().get(singleStock.getStock30MinData().size() - 1).toLongString());
//
//        }
//
//        if (singleStock.getStock60MinData() != null) {
//            Logs.e("60 MIN: " + singleStock.getStock60MinData().get(singleStock.getStock60MinData().size() - 1).toLongString());
//
//        }
        return singleStock;
    }

    private ArrayList<StockDataElement> getPartDataList(Stock.DateTypes dateType, final Date startDate, final int length, final Date endDate) {
        ArrayList<StockDataElement> stockData = getStockDataList(dateType);
        ArrayList<StockDataElement> outputList = new ArrayList<>();
        Date start;
        Date end;
        if (startDate != null && endDate != null) {
            start = startDate;
            end = endDate;

        } else if (startDate == null && endDate == null) {
            start = stockData.get(0).getDate();
            end = stockData.get(stockData.size() - 1).getDate();

        } else if (startDate != null && endDate == null) {
            start = startDate;
            Date tempStart = null;
            Date tempEnd = null;
            if (length > 0) {   // To a certain date of this stock
                for (int idx = 0; idx < stockData.size(); idx++) {
                    Date read = stockData.get(idx).getDate();

                    if (read.getTime() >= start.getTime()) {
                        tempStart = stockData.get(idx).getDate();
                        if ((idx + length) < stockData.size()) {
                            tempEnd = stockData.get(idx + length - 1).getDate();
                        } else {
                            tempEnd = stockData.get(stockData.size() - 1).getDate();
                        }
                        break;
                    }
                }

                if (tempStart != null && tempEnd != null) {
                    start = tempStart;
                    end = tempEnd;
                } else {
                    return null;
                }
            } else {    // To last date of this stock
                end = stockData.get(stockData.size() - 1).getDate();
            }

        } else if (startDate == null && endDate != null) {
            end = endDate;
            Date tempStart = null;
            Date tempEnd = null;

            if (length > 0) {   // To a certain date of this stock
                for (int idx = stockData.size() - 1; idx >= 0; idx--) {
                    Date read = stockData.get(idx).getDate();

                    if (read.getTime() <= end.getTime()) {
                        tempEnd = stockData.get(idx).getDate();
                        if (idx >= length) {
                            tempStart = stockData.get(idx + 1 - length).getDate();
                        } else {
                            tempStart = stockData.get(0).getDate();
                        }
                        break;
                    }
                }

                if (tempStart != null && tempEnd != null) {
                    start = tempStart;
                    end = tempEnd;
                } else {
                    return null;
                }
            } else {    // To last date of this stock
                start = stockData.get(0).getDate();
            }
        } else {
            Logs.e("error!!!!");
            return null;
        }
        // read data
        if (stockData.size() > 5) {
            for (int i = 0; i < stockData.size(); i++) {
                StockDataElement get = stockData.get(i);

                if (get.getDate() == null) {
                    Logs.e(this.getStockCode() + " date is null!");
                    return null;
                }

                Date read = get.getDate();
                if (read.getTime() >= start.getTime() && read.getTime() <= end.getTime()) {
                    outputList.add(get);
                }

            }
        } else {
            return null;
        }

        return outputList;
    }

    public ArrayList<Date> getDateList(Stock.DateTypes dateType) {
        ArrayList<StockDataElement> stockData = getStockDataList(dateType);
        ArrayList<Date> dateList = new ArrayList<>();
        stockData.forEach((aThi) -> {
            dateList.add(aThi.getDate());
        });
        return dateList;
    }

    public ArrayList<String> getDateStringList(Stock.DateTypes dateType) {
        ArrayList<StockDataElement> stockData = getStockDataList(dateType);
        ArrayList<String> dateList = new ArrayList<>();
        stockData.forEach((aThi) -> {
            dateList.add(aThi.getDateString());
        });
        return dateList;
    }

    public Date[] getDateArray(Stock.DateTypes dateType) {
        ArrayList<StockDataElement> stockData = getStockDataList(dateType);
        Date[] dateArray = new Date[stockData.size()];
        for (int i = 0; i < stockData.size(); i++) {
            dateArray[i] = stockData.get(i).getDate();

        }

        return dateArray;
    }

    public ArrayList<String> getDataList(Stock.DateTypes dateType, Stock.DataTypes dataType) {
        ArrayList<StockDataElement> stockData = getStockDataList(dateType);
        ArrayList<String> outputList = new ArrayList<>();

        switch (dataType) {
            case OPEN_PRICE:
                stockData.forEach((aThi) -> {
                    outputList.add(String.valueOf(aThi.getOpenPrice()));
                });
                break;
            case HIGHEST_PRICE:
                stockData.forEach((aThi) -> {
                    outputList.add(String.valueOf(aThi.getHighestPrice()));
                });
                break;
            case LOWEST_PRICE:
                stockData.forEach((aThi) -> {
                    outputList.add(String.valueOf(aThi.getLowestPrice()));
                });
                break;
            case CLOSE_PRICE:
                stockData.forEach((aThi) -> {
                    outputList.add(String.valueOf(aThi.getClosePrice()));
                });
                break;
            case VOL:
                stockData.forEach((aThi) -> {
                    outputList.add(String.valueOf(aThi.getVolume()));
                });
                break;
            case CAPITAL:
                stockData.forEach((aThi) -> {
                    outputList.add(String.valueOf(aThi.getCapital()));
                });
            case RATE_OF_AMPLITUDE:
                stockData.forEach((aThi) -> {
                    outputList.add(String.valueOf(aThi.getRateOfAmplitude()));
                });
                break;
            case RATE_OF_VOLATILITY:
                stockData.forEach((aThi) -> {
                    outputList.add(String.valueOf(aThi.getRateOfVolatility()));
                });
            case RATE_OF_TURNOVER:
                stockData.forEach((aThi) -> {
                    outputList.add(String.valueOf(aThi.getRateOfTurnover()));
                });
                break;
            default:
                break;
        }
        return outputList;

    }

    /**
     * get an Array of data based on "DATA TYPE"
     *
     * @param dateType
     * @param dataType
     * @return
     */
    public float[] getDataArray(Stock.DateTypes dateType, Stock.DataTypes dataType) {
        ArrayList<StockDataElement> stockData = getStockDataList(dateType);
        if (stockData == null) {
            return null;
        }
        float[] outputArray = new float[stockData.size()];

        switch (dataType) {
            case OPEN_PRICE:
                for (int i = 0; i < stockData.size(); i++) {
                    outputArray[i] = stockData.get(i).getOpenPrice();
                }
                break;
            case HIGHEST_PRICE:
                for (int i = 0; i < stockData.size(); i++) {
                    outputArray[i] = stockData.get(i).getHighestPrice();
                }
                break;
            case LOWEST_PRICE:
                for (int i = 0; i < stockData.size(); i++) {
                    outputArray[i] = stockData.get(i).getLowestPrice();
                }
                break;
            case CLOSE_PRICE:
                for (int i = 0; i < stockData.size(); i++) {
                    outputArray[i] = stockData.get(i).getClosePrice();
                }
                break;
            case VOL:
                for (int i = 0; i < stockData.size(); i++) {
                    outputArray[i] = stockData.get(i).getVolume();
                }
                break;
            case CAPITAL:
                for (int i = 0; i < stockData.size(); i++) {
                    outputArray[i] = stockData.get(i).getCapital();
                }
                break;
            case RATE_OF_AMPLITUDE:
                for (int i = 0; i < stockData.size(); i++) {
                    outputArray[i] = stockData.get(i).getRateOfAmplitude();
                }
                break;
            case RATE_OF_VOLATILITY:
                for (int i = 0; i < stockData.size(); i++) {
                    outputArray[i] = stockData.get(i).getRateOfVolatility();
                }
                break;
            case RATE_OF_TURNOVER:
                for (int i = 0; i < stockData.size(); i++) {
                    outputArray[i] = stockData.get(i).getRateOfTurnover();
                }
                break;
            default:
                break;
        }

        return outputArray;

    }

    public double[] getDataArrayDouble(Stock.DateTypes dateType, Stock.DataTypes dataType) {
        float[] floatArray = getDataArray(dateType, dataType);
        double[] outputArray = new double[floatArray.length];
        for (int i = 0; i < floatArray.length; i++) {
            outputArray[i] = (double) floatArray[i];

        }

        return outputArray;
    }

    /**
     * return a stock data list by DATE type
     *
     * @param dateType
     * @return
     */
    private ArrayList<StockDataElement> getStockDataList(Stock.DateTypes dateType) {
        ArrayList<StockDataElement> stockData = new ArrayList<>();

        switch (dateType) {
            case Min5:
                stockData = this.stock5MinData;
                break;
            case Min15:
                stockData = this.stock15MinData;
                break;
            case Min30:
                stockData = this.stock30MinData;
                break;
            case Min60:
                stockData = this.stock60MinData;
                break;
            case Daliy:
                stockData = this.stockDaliyData;
                break;
            default:
                break;
        }
        return stockData;
    }

    //==============================================================
    /**
     * get the index of given date, if it's not in this stock, then find the
     * nearest date index
     *
     * @param isFromback the flag to determine how to get the nearest date, from
     * back to forward, take the date after the given date; other take the date
     * before the give date
     * @param date
     * @return
     */
    public int getDateIndex(boolean isFromback, String date) {
        int index = -1;
        //If it has the date, will return the index of this date
        for (int idx = 0; idx < stockDaliyData.size(); idx++) {
            if (stockDaliyData.get(idx).getDate().equals(date)) {
                return idx;
            }
        }

        for (int idx = 0; idx < stockDaliyData.size(); idx++) {
            try {
                Date searchDate = Common.DATE_OF_READIN_DALIY_SDF.parse(date);
                Date node = stockDaliyData.get(idx).getDate();

                if (searchDate.compareTo(stockDaliyData.get(0).getDate()) < 0) {
//                    Logs.e(this.getStockCode() + "  " + date + ", Index is: " + idx);
                    return 0;
                }
                if (searchDate.compareTo(stockDaliyData.get(stockDaliyData.size() - 1).getDate()) >= 0) {
//                    Logs.e(this.getStockCode() + "  " + date + ", Index is: " + (this.size() - 1));
                    return stockDaliyData.size() - 1;
                }

                if (searchDate.compareTo(node) < 0) {
                    if (isFromback) {
//                        Logs.e(this.getStockCode() + "  " + date + ", Index is: " + (idx));
                        return idx + 1;
                    } else {
//                        Logs.e(this.getStockCode() + "  " + date + ", Index is: " + (idx - 1));
                        return idx - 1;
                    }

                }
            } catch (ParseException e) {
            }

        }
        return index;
    }

    public ArrayList<ArrayList<String>> getDateCloseVolList(String startDate, String endDate, int length) {
        ArrayList<ArrayList<String>> outputList = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<String> closeList = new ArrayList<>();
        ArrayList<String> volList = new ArrayList<>();
        int readCount = 0;
        // From back to forward
        if (startDate.isEmpty() && endDate.isEmpty()) {
            startDate = "0000/00/00";
//            Logs.e("fuck");
        }
        if (startDate.isEmpty() && !endDate.isEmpty()) {
//            Logs.e("fuck 1");
            try {
                int endDatePosition = getDateIndex(true, endDate);
//                Logs.e("endPosition: " + endDatePosition);
                if (endDatePosition == -1) {
                    Logs.e("Get Date index erro, returned!");
                    return null;
                }
                int startIndex;

                if (length <= endDatePosition) {
                    startIndex = endDatePosition - length + 1;
                } else {
                    startIndex = 0;
                }

                for (int idx = startIndex; idx < endDatePosition + 1; idx++) {
                    dateList.add(stockDaliyData.get(idx).getDateString());
                    closeList.add(String.valueOf(stockDaliyData.get(idx).getClosePrice()));
                    volList.add(String.valueOf(stockDaliyData.get(idx).getVolume()));
                }
            } catch (Exception e) {
                return null;
            }
        } else if (!startDate.isEmpty() && endDate.isEmpty()) {
//            Logs.e("fuck 2");
            // From forward to back
            try {
                int startDatePosition = getDateIndex(false, startDate);
//                Logs.e("start at: " + startDatePosition);
                if (startDatePosition == -1) {
                    Logs.e("Get Date index erro");
                    return null;
                }
                int endIndex;

                if (length + startDatePosition + 1 <= stockDaliyData.size()) {
                    endIndex = length + startDatePosition;
                } else {
                    endIndex = stockDaliyData.size();
                }

                for (int idx = startDatePosition; idx < endIndex; idx++) {
                    dateList.add(stockDaliyData.get(idx).getDateString());
                    closeList.add(String.valueOf(stockDaliyData.get(idx).getClosePrice()));
                    volList.add(String.valueOf(stockDaliyData.get(idx).getVolume()));
                }
            } catch (Exception e) {
                GlobalParameters.logErro(LogInformation.Category.CALC, e.toString());
                Logs.e(e.toString());
                return null;
            }

        } else {
            Logs.e("Bad parameter!");
            return null;
        }

        outputList.add(dateList);
        outputList.add(closeList);
        outputList.add(volList);

//        Logs.e(this.getStockCode() + " Get Data done!");
        return outputList;
    }

    public Map<String, ArrayList<String>> toMapData() {
        Map<String, ArrayList<String>> output = new LinkedHashMap<>();

        output.put("Date", getDateStringList(Stock.DateTypes.Daliy));
        output.put("Open", getDataList(Stock.DateTypes.Daliy, Stock.DataTypes.OPEN_PRICE));
        output.put("Highest", getDataList(Stock.DateTypes.Daliy, Stock.DataTypes.HIGHEST_PRICE));
        output.put("Lowest", getDataList(Stock.DateTypes.Daliy, Stock.DataTypes.LOWEST_PRICE));
        output.put("Close", getDataList(Stock.DateTypes.Daliy, Stock.DataTypes.CLOSE_PRICE));
        output.put("Capital", getDataList(Stock.DateTypes.Daliy, Stock.DataTypes.CAPITAL));
        output.put("Volume", getDataList(Stock.DateTypes.Daliy, Stock.DataTypes.VOL));

        return output;

    }

}
