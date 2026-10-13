package com.dsa.model;

/**
 * One measured row of a performance comparison.
 *
 * Every value except expectedComplexity is produced by actually running the code;
 * expectedComplexity is the theoretical Big-O and is displayed separately from the measurements.
 */
public class OperationResult {

    private final String subject;            // algorithm or data structure, e.g. "Binary Search"
    private final String operation;          // what was done, e.g. "search for 750"
    private final int inputSize;             // n (elements) or V (vertices)
    private final long operationCount;       // measured count, e.g. comparisons or shifts
    private final String countMetric;        // what operationCount measures
    private final long elapsedNanos;         // measured time
    private final String result;             // short outcome, e.g. "found at 749"
    private final String expectedComplexity; // theoretical complexity, e.g. "O(log n)"
    private final String details;            // optional extra information (may be null)

    public OperationResult(String subject, String operation, int inputSize, long operationCount,
                           String countMetric, long elapsedNanos, String result,
                           String expectedComplexity, String details) {
        this.subject = subject;
        this.operation = operation;
        this.inputSize = inputSize;
        this.operationCount = operationCount;
        this.countMetric = countMetric;
        this.elapsedNanos = elapsedNanos;
        this.result = result;
        this.expectedComplexity = expectedComplexity;
        this.details = details;
    }

    public OperationResult(String subject, String operation, int inputSize, long operationCount,
                           String countMetric, long elapsedNanos, String result, String expectedComplexity) {
        this(subject, operation, inputSize, operationCount, countMetric, elapsedNanos, result,
                expectedComplexity, null);
    }

    public String getSubject() {
        return subject;
    }

    public String getOperation() {
        return operation;
    }

    public int getInputSize() {
        return inputSize;
    }

    public long getOperationCount() {
        return operationCount;
    }

    public String getCountMetric() {
        return countMetric;
    }

    public long getElapsedNanos() {
        return elapsedNanos;
    }

    public String getResult() {
        return result;
    }

    public String getExpectedComplexity() {
        return expectedComplexity;
    }

    public String getDetails() {
        return details;
    }
}
