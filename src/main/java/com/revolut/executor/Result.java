package com.revolut.executor;

public class Result {
    final boolean success;
    final Exception failure;
    final Object entity;

    private Result(boolean success, Exception failure, Object entity) {
        this.success = success;
        this.failure = failure;
        this.entity=entity;
    }

    public Result(boolean success) {
        this(success, null);
    }

    public Result(boolean success, Object entity) {
        this(success, null, entity);
    }

    public Result(Exception exception) {
        this(false, exception, null);
    }

    public boolean wasSuccessful() {
        return success;
    }

    public Exception getFailure() {
        return failure;
    }

    public Object getEntity() {
        return entity;
    }
}