package com.half.nettyrpc.client.handler;

import com.half.nettyRpc.objectMsg.RpcRequest;
import com.half.nettyRpc.objectMsg.RpcResponse;

/**
 * @author wangwei
 * @description
 * @date Create in 2018-05-26 15:31
 **/
public interface ClientHandler<T1,T2> {

    public T2 invoke(T1 request) throws Exception ;
}
