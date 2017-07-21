/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.tools;

import stockany.tools.Common;
import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import stockany.tools.Common.Response;

/**
 *
 * @author JianGe
 */
public class AlertDialog implements Runnable {

    private DialogType Type;
    private Modality modality;
    private String Msg;

    public AlertDialog() {

    }

    public AlertDialog(DialogType type, Modality modality, String message) {
        this.Type = type;
        this.modality = modality;
        this.Msg = message;

    }

    private static Response buttonSelected = Response.CANCEL;

    @Override
    public void run() {
        if (modality == null) {
            new AlertDialog.Dialog(Type, Modality.APPLICATION_MODAL, new Stage(), Msg).showDialog();
        } else {
            new AlertDialog.Dialog(Type, modality, new Stage(), Msg).showDialog();
        }

    }

    static class Dialog extends Stage {

        public Dialog(DialogType title, Modality modality, Stage owner, String message) {

            setTitle(title + "");

            initStyle(StageStyle.UTILITY);
            initModality(modality);
            initOwner(owner);
            setResizable(true);

            HBox outBox = new HBox();

            Scene scene = new Scene(outBox, 350, 100);

            VBox rightBox = new VBox();

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);

            Button yesButton = new Button("OK");
            yesButton.setPrefSize(70, 30);
            yesButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    close();
                    buttonSelected = Common.Response.YES;
                }
            });

            Button noButton = new Button("No");
            noButton.setPrefSize(70, 30);
            noButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    close();
                    buttonSelected = Common.Response.NO;
                }
            });

            Button cancelButton = new Button("Cancel");
            cancelButton.setPrefSize(70, 30);
            cancelButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    close();
                    buttonSelected = Common.Response.CANCEL;
                }
            });

            String imageFilePath;

            switch (title) {
                case Confirm:
                    imageFilePath = ".\\res\\sign-question-icon.png";
                    gridPane.add(yesButton, 1, 0);
                    gridPane.add(cancelButton, 2, 0);
                    gridPane.add(noButton, 3, 0);
                    gridPane.setAlignment(Pos.BASELINE_RIGHT);
                    break;
                case Warn:
                    imageFilePath = ".\\res\\sign-warning-icon.png";
                    gridPane.add(yesButton, 1, 0);
                    gridPane.setAlignment(Pos.BASELINE_RIGHT);
                    break;
                case Erro:
                    imageFilePath = ".\\res\\sign-error-icon.png";
                    gridPane.add(yesButton, 1, 0);
                    gridPane.setAlignment(Pos.BASELINE_RIGHT);
                    break;
                case Info:
                    imageFilePath = ".\\res\\sign-info-icon.png";
                    gridPane.add(yesButton, 1, 0);
                    gridPane.setAlignment(Pos.BASELINE_RIGHT);
                    break;
                default:
                    gridPane.add(yesButton, 1, 0);
                    gridPane.setAlignment(Pos.BASELINE_RIGHT);
                    imageFilePath = ".\\res\\sign-info-icon.png";
                    break;

            }

            File imageFile = new File(imageFilePath);
            ImageView icon = new ImageView();

            try {
                icon.setImage(new Image(imageFile.toURI().toURL().toString(), true));
            } catch (MalformedURLException ex) {
                Logger.getLogger(AlertDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            icon.setFitHeight(60);
            icon.setFitWidth(60);

            Message msg = new Message(message);

            rightBox.getChildren().addAll(msg, gridPane);
            outBox.getChildren().addAll(icon, rightBox);

            setScene(scene);
        }

        public void showDialog() {
            toFront();
            sizeToScene();
            centerOnScreen();
            showAndWait();

        }
    }

    static class Message extends TextArea {

        public Message(String msg) {
            super(msg);
            setEditable(false);
            setWidth(250);
            setHeight(100);

        }
    }

    public static enum DialogType {
        Confirm,
        Warn,
        Erro,
        Info
    }

    public static Common.Response showConfirmDialog(Modality modality, Stage owner, String message) {

        new AlertDialog.Dialog(DialogType.Confirm, modality, owner, message).showDialog();

        return buttonSelected;
    }

    public static Common.Response showWarnDialog(Stage owner, String message) {

        new AlertDialog.Dialog(DialogType.Warn, Modality.APPLICATION_MODAL, owner, message).showDialog();

        return buttonSelected;
    }

    public static Common.Response showErrorDialog(Stage owner, String message) {

        new AlertDialog.Dialog(DialogType.Erro, Modality.APPLICATION_MODAL, owner, message).showDialog();

        return buttonSelected;
    }

    public static Common.Response showInfoDialog(Stage owner, String message) {

        new AlertDialog.Dialog(DialogType.Info, Modality.APPLICATION_MODAL, owner, message).showDialog();

        return buttonSelected;
    }

}
