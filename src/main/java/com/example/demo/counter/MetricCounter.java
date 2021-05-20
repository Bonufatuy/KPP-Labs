package com.example.demo.counter;

import com.example.demo.exceptions.InternalServerException;
import org.springframework.stereotype.Component;
import java.util.concurrent.Semaphore;


@Component
public class MetricCounter {

    private int counter = 0;
    Semaphore semaphore = new Semaphore(1);

    public int getCounter(){
        try {
            semaphore.acquire();
            return counter;
        } catch (InterruptedException e) {
            throw new InternalServerException();
        } finally {
            semaphore.release();
        }
    }

    public void incrementCounter() {
        try {
            semaphore.acquire();
            ++counter;
        } catch (InterruptedException e) {
            throw new InternalServerException();
        } finally {
            semaphore.release();
        }
    }

}
