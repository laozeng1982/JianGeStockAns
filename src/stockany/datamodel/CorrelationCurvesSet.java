/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.datamodel;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author JianGe
 */
public class CorrelationCurvesSet {
    Map<String, ArrayList<String>> correlation;

    public ArrayList<String> getCorrelation(String corrType) {
        return correlation.get(corrType);
    }
}
