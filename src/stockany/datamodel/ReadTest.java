/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.datamodel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import stockany.tools.Common;
import tools.files.FileHelper;
import tools.files.IOHelper;
import tools.utilities.Logs;

/**
 *
 * @author JianGe
 */
public class ReadTest {

    public static void main(String[] args) {
        read();
    }

    public static void read() {
        String fileName = "D:\\StockAnalyse\\ORG_DATA\\feihu\\TXTMIN5_2006to2008\\SH600000.csv";

        try {
            long startTime = System.currentTimeMillis();
            ArrayList<String> reading = IOHelper.readAsciiFile(fileName, "GB2312");
            StringBuilder timeFlag = new StringBuilder();
            for (int i = 0; i < reading.size(); i++) {
                String[] get = reading.get(i).split(",");
                Date date = Common.DATE_OF_READIN_MIN_SDF.parse(get[0] + " " + get[1]);
                Calendar calender = Calendar.getInstance();
                calender.setTime(date);
                timeFlag.append(calender.get(Calendar.HOUR_OF_DAY)).append(calender.get(Calendar.MINUTE));
//                Logs.e(timeFlag.toString());
                if (timeFlag.toString().equals("935") ) {
                    Logs.e(Common.DATE_OF_READIN_MIN_SDF.format(date));
                } else {
                     timeFlag.delete(0, timeFlag.length());
                }

            }
            Logs.e("Spend: " + (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
        }

    }
}
