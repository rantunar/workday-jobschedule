package com.workday.java.services.impl;

import com.workday.java.services.Job;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobImpl implements Job {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private Random random = new Random();

    private long customerId;
    private long uniqueId = random.nextLong();
    private int duration;
    private boolean executed = false;

    public JobImpl() {
        customerId = random.nextLong();
        duration = 100;
    }

    public JobImpl(long customerId, int duration) {
        this.customerId = customerId;
        this.duration = duration;
    }
    @Override
    public long customerId() {
        return customerId;
    }

    @Override
    public long uniqueId() {
        return uniqueId;
    }

    @Override
    public int duration() {
        return duration;
    }

    public boolean isExecuted() {
        return executed;
    }

    @Override
    public void execute() {
        try {
            Thread.sleep(duration);
            logger.info("job executed = "+Thread.currentThread().getName()+" - "+customerId);
            executed = true;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
