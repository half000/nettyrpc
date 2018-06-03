package com.half.nettyrpc.server.hanlder;

import com.half.nettyRpc.protobuf.MsgRequest;
import com.half.nettyRpc.protobuf.MsgResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof MsgRequest.msgRequest){
            MsgRequest.msgRequest req=(MsgRequest.msgRequest) msg;
            MsgResponse.msgResponse.Builder builder=MsgResponse.msgResponse.newBuilder();
            builder.setMessageId(((MsgRequest.msgRequest) msg).getMessageId());
            builder.setCode(1);
            builder.setData("test");
            ctx.channel().writeAndFlush(builder.build());
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }
}
