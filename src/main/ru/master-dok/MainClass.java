package ru.geekbrains;

import ru.geekbrains.stage.Road;
import ru.geekbrains.stage.Tunnel;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/*
 1. Все участники должны стартовать одновременно, несмотря на разное время  подготовки.
 2. В тоннель не может одновременно заехать больше половины участников (условность).
    Попробуйте все это синхронизировать.
    Когда все завершат гонку, нужно выдать объявление об окончании.
    Можно корректировать классы (в том числе конструктор машин)
    и добавлять объекты классов из пакета util.concurrent.
*/

public class MainClass {

    public static final int CARS_COUNT = 4;
    private static ExecutorService executorService = Executors.newFixedThreadPool(CARS_COUNT);
    public static final AtomicInteger finishCount = new AtomicInteger(0);

    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");

        CyclicBarrier cb = new CyclicBarrier(CARS_COUNT + 1,
                ()-> System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!"));
        CountDownLatch cdl = new CountDownLatch(CARS_COUNT);

        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), cb, cdl, finishCount);
        }

        for (int i = 0; i < cars.length; i++) {
            executorService.execute(cars[i]);
        }

        try {
            cb.await();
        } catch (InterruptedException | BrokenBarrierException ex) {
            ex.printStackTrace();
        }

        try {
            cdl.await();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
            executorService.shutdown();
        }
    }

}
