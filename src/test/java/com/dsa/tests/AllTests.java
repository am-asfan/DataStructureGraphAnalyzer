package com.dsa.tests;

/**
 * Runs every automated test and prints a summary.
 * Exit code 0 = all tests passed, 1 = at least one failure.
 */
public class AllTests {

    public static void main(String[] args) {
        ArrayManagerTest.run();
        CustomStackTest.run();
        CustomQueueTest.run();
        CustomLinkedListTest.run();
        SearchAlgorithmsTest.run();
        GraphTest.run();
        PerformanceAnalyzerTest.run();
        InputValidatorTest.run();
        IntegrationTest.run();

        System.out.println();
        System.out.println("==================================================");
        System.out.println("Checks passed: " + TestHelper.getPassed());
        System.out.println("Checks failed: " + TestHelper.getFailures().size());
        for (String failure : TestHelper.getFailures()) {
            System.out.println("  - " + failure);
        }
        System.out.println("==================================================");
        System.exit(TestHelper.getFailures().isEmpty() ? 0 : 1);
    }
}
