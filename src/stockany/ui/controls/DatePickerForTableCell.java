/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui.controls;

/**
 *
 * @author JianGe
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import stockany.datamodel.GlobalParameters;
import stockany.datamodel.SingleStock;
import stockany.tools.AlertDialog;
import stockany.tools.Common;
import jiangestockans.JianGeStockAns;
import stockany.ui.ReviewTabController;
import tools.utilities.Logs;

public class DatePickerForTableCell {

    private DatePicker datePicker = new DatePicker();
    private int rowId;

    public DatePickerForTableCell(int rowNumber) {
        this.rowId = rowNumber;

        datePicker.setOnAction((ActionEvent event) -> {

            String currentClosePrice = null;

            ReviewTabController reviewTabController = JianGeStockAns.reviewTabController;

            String stockCode = reviewTabController.getReviewRecords().get(this.rowId - 1).getStockCode();

            Logs.e(this.rowId + stockCode);

            SingleStock singleStock;
            for (int index = 0; index < GlobalParameters.AllStockSetsForCalc.getStocksList().size(); index++) {
                if (GlobalParameters.AllStockSetsForCalc.getStocksList().get(index).getStockCode().toLowerCase().equals(stockCode.toLowerCase())) {
                    singleStock = GlobalParameters.AllStockSetsForCalc.getStocksList().get(index);

                    int lastPosition = singleStock.getDateList(Common.Stock.DateTypes.Daliy).size() - 1;
                    LocalDate lastDate = singleStock.getDateList(Common.Stock.DateTypes.Daliy).get(lastPosition).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    if (datePicker.getValue().toEpochDay() > lastDate.toEpochDay()) {
                        currentClosePrice = singleStock.getDataList(Common.Stock.DateTypes.Daliy, Common.Stock.DataTypes.CLOSE_PRICE).get(lastPosition);
                        datePicker.setValue(lastDate);

                    } else {
                        for (int idx = 0; idx < singleStock.getDateList(Common.Stock.DateTypes.Daliy).size(); idx++) {
                            LocalDate date = singleStock.getDateList(Common.Stock.DateTypes.Daliy).get(idx).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                            if (date.toEpochDay() == datePicker.getValue().toEpochDay()) {
                                currentClosePrice = singleStock.getDataList(Common.Stock.DateTypes.Daliy, Common.Stock.DataTypes.CLOSE_PRICE).get(idx);
                                break;
                            }
                        }
                    }
                    break;
                }
            }

            if (currentClosePrice == null || currentClosePrice.isEmpty()) {
                AlertDialog.showErrorDialog(new Stage(), "The day you selected has no close price!");
                return;
            }

            float startClosePrice = 0;
            float endClosePrice = 0;

            if (event.getSource() == reviewTabController.getStartDateCol().getCellData(this.rowId - 1)) {
                reviewTabController.getReviewRecords().get(this.rowId - 1).setObStartClosePrice(currentClosePrice);
            } else if (event.getSource() == reviewTabController.getEndDateCol().getCellData(this.rowId - 1)) {
                LocalDate endDate = datePicker.getValue();
                LocalDate startDate = reviewTabController.getStartDateCol().getCellData(this.rowId - 1).getValue();
                if (endDate.isBefore(startDate)) {
                    datePicker.setValue(LocalDate.now());
                    return;
                }
                reviewTabController.getReviewRecords().get(this.rowId - 1).setObEndClosePrice(currentClosePrice);
            }

            startClosePrice = Float.valueOf(reviewTabController.getReviewRecords().get(this.rowId - 1).getObStartClosePrice());
            if (reviewTabController.getReviewRecords().get(this.rowId - 1).getObEndClosePrice() != null) {
                endClosePrice = Float.valueOf(reviewTabController.getReviewRecords().get(this.rowId - 1).getObEndClosePrice());
            }

            if (startClosePrice > 0 && endClosePrice > 0) {
                float profit = (endClosePrice - startClosePrice) * 100 / startClosePrice;
                reviewTabController.getReviewRecords().get(this.rowId - 1).setObProfit(profit + "");
                Logs.e("End: " + reviewTabController.getReviewRecords().get(this.rowId - 1).getObEndDate().toString());
                Logs.e("Start: " + reviewTabController.getReviewRecords().get(this.rowId - 1).getObStartDate().toString());
                long days = reviewTabController.getReviewRecords().get(this.rowId - 1).getObEndDate().toEpochDay() - reviewTabController.getReviewRecords().get(this.rowId - 1).getObStartDate().toEpochDay();

                float efficency = 0;
                if (days > 0) {
                    efficency = profit / days;

                }
                Logs.e(days + ", efficency " + efficency);
                reviewTabController.getReviewRecords().get(this.rowId - 1).setObEfficiency(efficency + "");
            }
        });
    }

    public ObservableValue<DatePicker> getObserveDatePicker() {
        return new ObservableValue<DatePicker>() {
            @Override
            public void addListener(ChangeListener<? super DatePicker> listener) {
            }

            @Override
            public void removeListener(ChangeListener<? super DatePicker> listener) {

            }

            @Override
            public DatePicker getValue() {
                return datePicker;
            }

            @Override
            public void addListener(InvalidationListener listener) {
            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        };
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public void setDatePicker(Date date) {
        this.datePicker.setValue(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public void setDatePicker(LocalDate localDate) {
        this.datePicker.setValue(localDate);
    }

    public LocalDate getLocalDate() {
        return datePicker.getValue();
    }

    public Date getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sdf.parse(datePicker.getValue().toString());
        } catch (ParseException e) {
        }
        return date;
    }

}
