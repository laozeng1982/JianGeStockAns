/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.datamodel;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Properties;
import tools.utilities.Logs;

public class PropertyHelper {
    
    private static String propertyPath;
    private static Properties prop = new Properties();
    
    public PropertyHelper(String path) {
        PropertyHelper.propertyPath = path;
        initProperty();
    }
    
    public void initProperty() {
        try {
            //Read a.properties
            InputStream inFile = new BufferedInputStream(new FileInputStream(this.propertyPath));
            prop.load(inFile);     //Load property
            Iterator<String> it = prop.stringPropertyNames().iterator();
            while (it.hasNext()) {
                String key = it.next();
//                Logs.e(key + " is : " + prop.getProperty(key));
            }
            inFile.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the value of the key
     *
     * @param key
     * @return String
     */
    public static String getKeyValue(String key) {
        return prop.getProperty(key);
    }

    /**
     * Read the value of the key
     *
     * @param filePath
     * @param key
     * @return
     */
    public static String readValue(String filePath, String key) {
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(
                    filePath));
            props.load(in);
            String value = props.getProperty(key);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Update or Insert a pair of key and value If the key exists, update it;
     * otherwise insert a pair
     *
     * @param keyname
     * @param keyvalue
     */
    public static void writeProperties(String keyname, String keyvalue) {
        try {
            OutputStream fos = new FileOutputStream(propertyPath);
            prop.setProperty(keyname, keyvalue);
            prop.store(fos, "Update '" + keyname + "' value");
        } catch (IOException e) {
            System.err.println("Wrong");
        }
    }

    /**
     * Update or Insert a pair of key and value If the key exists, update it;
     * otherwise insert a pair Then save to file!
     *
     * @param keyname
     * @param keyvalue
     */
    public static void saveProperty(String keyname, String keyvalue) {
        try {
            ///Save to file
//            Logs.e("Path is: " + propertyPath);
            prop.load(new FileInputStream(propertyPath));
            FileOutputStream oFile = new FileOutputStream(propertyPath, false);//true means append
            prop.setProperty(keyname, keyvalue);
            prop.store(oFile, "Update '" + keyname + "' value");
            oFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
