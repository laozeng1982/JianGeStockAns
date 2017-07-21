/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.datamodel.records;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author JianGe
 */
public class Record {

    public SimpleStringProperty idProperty;
    public SimpleStringProperty stockCodeProperty;
    public SimpleStringProperty stockNameProperty;

    /**
     *
     * @param id
     * @param code
     * @param name
     */
    public Record(int id, String code, String name) {
        this.idProperty = new SimpleStringProperty(String.valueOf(id));
        this.stockCodeProperty = new SimpleStringProperty(code);
        this.stockNameProperty = new SimpleStringProperty(name);
    }

    public Record(int id) {
        this.idProperty = new SimpleStringProperty(String.valueOf(id));
        this.stockCodeProperty = new SimpleStringProperty();
        this.stockNameProperty = new SimpleStringProperty();
    }

    public String getID() {
        return idProperty.get();
    }

    public int getIntID() {
        return Integer.valueOf(idProperty.get());
    }

    public void setID(String ID) {
        this.idProperty.set(ID);
    }

    public SimpleStringProperty idProperty() {
        return idProperty;
    }

    public void setID(SimpleStringProperty ID) {
        this.idProperty = ID;
    }

    public String getStockCode() {
        return stockCodeProperty.get();
    }

    public SimpleStringProperty stockCodeProperty() {
        return stockCodeProperty;
    }

    public void setStockCode(SimpleStringProperty stockCodeProperty) {
        this.stockCodeProperty = stockCodeProperty;
    }

    public void setStockCode(String stockCode) {
        this.stockCodeProperty = new SimpleStringProperty(stockCode);
    }

    public String getStockName() {
        return stockNameProperty.get();
    }

    public SimpleStringProperty stockNameProperty() {
        return stockNameProperty;
    }

    public void setStockName(SimpleStringProperty stockNameProperty) {
        this.stockNameProperty = stockNameProperty;
    }

    public void setStockName(String stockName) {
        this.stockNameProperty = new SimpleStringProperty(stockName);
    }

}
