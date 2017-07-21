/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import tools.utilities.Logs;

/**
 * FXML Controller class
 *
 * @author JianGe
 */
public class SearchResultsPlotsController implements Initializable {

    //Store tabs when windows is close and restore tabs when it come up 
    private static final ArrayList<AnalyzePlotTabController> savedTabList = new ArrayList<>();
    @FXML
    private TabPane SearchResultsTabPane;
    @FXML
    private MenuItem menuItemAddTab;
    @FXML
    private MenuItem menuItemOtherTab;
    @FXML
    private MenuItem menuItemRemoveAllTab;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //Restore
        savedTabList.forEach((tabController) -> {
            SearchResultsTabPane.getTabs().add(tabController);
        });

    }

    @FXML
    private void onMenuItemAddTab(ActionEvent event) {
        AnalyzePlotTabController analyzePlotTabController = new AnalyzePlotTabController("Test");
        SearchResultsTabPane.getTabs().add(analyzePlotTabController);
        Logs.e("Add tab: " + SearchResultsTabPane.getTabs().get(0).getText());
        SearchResultsTabPane.getSelectionModel().select(getLastAddTabIndex());
    }

    @FXML
    private void onMenuItemRemoveOtherTab(ActionEvent event) {
        AnalyzePlotTabController currentTabController = (AnalyzePlotTabController) SearchResultsTabPane.getSelectionModel().getSelectedItem();
        SearchResultsTabPane.getTabs().clear();
        SearchResultsTabPane.getTabs().add(currentTabController);

    }

    @FXML
    private void onMenuItemRemoveAllTab(ActionEvent event) {
        SearchResultsTabPane.getTabs().clear();
    }

    private int getLastAddTabIndex() {

        int numberOfTabs = SearchResultsTabPane.getTabs().size();
        int selectedIndex = SearchResultsTabPane.getSelectionModel().getSelectedIndex();

        selectedIndex = numberOfTabs - 1;
        return selectedIndex;

    }

    public void updateSourceChartPlots(final String code, final String name, final Date startDate, final int duration, final Date endDate, boolean isStockChange) {
        AnalyzePlotTabController currentTab = (AnalyzePlotTabController) SearchResultsTabPane.getTabs().get(getTabIndexByTabName(code));

        currentTab.updateSourceChart(code, name, startDate, duration, endDate, isStockChange);
    }

    public void updateSearchedChartPlots(final String code, final String name, final Date startDate, final int duration, final Date endDate, boolean isStockChange) {
        AnalyzePlotTabController currentTab = (AnalyzePlotTabController) SearchResultsTabPane.getTabs().get(getTabIndexByTabName(code));

        currentTab.updateMatchedChart(code, name, startDate, duration, endDate, isStockChange);

    }

    public void newPlotsTab(final String code, final String name, final Date startDate, final int duration, final Date endDate, boolean isStockChange) {
        int tabId = getTabIndexByTabName(code);

        if (tabId != -1) {
            //have this tab
            updateSourceChartPlots(code, name, startDate, duration, endDate, isStockChange);
            SearchResultsTabPane.getSelectionModel().select(tabId);
        } else {
            //don't have this tab
            addTab(code);
            updateSourceChartPlots(code, name, startDate, duration, endDate, isStockChange);
            SearchResultsTabPane.getSelectionModel().select(getLastAddTabIndex());
//            SearchResultsTabPane.setS;
        }
    }

    /**
     * Get the Tab's index (by tabName)
     *
     * @param tabName
     * @return the Index in the tab list, if does not have this tab return -1
     * else return the index
     */
    private int getTabIndexByTabName(String tabName) {
        int tabIndex = -1;
        for (int idx = 0; idx < SearchResultsTabPane.getTabs().size(); idx++) {
            if (SearchResultsTabPane.getTabs().get(idx).getText().equals(tabName)) {
                tabIndex = idx;
                break;
            }

        }

        return tabIndex;
    }

    public AnalyzePlotTabController getTabByTabName(String tabName) {
        AnalyzePlotTabController analyzePlotTabController = null;
        // If it is in the tab list, return it;
        for (int idx = 0; idx < SearchResultsTabPane.getTabs().size(); idx++) {
            if (SearchResultsTabPane.getTabs().get(idx).getText().equals(tabName)) {
                analyzePlotTabController = (AnalyzePlotTabController) SearchResultsTabPane.getTabs().get(idx);
//                Logs.e("have this tab");
                return analyzePlotTabController;
            }

        }

        // Create a new one and add it to Tabpane, If it is not in the tab list
        return addTab(tabName);
    }

    private AnalyzePlotTabController addTab(String name) {
        AnalyzePlotTabController analyzePlotTabController = new AnalyzePlotTabController(name);
        SearchResultsTabPane.getTabs().add(analyzePlotTabController);

        return analyzePlotTabController;
    }

    public void closeTab() {
        savedTabList.clear();
        SearchResultsTabPane.getTabs().forEach((object) -> {
            savedTabList.add((AnalyzePlotTabController) object);
        });
    }

}
