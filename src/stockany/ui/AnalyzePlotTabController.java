/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import stockany.datamodel.GlobalParameters;
import stockany.tools.AlertDialog;
import stockany.tools.Common;
import stockany.tools.Common.Stock;
import tools.utilities.Logs;

/**
 * FXML Controller class
 *
 * @author JianGe
 */
public class AnalyzePlotTabController extends Tab implements Initializable {

    private FinanceChartPaneController sourceChartPaneController;
    private FinanceChartPaneController matchedChartPaneController;
    private int lastSourceDuration = 0;
    private int lastMatchedDuration = 0;

    //For searchplot, as a mark 
    public static Date lastSourceStartCalendar;
    public static Date lastSourceEndCalendar;
    public static Date lastMatchedStartCalendar;
    public static Date lastMatchedEndCalendar;

    @FXML
    private AnchorPane plotPane;
    @FXML
    private VBox chartPaneVbox = new VBox();
    @FXML
    private CheckBox chkBx_AllSource;
    @FXML
    private TextField txtF_SourceLength;
    @FXML
    private CheckBox chkBx_AllMatched;
    @FXML
    private TextField txtF_MatchedLength;

    public AnalyzePlotTabController(String name) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AnalyzePlotTab.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.chartPaneVbox.autosize();
        this.setClosable(true);

        try {
            fxmlLoader.load();
            this.setText(name);

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

    }

    public void updateSourceChart(final String stockCode, final String stockName, final Date startDate, final int duration, final Date endDate, boolean isStockChange) {
        String startDateString;
        String endDateString;
        if (startDate == null) {
            startDateString = "";
        } else {
            startDateString = Common.DATE_OF_READIN_DALIY_SDF.format(startDate);
        }

        if (endDate == null) {
            endDateString = "";
        } else {
            endDateString = Common.DATE_OF_READIN_DALIY_SDF.format(endDate);
        }
//        Logs.e("Source: " + startDateString + " , " + duration + " , " + endDateString);

        sourceChartPaneController = new FinanceChartPaneController(stockCode, stockName, startDate, duration, endDate);

        if (chartPaneVbox.getChildren().size() > 1) {
            // Before remove the chart, restore the mark line
            if (isStockChange) {
                lastSourceStartCalendar = startDate;
                lastSourceEndCalendar = endDate;
            } else {
                lastSourceStartCalendar = ((FinanceChartPaneController) chartPaneVbox.getChildren().get(1)).getLastStartDate();
                if (lastSourceStartCalendar == null) {
                    lastSourceStartCalendar = startDate;
                }

                lastSourceEndCalendar = ((FinanceChartPaneController) chartPaneVbox.getChildren().get(1)).getLastEndDate();
                if (lastSourceEndCalendar == null) {
                    lastSourceEndCalendar = endDate;
                }
            }

            sourceChartPaneController.setMarkStartDate(lastSourceStartCalendar);
            sourceChartPaneController.setMarkEndDate(lastSourceEndCalendar);

            sourceChartPaneController.showChart();

            //We just two cells of this vbox, so every time we remove one first, then add a new one
            chartPaneVbox.getChildren().remove(1);
        }
        chartPaneVbox.getChildren().add(1, sourceChartPaneController);

    }

    public void updateMatchedChart(final String stockCode, final String stockName, final Date startDate, final int duration, final Date endDate, boolean isStockChange) {
        String startDateString;
        String endDateString;
        if (startDate == null) {
            startDateString = "";
        } else {
            startDateString = Common.DATE_OF_READIN_DALIY_SDF.format(startDate);
        }

        if (endDate == null) {
            endDateString = "";
        } else {
            endDateString = Common.DATE_OF_READIN_DALIY_SDF.format(endDate);
        }
//        Logs.e("Matched: " + startDateString + " , " + duration + " , " + endDateString);

        matchedChartPaneController = new FinanceChartPaneController(stockCode, stockName, startDate, duration, endDate);

        if (chartPaneVbox.getChildren().size() == 3) {
            // Before remove the chart, restore the mark line
            if (isStockChange) {
                lastMatchedStartCalendar = startDate;
                lastMatchedEndCalendar = endDate;
            } else {
                lastMatchedStartCalendar = ((FinanceChartPaneController) chartPaneVbox.getChildren().get(2)).getLastStartDate();
                if (lastMatchedStartCalendar == null) {
                    lastMatchedStartCalendar = startDate;
                }
                lastMatchedEndCalendar = ((FinanceChartPaneController) chartPaneVbox.getChildren().get(2)).getLastEndDate();
                if (lastMatchedEndCalendar == null) {
                    lastMatchedEndCalendar = endDate;
                }
            }

            matchedChartPaneController.setMarkStartDate(lastMatchedStartCalendar);
            matchedChartPaneController.setMarkEndDate(lastMatchedEndCalendar);

            matchedChartPaneController.showChart();
            //We just two cells of this vbox, so every time we remove one first, then add a new one

            chartPaneVbox.getChildren().remove(2);
        }
        chartPaneVbox.getChildren().add(matchedChartPaneController);

    }

    public FinanceChartPaneController getSourceChartPaneController() {
        return sourceChartPaneController;
    }

    public void setSourceChartPaneController(FinanceChartPaneController sourceChartPaneController) {
        this.sourceChartPaneController = sourceChartPaneController;
    }

    public FinanceChartPaneController getMatchedChartPaneController() {
        return matchedChartPaneController;
    }

    public void setMatchedChartPaneController(FinanceChartPaneController matchedChartPaneController) {
        this.matchedChartPaneController = matchedChartPaneController;
    }

    private void printSize() {
        for (int i = 0; i < chartPaneVbox.getChildren().size(); i++) {
            FinanceChartPaneController aa = (FinanceChartPaneController) chartPaneVbox.getChildren().get(i);
            Logs.e(i + " , " + aa.getStockCode() + "  ,  " + aa.getWidth() + "  ,  " + aa.getHeight());

        }
    }

    @FXML
    private void onChkBxAllSource(ActionEvent event) {
        if (chkBx_AllSource.isSelected()) {
            txtF_SourceLength.setDisable(false);
        } else {
            txtF_SourceLength.setDisable(true);
        }

        onLengthChanged(true, true);

    }

    @FXML
    private void onTxtFSourceLength(ActionEvent event) {
        onLengthChanged(true, false);
    }

    @FXML
    private void onChkBxAllMatched(ActionEvent event) {
        if (chkBx_AllMatched.isSelected()) {
            txtF_MatchedLength.setDisable(false);
        } else {
            txtF_MatchedLength.setDisable(true);
        }

        onLengthChanged(false, true);

    }

    @FXML
    private void onTxtFMatchedLength(ActionEvent event) {
        onLengthChanged(false, false);
    }

    private void onLengthChanged(boolean isSource, boolean isFromChkBx) {

        // Get the selected table and table name
        String tableName = sourceChartPaneController.getStockCode();

        // Get the orignal stockcode and stockname, etc.
        String stockCode;
        String stockName;
        Date startDate;
        int showLength;
        Date endDate;

        if (isSource) {
            stockCode = sourceChartPaneController.getStockCode();
            stockName = sourceChartPaneController.getStockName();
//            startDate = sourceChartPaneController.getDisplayStartDate();
            startDate = sourceChartPaneController.getMarkStartDate();

            if (lastSourceDuration == 0) {
                lastSourceDuration = sourceChartPaneController.getDurationDays();
                showLength = lastSourceDuration + Integer.valueOf(txtF_SourceLength.getText());
            } else {
                showLength = lastSourceDuration;

                lastSourceDuration = 0;
            }
            endDate = getEndDate(stockCode, startDate, showLength);
            updateSourceChart(stockCode, stockName, startDate, showLength, endDate, false);

        } else {
            stockCode = matchedChartPaneController.getStockCode();
            stockName = matchedChartPaneController.getStockName();
//            startDate = matchedChartPaneController.getDisplayStartDate();
            startDate = matchedChartPaneController.getMarkStartDate();
            if (lastMatchedDuration == 0) {
                lastMatchedDuration = matchedChartPaneController.getDurationDays();
                showLength = lastMatchedDuration + Integer.valueOf(txtF_MatchedLength.getText());
            } else {

                showLength = lastMatchedDuration;

                lastMatchedDuration = 0;
            }
            endDate = getEndDate(stockCode, startDate, showLength);
            updateMatchedChart(stockCode, stockName, startDate, showLength, endDate, false);
        }

    }

    private Date getEndDate(String stockCode, Date startDate, int length) {
        Date endDate = null;
        ArrayList<Date> dateList = GlobalParameters.AllStockSetsForCalc.getSingleStockByCode(stockCode).getDateList(Stock.DateTypes.Daliy);
        for (int idx = 0; idx < dateList.size(); idx++) {
            if (dateList.get(idx).equals(startDate)) {
                if ((idx + length) < dateList.size()) {
                    endDate = dateList.get(idx + length);
                } else {
                    endDate = dateList.get(dateList.size() - 1);
                }
            }

        }
        return endDate;
    }

    private void clearChkBox() {
        Logs.e("cleared");
        if (chkBx_AllSource.isSelected()) {
            chkBx_AllSource.setSelected(false);
        }
        if (chkBx_AllMatched.isSelected()) {
            chkBx_AllMatched.setSelected(false);
        }

    }

    @FXML
    private void onChangeColor(ActionEvent event) {
        AlertDialog.showInfoDialog(new Stage(), "To be continued!");
    }

}
