
import tools.utilities.Logs;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author JianGe
 */
public class Test {

    private int aa = 0;

    private int aasdf = 11231;
    public float cc = 1.0f;

    public static void main(String[] args) {
        System.out.println("Test.main()" + "aa is : ");
    }

    public void speack() {
        System.out.println("Test.speack()" + aasdf);
    }
    
    public void say() {
        Logs.e(aa);
    }
    
    @Override
    public String toString() {
        return getClass().getName();
    }
}
