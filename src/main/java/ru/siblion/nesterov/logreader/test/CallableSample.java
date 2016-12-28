package ru.siblion.nesterov.logreader.test;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Created by alexander on 28.12.2016.
 */
public class CallableSample implements Callable<String> {
    private int num;
    CallableSample(int num) {
        this.num = num;
    }
    public String call() throws Exception {
        if(false) {
            throw new IOException("error during task processing");
        }
        System.out.println("task " + num + " is processing");
        Random random = new Random();
        Thread.sleep(random.nextInt(10000));
        return "result num=" + num;
    }
}
