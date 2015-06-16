package lambdasinaction.chap7;

/**
 * This harness demonstrates difference between recursive implementation vs usage of fork/join (for divide-conquer algorithms)
 */
public class ForkJoinHarness {
    public static void main(String[] args) {
        System.out.println("ForkJoin sum done in: " + ParallelStreamsHarness.measurePerf(ForkJoinSumStatelessCalculator::forkJoinSum, 1_000_000_000L) + " msecs" );
        System.out.println("Recursive sum done in: " + ParallelStreamsHarness.measurePerf(ForkJoinSumStatelessCalculator::recursiveSum, 1_000_000_000L) + " msecs" );
    }
}
