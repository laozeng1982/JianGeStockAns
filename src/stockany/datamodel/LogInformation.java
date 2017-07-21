/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.datamodel;

import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import tools.utilities.StringUtils;

/**
 * Data model of Log informations
 *
 * @author JianGe
 */
public class LogInformation {

    public static class Type {

        public static final String INFO = "Info";
        public static final String WARN = "Warn";
        public static final String ERRO = "Erro";

    }

    public static class Category {

        public static final String CALC = "Calculation";
        public static final String ANALY = "Analyze";
        public static final String FILES = "Files";
        public static final String STOCK = "Stock";

    }

    private static int logId = 1;

    private SimpleStringProperty id;
    private SimpleStringProperty time;
    private SimpleStringProperty type;
    private SimpleStringProperty category;
    private SimpleStringProperty message;
    private static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(dateTimePattern);

    public LogInformation(String time, String type, String category, String msg) {
        this.id = new SimpleStringProperty(String.valueOf(logId));
        this.time = new SimpleStringProperty(time);
        this.type = new SimpleStringProperty(type);
        this.category = new SimpleStringProperty(category);
        this.message = new SimpleStringProperty(msg);
        logId++;
//        Logs.e("LogID is: " + this.ID.get());
    }

    public static LogInformation createLog(String type, String category, String msg) {
        return new LogInformation(sdf.format(new Date()), type, category, msg);
        
    }

    public static LogInformation createInfoLog(String category, String msg) {
        return createLog(LogInformation.Type.INFO, category, msg);
    }

    public static LogInformation createWarnLog(String category, String msg) {
        return createLog(LogInformation.Type.WARN, category, msg);
    }

    public static LogInformation createErroLog(String category, String msg) {
        return createLog(LogInformation.Type.ERRO, category, msg);
    }

    public SimpleStringProperty idProperty() {
        return id;
    }
    public String getId() {
        return id.get();
    }

    public void setId(SimpleStringProperty ID) {
        this.id = ID;
    }

    public void setId(String ID) {
        this.id.set(ID);
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }
    public String getTime() {
        return time.get();
    }

    public void setTime(SimpleStringProperty time) {
        this.time = time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }
    
    public String getType() {
        return type.get();
    }

    public void setType(SimpleStringProperty type) {
        this.type = type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }
    
    public String getCategory() {
        return this.category.get();
    }

    public void setCategory(SimpleStringProperty Category) {
        this.category = Category;
    }

    public void setCategory(String Category) {
        this.category.set(Category);
    }

    public SimpleStringProperty messageProperty() {
        return message;
    }
    public String getMessage() {
        return message.get();
    }

    public void setMessage(SimpleStringProperty info) {
        this.message = info;
    }

    public void setMessage(String info) {
        this.message.set(info);
    }

    @Override
    public String toString() {
        return StringUtils.formatLeftS(this.time.get(), 25)
                + StringUtils.formatLeftS(this.type.get(), 10)
                + StringUtils.formatLeftS(this.category.get(), 20)
                + StringUtils.formatLeftS(this.message.get(), 200);
    }

}
