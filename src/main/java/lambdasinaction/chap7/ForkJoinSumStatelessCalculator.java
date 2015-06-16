package lambdasinaction.chap7;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import static lambdasinaction.chap7.ParallelStreamsHarness.FORK_JOIN_POOL;

public class ForkJoinSumStatelessCalculator extends RecursiveTask<Long> {

    public static final long THRESHOLD = 10_000;

    private final int start;
    private final int end;

    public ForkJoinSumStatelessCalculator(int n) {
        this(0, n);
    }

    private ForkJoinSumStatelessCalculator(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        int length = end - start;
        if (length <= THRESHOLD) {
            return computeSequentially();
        }
        ForkJoinSumStatelessCalculator leftTask = new ForkJoinSumStatelessCalculator(start, start + length/2);
        leftTask.fork();
        ForkJoinSumStatelessCalculator rightTask = new ForkJoinSumStatelessCalculator(start + length/2, end);
        Long rightResult = rightTask.compute();
        Long leftResult = leftTask.join();
        return leftResult + rightResult;
    }

    private long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += i;
        }
        return sum;
    }

    private static long computeSequentially(int start, int end) {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += i;
        }
        return sum;
    }

    public static long computeRecursively(int start, int end) {
        int length = end - start;
        if (length <= THRESHOLD) {
            return computeSequentially(start, end);
        }
        long leftSum = computeRecursively(start, start + length/2);
        long rightSum = computeRecursively(start + length / 2, end);
        return leftSum + rightSum;
    }

    public static long forkJoinSum(long n) {
        ForkJoinTask<Long> task = new ForkJoinSumStatelessCalculator((int) n);
        return FORK_JOIN_POOL.invoke(task);
    }

    public static long recursiveSum(long n) {
        return computeRecursively(0, (int) n);
    }
}