Please document here:
* Classes you have implemented or modified, including test classes:

#### JobRunnerImpl
JobRunner interface implementation to dequeue the data and supply it to executor service for further execution of jobs.

#### JobImpl
Copied the native job implementation from test package.

#### JobQueueImpl
Copied the native job queue implementation from test package.

#### PriorityExecutorService
Responsible to create the thread pool object with predefined parameters and then execute each job via the thread pool executor service.

#### FairExecutionPriorityStrategyImpl
It's a implementation class for PriorityStrategy interface to write down the priority calculation logic as below-
job -> customerid mapped with summation of the duration to create the priority,
ex -
job1 -> customerid = 1 -> duration = 100 -> priority = sum(customerid = 1 duration) = 100
job2 -> customerid = 1 -> duration = 100 -> priority = sum(customerid = 1 duration) = 200
job3 -> customerid = 1 -> duration = 100 -> priority = sum(customerid = 1 duration) = 300
job4 -> customerid = 2 -> duration = 100 -> priority = sum(customerid = 2 duration) = 100

#### PriorityStrategy interface
It's an interface with single method as priority to be implemented as per user required strategy

#### RunnableAdapter
It's a class to implements runnable interface and working as an adapter class to inject the thread execution method

#### RunnablePriorityAdapter
It's an adapter class which extends FutureTask and the Comparable interface to set the job execution order as per their priority as a future execution.

#### JobUtils
It's an util class to store the static and final constants regarding thread pool properties and priority queue properties.

#### JobRunnerTests
It's a test class to test different scenario for JobRunnerImpl class.

* Any assumptions that affected your design
* Any shortcomings of your implementation
#### Job Starvation
The priority calculation logic may cause job starvation for some cases if any job for one unique customer id having the duration 100 or low, for example-
job8 -> customerid = 101 -> duration = 10 -> priority = (sum of customerid = 101 duration) = 10
job8 priority(10) less than all the available jobs in the queue.

* An explanation of your definition of fairness execution
#### Fair Execution
All the jobs will be inserted in the priority queue with a defined priority as per their customer id so that each customer id job can have similar priority and can be executed fairly.
job1 -> customerid = 1 -> duration = 100 -> priority = sum(customerid = 1 duration) = 100
job2 -> customerid = 1 -> duration = 100 -> priority = sum(customerid = 1 duration) = 200
job3 -> customerid = 1 -> duration = 100 -> priority = sum(customerid = 1 duration) = 300
job4 -> customerid = 2 -> duration = 100 -> priority = sum(customerid = 2 duration) = 100
job5 -> customerid = 2 -> duration = 100 -> priority = sum(customerid = 2 duration) = 200
job6 -> customerid = 2 -> duration = 100 -> priority = sum(customerid = 2 duration) = 300
In above example job3 & job6 having highest priority will execute first.