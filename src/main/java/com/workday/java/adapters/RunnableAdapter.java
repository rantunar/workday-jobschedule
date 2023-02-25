package com.workday.java.adapters;

import com.workday.java.services.Job;

public class RunnableAdapter implements Runnable {

    private Integer priority;
    private Job job;

    public RunnableAdapter(Job job, int priority) {
        this.job = job;
        this.priority = priority;
    }

    @Override
    public void run() {
        this.job.execute();
    }

    public Integer getPriority() {
        return this.priority;
    }
}