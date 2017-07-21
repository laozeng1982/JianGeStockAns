/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.datamodel;

import java.util.ArrayList;
import stockany.tools.Common.Stock;
import tools.utilities.Logs;

/**
 *
 * @author JianGe
 */
public class StockSets implements Cloneable {

    private ArrayList<SingleStock> stocksList = new ArrayList<>();

    @Override
    public StockSets clone() throws CloneNotSupportedException {
        StockSets clonedSets = null;
        try {
            clonedSets = (StockSets) super.clone();
            for (int i = 0; i < stocksList.size(); i++) {
                clonedSets.add(stocksList.get(i).clone());

            }
        } catch (CloneNotSupportedException e) {
        }
        return clonedSets;
    }

    public void initAllCurves(Stock.DateTypes dateType) {
        stocksList.forEach((thi) -> {
            thi.calculateSub(dateType);
        });
//        Logs.e("init done!");
    }

    public ArrayList<SingleStock> getStocksList() {
        return stocksList;
    }

    public void setStocksList(ArrayList<SingleStock> stocksList) {
        this.stocksList = stocksList;
    }

    public SingleStock getSingleStockByCode(String stockCode) {
        for (SingleStock aThi : stocksList) {
            if (aThi.getStockCode().equals(stockCode)) {
                return aThi;
            }
        }
        return null;
    }

    public SingleStock getSingleStockByName(String stockName) {
        for (SingleStock aThi : stocksList) {
            if (aThi.getStockName().equals(stockName)) {
                return aThi;
            }
        }
        return null;
    }

    public ArrayList<String> getStockCodeList() {
        ArrayList<String> stockCodeList = new ArrayList<>();
        stocksList.forEach((singleStock) -> {
            stockCodeList.add(singleStock.getStockCode());
        });
        return stockCodeList;
    }

    public ArrayList<String> getStockNameList() {
        ArrayList<String> stockNameList = new ArrayList<>();
        stocksList.forEach((singleStock) -> {
            stockNameList.add(singleStock.getStockName());
        });
        return stockNameList;
    }

    public ArrayList<String> getStockCodeNameList() {
        ArrayList<String> stockCodeNameList = new ArrayList<>();
        stocksList.forEach((singleStock) -> {
            stockCodeNameList.add(singleStock.getStockCode() + " : " + singleStock.getStockName());
        });
        return stockCodeNameList;
    }

    public boolean add(SingleStock singleStock) {
        boolean isSucced = stocksList.add(singleStock);
        if (isSucced) {
            stocksList.get(stocksList.size() - 1).setID(String.valueOf(stocksList.size()));
        } else {
            Logs.e("Add single stock to StockSets is error!");
        }

        return isSucced;
    }
}
