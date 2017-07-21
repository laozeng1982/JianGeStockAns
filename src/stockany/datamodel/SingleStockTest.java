/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.datamodel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import stockany.tools.StockDataParser;
import tools.files.IOHelper;
import tools.utilities.Logs;
import stockany.tools.Common;

/**
 *
 * @author JianGe
 */
public class SingleStockTest {

    public static void main(String[] args) {
//        test15min();
//        test30min();
        test60min();
    }

    public static void test15min() {

        StockDataParser dataParser = new StockDataParser(
                IOHelper.readAsciiFile("G:\\Program Files\\new_zx_allin1\\T0002\\export\\SH600000.txt", Common.AsciiUnicode.GB2312),
                IOHelper.readAsciiFile("D:\\StockAnalyse\\ORG_DATA\\feihu\\TXTMIN5_2006to2008\\SH600000.csv", Common.AsciiUnicode.GB2312));

        SingleStock singleStock = dataParser.getSingleStock();
//        for (int i = 0; i < singleStock.getStockDaliyData().size(); i++) {
//            Logs.e(singleStock.getStockDaliyData().get(i).toLongString());
//
//        }

        singleStock.make15MinData();

        for (int i = 0; i < singleStock.getStock15MinData().size(); i++) {
            Logs.e(singleStock.getStock15MinData().get(i).toLongString());

        }

    }

    public static void test30min() {

        StockDataParser dataParser = new StockDataParser(
                IOHelper.readAsciiFile("G:\\Program Files\\new_zx_allin1\\T0002\\export\\SH600000.txt", Common.AsciiUnicode.GB2312),
                IOHelper.readAsciiFile("D:\\StockAnalyse\\ORG_DATA\\feihu\\TXTMIN5_2006to2008\\SH600000.csv", Common.AsciiUnicode.GB2312));

        SingleStock singleStock = dataParser.getSingleStock();
//        for (int i = 0; i < singleStock.getStockDaliyData().size(); i++) {
//            Logs.e(singleStock.getStockDaliyData().get(i).toLongString());
//
//        }

        singleStock.make30MinData();

        for (int i = 0; i < singleStock.getStock30MinData().size(); i++) {
            Logs.e(singleStock.getStock30MinData().get(i).toLongString());

        }

    }

    public static void test60min() {

        StockDataParser dataParser = new StockDataParser(
                IOHelper.readAsciiFile("G:\\Program Files\\new_zx_allin1\\T0002\\export\\SH600000.txt", Common.AsciiUnicode.GB2312),
                IOHelper.readAsciiFile("D:\\StockAnalyse\\ORG_DATA\\feihu\\TXTMIN5_2006to2008\\SH600000.csv", Common.AsciiUnicode.GB2312));

        SingleStock singleStock = dataParser.getSingleStock();
//        for (int i = 0; i < singleStock.getStockDaliyData().size(); i++) {
//            Logs.e(singleStock.getStockDaliyData().get(i).toLongString());
//
//        }

        singleStock.make60MinData();

        for (int i = 0; i < singleStock.getStock60MinData().size(); i++) {
            Logs.e(singleStock.getStock60MinData().get(i).toLongString());

        }

    }

    public static void test() {
        Date date = new Date();
        Date date2 = new Date();
        date2.setTime(date.getTime() + 60 * 5 * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Logs.e("time is: " + sdf.format(date));
        Logs.e("time is: " + sdf.format(date2));
        date.setTime(date.getTime() + 60 * 10 * 1000);
        Logs.e("time is: " + sdf.format(date));

        try {
            Date date3 = sdf.parse("11:39");
            Logs.e("date3: " + date3);
        } catch (ParseException ex) {
            Logger.getLogger(SingleStockTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
