/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.datamodel;

import java.util.ArrayList;
import java.util.Map;
import tools.utilities.Logs;

/**
 *
 * @author JianGe
 */
public class CorrelationCurves extends ArrayList<String> {

    public double getMax() {
        double max = 0.0;
        for (int i = 0; i < this.size(); i++) {
            Logs.e(this.get(i));
            max = (max < Double.valueOf(this.get(i))) ? Double.valueOf(this.get(i)) : max;

        }
        Logs.e("Max is: " + max);
        return max;
    }
}
