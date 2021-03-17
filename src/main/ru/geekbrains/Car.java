package ru.geekbrains;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Car implements Runnable {
    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;
    }
/*
    private static boolean isWinner = false;
    private static Lock lock = new ReentrantLock();
*/
    private Race race;
    private int speed;
    private CyclicBarrier cb;
    private CountDownLatch cdl;
    private AtomicInteger finishCount;
    private String name;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }

    protected Car(Race race, int speed, CyclicBarrier cb, CountDownLatch cdl, AtomicInteger finishCount) {
        this.race = race;
        this.speed = speed;
        this.cb = cb;
        this.cdl = cdl;
        this.finishCount = finishCount;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            cb.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        cdl.countDown();
        int finishPlace = finishCount.incrementAndGet();
        if (finishPlace == 1) {
            System.out.println(this.name + " ПОБЕДИЛ В ГОНКЕ!!!");
        } else {
            System.out.printf("%s занял %d место%n", this.name, finishPlace);
        }
/*
        if (isWinner())
            System.out.println(this.name + " победил в гонке!");
*/

    }

/*
    private boolean isWinner() {
        if (!isWinner) {
            try {
                lock.lock();
                isWinner = true;
            } finally {
                lock.unlock();
            }
            return true;
        } else {
            return false;
        }
    }
*/

/* второй вариант реализации
    private synchronized boolean isWinner() {
        if (!isWinner) {
            isWinner = true;
            return true;
        } else {
            return false;
        }
    }
*/

}