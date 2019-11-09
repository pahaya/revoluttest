package ru.pahaya.entity;

/**
 * Result of operations
 */
public class Result {

    private final boolean result;

    private Result(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public static Result of(boolean result) {
        return new Result(result);
    }
}
