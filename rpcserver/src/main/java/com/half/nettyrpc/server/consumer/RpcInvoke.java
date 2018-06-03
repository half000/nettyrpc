package com.half.nettyrpc.server.consumer;

/**
 * @author wangwei
 * @description
 * @date Create in 2018-06-03 11:49
 **/
public interface RpcInvoke {

    public String invoke(String method,String head,String param);
}
