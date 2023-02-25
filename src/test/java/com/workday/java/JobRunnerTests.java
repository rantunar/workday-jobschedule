package com.workday.java;

import com.workday.java.services.Job;
import com.workday.java.services.JobQueue;
import com.workday.java.services.JobRunner;
import com.workday.java.services.impl.FairExecutionPriorityStrategyImpl;
import com.workday.java.services.impl.JobImpl;
import com.workday.java.services.impl.JobQueueImpl;
import com.workday.java.services.impl.PriorityExecutorService;
import com.workday.java.services.impl.JobRunnerImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * These are integration tests that cover the basic requirements of a JobRunner,
 * the NaiveJobRunner does not meet all of them so some of these tests are ignored.
 * Your implementation of JobRunner should pass this tests, feel free to copy
 * this class and adapt it to your solution.
 */
public class JobRunnerTests {

    private JobRunner jobRunner;

    @Before
    public void setup() {
        PriorityExecutorService priorityExecutorService = new PriorityExecutorService(new FairExecutionPriorityStrategyImpl());
        this.jobRunner = new JobRunnerImpl(priorityExecutorService);
    }

    @Test
    public void shouldEventuallyExecuteAllJobs() throws InterruptedException {
        final List<Job> jobs = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            jobs.add(new JobImpl());
        }
        // There are 5 jobs of 100ms each = 500ms of cpu time
        final JobQueue testQueue = new JobQueueImpl(jobs);
        new Thread(() -> this.jobRunner.run(testQueue)).start();
        Thread.sleep(1000); // 1s is enough to execute all jobs even for the naive implementation
        assertEquals(testQueue.length(), 0);
        for (final Job job : jobs) {
            JobImpl executableJob = (JobImpl) job;
            assertTrue(executableJob.isExecuted());
        }
    }

    @Test
    public void shouldExecuteJobsWithPerformance() throws InterruptedException {
        final List<Job> jobs = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            jobs.add(new JobImpl());
        }
        // There are 20 jobs of 100ms each = 2s of cpu time
        final JobQueue testQueue = new JobQueueImpl(jobs);
        new Thread(() -> this.jobRunner.run(testQueue)).start();
        Thread.sleep(1000); // Only 1s wait should be enough for all jobs to be executed
        for (final Job job : jobs) {
            JobImpl executableJob = (JobImpl) job;
            assertTrue(executableJob.isExecuted());
        }
    }

    @Test
    public void shouldExecuteJobsWithFairness() throws InterruptedException {
        final List<Integer> customerIds = new ArrayList<>();
        for(int i = 1; i <= 100; i++) {
            customerIds.add(i);
        }
        final List<Job> jobs = new ArrayList<>();
        for(final Integer customerId: customerIds) {
            for(int i = 1; i <= 1000; i++) {
                jobs.add(new JobImpl(customerId, 100));
            }
        }
        // There are 100000 jobs of 100ms each
        final JobQueue testQueue = new JobQueueImpl(jobs);
        new Thread(() -> this.jobRunner.run(testQueue)).start();
        Thread.sleep(10000); // This should be enough to execute about 10% of the jobs on a modern pc
        for(final Integer customerId : customerIds) {
            int executedJobs = 0;
            for(final Job job: jobs) {
                if(((JobImpl) job).isExecuted() && job.customerId() == customerId.intValue()) {
                    executedJobs++;
                }
            }
            // For every customer there should be at least 1 executed job
            assertTrue(executedJobs > 0);
        }
    }

    @Test
    public void shouldShutdownGracefully() throws InterruptedException {
        List<Job> jobs = Arrays.asList(new JobImpl(), new JobImpl(), new JobImpl(), new JobImpl());
        JobQueue testQueue = new JobQueueImpl(jobs);
        //JobRunner jobRunner = new NaiveJobRunner();
        Thread runningThread = new Thread(() -> this.jobRunner.run(testQueue));
        runningThread.start();
        jobRunner.shutdown();
        assertTrue(testQueue.length() > 0);
    }

}
