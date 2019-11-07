package ru.pahaya.entity;

public class Result {

    private final boolean result;

    public Result(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public static Result of(boolean result) {
        return new Result(result);
    }
}
