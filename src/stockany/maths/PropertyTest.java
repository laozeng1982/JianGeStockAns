/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.maths;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

public class PropertyTest {

    public static void main(String[] args) {
        Properties prop = new Properties();
        try {
            //Read a.properties
            InputStream in = new BufferedInputStream(new FileInputStream(".\\a.properties"));
            prop.load(in);     //Load property
            Iterator<String> it = prop.stringPropertyNames().iterator();
            while (it.hasNext()) {
                String key = it.next();
                System.out.println(key + ":" + prop.getProperty(key));
            }
            in.close();

            ///Save to b.properties
            FileOutputStream oFile = new FileOutputStream("b.properties", false);//true means append
            prop.setProperty("fuck1", "deeply");
            prop.setProperty("fuck", "deeply");
//            prop.setProperty("phone", "10086");
            

            prop.store(oFile, "The New properties file");
            oFile.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
