/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.maths;

import java.util.ArrayList;
import tools.utilities.Logs;

/**
 *
 * @author HP
 */
public class CorrelationsTest {

    public static void main(String[] args) {
        ArrayList<String> xList = new ArrayList<>();
        ArrayList<String> yList = new ArrayList<>();
        
        xList.add(String.valueOf(2));
        xList.add(String.valueOf(4));
        xList.add(String.valueOf(7));
        xList.add(String.valueOf(5));
        xList.add(String.valueOf(6));
        xList.add(String.valueOf(7));
        xList.add(String.valueOf(8));

        yList.add(String.valueOf(2));
        yList.add(String.valueOf(4));
        yList.add(String.valueOf(7));
        yList.add(String.valueOf(5));
        yList.add(String.valueOf(6));
        yList.add(String.valueOf(7));
        yList.add(String.valueOf(8));
        
        yList.add(String.valueOf(6));
        yList.add(String.valueOf(8));
        yList.add(String.valueOf(4));
        yList.add(String.valueOf(5));
        yList.add(String.valueOf(2));
        yList.add(String.valueOf(6));
        yList.add(String.valueOf(1));

        yList.add(String.valueOf(5));
        yList.add(String.valueOf(2));
        yList.add(String.valueOf(6));
        yList.add(String.valueOf(1));
        yList.add(String.valueOf(5));
        yList.add(String.valueOf(2));
        yList.add(String.valueOf(6));
        yList.add(String.valueOf(1));

//        Correlations correlations = new Correlations(xList, yList);
        CorrelationsPearson correlations = new CorrelationsPearson(xList, yList);

//        correlations.runSingleCorrelation();
        correlations.runCorrelationsNormalized();

//        Logs.e("Correlation of x and y is: " + correlations.getSingleCorrelation());
        for (int i = 0; i < correlations.getCorrelations().length; i++) {
            Logs.e(i + " Correlation of x and y is: " + correlations.getCorrelations()[i]);

        }

        correlations.runCorrelations();
        for (int i = 0; i < correlations.getCorrelations().length; i++) {
            Logs.e("No Mean " + i + " Correlation of x and y is: " + correlations.getCorrelations()[i]);

        }
    }
}
