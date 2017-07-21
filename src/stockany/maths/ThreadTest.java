/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockany.maths;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

/**
 *
 * @author JianGe
 */
public class ThreadTest {

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            Runnable1 r = new Runnable1(i);
            Thread t = new Thread(r);
            t.start();

        }
    }
}

class Runnable1 implements Runnable {

    private final int thread_nmber;

    public Runnable1(int number) {
        this.thread_nmber = number;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("Thread-----:" + thread_nmber);
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            System.out.println("Local Time " + LocalDate.now().toString());
        }
    }
}
