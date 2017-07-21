/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.datamodel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author JianGe
 */
public class FileDetail {

    private SimpleStringProperty FileName;
    private SimpleStringProperty Type;
    private SimpleStringProperty Size;
    private SimpleStringProperty LastModified;
    private final boolean isFolder;
    private final boolean exists;
    private SimpleDateFormat fmt;

    public FileDetail(File file) {
        isFolder = file.isDirectory();
        exists = file.exists();
        if (exists) {
            this.FileName = new SimpleStringProperty(file.getName());
            fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = new Date(file.lastModified());
            this.LastModified = new SimpleStringProperty(fmt.format(date));

            if (isFolder) {
                this.Type = new SimpleStringProperty("Folder");
                this.Size = new SimpleStringProperty(getSizeString(file));
            } else {
                String fileName = file.getName();
                this.Type = new SimpleStringProperty(fileName.substring(fileName.lastIndexOf(".") + 1));
                this.Size = new SimpleStringProperty(getSizeString(file));
            }
        }
    }

    private String getSizeString(File file) {
        String size = null;
        final long length = getTotalSizeOfFilesInDir(file);

        final long KB = 1024;

        if (length < KB * KB) {
            size = length / KB + " KB";
        } else {
            size = length / (KB * KB) + " MB";
        }
        return size;
    }

    private long getTotalSizeOfFilesInDir(final File file) {
        if (file.isFile()) {
            return file.length();
        }
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null) {
            for (final File child : children) {
                total += getTotalSizeOfFilesInDir(child);
            }
        }
        return total;
    }

    public SimpleStringProperty fileNameProperty() {
        return FileName;
    }

    public String getFileName() {
        return FileName.get();
    }

    public void setFileName(String fileName) {
        FileName = new SimpleStringProperty(fileName);
    }

    public SimpleStringProperty typeProperty() {
        return Type;
    }

    public String getType() {
        return Type.get();
    }

    public void setType(String type) {
        this.Type = new SimpleStringProperty(type);
    }

    public String Size() {
        return Size.get();
    }

    public SimpleStringProperty sizeProperty() {
        return Size;
    }

    public void setSize(SimpleStringProperty Size) {
        this.Size = Size;
    }

    public SimpleStringProperty lastModProperty() {
        return LastModified;
    }

    public String getLastModified() {
        return LastModified.get();
    }

    public void setLastModified(String lastModified) {
        LastModified = new SimpleStringProperty(lastModified);
    }

}
