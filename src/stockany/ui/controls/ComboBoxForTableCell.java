/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui.controls;

/**
 *
 * @author JianGe
 */
import java.util.ArrayList;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;

public class ComboBoxForTableCell {

    ComboBox<String> cmBox = new ComboBox<>();

    public ComboBoxForTableCell() {

        ArrayList<String> list = new ArrayList();

        list.add("MACD 60分钟金叉");   // MACD 60 minutes Gold Cross
        list.add("MACD日线零下金叉");   // MACD Daily Gold Cross upper zero
        list.add("MACD日线零上金叉");   // MACD Daily Gold Cross down zero
        list.add("60均线横盘"); // 60 days ma
        list.add("60 分钟底部共振");    // 60 minutes 
        list.add("日线共振");   // Daily 
        list.add("急跌抄底");   // Rapid go down
        cmBox.setEditable(true);
        cmBox.setItems(FXCollections.observableArrayList(list));
//        cmBox.getSelectionModel().select(0);
    }

    public ObservableValue<ComboBox> getComboBox() {
        return new ObservableValue<ComboBox>() {
            @Override
            public void addListener(ChangeListener<? super ComboBox> listener) {
            }

            @Override
            public void removeListener(ChangeListener<? super ComboBox> listener) {

            }

            @Override
            public ComboBox getValue() {
                return cmBox;
            }

            @Override
            public void addListener(InvalidationListener listener) {
            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        };
    }

    public String getSelectedItem() {
        return cmBox.getSelectionModel().getSelectedItem();
    }

    public void setSelectedItem(String item) {
         cmBox.getSelectionModel().select(item);
    }
}
