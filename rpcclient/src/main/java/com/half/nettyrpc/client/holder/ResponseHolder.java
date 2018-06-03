package com.half.nettyrpc.client.holder;

public class ResponseHolder<T> {
    private T response;

    public void setResponse(T response) {
        this.response = response;
        synchronized (this) {
            notify();
        }
    }

    public T getResponse() throws Exception {
        synchronized (this) {
            wait(10000);
        }
        return response;
    }

    public T getResponse(long timeout) throws Exception {
        synchronized (this) {
            wait(timeout);
        }
        return response;
    }
}