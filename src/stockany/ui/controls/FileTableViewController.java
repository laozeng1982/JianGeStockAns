/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.ui.controls;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import stockany.datamodel.FileDetail;
import tools.files.FileHelper;
import tools.utilities.Logs;

/**
 * FXML Controller class
 *
 * @author JianGe
 */
public class FileTableViewController extends VBox implements Initializable {

    private String FilePath;
    private ObservableList<FileDetail> tablelist;

    @FXML
    private TextField txtF_FilePath;
    @FXML
    private TableView<FileDetail> fileTableView;
    @FXML
    private TableColumn<FileDetail, String> fileNameCol;
    @FXML
    private TableColumn<FileDetail, String> fileTypeCol;
    @FXML
    private TableColumn<FileDetail, String> fileSizeCol;
    @FXML
    private TableColumn<FileDetail, String> fileModifyCol;
    @FXML
    private MenuItem muItemDelete;

    public FileTableViewController(String filePath) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FileTableView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.FilePath = filePath;

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private Thread initFileListThread = new Thread(new Runnable() {
        @Override
        public void run() {
            File directory = new File(FilePath);
            if (directory.exists()) {
                if (directory.isDirectory()) {
//                Logs.e(directory.toString());
                    for (File listFile : directory.listFiles()) {
                        tablelist.add(new FileDetail(listFile));
                    }
                } else {
                    File parent = directory.getParentFile();
//                Logs.e(parent.toString());
                    for (File listFile : parent.listFiles()) {
                        tablelist.add(new FileDetail(listFile));
                    }
                }

            } else {
                Logs.e(FilePath + "does not exist!");
            }
        }
    });

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tablelist = FXCollections.observableArrayList();

        refreshFolder();

        txtF_FilePath.setText(FilePath);

        fileTableView.setItems(tablelist);
        fileNameCol.setCellValueFactory(cellData -> cellData.getValue().fileNameProperty());
        fileTypeCol.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        fileSizeCol.setCellValueFactory(cellData -> cellData.getValue().sizeProperty());
        fileModifyCol.setCellValueFactory(cellData -> cellData.getValue().lastModProperty());

        fileNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        fileTypeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        fileSizeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        fileModifyCol.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    @FXML
    private void onMouseClickOnFile(MouseEvent event) {

        if (event.getClickCount() == 2) {
            // Method1: Using Runtime class
//            String cmd;
//            Runtime.getRuntime().exec(cmd);

            // Method2: Using Desktop class
            int selectedRowNumber = fileTableView.getSelectionModel().getSelectedIndex();
            String filename = fileNameCol.getCellData(selectedRowNumber);

            Desktop desk = Desktop.getDesktop();
            try {
                String fileAbsolutePath;
                if ((new File(FilePath)).isDirectory()) {
                    fileAbsolutePath = FilePath + "\\" + filename;
                } else {
                    fileAbsolutePath = FilePath;
                }
//                Logs.e("file abs path: " + fileAbsolutePath);

                File file = new File(fileAbsolutePath);

                //openFile
                desk.open(file);
            } catch (IOException e) {

            }
        }
    }

    @FXML
    private void onMenuDeleteFile(ActionEvent event) {
        int selectedRowNumber = fileTableView.getSelectionModel().getSelectedIndex();
        String filename = fileNameCol.getCellData(selectedRowNumber);

        try {
            FileHelper.deleteFolder(FilePath + "\\" + filename);
            refreshFolder();
        } catch (Exception e) {
        }
    }

    public void refreshFolder() {
        initFileListThread.start();
    }
}
