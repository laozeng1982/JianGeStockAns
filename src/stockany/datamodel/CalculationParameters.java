/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.datamodel;

import java.util.ArrayList;
import java.util.Date;
import javax.rmi.CORBA.Util;
import stockany.tools.Common;
import stockany.tools.Common.Stock;
import tools.utilities.Logs;

/**
 *
 * @author JianGe
 */
public class CalculationParameters {

    private ArrayList<String> SourceStocksCodeList;
    private ArrayList<String> SourceStocksNameList;
    private ArrayList<PatternType> ProcessPatternList;
    private ArrayList<Stock.DateTypes> ProcessDateTypeList;
    private ArrayList<Integer> ProcessLengthList;
    private int ThreadNumber;
    private Date StartDate = null;
    private Date EndDate = null;

    public CalculationParameters() {

        this.SourceStocksCodeList = new ArrayList<>();
        this.SourceStocksNameList = new ArrayList<>();
        this.ProcessPatternList = new ArrayList<>();
        this.ProcessDateTypeList = new ArrayList<>();
        this.ProcessLengthList = new ArrayList<>();
    }

    public void clearAllParameters() {
        this.SourceStocksCodeList.clear();
        this.SourceStocksNameList.clear();
        this.ProcessPatternList.clear();
        this.ProcessDateTypeList.clear();
        this.ProcessLengthList.clear();
    }

    //Getter and Setter
    public ArrayList<String> getSourceStocksCodeList() {
        return SourceStocksCodeList;
    }

    public void setSourceStocksCodeList(ArrayList<String> StocksList) {
        this.SourceStocksCodeList = StocksList;
    }

    public ArrayList<String> getSourceStocksNameList() {
        return SourceStocksNameList;
    }

    public void setSourceStocksNameList(ArrayList<String> StocksNameList) {
        this.SourceStocksNameList = StocksNameList;
    }

    public ArrayList<PatternType> getProcessPatternList() {
        return ProcessPatternList;
    }

    public void setProcessPatternList(ArrayList<PatternType> ProcessPatternList) {
        this.ProcessPatternList = ProcessPatternList;
    }

    public void addProcessPattern(PatternType processPattern) {
        this.ProcessPatternList.add(processPattern);
    }

    public ArrayList<Stock.DateTypes> getProcessDateTypeList() {
        return ProcessDateTypeList;
    }

    public void setProcessDateTypeList(ArrayList<Stock.DateTypes> ProcessDateTypeList) {
        this.ProcessDateTypeList = ProcessDateTypeList;
    }

    public void addProcessDateType(Stock.DateTypes dateType) {
        this.ProcessDateTypeList.add(dateType);
    }

    public ArrayList<Integer> getProcessLengthList() {
        return ProcessLengthList;
    }

    public void setProcessLengthList(ArrayList<Integer> ProcessLengthList) {
        this.ProcessLengthList = ProcessLengthList;
    }

    public int getThreadNumber() {
        return ThreadNumber;
    }

    public void setThreadNumber(int ThreadNumber) {
        this.ThreadNumber = ThreadNumber;
    }

    public void setThreadNumber(String ThreadNumber) {
        this.ThreadNumber = Integer.valueOf(ThreadNumber);
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date StartDate) {
        this.StartDate = StartDate;

    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date EndDate) {
        this.EndDate = EndDate;
    }

    public void printParameter(boolean usingLogs) {

        GlobalParameters.logInfo(LogInformation.Category.CALC, "------------------------Parameter Checking Start------------------------");

        for (int idx = 0; idx < SourceStocksCodeList.size(); idx++) {
            GlobalParameters.logInfo(LogInformation.Category.CALC, "StockCode: " + SourceStocksCodeList.get(idx) + " , StockName: " + SourceStocksNameList.get(idx));
        }

        GlobalParameters.logInfo(LogInformation.Category.CALC, "Total : " + SourceStocksCodeList.size() + " Stocks.");

        GlobalParameters.logInfo(LogInformation.Category.CALC, "Thread Number: " + ThreadNumber);

        GlobalParameters.logInfo(LogInformation.Category.CALC, "Search Patterns: " + ProcessPatternList.toString().replace("[", "").replace("]", ""));

        GlobalParameters.logInfo(LogInformation.Category.CALC, "Date Types: " + ProcessDateTypeList.toString().replace("[", "").replace("]", ""));

        GlobalParameters.logInfo(LogInformation.Category.CALC, "Process Correlation Length are: " + ProcessLengthList.toString().replace("[", "").replace("]", ""));

        if (StartDate == null) {
            GlobalParameters.logInfo(LogInformation.Category.CALC, "StartDate: " + StartDate + " , EndDate: " + Common.DATE_OF_READIN_DALIY_SDF.format(EndDate) + ", <<<----------- Backward");
        } else {
            GlobalParameters.logInfo(LogInformation.Category.CALC, "StartDate: " + Common.DATE_OF_READIN_DALIY_SDF.format(StartDate) + " , EndDate: " + EndDate + ", Forward ----------->>>");
        }

        GlobalParameters.logInfo(LogInformation.Category.CALC, "------------------------Parameter Checking Ends------------------------");

        if (usingLogs) {
            Logs.e("------------------------Parameter Checking Start------------------------");

            for (int idx = 0; idx < SourceStocksCodeList.size(); idx++) {
                Logs.e("StockCode: " + SourceStocksCodeList.get(idx) + " , StockName: " + SourceStocksNameList.get(idx));
            }

            Logs.e("Total : " + SourceStocksCodeList.size() + " Stocks.");

            Logs.e("Process Correlation Thread Number are: " + ThreadNumber);

            Logs.e("Search Patterns: " + ProcessPatternList.toString().replace("[", "").replace("]", ""));

            Logs.e("Date Types: " + ProcessDateTypeList.toString().replace("[", "").replace("]", ""));

            Logs.e("Process Correlation Length are: " + ProcessLengthList.toString());

            if (StartDate == null) {
                Logs.e("StartDate: " + StartDate + " , EndDate: " + Common.DATE_OF_READIN_DALIY_SDF.format(EndDate) + ", <<<----------- Backward");
            } else {
                Logs.e("StartDate: " + Common.DATE_OF_READIN_DALIY_SDF.format(StartDate) + " , EndDate: " + EndDate + ", Forward ----------->>>");
            }

            Logs.e("------------------------Parameter Checking Ends------------------------");
        }

    }

    public static enum PatternType {
        Price, // Price Trend
        OCHL, // Open, Close, Highest, Lowest
        Ampl, // 
        Volate,
        Volume,
        Turnover
    }
}
