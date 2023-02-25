package com.workday.java.adapters;

import java.util.concurrent.FutureTask;

public class RunnablePriorityAdapter extends FutureTask<RunnablePriorityAdapter>
        implements Comparable<RunnablePriorityAdapter> {

    private RunnableAdapter runnableAdapter;

    public RunnablePriorityAdapter(RunnableAdapter runnableAdapter) {
        super(runnableAdapter, null);
        this.runnableAdapter = runnableAdapter;
    }

    @Override
    public int compareTo(RunnablePriorityAdapter runnablePriorityAdapter) {
        return Integer.compare(this.runnableAdapter.getPriority(), runnablePriorityAdapter.runnableAdapter.getPriority());
    }

}


