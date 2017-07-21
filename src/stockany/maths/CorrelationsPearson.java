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
 * @author JianGe
 */
public class CorrelationsPearson {

    float[] x;
    float[] y;
    int winLength = 30;  //The length of move windows, default is 30 !
    float[] correlations;  //Correlation curve
    float singleCorrelation;   //Correlation of two curves
    float maxCorrelation = 0.012345f;
    int maxPosition = 0;

    public CorrelationsPearson(float[] x, float[] y) {
        this.x = x;
        this.y = y;
    }

    public CorrelationsPearson(ArrayList<String> xList, ArrayList<String> yList) {

        this.x = new float[xList.size()];
        this.y = new float[yList.size()];

        for (int i = 0; i < xList.size(); i++) {
            this.x[i] = Float.parseFloat(xList.get(i));
        }
        for (int i = 0; i < yList.size(); i++) {
            this.y[i] = Float.parseFloat(yList.get(i));
        }

    }

    public CorrelationsPearson(float[] x, float[] y, int winlen) {
        this(x, y);
        this.winLength = winlen;
    }

    public CorrelationsPearson(ArrayList<String> xList, ArrayList<String> yList, int winlen) {
        this(xList, yList);
        this.winLength = winlen;
    }

    public float[] getX() {
        return x;
    }

    public void setX(float[] x) {
        this.x = x;
    }

    public float[] getY() {
        return y;
    }

    public void setY(float[] y) {
        this.y = y;
    }

    public float getMaxCorrelation() {
        return maxCorrelation < 0 ? 0.012345f : maxCorrelation;
    }

    public int getMaxPosition() {
        return maxPosition;
    }

    public float[] getCorrelations() {
        return correlations;
    }

    public float getSingleCorrelation() {
        return singleCorrelation;
    }

    public void runSingleCorrelation() {
        float xMean = ArrayMath.meanVal(x);
        float yMean = ArrayMath.meanVal(y);
        float upper = 0.0f;
        float lower1 = 0.0f;
        float lower2 = 0.0f;
        for (int i = 0; i < x.length; i++) {
            upper += (x[i] - xMean) * (y[i] - yMean);
            lower1 += (x[i] - xMean) * (x[i] - xMean);
            lower2 += (y[i] - yMean) * (y[i] - yMean);
        }
        singleCorrelation = (float) (upper / Math.sqrt(lower1 * lower2));
    }

    /**
     * Moving window correlation calculation normalized correlation
     */
    public void runCorrelationsNormalized() {
        //determine x and y length
        if (x.length > y.length || x.length == 0) {
//            Logs.e("Error, x length is longer than y length");
            return;
        }

//        if (y.length < winLength) {
//            Logs.e("Error, window length is longer than y length");
//            return;
//        }
        correlations = new float[y.length - x.length + 1];

        int length = x.length;
        float xMean = ArrayMath.meanVal(x);

        for (int idx = 0; idx < y.length - x.length + 1; idx++) {
            float[] yUsing = new float[length];

            for (int i = 0; i < length; i++) {
                yUsing[i] = y[idx + i];
            }

            float yMean = ArrayMath.meanVal(yUsing);
            float upper = 0.0f;
            float lower1 = 0.0f;
            float lower2 = 0.0f;

            for (int i = 0; i < length; i++) {
                upper += (x[i] - xMean) * (yUsing[i] - yMean);
                lower1 += (x[i] - xMean) * (x[i] - xMean);
                lower2 += (yUsing[i] - yMean) * (yUsing[i] - yMean);
            }

            singleCorrelation = (float) (upper / Math.sqrt(lower1 * lower2));
            correlations[idx] = singleCorrelation;
        }
    }

    /**
     * Moving window correlation calculation normalized correlation
     */
    public void runMaxNormalizedCorrelation() {
        //determine x and y length
        if (x == null || y == null || x.length > y.length || x.length == 0) {
//            Logs.e("Error, x length is longer than y length");
            return;
        }

//        if (y.length < winLength) {
//            Logs.e("Error, window length is longer than y length");
//            return;
//        }
        int length = x.length;
        float xMean = ArrayMath.meanVal(x);

        for (int idx = 0; idx < y.length - x.length + 1; idx++) {
            float[] yUsing = new float[length];

            for (int i = 0; i < length; i++) {
                yUsing[i] = y[idx + i];
            }

            float yMean = ArrayMath.meanVal(yUsing);
            float upper = 0.0f;
            float lower1 = 0.0f;
            float lower2 = 0.0f;

            for (int i = 0; i < length; i++) {
                upper += (x[i] - xMean) * (yUsing[i] - yMean);
                lower1 += (x[i] - xMean) * (x[i] - xMean);
                lower2 += (yUsing[i] - yMean) * (yUsing[i] - yMean);
            }

            float correlation = (float) (upper / Math.sqrt(lower1 * lower2));
            if (maxCorrelation <= correlation) {
                maxCorrelation = correlation;
                maxPosition = idx;
            }

        }
    }

    /**
     * Moving window correlation calculation normalized correlation
     */
    public void runMaxCorrelation() {
        //determine x and y length
        if (x.length > y.length || x.length == 0) {
//            Logs.e("Error, x length is longer than y length");
            return;
        }

//        if (y.length < winLength) {
//            Logs.e("Error, window length is longer than y length");
//            return;
//        }
        int length = x.length;

        for (int idx = 0; idx < y.length - x.length + 1; idx++) {
            float[] yUsing = new float[length];

            for (int i = 0; i < length; i++) {
                yUsing[i] = y[idx + i];
            }

            float upper = 0.0f;
            float lower1 = 0.0f;
            float lower2 = 0.0f;

            for (int i = 0; i < length; i++) {
                upper += (x[i]) * (yUsing[i]);
                lower1 += (x[i]) * (x[i]);
                lower2 += (yUsing[i]) * (yUsing[i]);
            }

            float correlation = (float) (upper / Math.sqrt(lower1 * lower2));
            if (maxCorrelation <= correlation) {
                maxCorrelation = correlation;
                maxPosition = idx;
            }

        }
    }

    public void runCorrelations() {
        //determine x and y length
        if (x.length > y.length) {
            Logs.e("Error, x length is longer than y length");
            return;
        }

//        if (y.length < winLength) {
//            Logs.e("Error, window length is longer than y length");
//            return;
//        }
        correlations = new float[y.length - x.length + 1];

        int length = x.length;

        for (int idx = 0; idx < y.length - x.length + 1; idx++) {
            float[] yUsing = new float[length];

            for (int i = 0; i < length; i++) {
                yUsing[i] = y[idx + i];
            }

            float upper = 0.0f;
            float lower1 = 0.0f;
            float lower2 = 0.0f;

            for (int i = 0; i < length; i++) {
                upper += (x[i]) * (yUsing[i]);
                lower1 += (x[i]) * (x[i]);
                lower2 += (yUsing[i]) * (yUsing[i]);
            }

            singleCorrelation = (float) (upper / Math.sqrt(lower1 * lower2));
            correlations[idx] = singleCorrelation;
        }
    }
}
