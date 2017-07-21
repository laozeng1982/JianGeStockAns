/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.datamodel.records;

import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;
import stockany.ui.controls.DatePickerForTableCell;

/**
 *
 * @author JianGe
 */
public class TreadRecords extends Record {
    
    private DatePickerForTableCell DateProperty;
    private SimpleStringProperty hoursProperty;
    private SimpleStringProperty minutesProperty;
    private SimpleStringProperty flagProperty;
    private SimpleStringProperty priceProperty;
    private SimpleStringProperty amountProperty;
    private SimpleStringProperty transferFeeProperty;
    private SimpleStringProperty commissionFeeProperty;
    private SimpleStringProperty taxProperty;
    private SimpleStringProperty totalCostProperty;
    private SimpleStringProperty commentsProperty;
    
    private double rateOfCommisson;
    private double rateOfTax;
    private double rateOfTransfer;
    
    public TreadRecords(int id, String code, String name) {
        super(id, code, name);
        init();
    }
    
    private void init() {
        DateProperty = new DatePickerForTableCell(this.getIntID());
        hoursProperty = new SimpleStringProperty();
        minutesProperty = new SimpleStringProperty();
        flagProperty = new SimpleStringProperty();
        priceProperty = new SimpleStringProperty();
        amountProperty = new SimpleStringProperty();
        transferFeeProperty = new SimpleStringProperty();
        commissionFeeProperty = new SimpleStringProperty();
        taxProperty = new SimpleStringProperty();
        totalCostProperty = new SimpleStringProperty();
        commentsProperty = new SimpleStringProperty();
        
        rateOfCommisson = 0.0003;
        rateOfTax = 0.0001;
        rateOfTransfer = 0.001;
    }
    
    public void setDate(LocalDate Date) {
        this.DateProperty.setDatePicker(Date);
    }
    
    public LocalDate getLocalDate() {
        return this.DateProperty.getLocalDate();
    }
    
    public DatePickerForTableCell getDateProperty() {
        return DateProperty;
    }
    
    public void setDateProperty(DatePickerForTableCell DateProperty) {
        this.DateProperty = DateProperty;
    }
    
    public String getHours() {
        return hoursProperty.get();
    }
    
    public void setHours(String hours) {
        this.hoursProperty.set(hours);
    }
    
    public SimpleStringProperty getHoursProperty() {
        return hoursProperty;
    }
    
    public void setHoursProperty(SimpleStringProperty hoursProperty) {
        this.hoursProperty = hoursProperty;
    }
    
    public String getMinutes() {
        return minutesProperty.get();
    }
    
    public void setMinutes(String minutes) {
        this.minutesProperty.set(minutes);
    }
    
    public SimpleStringProperty getMinutesProperty() {
        return minutesProperty;
    }
    
    public void setMinutesProperty(SimpleStringProperty minutesProperty) {
        this.minutesProperty = minutesProperty;
    }
    
    public String getFlag() {
        return flagProperty.get();
    }
    
    public void setFlagProperty(String flag) {
        this.flagProperty.set(flag);
    }
    
    public SimpleStringProperty getFlagProperty() {
        return flagProperty;
    }
    
    public void setFlagProperty(SimpleStringProperty flagProperty) {
        this.flagProperty = flagProperty;
    }
    
    public String getPrice() {
        return priceProperty.get();
    }
    
    public void setPrice(String price) {
        this.priceProperty.set(price);
    }
    
    public SimpleStringProperty getPriceProperty() {
        return priceProperty;
    }
    
    public void setPriceProperty(SimpleStringProperty priceProperty) {
        this.priceProperty = priceProperty;
    }
    
    public String getAmount() {
        return amountProperty.get();
    }
    
    public void setAmount(String amount) {
        this.amountProperty.set(amount);
    }
    
    public SimpleStringProperty getAmountProperty() {
        return amountProperty;
    }
    
    public void setAmountProperty(SimpleStringProperty amountProperty) {
        this.amountProperty = amountProperty;
    }
    
    public String getTransferFee() {
        return transferFeeProperty.get();
    }
    
    public void setTransferFee(String transferFee) {
        this.transferFeeProperty.set(transferFee);
    }
    
    public SimpleStringProperty getTransferFeeProperty() {
        return transferFeeProperty;
    }
    
    public void setTransferFeeProperty(SimpleStringProperty transferFeeProperty) {
        this.transferFeeProperty = transferFeeProperty;
    }
    
    public String getCommissionFee() {
        return commissionFeeProperty.get();
    }
    
    public void setCommissionFee(String commissionFee) {
        this.commissionFeeProperty.set(commissionFee);
    }
    
    public SimpleStringProperty getCommissionFeeProperty() {
        return commissionFeeProperty;
    }
    
    public void setCommissionFeeProperty(SimpleStringProperty commissionFeeProperty) {
        this.commissionFeeProperty = commissionFeeProperty;
    }
    
    public String getTax() {
        return taxProperty.get();
    }
    
    public void setTax(String tax) {
        this.taxProperty.set(tax);
    }
    
    public SimpleStringProperty getTaxProperty() {
        return taxProperty;
    }
    
    public void setTaxProperty(SimpleStringProperty taxProperty) {
        this.taxProperty = taxProperty;
    }
    
    public String getTotalCost() {
        return totalCostProperty.get();
    }
    
    public void setTotalCost(String totalCost) {
        this.totalCostProperty.set(totalCost);
    }
    
    public SimpleStringProperty getTotalCostProperty() {
        return totalCostProperty;
    }
    
    public void setTotalCostProperty(SimpleStringProperty totalCostProperty) {
        this.totalCostProperty = totalCostProperty;
    }
    
    public String getComments() {
        return commentsProperty.get();
    }
    
    public void setComments(String comments) {
        this.commentsProperty.set(comments);
    }
    
    public SimpleStringProperty getCommentsProperty() {
        return commentsProperty;
    }
    
    public void setCommentsProperty(SimpleStringProperty commentsProperty) {
        this.commentsProperty = commentsProperty;
    }
    
    public double getRateOfCommisson() {
        return rateOfCommisson;
    }
    
    public void setRateOfCommisson(double rateOfCommisson) {
        this.rateOfCommisson = rateOfCommisson;
    }
    
    public double getRateOfTax() {
        return rateOfTax;
    }
    
    public void setRateOfTax(double rateOfTax) {
        this.rateOfTax = rateOfTax;
    }
    
    public double getRateOfTransfer() {
        return rateOfTransfer;
    }
    
    public void setRateOfTransfer(double rateOfTransfer) {
        this.rateOfTransfer = rateOfTransfer;
    }
    
}
