package com.example.java11lab.task02;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PoolExample {

    public static void main(String[] args) throws InterruptedException {

        // создаем пул для выполнения наших задач
        //   максимальное количество созданных задач - 3
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                // не изменяйте эти параметры
                3, 3, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(3) 
        );

        // сколько задач выполнилось
        AtomicInteger count = new AtomicInteger(0);

        // сколько задач выполняется
        AtomicInteger inProgress = new AtomicInteger(0);

        // отправляем задачи на выполнение
        for (int i = 0; i < 30; i++) {
            final int number = i;
            Thread.sleep(10); 
            
            while (executor.getQueue().remainingCapacity() == 0) {
                Thread.sleep(10);
            }

            System.out.println("Creating #" + number);

            executor.submit(() -> {
                int working = inProgress.incrementAndGet();

                System.out.println("Start #" + number + ", in progress: " + working);

                try {
                    // тут какая-то полезная работа
                    Thread.sleep(Math.round(1000 + Math.random() * 2000));
                } catch (InterruptedException e) {
                    // ignore
                }

                working = inProgress.decrementAndGet();
                System.out.println("End #" + number + ", in progress: " + working + ", done tasks: " + count.incrementAndGet());

                return null;
            });
        }

        executor.shutdown();
    }
}
