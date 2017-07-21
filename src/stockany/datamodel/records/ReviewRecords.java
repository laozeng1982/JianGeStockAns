/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.datamodel.records;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import tools.utilities.StringUtils;
import stockany.ui.controls.ComboBoxForTableCell;
import stockany.ui.controls.DatePickerForTableCell;

/**
 *
 * @author JianGe
 */
public class ReviewRecords extends Record implements Initializable {

    private SimpleStringProperty obReferenceProperty;
    private ComboBoxForTableCell obMethodPropertys;
    private DatePickerForTableCell obStartDatePicker;
    private SimpleStringProperty obStartClosePriceProperty;
    private DatePickerForTableCell obEndDatePicker;
    private SimpleStringProperty obEndClosePriceProperty;
    private SimpleStringProperty obProfitProperty;
    private SimpleStringProperty obEfficiencyProperty;
    private SimpleStringProperty obCommentsProperty;

    public ReviewRecords(int id, String code, String name, Date startDate, Date endDate) {

        super(id, code, name);
        init();

        if (startDate != null) {
            this.obStartDatePicker.setDatePicker(startDate);
        }
        if (endDate != null) {
            this.obEndDatePicker.setDatePicker(endDate);
        }

    }

    public ReviewRecords(int id) {
        super(id);
        init();
    }

    private void init() {
        this.obReferenceProperty = new SimpleStringProperty();
        this.obMethodPropertys = new ComboBoxForTableCell();
        this.obStartDatePicker = new DatePickerForTableCell(this.getIntID());
        this.obStartClosePriceProperty = new SimpleStringProperty();
        this.obEndDatePicker = new DatePickerForTableCell(this.getIntID());
        this.obEndClosePriceProperty = new SimpleStringProperty();
        this.obProfitProperty = new SimpleStringProperty();
        this.obEfficiencyProperty = new SimpleStringProperty();
        this.obCommentsProperty = new SimpleStringProperty();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public String getObReference() {
        return obReferenceProperty.get();
    }

    public void setObReference(String obReference) {
        this.obReferenceProperty.set(obReference);
    }

    public SimpleStringProperty obReferenceProperty() {
        return obReferenceProperty;
    }

    public void setObReferenceProperty(SimpleStringProperty obReferenceProperty) {
        this.obReferenceProperty = obReferenceProperty;
    }

    public ComboBoxForTableCell getObMethodPropertys() {
        return obMethodPropertys;
    }

    public ComboBoxForTableCell obMethodPropertys() {
        return obMethodPropertys;
    }

    public String getSelectedObMethod() {
        return obMethodPropertys.getSelectedItem();
    }

    public void setObMethod(String obMethod) {
        this.obMethodPropertys.setSelectedItem(obMethod);
    }

    public LocalDate obStartDate() {
        return obStartDatePicker.getLocalDate();
    }

    public DatePickerForTableCell obStartDatePicker() {
        return obStartDatePicker;
    }

    public LocalDate getObStartDate() {
        return obStartDatePicker.getLocalDate();
    }

    public void setObStartDate(LocalDate obStartDate) {
        this.obStartDatePicker.setDatePicker(obStartDate);
    }

    public String getObStartClosePrice() {
        return obStartClosePriceProperty.get();
    }

    public SimpleStringProperty obStartClosePriceProperty() {
        return obStartClosePriceProperty;
    }

    public void setObStartClosePrice(String obStartClosePrice) {
        this.obStartClosePriceProperty.set(obStartClosePrice);
    }

    public void setObStartClosePriceProperty(SimpleStringProperty obStartClosePriceProperty) {
        this.obStartClosePriceProperty = obStartClosePriceProperty;
    }

    public LocalDate getObEndDate() {
        return obEndDatePicker.getLocalDate();
    }

    public DatePickerForTableCell obEndDatePicker() {
        return obEndDatePicker;
    }

    public void setObEndDate(LocalDate obEndDate) {
        this.obEndDatePicker.setDatePicker(obEndDate);

    }

    public String getObEndClosePrice() {
        return obEndClosePriceProperty.get();
    }

    public SimpleStringProperty getObEndClosePriceProperty() {
        return obEndClosePriceProperty;
    }

    public SimpleStringProperty obEndClosePriceProperty() {
        return obEndClosePriceProperty;
    }

    public void setObEndClosePrice(String obEndClosePrice) {
        this.obEndClosePriceProperty.set(obEndClosePrice);
    }

    public void setObEndClosePriceProperty(SimpleStringProperty obEndClosePriceProperty) {
        this.obEndClosePriceProperty = obEndClosePriceProperty;
    }

    public String getObProfitProperty() {
        return obProfitProperty.get();
    }

    public SimpleStringProperty obProfitProperty() {
        return obProfitProperty;
    }

    public void setObProfit(String obProfit) {
        this.obProfitProperty.set(obProfit);
    }

    public void setObProfitProperty(SimpleStringProperty obProfitProperty) {
        this.obProfitProperty = obProfitProperty;
    }

    public String getObEfficiencyProperty() {
        return obEfficiencyProperty.get();
    }

    public SimpleStringProperty obEfficiencyProperty() {
        return obEfficiencyProperty;
    }

    public void setObEfficiency(String obEfficiency) {
        this.obEfficiencyProperty.set(obEfficiency);
    }

    public void setObEfficiencyProperty(SimpleStringProperty obEfficiencyProperty) {
        this.obEfficiencyProperty = obEfficiencyProperty;
    }

    public String getObComments() {
        return obCommentsProperty.get();
    }

    public void setObComments(String obComments) {
        this.obCommentsProperty.set(obComments);
    }

    public SimpleStringProperty obCommentsProperty() {
        return obCommentsProperty;
    }

    public void setObCommentsProperty(SimpleStringProperty obCommentsProperty) {
        this.obCommentsProperty = obCommentsProperty;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (stockCodeProperty != null && stockCodeProperty.get() != null && !stockCodeProperty.get().isEmpty()) {
            stringBuilder.append(StringUtils.formatLeftS(stockCodeProperty.get()))
                    .append(StringUtils.formatLeftS(stockNameProperty.get()));
        } else {
            stringBuilder.append("null data");
            return stringBuilder.toString();
        }

        if (obReferenceProperty != null) {
            stringBuilder.append(StringUtils.formatLeftS(obReferenceProperty.get(), 50));
        } else {
            stringBuilder.append(StringUtils.formatLeftS("null", 50));
        }

        if (obMethodPropertys != null) {
            stringBuilder.append(StringUtils.formatLeftS(obMethodPropertys.getSelectedItem()));
        } else {
            stringBuilder.append(StringUtils.formatLeftS("null"));
        }

        if (obStartDatePicker != null && obStartDatePicker.getLocalDate() != null) {
            stringBuilder.append(StringUtils.formatLeftS(obStartDatePicker.getLocalDate().toString()));
        } else {
            stringBuilder.append(StringUtils.formatLeftS("null"));
        }

        if (obStartClosePriceProperty != null) {
            stringBuilder.append(StringUtils.formatLeftS(obStartClosePriceProperty.get()));
        } else {
            stringBuilder.append(StringUtils.formatLeftS("null"));
        }

        if (obEndDatePicker != null && obEndDatePicker.getLocalDate() != null) {
            stringBuilder.append(StringUtils.formatLeftS(obEndDatePicker.getLocalDate().toString()));
        } else {
            stringBuilder.append(StringUtils.formatLeftS("null"));
        }

        if (obEndClosePriceProperty != null) {
            stringBuilder.append(StringUtils.formatLeftS(obEndClosePriceProperty.get()));
        } else {
            stringBuilder.append(StringUtils.formatLeftS("null"));
        }

        if (obProfitProperty != null) {
            stringBuilder.append(StringUtils.formatLeftS(obProfitProperty.get()));
        } else {
            stringBuilder.append(StringUtils.formatLeftS("null"));
        }

        if (obEfficiencyProperty != null) {
            stringBuilder.append(StringUtils.formatLeftS(obEfficiencyProperty.get()));
        } else {
            stringBuilder.append(StringUtils.formatLeftS("null"));
        }

        if (obCommentsProperty != null) {
            stringBuilder.append(StringUtils.formatLeftS(obCommentsProperty.get()));
        } else {
            stringBuilder.append(StringUtils.formatLeftS("null", 50));
        }

        return stringBuilder.toString();
    }

    public ArrayList<String> toStringArrayList() {
        ArrayList<String> arrayList = new ArrayList<>();

        if (stockCodeProperty != null && stockCodeProperty.get() != null && !stockCodeProperty.get().isEmpty()) {
            arrayList.add(stockCodeProperty.get());
            arrayList.add(stockNameProperty.get());
        } else {
            return null;
        }

        if (obReferenceProperty != null) {
            arrayList.add(obReferenceProperty.get());
        } else {
            arrayList.add("null");
        }

        if (obMethodPropertys != null) {
            arrayList.add(obMethodPropertys.getSelectedItem());
        } else {
            arrayList.add("null");
        }

        if (obStartDatePicker != null && obStartDatePicker.getLocalDate() != null) {
            arrayList.add(obStartDatePicker.getLocalDate().toString());
        } else {
            arrayList.add("null");
        }

        if (obCommentsProperty != null) {
            arrayList.add(obCommentsProperty.get());
        } else {
            arrayList.add("null");
        }

//        arrayList.add(obEndDate.getLocalDate().toString());
//        arrayList.add(obEndClosePriceProperty.get());
//        arrayList.add(obProfitProperty.get());
//        arrayList.add(obEfficiencyProperty.get());
        return arrayList;
    }

}
