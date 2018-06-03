package com.half.nettyrpc.server.consumer;

import com.half.nettyRpc.protobuf.MsgRequest;
import com.half.nettyRpc.protobuf.MsgResponse;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author wangwei
 * @description 消息接收task，负责具体的消息消费处理
 * @date Create in 2018-06-03 11:29
 **/
public class MessageRecvTask implements Runnable{

    private ChannelHandlerContext ctx = null;
    MsgRequest.msgRequest msg=null;

    public MessageRecvTask(ChannelHandlerContext ctx, MsgRequest.msgRequest msg) {
        this.ctx = ctx;
        this.msg = msg;
    }

    @Override
    public void run() {
        String head=msg.getHead();
        String method=msg.getMethodName();
        String params=msg.getParams();
        //TODO  根据方法名反射拿到相应对象与方法，调用得到结果
        RpcInvoke rpcInvoke=new RpcInvokeImpl();
        String data=rpcInvoke.invoke(method, head, params);
        MsgResponse.msgResponse.Builder builder=MsgResponse.msgResponse.newBuilder();
        builder.setMessageId( msg.getMessageId());
        builder.setCode(1);
        builder.setData("data");
        ctx.channel().writeAndFlush(builder.build());

    }
}
