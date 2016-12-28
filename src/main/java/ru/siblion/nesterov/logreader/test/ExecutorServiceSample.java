package ru.siblion.nesterov.logreader.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by alexander on 28.12.2016.
 */
public class ExecutorServiceSample {
    public static void main(String[] args) {
        ExecutorService es1 = Executors.newFixedThreadPool(10);

        List<Future<String>> tasks = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            tasks.add(es1.submit(new CallableSample(i)));
        }

        if(!tasks.get(19).isDone()) {
            for (Future<String> task : tasks) {
                if (task.isDone()) {
                    try {
                        System.out.println(task.get() + "IS DONE ");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        es1.shutdown();
    }
}
