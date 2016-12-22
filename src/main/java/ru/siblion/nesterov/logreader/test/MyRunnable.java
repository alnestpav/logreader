package ru.siblion.nesterov.logreader.test;

import ru.siblion.nesterov.logreader.type.Request;

/**
 * Created by alexander on 22.12.2016.
 */
public class MyRunnable implements Runnable {
    private Request request;
    public MyRunnable(Request request) {
        this.request = request;
    }

    public void run() {
        request.saveResultToFile();
    }
}
