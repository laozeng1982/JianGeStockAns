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
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import tools.utilities.Logs;

public class ButtonForTableCell {

   Button button = new Button("OK");

    public ButtonForTableCell() {
        button.setOnAction((event) -> {
            Logs.e("fuck");
        });
    }

    public ObservableValue<Button> getButton() {
        return new ObservableValue<Button>() {
            @Override
            public void addListener(ChangeListener<? super Button> listener) {
            }

            @Override
            public void removeListener(ChangeListener<? super Button> listener) {

            }

            @Override
            public Button getValue() {
                return button;
            }

            @Override
            public void addListener(InvalidationListener listener) {
            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        };
    }



}
