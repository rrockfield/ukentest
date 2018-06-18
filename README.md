# Uken Backend Engineer Challenges

## Question 1

NOTE: This is my very first time working with Redis, please don't expect an expert level on this tool.

### Challenge A

In the test files you may find a class [ChallengeA_IT](src/test/java/com/uken/rockfield/question1/ChallengeA_IT.java). 
This class contains 4 tests:
- consecutiveInsertionTest
- singleTransactionInsertionTest
- singlePipelineInsertionTest
- parallelPipelineInsertionTest

#### consecutiveInsertionTest (ignored and deprecated): 
This is pretty much the same test you posted in the example. 
It is ignored and deprecated in order to avoid the execution when testing the selected methods.

#### singleTransactionInsertionTest: 
My first attempt to solve the problem was including all of the insertions in a single transaction operation. 
The time gain was worth it, as I was able to get all the operations done in approximately 45 seconds to a remote Redis.

#### singlePipelineInsertionTest: 
My second attempt to improve performance was replacing the transaction with a pipeline to see if Redis was adding some overhead to the transaction in case of an unexpected rollback. 
The time gain was negligible, barely 1 second over 100000 insertions.

#### parallelPipelineInsertionTest (ignored): 
My last attempt to improve performance was applying divide and conquer with local parallelism.
Unexpectedly, the 10 threads I used lasted even more than the single pipeline test, and that's the reason why this test was marked as ignored.

### Challenge B

In the test files you may find a class [ChallengeB_IT](src/test/java/com/uken/rockfield/question1/ChallengeB_IT.java). 

My solution was to add a [buffer](src/main/java/com/uken/rockfield/question1/RedisBuffer.java) before sending the insertion to Redis. 
The [buffer](src/main/java/com/uken/rockfield/question1/RedisBuffer.java) is supposed to hold all of the insertions in a temporary map until the timer triggers.
The timer executes all of the insertions saved in the map in a single Redis pipeline using a parallel thread, meanwhile the main thread replaces the map instance with a brand new one.
The [test](src/test/java/com/uken/rockfield/question1/ChallengeB_IT.java) executes a single thread for testing the functionality only, a proper load test is still required to measure the throughput.

## Question 2

I'm still thinking about this problem. 
My first approach is to keep the events indexed by user where the latest events are stored for easy access whenever the latest 100 events are requested. 
A scheduled task should compress and send to another historical storage (Amazon S3 maybe) all of the events greater than the 100th per user like a Garbage Collector.

## Question 3

There are no validations, so for the sake of my solution I'm assuming that:
1. Time ranges are surrounded with parentheses, starting with "(" and ending with ")".
2. Starting time is separated from ending time with a dash ("-").
3. Time ranges are separated by commas (",").
4. Time ranges are in ascending order and don't overlap.
5. Time starts at 0:00 and ends at 24:00

The test values are executed from [TimeRangeSubstractionTest](src/test/java/com/uken/rockfield/question3/TimeRangeSubstractionTest.java) as proposed by the challenge. However a new custom test was added: MultiSplitTest.

The main class [TimeRangeSubstraction](src/main/java/com/uken/rockfield/question3/TimeRangeSubstraction.java) implements a static method as the entry point:

String substraction(String minuend, String subtrahend)

The solution transforms the time strings into [TimeRange](src/main/java/com/uken/rockfield/question3/TimeRange.java) objects. This object implements a binary tree whenever a time range is split by the substraction, otherwise it behaves as a single range. 