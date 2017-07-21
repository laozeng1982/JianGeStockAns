/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.datamodel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import stockany.tools.Common;
import stockany.tools.Common.Stock;

/**
 *
 * @author JianGe
 */
public class StockDataElement implements Cloneable {

    protected Stock.DateTypes type;
    protected Date date;
    protected float openPrice;
    protected float highestPrice;
    protected float lowestPrice;
    protected float closePrice;
    protected float capital;
    protected float volume;
    protected int reserved;

    //===========================================
    protected float rateOfVolatility;    // Price rate: (ThisClosePrice - LastClosePrice)/LastClosePrice
    protected float rateOfAmplitude; // Price rate: (Highest - Lowest)/LastClosePirce
    protected float rateOfTurnover;  // 

    protected float priceGap;
    protected boolean red;

    public StockDataElement(Stock.DateTypes type, Date date, float open, float close, float highest, float lowest, float capital, float vol) {
        this(type);
        this.date = date;
        this.openPrice = open;
        this.closePrice = close;
        this.highestPrice = highest;
        this.lowestPrice = lowest;
        this.capital = capital;
        this.volume = vol;
    }

    public StockDataElement(Stock.DateTypes type) {
        this.type = type;
        date = null;
        openPrice = 0;
        highestPrice = 0;
        lowestPrice = 0;
        closePrice = 0;
        capital = 0;
        volume = 0;
        reserved = 0;
        rateOfVolatility = 0;
        rateOfAmplitude = 0;
        rateOfTurnover = 0;
        priceGap = 0;
        red = false;
    }

    public StockDataElement() {

    }

    @Override
    public StockDataElement clone() throws CloneNotSupportedException {
        StockDataElement stockDataElement = null;

        try {
            stockDataElement = (StockDataElement) super.clone();

        } catch (CloneNotSupportedException e) {
        }

        return stockDataElement;
    }

    //============================================
    public Stock.DateTypes getType() {
        return type;
    }

    public void setType(Stock.DateTypes type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public String getDateString() {
        if (type == Stock.DateTypes.Daliy) {
            return Common.DATE_OF_READIN_DALIY_SDF.format(date);
        } else {
            return Common.DATE_OF_OUTPUT_MIN_SDF.format(date);
        }

    }

    public void setDate(Date day) {
        this.date = day;
    }

    public float getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(float openPrice) {
        this.openPrice = openPrice;
    }

    public float getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(float highestPrice) {
        this.highestPrice = highestPrice;
    }

    public float getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(float lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public float getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(float closePrice) {
        this.closePrice = closePrice;
    }

    public float getCapital() {
        return capital;
    }

    public void setCapital(float capital) {
        this.capital = capital / 10000.0f; //10 K
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume / 100.0f;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public float getPriceGap() {
        return priceGap;
    }

    public void setPriceGap(float priceGap) {
        this.priceGap = priceGap;
    }

    public boolean isRed() {
        return red;
    }

    public void setRed(boolean isRed) {
        this.red = isRed;
    }

    public float getRateOfVolatility() {
        return rateOfVolatility;
    }

    public void setRateOfVolatility(float rateOfVolatility) {
        this.rateOfVolatility = rateOfVolatility;
    }

    public float getRateOfAmplitude() {
        return rateOfAmplitude;
    }

    public void setRateOfAmplitude(float rateOfAmplitude) {
        this.rateOfAmplitude = rateOfAmplitude;
    }

    public float getRateOfTurnover() {
        return rateOfTurnover;
    }

    public void setRateOfTurnover(float rateOfTurnover) {
        this.rateOfTurnover = rateOfTurnover;
    }

    public Date getTimeFlag() {
        Calendar calender;
        StringBuilder timeString = new StringBuilder();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        calender = Calendar.getInstance();
        calender.setTime(date);

        timeString.append(calender.get(Calendar.HOUR_OF_DAY)).append(":").append(calender.get(Calendar.MINUTE));
        
        Date date = null;
        try {
            date = sdf.parse(timeString.toString());
        } catch (ParseException e) {
        }

        return date;

    }

    //Just for test output
    public String toLongString() {
        String longString = (Common.LOG_DATE_SDF.format(this.date) + " ,Open: " + this.openPrice + " ,High: " + this.highestPrice
                + " ,Low: " + this.lowestPrice + " ,Close: " + this.closePrice + " ,Capital: " + this.capital + " ,Vol: " + this.volume);
        return longString;
    }
}
