package com.half.nettyRpc.objectMsg;

import java.io.Serializable;

public class RpcResponse implements Serializable {
    private Long id;
    private Exception exception;
    private Object result;

    public Object getResult() throws Exception {
        if (this.exception != null) {
            throw this.exception;
        }
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "id=" + id +
                ", exception=" + exception +
                ", result=" + result +
                '}';
    }
}