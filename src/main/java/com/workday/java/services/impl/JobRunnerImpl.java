package com.workday.java.services.impl;

import com.workday.java.services.Job;
import com.workday.java.services.JobQueue;
import com.workday.java.services.JobRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobRunnerImpl implements JobRunner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private volatile boolean shouldContinue = true;

    private volatile boolean shutdownFinished = false;

    private PriorityExecutorService priorityExecutorService;

    public JobRunnerImpl(PriorityExecutorService priorityExecutorService) {
        this.priorityExecutorService = priorityExecutorService;
    }

    @Override
    public void run(final JobQueue jobQueue) {
        logger.info("Queue size = "+jobQueue.length());
        while (shouldContinue) {
            Job job = jobQueue.pop();
            this.priorityExecutorService.executeJob(job);
        }
        logger.info("shutting down");
        shutdownFinished = true;
    }

    @Override
    public void shutdown() {
        logger.info("Calling JobRunner.shutdown");
        shouldContinue = false;
        while (!shutdownFinished) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
