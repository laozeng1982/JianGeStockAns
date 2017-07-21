/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import stockany.datamodel.SingleStock;
import java.util.ArrayList;
import java.util.Date;
import stockany.datamodel.StockDataElement;
import tools.utilities.Logs;

/**
 *
 * @author JianGe
 */
public final class StockDataParser {

    private final SingleStock SingleStock = new SingleStock();

    public SingleStock getSingleStock() {
        return SingleStock;
    }

    /**
     * create a date parser and parse data when it is created!
     *
     * @param daliySource
     * @param min5Source
     */
    public StockDataParser(ArrayList<String> daliySource, ArrayList<String> min5Source) {
        parseAscDataFile(daliySource);
        parseAscDataFile(min5Source);
    }

    //--------------------------------------------------------------------
    /**
     *
     * @param src the source String data per day
     * @return a single day's information
     */
    private StockDataElement createDataElement(String src, boolean isDaily) {
        StockDataElement dataElement = null;

        String[] srcNoSpace = src.split(",");
        try {
            if (isDaily) {
                dataElement = new StockDataElement(Common.Stock.DateTypes.Daliy);
                dataElement.setDate(Common.DATE_OF_READIN_DALIY_SDF.parse(srcNoSpace[0]));
                dataElement.setOpenPrice(Float.valueOf(srcNoSpace[1]));
                dataElement.setHighestPrice(Float.valueOf(srcNoSpace[2]));
                dataElement.setLowestPrice(Float.valueOf(srcNoSpace[3]));
                dataElement.setClosePrice(Float.valueOf(srcNoSpace[4]));
                dataElement.setVolume(Float.valueOf(srcNoSpace[5]));
                dataElement.setCapital(Float.valueOf(srcNoSpace[6]));
//                Logs.e(dataElement.toLongString());
            } else {
                dataElement = new StockDataElement(Common.Stock.DateTypes.Min5);
                if (srcNoSpace[1].equals("1300")) {
                    dataElement.setDate(Common.DATE_OF_READIN_MIN_SDF.parse(srcNoSpace[0] + "-" + "1130"));
                } else {
                    dataElement.setDate(Common.DATE_OF_READIN_MIN_SDF.parse(srcNoSpace[0] + "-" + srcNoSpace[1]));
                }
                dataElement.setOpenPrice(Float.valueOf(srcNoSpace[2]));
                dataElement.setHighestPrice(Float.valueOf(srcNoSpace[3]));
                dataElement.setLowestPrice(Float.valueOf(srcNoSpace[4]));
                dataElement.setClosePrice(Float.valueOf(srcNoSpace[5]));
                dataElement.setVolume(Float.valueOf(srcNoSpace[6]));
                dataElement.setCapital(Float.valueOf(srcNoSpace[7]));
            }

        } catch (ParseException e) {
        }

        return dataElement;
    }

    /**
     *
     * @param src the source byte data per day
     * @return a single day's information
     */
    private StockDataElement createSingleDayData(byte[] src) {
        StockDataElement singleStockPerDay = new StockDataElement(Common.Stock.DateTypes.Daliy);
        final int steps = 4;    //4 bytes per record

        for (int index = 0; index < 8; index++) {
            byte[] parseData = new byte[steps];
            for (int idx = 0; idx < steps; idx++) {
                //make data to parse as a single record!
                parseData[idx] = src[index * steps + idx];
            }
            try {
                switch (index) {
                    case 0:
                        String dateString = String.valueOf(Integer.parseInt(tools.utilities.Utils.bytesToHex(parseData), 16));
                        singleStockPerDay.setDate(Common.DATE_OF_READIN_DALIY_SDF.parse(dateString));
                        break;
                    case 1:
                        singleStockPerDay.setOpenPrice(Integer.parseInt(tools.utilities.Utils.bytesToHex(parseData), 16));
                        break;
                    case 2:
                        singleStockPerDay.setHighestPrice(Integer.parseInt(tools.utilities.Utils.bytesToHex(parseData), 16));
                        break;
                    case 3:
                        singleStockPerDay.setLowestPrice(Integer.parseInt(tools.utilities.Utils.bytesToHex(parseData), 16));
                        break;
                    case 4:
                        singleStockPerDay.setClosePrice(Integer.parseInt(tools.utilities.Utils.bytesToHex(parseData), 16));
                        break;
                    case 5:
                        singleStockPerDay.setCapital(Integer.parseInt(tools.utilities.Utils.bytesToHex(parseData), 16));
                        break;
                    case 6:
                        singleStockPerDay.setVolume(Integer.parseInt(tools.utilities.Utils.bytesToHex(parseData), 16));
                        break;
                    case 7:
                        singleStockPerDay.setReserved(Integer.parseInt(tools.utilities.Utils.bytesToHex(parseData), 16));
                        break;
                    default:
                        break;
                }
            } catch (NumberFormatException | ParseException e) {
            }

        }

        return singleStockPerDay;
    }

    /**
     * method to parse data from ASCII file, which was saved from tongdaxin
     *
     *
     */
    public void parseAscDataFile(ArrayList<String> stockDataList) {

        boolean isDaily = true;
        String readString = null;
        String code, name;
        int readLine = 1;

        if (stockDataList == null) {
            return;
        }

        for (int idx = 0; idx < stockDataList.size(); idx++) {
            readString = stockDataList.get(idx);

            if (readLine == 1) {
                code = readString.substring(0, 6);
                if (readString.startsWith("6")) {
                    SingleStock.setStockCode("SH" + code);

                } else {
                    SingleStock.setStockCode("SZ" + code);

                }

                name = readString.replaceAll(code, "").replaceAll("日线", "").replace("前复权", "").replaceAll(" +", "");
                SingleStock.setStockName(name);

                readLine++;
                continue;
            }
            if (readLine == 2) {
                isDaily = !readString.contains("时间");
                readLine++;
                continue;
            }
            if (readString.contains(":")) {
                SingleStock.setStockSize(String.valueOf(stockDataList.size() - 3));
                break;
            }

            try {
                if (isDaily) {
                    SingleStock.getStockDaliyData().add(createDataElement(readString, isDaily));
                } else {
                    SingleStock.getStock5MinData().add(createDataElement(readString, isDaily));
                }

                readLine++;

            } catch (Exception e) {
            }

        }
    }

    public SingleStock getPartSingleStock(final String startDate, final int length, final String endDate) {

        SingleStock singleStock = new SingleStock();

        try {
            Date start;
            Date end;

            if (!startDate.isEmpty() && !endDate.isEmpty()) {
                start = Common.DATE_OF_READIN_DALIY_SDF.parse(startDate);
                end = Common.DATE_OF_READIN_DALIY_SDF.parse(endDate);

            } else if (startDate.isEmpty() && endDate.isEmpty()) {
                start = SingleStock.getStockDaliyData().get(0).getDate();
                end = SingleStock.getStockDaliyData().get(SingleStock.getStockDaliyData().size() - 1).getDate();

            } else if (!startDate.isEmpty() && endDate.isEmpty()) {
                start = Common.DATE_OF_READIN_DALIY_SDF.parse(startDate);
                Date tempStart = null;
                Date tempEnd = null;
                if (length > 0) {   // To a certain date of this stock
                    for (int idx = 0; idx < SingleStock.getStockDaliyData().size(); idx++) {
                        Date read = SingleStock.getStockDaliyData().get(idx).getDate();

                        if (read.getTime() >= start.getTime()) {
                            tempStart = SingleStock.getStockDaliyData().get(idx).getDate();
                            if ((idx + length) < SingleStock.getStockDaliyData().size()) {
                                tempEnd = SingleStock.getStockDaliyData().get(idx + length - 1).getDate();
                            } else {
                                tempEnd = SingleStock.getStockDaliyData().get(SingleStock.getStockDaliyData().size() - 1).getDate();
                            }
                            break;
                        }
                    }

                    if (tempStart != null && tempEnd != null) {
                        start = tempStart;
                        end = tempEnd;
                    } else {
                        return null;
                    }
                } else {    // To last date of this stock
                    end = SingleStock.getStockDaliyData().get(SingleStock.getStockDaliyData().size() - 1).getDate();
                }

            } else if (startDate.isEmpty() && !endDate.isEmpty()) {
                end = Common.DATE_OF_READIN_DALIY_SDF.parse(endDate);
                Date tempStart = null;
                Date tempEnd = null;

                if (length > 0) {   // To a certain date of this stock
                    for (int idx = SingleStock.getStockDaliyData().size() - 1; idx >= 0; idx--) {
                        Date read = SingleStock.getStockDaliyData().get(idx).getDate();

                        if (read.getTime() <= end.getTime()) {
                            tempEnd = SingleStock.getStockDaliyData().get(idx).getDate();
                            if (idx >= length) {
                                tempStart = SingleStock.getStockDaliyData().get(idx + 1 - length).getDate();
                            } else {
                                tempStart = SingleStock.getStockDaliyData().get(0).getDate();
                            }
                            break;
                        }
                    }

                    if (tempStart != null && tempEnd != null) {
                        start = tempStart;
                        end = tempEnd;
                    } else {
                        return null;
                    }
                } else {    // To last date of this stock
                    start = SingleStock.getStockDaliyData().get(0).getDate();
                }
            } else {
                Logs.e("error!!!!");
                return null;
            }

            // read data
            if (SingleStock.getStockDaliyData().size() > 5) {
                for (StockDataElement thi : SingleStock.getStockDaliyData()) {
                    if (thi.getDate().equals("") || thi.getDate().equals(" ")) {
                        Logs.e(SingleStock.getStockCode() + " date is null!");
                        return null;
                    } else {
//                            Logs.e(thi.getDate());
                    }
                    Date read = thi.getDate();
                    if (read.getTime() >= start.getTime() && read.getTime() <= end.getTime()) {
                        singleStock.getStockDaliyData().add(thi);
                    }

                }
            } else {
                return null;
            }
        } catch (ParseException e) {
            Logs.e(SingleStock.getStockCode());
            return null;
        }
        return singleStock;
    }

    /**
     * method to parse corr data from ASCII file, which was saved by this
     * software not ready
     *
     */
    public void parseCorrAscFile(ArrayList<String> dataList) {

        String tempString = null;
        int line = 1;
        for (int i = 0; i < dataList.size(); i++) {
            tempString = (String) dataList.get(i);
//            Logs.e("line " + line + ": " + tempString);
            String[] readString = tempString.split(" ");
            if (line == 1) {
                if (readString[0].startsWith("6")) {
                    SingleStock.setStockCode("SH" + readString[0]);

                } else {
                    SingleStock.setStockCode("SZ" + readString[0]);
                }

                SingleStock.setStockName(readString[1]);
                line++;
                continue;
            }
            if (line == 2) {
                line++;
                continue;
            }
            if (tempString.contains(":")) {
                break;
            }

            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

            try {

                SingleStock.getStockDaliyData().add(createDataElement(tempString, true));
                line++;

            } catch (Exception exception) {
            }

        }
    }

    public void parseBinaryFile(ArrayList<String> dataList) {
        byte[] buffer = new byte[32];

        for (int i = 0; i < dataList.size(); i++) {
            //String to byte
            buffer = dataList.get(i).getBytes();
            SingleStock.getStockDaliyData().add(createSingleDayData(buffer));

        }

    }
}
