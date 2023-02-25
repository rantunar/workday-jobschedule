package com.workday.java.services.impl;

import com.workday.java.services.Job;
import com.workday.java.services.PriorityStrategy;
import com.workday.java.utils.JobUtils;
import com.workday.java.adapters.RunnableAdapter;
import com.workday.java.adapters.RunnablePriorityAdapter;
import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PriorityExecutorService {

    private Executor executorService;
    private PriorityStrategy priorityStrategy;

    public PriorityExecutorService(PriorityStrategy priorityStrategy) {
        this.executorService = new ThreadPoolExecutor(JobUtils.CORE_POOL_SIZE, JobUtils.MAXIMUM_POOL_SIZE, JobUtils.KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>(JobUtils.PRIORITY_QUEUE_SIZE));
        this.priorityStrategy = priorityStrategy;
    }

    public void executeJob(Job job) {
        final RunnablePriorityAdapter executableJob = new RunnablePriorityAdapter(new RunnableAdapter(job, this.priorityStrategy.getPriority(job)));
        this.executorService.execute(executableJob);
    }
}
