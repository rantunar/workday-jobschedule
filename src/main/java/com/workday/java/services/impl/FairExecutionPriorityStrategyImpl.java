package com.workday.java.services.impl;

import com.workday.java.services.Job;
import com.workday.java.services.PriorityStrategy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FairExecutionPriorityStrategyImpl implements PriorityStrategy {

    private final Map<Long, Integer> map = new ConcurrentHashMap<>();

    @Override
    public Integer getPriority(Job job) {
        return this.map.merge(job.customerId(), Math.abs(job.duration()), Integer::sum);
    }
}
