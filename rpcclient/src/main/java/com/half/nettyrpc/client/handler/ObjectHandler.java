package com.half.nettyrpc.client.handler;

import com.half.nettyRpc.objectMsg.RpcRequest;
import com.half.nettyRpc.objectMsg.RpcResponse;
import com.half.nettyrpc.client.holder.ResponseHolder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangwei
 * @description
 * @date Create in 2018-05-26 15:23
 **/
public class ObjectHandler extends SimpleChannelInboundHandler<RpcResponse> implements ClientHandler<RpcRequest,RpcResponse>{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtobufHandler.class);

    private Channel channel;

    //request Id 与 response的映射
    private Map<Long, ResponseHolder> responseMap = new ConcurrentHashMap<Long, ResponseHolder>();

    @Override
    public void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        ResponseHolder holder = responseMap.get(response.getId());
        if (holder != null) {
            responseMap.remove(response.getId());
            holder.setResponse(response);
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        channel = ctx.channel();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("exceptionCaught", cause);
        ctx.close();
    }

    public RpcResponse invoke(RpcRequest request) throws Exception {
        ResponseHolder<RpcResponse> holder = new ResponseHolder<RpcResponse>();
        responseMap.put(request.getId(), holder);
        channel.writeAndFlush(request);
        return holder.getResponse();
    }
}
