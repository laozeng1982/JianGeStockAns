/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui;

import ChartDirector.*;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import stockany.datamodel.GlobalParameters;
import stockany.datamodel.SingleStock;
import stockany.tools.Common;
import tools.utilities.Logs;

/**
 * FXML Controller class
 *
 * @author JianGe
 */
public final class FinanceChartPaneController extends AnchorPane implements Initializable {

    private String stockCode = "";
    private String stockName = "";
    private Date[] timeStamps;
    private double[] volData;       //The volume values.
    private double[] highData;      //The high values.
    private double[] lowData;       //The low values.
    private double[] openData;      //The open values.
    private double[] closeData;     //The close values.

    private int durationDays;
    private Date displayStartDate;
    private Date displayEndDate;
    private GregorianCalendar displayStartCalendar = new GregorianCalendar();
    private GregorianCalendar displayEndCalendar = new GregorianCalendar();
    private Date markStartDate;
    private int markStartPosition = -1;
    private Date markEndDate;
    private int markEndPosition = -1;
    private boolean hasDrawMark = false;

    private int defaultWidth = 1600;
    private int defaultmainHeight = 210;
    private int defaultIndicatorHeight = 70;

    // An extra data series to compare with the close data
    private String compareStockCode = "";
    private double[] compareData = null;

    private ItermList timePeriodIItems = new ItermList();
    private ItermList chartSizeItems = new ItermList();
    private ItermList chartTypeItems = new ItermList();
    private ItermList priceBandItems = new ItermList();
    private ItermList avgTypeItems = new ItermList();
    private ItermList IndicatorsItems = new ItermList();

    // The resolution of the data in seconds. 1 day = 86400 seconds.
    private int resolution = 86400;

    // The moving average periods
    private ArrayList<Integer> avgPeriodList = new ArrayList<>();

    // This flag is used to suppress event handlers before complete initialization
    private boolean hasFinishedInitialization;

    // Print Area
    double maxPrintWdith;
    double maxPrintHeight;

    //Chart Data
    private FinanceChart mFinanceChart;

    // Controls
    private ChartViewer stockChartViewer;

    // Use the same action hanlder for all controls
    EventHandler eventHandler = new EventHandler() {
        @Override
        public void handle(Event event) {
            handleSelection(event);
        }
    };

    /// <summary>
    /// Handler for the actionPerformed event for the controls
    /// </summary>
    private void handleSelection(Event evt) {
//        Logs.e("handle " + evt.getSource().toString());
        if (evt.getSource().equals(cmBxSourceStockCode)) {
            stockCode = cmBxSourceStockCode.getSelectionModel().getSelectedItem().replaceAll(" +", "").split(":")[0];
        }

        if (evt.getSource().equals(txtFMovingAvgLength)) {

        }

        if (evt.getSource().equals(cmBxTimeBefore) || evt.getSource().equals(cmBxTimeAfter)) {
            this.displayStartCalendar = null;
        }

        if (hasFinishedInitialization) {
            drawChart(stockChartViewer);
        }
    }

    @FXML
    private void onContextMenu(ContextMenuEvent event) {
    }

    @FXML
    private void onBtnChangeColor(ActionEvent event) {
    }

    @FXML
    private void onScrollFinished(ScrollEvent event) {
        Logs.e(event.getSource());
    }

    @FXML
    private void onKStyleChoice(ActionEvent event) {
    }

    /// <summary>
    /// A utility class for adding items to ComboBox
    /// </summary>
    private static class CmbxItems {

        private final String m_key;
        private final String m_value;

        private CmbxItems(String key, String val) {
            m_key = key;
            m_value = val;
        }

        private String getKey() {
            return m_key;
        }

        private String getValue() {
            return m_value;
        }

        @Override
        public String toString() {
            return m_value;
        }
    }

    private static class ItermList extends ArrayList<CmbxItems> {

        public String getKey(String value) {
            String key = null;
            for (CmbxItems aThi : this) {
                if (aThi.getValue().equals(value)) {
                    key = aThi.getKey();
                }
            }
            return key;
        }

        public String getValue(String key) {
            String value = null;
            for (CmbxItems aThi : this) {
                if (aThi.getKey().equals(key)) {
                    value = aThi.getValue();
                }
            }
            return value;
        }
    }

    @FXML
    private GridPane gridPaneStockController;
    @FXML
    private ComboBox<String> cmBxSourceStockCode;
    @FXML
    private ComboBox<String> cmBxCompareStockCode;
    @FXML
    private ComboBox<String> cmBxTimeBefore;
    @FXML
    private ComboBox<String> cmBxTimeAfter;
    @FXML
    private ComboBox<String> cmBxChartSize;
    @FXML
    private CheckBox chkBxVolumeBar;
    @FXML
    private CheckBox chkBxParabolicSAR;
    @FXML
    private CheckBox chkBxLogScale;
    @FXML
    private CheckBox chkBxPercentageGrid;
    @FXML
    private ToggleGroup kStyleTglGroup;
    @FXML
    private RadioButton rdBtnKStyleDaily;
    @FXML
    private RadioButton rdBtnKStyle5Min;
    @FXML
    private RadioButton rdBtnKStyle15Min;
    @FXML
    private RadioButton rdBtnKStyle30Min;
    @FXML
    private RadioButton rdBtnKStyle60Min;
    @FXML
    private RadioButton rdBtnKStyleOther;
    @FXML
    private ComboBox<String> cmBxChartType;
    @FXML
    private ComboBox<String> cmBxPriceBands;
    @FXML
    private Button btnChangeColor;
    @FXML
    private TextField txtFMovingAvgLength;
    @FXML
    private ComboBox<String> cmBxMovingAvgType;
    @FXML
    private ComboBox<String> cmBxIndicator1;
    @FXML
    private ComboBox<String> cmBxIndicator2;
    @FXML
    private ComboBox<String> cmBxIndicator3;
    @FXML
    private ComboBox<String> cmBxIndicator4;

    @FXML
    private SwingNode swingNode;

    public FinanceChartPaneController(String stockCode, String stockName, Date startDate, int durationDays, Date endDate) {
        this(stockCode, stockName);
        this.markStartDate = startDate;
        this.markEndDate = endDate;
        this.displayStartDate = startDate;
        this.durationDays = durationDays;
        this.displayEndDate = endDate;

        if (startDate != null) {
            this.displayStartCalendar.setTime(startDate);
        } else {
            this.displayStartCalendar = null;
        }

        if (endDate != null) {
            this.displayEndCalendar.setTime(endDate);
        } else {
            this.displayEndCalendar = null;
        }

        initStockChart();
    }

    public FinanceChartPaneController(String stockCode, String stockName) {
        load();

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        maxPrintWdith = primaryScreenBounds.getWidth() - 20;
        maxPrintHeight = (primaryScreenBounds.getHeight() - 50) / 2;
        this.setPrefSize(maxPrintWdith, maxPrintHeight);
//        Logs.e(maxPrintWdith + " , " + maxPrintHeight);
        this.stockCode = stockCode;
        this.stockName = stockName;

    }

    public FinanceChartPaneController() {
        load();
        this.stockCode = "";
        this.stockName = "";
        this.displayStartDate = null;
        this.displayStartCalendar = null;
        this.durationDays = 0;
        this.displayEndDate = null;
        this.displayEndCalendar = null;

        initStockChart();
    }

    private void load() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FinanceChartPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();

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

    @FXML
    private void onMouseClicked(javafx.scene.input.MouseEvent event) {

    }

    @FXML
    private void onMouseMoved(javafx.scene.input.MouseEvent event) {

        if (cmBxSourceStockCode.getSelectionModel().getSelectedItem() != null) {
            if (!cmBxSourceStockCode.getSelectionModel().getSelectedItem().isEmpty()) {
                onChartMouseMoved(event);
            }
        }

    }

    public void onChartMouseMoved(javafx.scene.input.MouseEvent event) {

        ChartViewer viewer = getChartView(event);
        trackFinance((MultiChart) viewer.getChart(), markStartDate, markEndDate, viewer.getPlotAreaMouseX(), viewer.getPlotAreaMouseY());
        viewer.updateDisplay();
    }

    private ChartViewer getChartView(javafx.scene.input.MouseEvent event) {
        JScrollPane jsp = (JScrollPane) ((SwingNode) event.getSource()).getContent();
        JViewport jv = (JViewport) jsp.getComponent(0);
        ChartViewer viewer = (ChartViewer) jv.getComponent(0);

        return viewer;
    }

    // Add a movedMovedPlotArea event listener to draw the track cursor
    public void trackFinance(MultiChart m, Date markStartDate, Date markEndDate, int mouseX, int mouseY) {
        if (m == null) {
//            Logs.e("erro");
            return;
        }

        // Clear the current dynamic layer and get the DrawArea object to draw on it.
        DrawArea d = m.initDynamicLayer();

        // It is possible for a FinanceChart to be empty, so we need to check for it.
        if (m.getChartCount() == 0) {
            return;
        }

        // Get the data x-value that is nearest to the mouse
        int xValue = (int) (((XYChart) m.getChart(0)).getNearestXValue(mouseX));

        // Iterate the XY charts (main price chart and indicator charts) in the FinanceChart
        XYChart c = null;
        XYChart mainChart = null;
        for (int chartIndex = 0; chartIndex < m.getChartCount(); ++chartIndex) {
            c = (XYChart) m.getChart(chartIndex);

            // Variables to hold the legend entries
            String ohlcLegend = "";
            ArrayList legendEntries = new ArrayList();

            // Iterate through all layers to find the highest data point
            for (int idx = 0; idx < c.getLayerCount(); ++idx) {
                Layer layer = c.getLayerByZ(idx);
                int xIndex = layer.getXIndexOf(xValue);
                int dataSetCount = layer.getDataSetCount();

                // In a FinanceChart, only layers showing OHLC data can have 4 data sets
                if (dataSetCount == 4) {
                    mainChart = c;
                    double highValue = layer.getDataSet(0).getValue(xIndex);
                    double lowValue = layer.getDataSet(1).getValue(xIndex);
                    double openValue = layer.getDataSet(2).getValue(xIndex);
                    double closeValue = layer.getDataSet(3).getValue(xIndex);

                    if (closeValue != Chart.NoValue) {
                        // Build the OHLC legend
                        ohlcLegend = "Open: " + c.formatValue(openValue, "{value|P4}") + ", High: "
                                + c.formatValue(highValue, "{value|P4}") + ", Low: " + c.formatValue(lowValue,
                                "{value|P4}") + ", Close: " + c.formatValue(closeValue, "{value|P4}");

                        // We also draw an upward or downward triangle for up and down days and the % change
                        double lastCloseValue = layer.getDataSet(3).getValue(xIndex - 1);
                        if (lastCloseValue != Chart.NoValue) {
                            double change = closeValue - lastCloseValue;
                            double percent = change * 100 / closeValue;
                            // This a OCHL index, red is going up and green is going down in China
                            String symbol = ((change >= 0)
                                    ? "<*font,color=CC0000*><*img=@triangle,width=8,color=CC0000*>"
                                    : "<*font,color=008800*><*img=@invertedtriangle,width=8,color=008800*>");

                            ohlcLegend = ohlcLegend + "  " + symbol + " " + c.formatValue(change, "{value|P4}"
                            ) + " (" + c.formatValue(percent, "{value|2}") + "%)<*/font*>";
                        }

                        // Use a <*block*> to make sure the line does not wrap within the legend entry
                        ohlcLegend = "<*block*>" + ohlcLegend + "      <*/*>";
                    }
                } else {
                    // Iterate through all the data sets in the layer
                    for (int k = 0; k < layer.getDataSetCount(); ++k) {
                        ChartDirector.DataSet dataSet = layer.getDataSetByZ(k);

                        String name = dataSet.getDataName();
                        double value = dataSet.getValue(xIndex);
                        if ((!(name == null || name.equals(""))) && (value != Chart.NoValue)) {

                            // In a FinanceChart, the data set name consists of the indicator name and its
                            // latest value. It is like "Vol: 123M" or "RSI (14): 55.34". As we are generating
                            // the values dynamically, we need to extract the indictor name out, and also the
                            // volume unit (if any).
                            // The unit character, if any, is the last character and must not be a digit.
                            String unitChar = name.substring(name.length() - 1);
                            if (unitChar.compareTo("0") >= 0 && unitChar.compareTo("9") <= 0) {
                                unitChar = "";
                            }

                            // The indicator name is the part of the name up to the colon character.
                            int delimiterPosition = name.indexOf(":");
                            if (delimiterPosition != -1) {
                                name = name.substring(0, delimiterPosition);
                            }

                            // In a FinanceChart, if there are two data sets, it must be representing a range.
                            if (dataSetCount == 2) {
                                // We show both values in the range in a single legend entry
                                value = layer.getDataSet(0).getValue(xIndex);
                                double value2 = layer.getDataSet(1).getValue(xIndex);
                                name = name + ": " + c.formatValue(Math.min(value, value2), "{value|P3}")
                                        + " - " + c.formatValue(Math.max(value, value2), "{value|P3}");
                            } else {
                                // In a FinanceChart, only the layer for volume bars has 3 data sets for
                                // up/down/flat days
                                if (dataSetCount == 3) {
                                    // The actual volume is the sum of the 3 data sets.
                                    value = layer.getDataSet(0).getValue(xIndex) + layer.getDataSet(1
                                    ).getValue(xIndex) + layer.getDataSet(2).getValue(xIndex);
                                }

                                // Create the legend entry
                                name = name + ": " + c.formatValue(value, "{value|P3}") + unitChar;
                            }

                            // Build the legend entry, consist of a colored square box and the name (with the
                            // data value in it).
                            legendEntries.add("<*block*><*img=@square,width=8,edgeColor=000000,color="
                                    + Integer.toHexString(dataSet.getDataColor()) + "*> " + name + "<*/*>");
                        }
                    }
                }
            }

            // Get the plot area position relative to the entire FinanceChart
            PlotArea plotArea = c.getPlotArea();
            int plotAreaLeftX = plotArea.getLeftX() + c.getAbsOffsetX();
            int plotAreaTopY = plotArea.getTopY() + c.getAbsOffsetY();

            // The legend is formed by concatenating the legend entries.
            Collections.reverse(legendEntries);
            String legendText = Chart.stringJoin(legendEntries, "      ");

            // Add the date and the ohlcLegend (if any) at the beginning of the legend
            legendText = "<*block,valign=top,maxWidth=" + (plotArea.getWidth() - 5) + "*><*font=Arial Bold*>["
                    + c.xAxis().getFormattedLabel(xValue, "yyyy-mm-dd") + "]<*/font*>      " + ohlcLegend
                    + legendText;

            TTFText t = d.text(legendText, "Arial", 8);
            t.draw(plotAreaLeftX + 5, plotAreaTopY + 3, 0x000000, Chart.TopLeft);

            // Add a mark line
            if (markEndDate != null && markStartDate != null && !hasDrawMark) {
                for (int xIndex = 0; xIndex < c.xAxis().getMaxValue(); xIndex++) {
                    String readDate = c.xAxis().getFormattedLabel(xIndex, "yyyy-mm-dd");

                    try {
                        Date read = Common.DATAPICKER_DAY_SDF.parse(readDate);
                        if (read.getTime() == markStartDate.getTime()) {
                            markStartPosition = xIndex;
                        }
                        if (read.getTime() == markEndDate.getTime()) {
                            markEndPosition = xIndex;
                        }

                        if (markStartPosition != -1 && markEndPosition != -1) {
                            hasDrawMark = true;
                        }
                    } catch (ParseException e) {
                    }
                }
            }

            if (markStartPosition != -1) {
                d.vline(plotAreaTopY, plotAreaTopY + plotArea.getHeight(), c.getXCoor(markStartPosition) + c.getAbsOffsetX(),
                        d.dashLineColor(0x0835e4, 0x0501));
            }
            if (markEndPosition != -1) {
                d.vline(plotAreaTopY, plotAreaTopY + plotArea.getHeight(), c.getXCoor(markEndPosition) + c.getAbsOffsetX(),
                        d.dashLineColor(0xFF0000, 0x0501));
            }

            // Draw a vertical track line at the x-position, this a dynamic line, will dispear
            d.vline(plotAreaTopY, plotAreaTopY + plotArea.getHeight(), c.getXCoor(xValue) + c.getAbsOffsetX(),
                    d.dashLineColor(0x000000, 0x0301));

            if (mainChart != null) {

                d.hline(plotArea.getLeftX(), plotArea.getRightX(), mouseY, d.dashLineColor(0x000000, 0x0301));

                // Draw x-axis label
                String labelX = "<*block,bgColor=FFFFDD,margin=1,edgeColor=000000*>" + mainChart.xAxis().getFormattedLabel(xValue, "yyyy-mm-dd") + "<*/*>";
                TTFText hintX = d.text(labelX, "Arial Bold", 8);
                hintX.draw(mouseX, plotArea.getBottomY() + 5, 0x000000, Chart.Bottom);

                // Draw y-axis label
                double yValue = 0;
                if (mainChart.getPlotArea().getHeight() >= 400) {
                    yValue = mainChart.yAxis().getMaxValue() - (mouseY - 136.0d) * (mainChart.yAxis().getMaxValue() - mainChart.yAxis().getMinValue()) / 372.0d;
                } else {
                    yValue = mainChart.yAxis().getMaxValue() - (mouseY - 131.0d) * (mainChart.yAxis().getMaxValue() - mainChart.yAxis().getMinValue()) / 182.0d;
                }

                String labelY = "<*block,bgColor=FFFFDD,margin=1,edgeColor=000000*>" + mainChart.formatValue(yValue, "{value|P4}") + "<*/*>";
                TTFText hintY = d.text(labelY, "Arial Bold", 8);
                hintY.draw(plotArea.getLeftX() + 35, mouseY, 0x000000, Chart.Right);

            }

        }
    }

    public void initStockChart() {
        // Prepare controls

        // Chart Viewer
        stockChartViewer = new ChartViewer();
        stockChartViewer.setBackground(new java.awt.Color(255, 255, 255));
        stockChartViewer.setHorizontalAlignment(SwingConstants.CENTER);
        stockChartViewer.setOpaque(true);

        // Connect controls to event handlers
        for (int idx = 0; idx < gridPaneStockController.getChildren().size(); idx++) {
            Node node = gridPaneStockController.getChildren().get(idx);

            //                    Logs.e(node.getClass().toString());
            if (node instanceof ComboBox) {
                ((ComboBox) node).setOnAction(eventHandler);
            }
            if (node instanceof CheckBox) {
                ((CheckBox) node).setOnAction(eventHandler);
            }
            if (node instanceof TextField) {
                ((TextField) node).setOnAction(eventHandler);
            }

        }

        //Set init
        txtFMovingAvgLength.setText("5,10, 20, 60");

        // Init Time Periods
        timePeriodIItems.add(new CmbxItems("1", "1 day"));
        timePeriodIItems.add(new CmbxItems("2", "2 days"));
        timePeriodIItems.add(new CmbxItems("5", "5 days"));
        timePeriodIItems.add(new CmbxItems("10", "10 days"));
        timePeriodIItems.add(new CmbxItems("30", "1 month"));
        timePeriodIItems.add(new CmbxItems("60", "2 months"));
        timePeriodIItems.add(new CmbxItems("90", "3 months"));
        timePeriodIItems.add(new CmbxItems("180", "6 months"));
        timePeriodIItems.add(new CmbxItems("360", "1 year"));
        timePeriodIItems.add(new CmbxItems("720", "2 years"));
        timePeriodIItems.add(new CmbxItems("1080", "3 years"));
        timePeriodIItems.add(new CmbxItems("1440", "4 years"));
        timePeriodIItems.add(new CmbxItems("1800", "5 years"));
        timePeriodIItems.add(new CmbxItems("3600", "10 years"));

        ArrayList<String> timePeriods = new ArrayList<>();
        for (int i = 0; i < timePeriodIItems.size(); i++) {
            timePeriods.add(timePeriodIItems.get(i).getValue());
        }

        ObservableList<String> timePeriodsList = FXCollections.observableArrayList(timePeriods);
        cmBxTimeBefore.setItems(timePeriodsList);
        cmBxTimeAfter.setItems(timePeriodsList);
//        cmBxTimeBefore.getSelectionModel().select(8);

        // Init Chart Size
        chartSizeItems.add(new CmbxItems("50", "50"));
        chartSizeItems.add(new CmbxItems("100", "100"));
        chartSizeItems.add(new CmbxItems("150", "150"));
        chartSizeItems.add(new CmbxItems("200", "200"));

        ArrayList<String> chartSize = new ArrayList<>();
        for (int i = 0; i < chartSizeItems.size(); i++) {
            chartSize.add(chartSizeItems.get(i).getValue());
        }

        ObservableList<String> chartSizeList = FXCollections.observableArrayList(chartSize);
        cmBxChartSize.setEditable(true);
        cmBxChartSize.setItems(chartSizeList);
        cmBxChartSize.getSelectionModel().select(1);

        // Init Chart Type
        chartTypeItems.add(new CmbxItems("None", "None"));
        chartTypeItems.add(new CmbxItems("CandleStick", "CandleStick"));
        chartTypeItems.add(new CmbxItems("Close", "Closing Price"));
        chartTypeItems.add(new CmbxItems("Median", "Median Price"));
        chartTypeItems.add(new CmbxItems("OHLC", "OHLC"));
        chartTypeItems.add(new CmbxItems("TP", "Typical Price"));
        chartTypeItems.add(new CmbxItems("WC", "Weighted Close"));

        ArrayList<String> chartType = new ArrayList<>();
        for (int i = 0; i < chartTypeItems.size(); i++) {
            chartType.add(chartTypeItems.get(i).getValue());
        }

        ObservableList<String> chartTypeList = FXCollections.observableArrayList(chartType);
        cmBxChartType.setItems(chartTypeList);
        cmBxChartType.getSelectionModel().select(1);

        // Init Price Band
        priceBandItems.add(new CmbxItems("None", "None"));
        priceBandItems.add(new CmbxItems("BB", "Bollinger Band"));
        priceBandItems.add(new CmbxItems("DC", "Donchain Channel"));
        priceBandItems.add(new CmbxItems("Envelop", "Envelop (SMA 20 +/- 10%)"));

        ArrayList<String> priceBand = new ArrayList<>();
        for (int i = 0; i < priceBandItems.size(); i++) {
            priceBand.add(priceBandItems.get(i).getValue());
        }

        ObservableList<String> priceBandList = FXCollections.observableArrayList(priceBand);
        cmBxPriceBands.setItems(priceBandList);
        cmBxPriceBands.getSelectionModel().select(1);

        // Init Average Tpye
        avgTypeItems.add(new CmbxItems("None", "None"));
        avgTypeItems.add(new CmbxItems("SMA", "Simple"));
        avgTypeItems.add(new CmbxItems("EMA", "Exponential"));
        avgTypeItems.add(new CmbxItems("TMA", "Triangular"));
        avgTypeItems.add(new CmbxItems("WMA", "Weighted"));

        ArrayList<String> avgLong = new ArrayList<>();
        for (int i = 0; i < avgTypeItems.size(); i++) {
            avgLong.add(avgTypeItems.get(i).getValue());
        }

        ObservableList<String> avgLongList = FXCollections.observableArrayList(avgLong);
        cmBxMovingAvgType.setItems(avgLongList);
        cmBxMovingAvgType.getSelectionModel().select(1);

        // Init Indicators
        IndicatorsItems.add(new CmbxItems("None", "None"));
        IndicatorsItems.add(new CmbxItems("AccDist", "Accumulation/Distribution"));
        IndicatorsItems.add(new CmbxItems("AroonOsc", "Aroon Oscillator"));
        IndicatorsItems.add(new CmbxItems("Aroon", "Aroon Up/Down"));
        IndicatorsItems.add(new CmbxItems("ADX", "Avg Directional Index"));
        IndicatorsItems.add(new CmbxItems("ATR", "Avg True Range"));
        IndicatorsItems.add(new CmbxItems("BBW", "Bollinger Band Width"));
        IndicatorsItems.add(new CmbxItems("CMF", "Chaikin Money Flow"));
        IndicatorsItems.add(new CmbxItems("COscillator", "Chaikin Oscillator"));
        IndicatorsItems.add(new CmbxItems("CVolatility", "Chaikin Volatility"));
        IndicatorsItems.add(new CmbxItems("CLV", "Close Location Value"));
        IndicatorsItems.add(new CmbxItems("CCI", "Commodity Channel Index"));
        IndicatorsItems.add(new CmbxItems("DPO", "Detrended Price Osc"));
        IndicatorsItems.add(new CmbxItems("DCW", "Donchian Channel Width"));
        IndicatorsItems.add(new CmbxItems("EMV", "Ease of Movement"));
        IndicatorsItems.add(new CmbxItems("FStoch", "Fast Stochastic"));
        IndicatorsItems.add(new CmbxItems("MACD", "MACD"));
        IndicatorsItems.add(new CmbxItems("MDX", "Mass Index"));
        IndicatorsItems.add(new CmbxItems("Momentum", "Momentum"));
        IndicatorsItems.add(new CmbxItems("MFI", "Money Flow Index"));
        IndicatorsItems.add(new CmbxItems("NVI", "Neg Volume Index"));
        IndicatorsItems.add(new CmbxItems("OBV", "On Balance Volume"));
        IndicatorsItems.add(new CmbxItems("Performance", "Performance"));
        IndicatorsItems.add(new CmbxItems("PPO", "% Price Oscillator"));
        IndicatorsItems.add(new CmbxItems("PVO", "% Volume Oscillator"));
        IndicatorsItems.add(new CmbxItems("PVI", "Pos Volume Index"));
        IndicatorsItems.add(new CmbxItems("PVT", "Price Volume Trend"));
        IndicatorsItems.add(new CmbxItems("ROC", "Rate of Change"));
        IndicatorsItems.add(new CmbxItems("RSI", "RSI"));
        IndicatorsItems.add(new CmbxItems("SStoch", "Slow Stochastic"));
        IndicatorsItems.add(new CmbxItems("StochRSI", "StochRSI"));
        IndicatorsItems.add(new CmbxItems("TRIX", "TRIX"));
        IndicatorsItems.add(new CmbxItems("UO", "Ultimate Oscillator"));
        IndicatorsItems.add(new CmbxItems("Vol", "Volume"));
        IndicatorsItems.add(new CmbxItems("WilliamR", "William's %R"));

        ArrayList<String> indicators = new ArrayList<>();
        for (int i = 0; i < IndicatorsItems.size(); i++) {
            indicators.add(IndicatorsItems.get(i).getValue());
        }

        ObservableList<String> indicatorsList = FXCollections.observableArrayList(indicators);

        cmBxIndicator1.setItems(indicatorsList);
        cmBxIndicator2.setItems(indicatorsList);
        cmBxIndicator3.setItems(indicatorsList);
        cmBxIndicator4.setItems(indicatorsList);

        for (int idx = 0; idx < indicators.size(); ++idx) {
            switch (IndicatorsItems.get(idx).getKey()) {
                case "RSI":
                    cmBxIndicator1.getSelectionModel().select(idx);
                    break;
                case "MACD":
                    cmBxIndicator2.getSelectionModel().select(idx);
                    break;
                default:
                    break;
            }
        }
        cmBxIndicator3.getSelectionModel().select(11);
        cmBxIndicator4.getSelectionModel().select(0);

        // Init ChartView
        JScrollPane sourceChartPane = new JScrollPane(stockChartViewer);

        SwingUtilities.invokeLater(() -> {
            swingNode.setContent(sourceChartPane);
        });

        cmBxSourceStockCode.setItems(GlobalParameters.codeNameList);
        cmBxCompareStockCode.setItems(GlobalParameters.codeNameList);

        if (stockCode != null && !stockCode.isEmpty()) {
            for (int i = 0; i < cmBxSourceStockCode.getItems().size(); i++) {
                String selected = cmBxSourceStockCode.getItems().get(i);
                if (selected.contains(stockCode)) {
                    cmBxSourceStockCode.getSelectionModel().select(selected);
                    break;
                }
            }

        } else {
            cmBxSourceStockCode.getSelectionModel().select("");
        }

        // It is safe to handle events now.
        hasFinishedInitialization = true;
        // Update the chart
        drawChart(stockChartViewer);

    }

    /// <summary>
    /// Get the timeStamps, highData, lowData, openData, closeData and volData.
    /// </summary>
    /// <param name="stockCode">The stockCode symbol for the data series.</param>
    /// <param name="startDate">The starting date/time for the data series.</param>
    /// <param name="endDate">The ending date/time for the data series.</param>
    /// <param name="durationInDays">The number of trading days to get.</param>
    /// <param name="extraPoints">The extra leading data points needed in order to
    /// compute moving averages.</param>
    /// <returns>True if successfully obtain the data, otherwise false.</returns>
    protected boolean getData(String stockCode, GregorianCalendar startDate,
            GregorianCalendar endDate, int durationInDays, int extraPoints) {
        // This method should return false if the stockCode symbol is invalid. In this
        // sample code, as we are using a random number generator for the data, all
        // stockCode symbol is allowed, but we still assumed an empty symbol is invalid.
        if (stockCode.equals("")) {
            return false;
        }

        // In this demo, we can get 15 min, daily, weekly or monthly data depending on
        // the time range.
        resolution = 86400;
        if (durationInDays <= 10) {
            // 10 days or less, we assume 15 minute data points are available
            resolution = 900;

            // We need to adjust the startDate backwards for the extraPoints. We assume
            // 6.5 hours trading time per day, and 5 trading days per week.
            double dataPointsPerDay = 6.5 * 3600 / resolution;
            GregorianCalendar adjustedStartDate = new GregorianCalendar(startDate.get(Calendar.YEAR),
                    startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
            adjustedStartDate.add(Calendar.DAY_OF_MONTH,
                    -(int) Math.ceil(extraPoints / dataPointsPerDay * 7 / 5) - 2);

            // Get the required 15 min data
            get15MinData(stockCode, adjustedStartDate.getTime(), endDate.getTime());

        } else if (durationInDays >= 4.5 * 360) {
            // 4 years or more - use monthly data points.
            resolution = 30 * 86400;

            // Adjust startDate backwards to cater for extraPoints
            GregorianCalendar adjustedStartDate = (GregorianCalendar) startDate.clone();
            adjustedStartDate.add(Calendar.MONTH, -extraPoints);

            // Get the required monthly data
            getMonthlyData(stockCode, adjustedStartDate.getTime(), endDate.getTime());

        } else if (durationInDays >= 1.5 * 360) {
            // 1 year or more - use weekly points.
            resolution = 7 * 86400;

            // Adjust startDate backwards to cater for extraPoints
            GregorianCalendar adjustedStartDate = (GregorianCalendar) startDate.clone();
            adjustedStartDate.add(Calendar.DAY_OF_MONTH, -extraPoints * 7 - 6);

            // Get the required weekly data
            getWeeklyData(stockCode, adjustedStartDate.getTime(), endDate.getTime());

        } else {
            // Default - use daily points
            resolution = 86400;

            // Adjust startDate backwards to cater for extraPoints. We multiply the days
            // by 7/5 as we assume 1 week has 5 trading days.
            GregorianCalendar adjustedStartDate = new GregorianCalendar(startDate.get(Calendar.YEAR),
                    startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
            adjustedStartDate.add(Calendar.DAY_OF_MONTH, -(extraPoints * 7 + 4) / 5 - 2);

            // Get the required daily data
            getDailyData(stockCode, adjustedStartDate.getTime(), endDate.getTime());
        }

        return true;
    }

    /// <summary>
    /// Get 15 minutes data series for timeStamps, highData, lowData, openData, closeData
    /// and volData.
    /// </summary>
    /// <param name="stockCode">The stockCode symbol for the data series.</param>
    /// <param name="startDate">The starting date/time for the data series.</param>
    /// <param name="endDate">The ending date/time for the data series.</param>
    private void get15MinData(String stockCode, Date startDate, Date endDate) {
        //
        // In this demo, we use a random number generator to generate the data. In practice,
        // you may get the data from a database or by other means. If you do not have 15 
        // minute data, you may modify the "drawChart" method below to not using 15 minute
        // data.
        //
        getStockData(stockCode, startDate, endDate, 900);
    }

    /// <summary>
    /// Get daily data series for timeStamps, highData, lowData, openData, closeData
    /// and volData.
    /// </summary>
    /// <param name="stockCode">The stockCode symbol for the data series.</param>
    /// <param name="startDate">The starting date/time for the data series.</param>
    /// <param name="endDate">The ending date/time for the data series.</param>
    private void getDailyData(String stockCode, Date startDate, Date endDate) {
        //
        // In this demo, we use a random number generator to generate the data. In practice,
        // you may get the data from a database or by other means. A typical database code
        // example is like below. (This only shows a general idea. The exact details may differ
        // depending on your database brand and schema.)
        //
        //   //Connect to database using a suitable JDBC driver and the connect string
        //   Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        //   java.sql.Connection dbConn = java.sql.DriverManager.getConnection
        //        ("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=example.mdb");
        //
        //   //SQL statement to get the data. We use prepared statements in the example as it
        //   //offers a portable way to handle date/time formats.
        //   String sql = "Select recordDate, highData, lowData, openData, closeDate, volData " + 
        //       "From dailyFinanceTable Where stockCode = '" + stockCode + "' And recordDate >= ? " +
        //       " And recordDate <= ? Order By recordDate";
        //   java.sql.PreparedStatement stmt = dbConn.prepareStatement(sql);
        //   stmt.setDate(1, new java.sql.Date(startDate.getTime()));
        //   stmt.setDate(2, new java.sql.Date(endDate.getTime()));
        //
        //   //The most convenient way to read the SQL result into arrays is to use the 
        //   //ChartDirector DBTable utility.
        //   DBTable table = new DBTable(stmt.executeQuery());
        //   stmt.close();
        //   dbConn.close();
        //
        //   //Now get the data into arrays
        //   timeStamps = table.getColAsDateTime(0);
        //   highData = table.getCol(1);
        //   lowData = table.getCol(2);
        //   openData = table.getCol(3);
        //   closeData = table.getCol(4);
        //   volData = table.getCol(5);
        //

        getStockData(stockCode, startDate, endDate, 86400);

    }

    /// <summary>
    /// Get weekly data series for timeStamps, highData, lowData, openData, closeData
    /// and volData.
    /// </summary>
    /// <param name="stockCode">The stockCode symbol for the data series.</param>
    /// <param name="startDate">The starting date/time for the data series.</param>
    /// <param name="endDate">The ending date/time for the data series.</param>
    private void getWeeklyData(String stockCode, Date startDate, Date endDate) {
        //
        // In this demo, we use a random number generator to generate the data. In practice,
        // you may get the data from a database or by other means. If you do not have weekly
        // data, you may call "getDailyData" to get daily data first, and then call 
        // "convertDailyToWeeklyData" to convert it to weekly data, like:
        //
        //      getDailyData(startDate, endDate);
        //      convertDailyToWeeklyData();
        //
        getStockData(stockCode, startDate, endDate, 86400 * 7);
    }

    /// <summary>
    /// Get monthly data series for timeStamps, highData, lowData, openData, closeData
    /// and volData.
    /// </summary>
    /// <param name="stockCode">The stockCode symbol for the data series.</param>
    /// <param name="startDate">The starting date/time for the data series.</param>
    /// <param name="endDate">The ending date/time for the data series.</param>
    private void getMonthlyData(String stockCode, Date startDate, Date endDate) {
        //
        // In this demo, we use a random number generator to generate the data. In practice,
        // you may get the data from a database or by other means. If you do not have monthly
        // data, you may call "getDailyData" to get daily data first, and then call 
        // "convertDailyToMonthlyData" to convert it to monthly data, like:
        //
        //      getDailyData(startDate, endDate);
        //      convertDailyToMonthlyData();
        //
        getStockData(stockCode, startDate, endDate, 86400 * 30);

    }

    /// <summary>
    /// A random number generator designed to generate realistic financial data.
    /// </summary>
    /// <param name="stockCode">The stockCode symbol for the data series.</param>
    /// <param name="startDate">The starting date/time for the data series.</param>
    /// <param name="endDate">The ending date/time for the data series.</param>
    /// <param name="resolution">The period of the data series.</param>
    private void getStockData(String stockCode, Date startDate, Date endDate, int resolution) {
        SingleStock singleStock = GlobalParameters.AllStockSetsForCalc.
                getSingleStockByCode(stockCode).getPartSingleStock(startDate, 5000, endDate);
        if (singleStock != null) {
            timeStamps = singleStock.getDateArray(Common.Stock.DateTypes.Daliy);
            highData = singleStock.getDataArrayDouble(Common.Stock.DateTypes.Daliy, Common.Stock.DataTypes.HIGHEST_PRICE);
            lowData = singleStock.getDataArrayDouble(Common.Stock.DateTypes.Daliy, Common.Stock.DataTypes.LOWEST_PRICE);
            openData = singleStock.getDataArrayDouble(Common.Stock.DateTypes.Daliy, Common.Stock.DataTypes.OPEN_PRICE);
            closeData = singleStock.getDataArrayDouble(Common.Stock.DateTypes.Daliy, Common.Stock.DataTypes.CLOSE_PRICE);
            volData = singleStock.getDataArrayDouble(Common.Stock.DateTypes.Daliy, Common.Stock.DataTypes.VOL);
        } else {
            Logs.e("No such a stock: " + stockCode);
        }

    }

    /// <summary>
    /// A utility to convert daily to weekly data.
    /// </summary>
    private void convertDailyToWeeklyData() {
        aggregateData(new ArrayMath(timeStamps).selectStartOfWeek());
    }

    /// <summary>
    /// A utility to convert daily to monthly data.
    /// </summary>
    private void convertDailyToMonthlyData() {
        aggregateData(new ArrayMath(timeStamps).selectStartOfMonth());
    }

    /// <summary>
    /// An internal method used to aggregate daily data.
    /// </summary>
    private void aggregateData(ArrayMath aggregator) {
        timeStamps = Chart.NTime(aggregator.aggregate(Chart.CTime(timeStamps), Chart.AggregateFirst));
        highData = aggregator.aggregate(highData, Chart.AggregateMax);
        lowData = aggregator.aggregate(lowData, Chart.AggregateMin);
        openData = aggregator.aggregate(openData, Chart.AggregateFirst);
        closeData = aggregator.aggregate(closeData, Chart.AggregateLast);
        volData = aggregator.aggregate(volData, Chart.AggregateSum);
    }

    public void showChart() {
        drawChart(stockChartViewer);
    }

    public void drawChart(ChartViewer viewer) {
        GregorianCalendar startCalender;
        GregorianCalendar endCalender;
        int durationInDays;

        if (displayStartCalendar != null && durationDays != 0 && displayEndCalendar != null) {

            startCalender = displayStartCalendar;
            endCalender = displayEndCalendar;
            durationInDays = durationDays;

        } else {
            // If the trading day has not yet started (before 9:30am), or if the end date is on
            // on Sat or Sun, we set the end date to 4:00pm of the last trading day	

            // Start from last end, if the Time period change
            endCalender = new GregorianCalendar();
            if (displayEndCalendar != null) {
                int addDay;
                if (cmBxTimeAfter.getSelectionModel().getSelectedItem() != null) {
                    addDay = Integer.parseInt((timePeriodIItems.getKey(cmBxTimeAfter.getSelectionModel().getSelectedItem())));

                } else {
                    addDay = 1;
                }

                // always count from mark date
                Calendar calendar = Calendar.getInstance();
                if (markEndDate != null) {
                    calendar.setTime(markEndDate);
                } else {
                    calendar.setTime(markEndDate);
                }

                calendar.add(Calendar.DATE, addDay);
                endCalender.setTime(calendar.getTime());

//                Logs.e("Date :" + Utils.DATA_OF_DAY_SDF.format(calendar.getTime()));
//                Logs.e(" current at: " + Utils.DATA_OF_DAY_SDF.format(markEndDate));
//                Logs.e(" current end at: " + Utils.DATA_OF_DAY_SDF.format(endCalender.getTime()));
//                endCalender = displayEndCalendar;
            }

            while ((endCalender.get(Calendar.HOUR_OF_DAY) * 60 + endCalender.get(Calendar.MINUTE)
                    < 9 * 60 + 30) || (endCalender.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                    || (endCalender.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) {
                endCalender.add(Calendar.DAY_OF_MONTH, -1);
                endCalender.set(Calendar.HOUR_OF_DAY, 16);
                endCalender.set(Calendar.MINUTE, 0);
                endCalender.set(Calendar.SECOND, 0);
            }

            // The duration selected by the user
            if (cmBxTimeBefore.getSelectionModel().getSelectedItem() != null || cmBxTimeAfter.getSelectionModel().getSelectedItem() != null) {
                int before = 0;
                int after = 0;
                if (cmBxTimeBefore.getSelectionModel().getSelectedItem() != null) {
                    before = Integer.parseInt((timePeriodIItems.getKey(cmBxTimeBefore.getSelectionModel().getSelectedItem())));
                }
                if (cmBxTimeAfter.getSelectionModel().getSelectedItem() != null) {
                    after = Integer.parseInt((timePeriodIItems.getKey(cmBxTimeAfter.getSelectionModel().getSelectedItem())));
                }
                durationInDays = before + after + this.durationDays;
            } else {
                durationInDays = 360;
            }

            // Compute the start date by subtracting the duration from the end date.
            if (durationInDays >= 30) {
                // More or equal to 30 days - so we use months as the unit
                startCalender = new GregorianCalendar(endCalender.get(Calendar.YEAR), endCalender.get(Calendar.MONTH), 1);
                startCalender.add(Calendar.MONTH, -durationInDays / 30);
            } else {
                // Less than 30 days - use day as the unit. The starting point of the axis is
                // always at the start of the day (9:30am). Note that we use trading days, so
                // we skip Sat and Sun in counting the days.
                startCalender = new GregorianCalendar(endCalender.get(Calendar.YEAR), endCalender.get(Calendar.MONTH),
                        endCalender.get(Calendar.DAY_OF_MONTH), 9, 30, 0);
                for (int i = 1; i < durationInDays; ++i) {
                    startCalender.add(Calendar.DAY_OF_MONTH, (startCalender.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) ? -3 : -1);
                }
            }
        }

//        Logs.e(stockCode + " start at: " + startCalender.toInstant().toString());
//        Logs.e(stockCode + " end at: " + endCalender.toInstant().toString());
//        Logs.e(stockCode + " Duration is: " + durationInDays);
        // The moving average periods selected by the user.
        avgPeriodList.clear();
        String avgLength = txtFMovingAvgLength.getText();

        if (avgLength != null && avgLength != null) {
            String[] avgArray = avgLength.replaceAll(" +", "").split(",");
            for (String avgArray1 : avgArray) {
                int avgPeriod = Integer.valueOf(avgArray1);
                avgPeriodList.add(Math.max(0, Math.min(300, avgPeriod)));
            }
        }

        // We need extra leading data points in order to compute moving averages.
        int extraPoints = Math.max(20, Math.max(avgPeriodList.get(0), avgPeriodList.get(1)));

        // Get the data series to compare with, if any.
        if (null != cmBxCompareStockCode.getSelectionModel().getSelectedItem()) {
            compareStockCode = cmBxCompareStockCode.getSelectionModel().getSelectedItem().replaceAll(" +", "").split(":")[0];
            if (getData(compareStockCode, startCalender, endCalender, durationInDays, extraPoints)) {
                compareData = closeData;
            }
        } else {
            compareStockCode = "";
            compareData = null;
        }

        // The data series we want to get.
//        if (stockCode.isEmpty()) {
//            stockCode = cmBxSourceStockCode.getSelectionModel().getSelectedItem().replaceAll(" +", "").split(":")[0];
//        }
        if (!getData(stockCode, startCalender, endCalender, durationInDays, extraPoints)) {
            errMsg(viewer, "Please enter a valid stockCode symbol");
            return;
        }

        // We now confirm the actual number of extra points (data points that are before
        // the start date) as inferred using actual data from the database.
        extraPoints = timeStamps.length;
        Date cutOff = startCalender.getTime();
        for (int i = 0; i < timeStamps.length; ++i) {
            if (!timeStamps[i].before(cutOff)) {
                extraPoints = i;
                break;
            }
        }

        // Check if there is any valid data
        if (extraPoints >= timeStamps.length) {
            // No data - just display the no data message.
            errMsg(viewer, "No data available for the specified time period");
            return;
        }

        // In some finance chart presentation style, even if the data for the latest day 
        // is not fully available, the axis for the entire day will still be drawn, where
        // no data will appear near the end of the axis.
        if (resolution <= 86400) {
            // Add extra points to the axis until it reaches the end of the day. The end
            // of day is assumed to be 4:00pm (it depends on the stock exchange).
            GregorianCalendar lastTime = new GregorianCalendar();
            lastTime.setTime(timeStamps[timeStamps.length - 1]);
            int extraTrailingPoints = (int) ((16 * 3600 - lastTime.get(Calendar.HOUR_OF_DAY) * 3600
                    - lastTime.get(Calendar.MINUTE) * 60 - lastTime.get(Calendar.SECOND)) / resolution);
            if (extraTrailingPoints > 0) {
                Date[] extendedTimeStamps = new Date[timeStamps.length + extraTrailingPoints];
                System.arraycopy(timeStamps, 0, extendedTimeStamps, 0, timeStamps.length);
                for (int i = 0; i < extraTrailingPoints; ++i) {
                    lastTime.add(Calendar.SECOND, resolution);
                    extendedTimeStamps[i + timeStamps.length] = (Date) lastTime.getTime().clone();
                }
                timeStamps = extendedTimeStamps;
            }
        }

        //
        // At this stage, all data is available. We can draw the chart as according to 
        // user input.
        //
        //
        // Determine the chart size. In this demo, user can select 4 different chart sizes as designed. Or set a specific size.
        // Default is the large chart size.
        //
        int width = defaultWidth;
        int mainHeight = defaultmainHeight;
        int indicatorHeight = defaultIndicatorHeight;

        String selectedSize = cmBxChartSize.getSelectionModel().getSelectedItem();
        width = (int) ((Integer.valueOf(selectedSize) / 100.00d) * defaultWidth);

        // Create the chart object using the selected size
        mFinanceChart = new FinanceChart(width);

        // Disable default legend box, as we are using dynamic legend
        mFinanceChart.setLegendStyle("normal", 8, Chart.Transparent, Chart.Transparent);

        // Set the data into the chart object
        mFinanceChart.setData(timeStamps, highData, lowData, openData, closeData, volData, extraPoints);

        //
        // We configure the title of the chart. In this demo chart design, we put the 
        // company name as the top line of the title with left alignment.
        //
        mFinanceChart.addPlotAreaTitle(Chart.TopLeft, stockCode + ", " + stockName);

        // We displays the current date as well as the data resolution on the next line.
        String resolutionText = "";
        switch (resolution) {
            case 30 * 86400:
                resolutionText = "Monthly";
                break;
            case 7 * 86400:
                resolutionText = "Weekly";
                break;
            case 86400:
                resolutionText = "Daily";
                break;
            case 900:
                resolutionText = "15-min";
                break;
            default:
                break;
        }

        //
        // Add the first techical indicator according. In this demo, we draw the first
        // indicator on top of the main chart.
        //
        addIndicator(mFinanceChart, IndicatorsItems.getKey(cmBxIndicator1.getSelectionModel().getSelectedItem()), indicatorHeight);
//        Logs.e(IndicatorsItems.getKey(cmBxIndicator1.getSelectionModel().getSelectedItem()));
        //
        // Add the main chart
        //
        mFinanceChart.addMainChart(mainHeight);

        // Add a zone indicate last 
        // Add a mark line
        XYChart c = (XYChart) mFinanceChart.getChart(0);
        if (markEndDate != null && markStartDate != null) {

            for (int xIndex = 0; xIndex <= c.xAxis().getMaxValue(); xIndex++) {
                String readDate = c.xAxis().getFormattedLabel(xIndex, "yyyy-mm-dd");
                try {
                    Date read = Common.DATAPICKER_DAY_SDF.parse(readDate);
                    if (read.getTime() == markStartDate.getTime()) {
                        markStartPosition = xIndex;
                    }

                    if (read.getTime() == markEndDate.getTime()) {
                        markEndPosition = xIndex;
                    }
                } catch (ParseException e) {
                }
            }
        }

        if (markStartPosition != -1 && markEndPosition != -1) {
            mFinanceChart.addTrackedZone(markStartPosition, markEndPosition, 0xffcc66);
            // reset
            markStartPosition = -1;
            markEndPosition = -1;
        }

        displayStartDate = startCalender.getTime();
        displayEndDate = endCalender.getTime();
        int actualDays = (int) c.xAxis().getMaxValue() + 1;
        long days = (endCalender.getTimeInMillis() - startCalender.getTimeInMillis()) / (1000 * 24 * 3600);

        String info = "Start from: " + "<*font=ARLRDBD.ttf,color=ff0000,size=10*>" + Common.DATE_OF_READIN_DALIY_SDF.format(displayStartDate)
                + "<*font=arial.ttf,color=0F0F0F,size=8*>" + ", End at: "
                + "<*font=ARLRDBD.ttf,color=ff0000,size=10*>" + Common.DATE_OF_READIN_DALIY_SDF.format(displayEndDate)
                + "<*font=arial.ttf,color=0F0F0F,size=8*>" + ", Showing "
                + "<*font=ARLRDBD.ttf,color=ff0000,size=10*>" + actualDays
                + "<*font=arial.ttf,color=0F0F0F,size=8*>" + " days with data, in "
                + "<*font=ARLRDBD.ttf,color=ff0000,size=10*>" + days
                + "<*font=arial.ttf,color=0F0F0F,size=8*>" + " natural days";

        mFinanceChart.addPlotAreaTitle(Chart.BottomLeft, "<*font=arial.ttf,color=0F0F0F,size=8*>"
                + "Current " + mFinanceChart.formatValue(new Date(), "mmm dd, yyyy") + " - " + resolutionText + " chart, " + info);

        // A copyright message at the bottom left corner the title area
        mFinanceChart.addPlotAreaTitle(Chart.BottomRight, "<*font=arial.ttf,size=8*>(c) By Your JianGe");

        //
        // Set log or linear scale according to user preference
        //
        mFinanceChart.setLogScale(chkBxLogScale.isSelected());

        //
        // Set axis labels to show data values or percentage change to user preference
        //
        if (chkBxPercentageGrid.isSelected()) {
            mFinanceChart.setPercentageAxis();
        }

        //
        // Draw the main chart depending on the chart type the user has selected
        //
        String selectedType = chartTypeItems.getKey(cmBxChartType.getSelectionModel().getSelectedItem());
//        Logs.e(selectedType);
        if (null != selectedType) {
            switch (selectedType) {
                case "Close":
                    mFinanceChart.addCloseLine(0x000040);
                    break;
                case "TP":
                    mFinanceChart.addTypicalPrice(0x000040);
                    break;
                case "WC":
                    mFinanceChart.addWeightedClose(0x000040);
                    break;
                case "Median":
                    mFinanceChart.addMedianPrice(0x000040);
                    break;
                default:
                    break;
            }
        }

        //
        // Add comparison line if there is data for comparison
        //
        if (compareData != null) {
            if (compareData.length > extraPoints) {
                mFinanceChart.addComparison(compareData, 0x0000ff, compareStockCode);
            }
        }

        //
        // Add moving average lines.
        //0xEEEE00
        for (int idx = 0; idx < avgPeriodList.size(); idx++) {
            int color = 0x0D0D0D;
            switch (idx) {
                case 1:
                    color = 0xEE0000;
                    break;
                case 2:
                    color = 0x0000FF;
                    break;
                case 3:
                    color = 0x00FF00;
                    break;
                default:
                    break;

            }
            addMovingAvg(mFinanceChart, avgTypeItems.getKey(cmBxMovingAvgType.getSelectionModel().getSelectedItem()), avgPeriodList.get(idx), color);

        }

        //
        // Draw the main chart if the user has selected CandleStick or OHLC. We draw it
        // here to make sure it is drawn behind the moving average lines (that is, the
        // moving average lines stay on top.)
        //
        if (selectedType.equals("CandleStick")) {
            mFinanceChart.addCandleStick(0xff3333, 0x33ff33);
        } else if (selectedType.equals("OHLC")) {
            mFinanceChart.addHLOC(0x8000, 0x800000);
        }

        //
        // Add parabolic SAR if necessary
        //
        if (chkBxParabolicSAR.isSelected()) {
            mFinanceChart.addParabolicSAR(0.02, 0.02, 0.2, Chart.DiamondShape, 5, 0x008800, 0x000000);
        }

        //
        // Add price band/channel/envelop to the chart according to user selection
        //
        String selectedBand = priceBandItems.getKey(cmBxPriceBands.getSelectionModel().getSelectedItem());
//        Logs.e(selectedBand);
        if (null != selectedBand) {
            switch (selectedBand) {
                case "BB":
                    mFinanceChart.addBollingerBand(20, 2, 0x9999ff, 0xc06666ff);
                    break;
                case "DC":
                    mFinanceChart.addDonchianChannel(20, 0x9999ff, 0xc06666ff);
                    break;
                case "Envelop":
                    mFinanceChart.addEnvelop(20, 0.1, 0x9999ff, 0xc06666ff);
                    break;
                default:
                    break;
            }
        }

        //
        // Add volume bars to the main chart if necessary
        //
        if (chkBxVolumeBar.isSelected()) {
            mFinanceChart.addVolBars(indicatorHeight, 0xff3333, 0x33ff33, 0xc0c0c0);
        }

        //
        // Add additional indicators as according to user selection.
        //
        addIndicator(mFinanceChart, IndicatorsItems.getKey(cmBxIndicator2.getSelectionModel().getSelectedItem()), indicatorHeight);
        addIndicator(mFinanceChart, IndicatorsItems.getKey(cmBxIndicator3.getSelectionModel().getSelectedItem()), indicatorHeight);
        addIndicator(mFinanceChart, IndicatorsItems.getKey(cmBxIndicator4.getSelectionModel().getSelectedItem()), indicatorHeight);

        // Output chart to ChartViewer
        viewer.setImage(mFinanceChart.makeImage());
        viewer.setImageMap(mFinanceChart.getHTMLImageMap("", "", "title='" + mFinanceChart.getToolTipDateFormat()
                + " {value|P}'"));

        viewer.setChart(mFinanceChart);

    }

/// <summary>
/// Add a moving average line to the FinanceChart object.
/// </summary>
/// <param name="m">The FinanceChart object to add the line to.</param>
/// <param name="avgType">The moving average type (SMA/EMA/TMA/WMA).</param>
/// <param name="avgPeriod">The moving average period.</param>
/// <param name="color">The color of the line.</param>
    protected LineLayer addMovingAvg(FinanceChart m, String avgType, int avgPeriod, int color) {
        if (avgPeriod > 1) {
            if (null != avgType) {
                switch (avgType) {
                    case "SMA":
                        return m.addSimpleMovingAvg(avgPeriod, color);
                    case "EMA":
                        return m.addExpMovingAvg(avgPeriod, color);
                    case "TMA":
                        return m.addTriMovingAvg(avgPeriod, color);
                    case "WMA":
                        return m.addWeightedMovingAvg(avgPeriod, color);
                    default:
                        break;
                }
            }
        }
        return null;
    }

    /// <summary>
    /// Add an indicator chart to the FinanceChart object. In this demo example, the indicator
    /// parameters (such as the period used to compute RSI, colors of the lines, etc.) are hard
    /// coded to commonly used values. You are welcome to design a more complex user interface 
    /// to allow users to set the parameters.
    /// </summary>
    /// <param name="m">The FinanceChart object to add the line to.</param>
    /// <param name="indicator">The selected indicator.</param>
    /// <param name="height">Height of the chart in pixels</param>
    /// <returns>The XYChart object representing indicator chart.</returns>
    protected ChartDirector.XYChart addIndicator(FinanceChart m, String indicator, int height) {
        if (null != indicator) {
            switch (indicator) {
                case "RSI":
                    return m.addRSI(height, 14, 0x800080, 20, 0xff6666, 0x6666ff);
                case "StochRSI":
                    return m.addStochRSI(height, 14, 0x800080, 30, 0xff6666, 0x6666ff);
                case "MACD":
                    return m.addMACD(height, 26, 12, 9, 0xff, 0xff00ff, 0x8000);
                case "FStoch":
                    return m.addFastStochastic(height, 14, 3, 0x6060, 0x606000);
                case "SStoch":
                    return m.addSlowStochastic(height, 14, 3, 0x6060, 0x606000);
                case "ATR":
                    return m.addATR(height, 14, 0x808080, 0xff);
                case "ADX":
                    return m.addADX(height, 14, 0x8000, 0x800000, 0x80);
                case "DCW":
                    return m.addDonchianWidth(height, 20, 0xff);
                case "BBW":
                    return m.addBollingerWidth(height, 20, 2, 0xff);
                case "DPO":
                    return m.addDPO(height, 20, 0xff);
                case "PVT":
                    return m.addPVT(height, 0xff);
                case "Momentum":
                    return m.addMomentum(height, 12, 0xff);
                case "Performance":
                    return m.addPerformance(height, 0xff);
                case "ROC":
                    return m.addROC(height, 12, 0xff);
                case "OBV":
                    return m.addOBV(height, 0xff);
                case "AccDist":
                    return m.addAccDist(height, 0xff);
                case "CLV":
                    return m.addCLV(height, 0xff);
                case "WilliamR":
                    return m.addWilliamR(height, 14, 0x800080, 30, 0xff6666, 0x6666ff);
                case "Aroon":
                    return m.addAroon(height, 14, 0x339933, 0x333399);
                case "AroonOsc":
                    return m.addAroonOsc(height, 14, 0xff);
                case "CCI":
                    return m.addCCI(height, 20, 0x800080, 100, 0xff6666, 0x6666ff);
                case "EMV":
                    return m.addEaseOfMovement(height, 9, 0x6060, 0x606000);
                case "MDX":
                    return m.addMassIndex(height, 0x800080, 0xff6666, 0x6666ff);
                case "CVolatility":
                    return m.addChaikinVolatility(height, 10, 10, 0xff);
                case "COscillator":
                    return m.addChaikinOscillator(height, 0xff);
                case "CMF":
                    return m.addChaikinMoneyFlow(height, 21, 0x8000);
                case "NVI":
                    return m.addNVI(height, 255, 0xff, 0x883333);
                case "PVI":
                    return m.addPVI(height, 255, 0xff, 0x883333);
                case "MFI":
                    return m.addMFI(height, 14, 0x800080, 30, 0xff6666, 0x6666ff);
                case "PVO":
                    return m.addPVO(height, 26, 12, 9, 0xff, 0xff00ff, 0x8000);
                case "PPO":
                    return m.addPPO(height, 26, 12, 9, 0xff, 0xff00ff, 0x8000);
                case "UO":
                    return m.addUltimateOscillator(height, 7, 14, 28, 0x800080, 20, 0xff6666, 0x6666ff);
                case "Vol":
                    return m.addVolIndicator(height, 0x99ff99, 0xff9999, 0xc0c0c0);
                case "TRIX":
                    return m.addTRIX(height, 12, 0xff);
                default:
                    break;
            }
        }
        return null;
    }

    /// <summary>
    /// Creates a dummy chart to show an error message.
    /// </summary>
    /// <param name="viewer">The WinChartViewer to display the error message.</param>
    /// <param name="msg">The error message</param>
    protected void errMsg(ChartViewer viewer, String msg) {
        MultiChart m = new MultiChart(400, 200);
        m.addTitle2(Chart.Center, msg, "Arial", 10).setMaxWidth(m.getWidth());
        viewer.setImage(m.makeImage());
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Date getMarkStartDate() {
        return markStartDate;
    }

    public void setMarkStartDate(Date markStartDate) {
        this.markStartDate = markStartDate;
    }

    public Date getMarkEndDate() {
        return markEndDate;
    }

    public void setMarkEndDate(Date markEndDate) {
        this.markEndDate = markEndDate;
    }

    public Date getLastStartDate() {
        if (markStartDate != null) {
//            Logs.e(stockCode + " Last Start: " + Utils.DATA_OF_DAY_SDF.format(markStartDate));
        }

        return markStartDate;

    }

    public Date getLastEndDate() {
        if (markEndDate != null) {
//            Logs.e(stockCode + " Last End: " + Utils.DATA_OF_DAY_SDF.format(markEndDate));
        }

        return markEndDate;

    }

    public void setCmBxSourceStockCodeItems(ObservableList<String> codeList) {
        this.cmBxSourceStockCode.setItems(codeList);
    }

    public void setCmBxSourceStockSelected(int index) {
        this.cmBxSourceStockCode.getSelectionModel().select(index);
    }

    public void setDefaultWidth(int defaultWidth) {
        this.defaultWidth = defaultWidth;
    }

    public void setDefaultMainHeight(int defaultHeight) {
        this.defaultmainHeight = defaultHeight;
    }

    public void setDefaultIndicatorHeight(int defaultIndicatorHeight) {
        this.defaultIndicatorHeight = defaultIndicatorHeight;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public Date getDisplayStartDate() {
        return displayStartDate;
    }

    public void setDisplayStartDate(Date displayStartDate) {
        this.displayStartDate = displayStartDate;
    }

    public Date getDisplayEndDate() {
        return displayEndDate;
    }

    public void setDisplayEndDate(Date displayEndDate) {
        this.displayEndDate = displayEndDate;
    }

}
