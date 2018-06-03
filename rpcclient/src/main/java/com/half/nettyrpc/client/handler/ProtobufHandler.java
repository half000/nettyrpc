package com.half.nettyrpc.client.handler;

import com.half.nettyRpc.objectMsg.RpcRequest;
import com.half.nettyRpc.objectMsg.RpcResponse;
import com.half.nettyRpc.protobuf.MsgRequest;
import com.half.nettyRpc.protobuf.MsgResponse;
import com.half.nettyrpc.client.holder.ResponseHolder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
  * @author wangwei
  * @date 2018-05-23 23:22
  */
public class ProtobufHandler extends SimpleChannelInboundHandler<MsgResponse.msgResponse> implements ClientHandler<MsgRequest.msgRequest,MsgResponse.msgResponse>{


     private  final Logger LOGGER = LoggerFactory.getLogger(getClass());

     private Channel channel;

     //request Id 与 response的映射
     private Map<Long, ResponseHolder> responseMap = new ConcurrentHashMap<Long, ResponseHolder>();

     public void channelRead0(ChannelHandlerContext ctx, MsgResponse.msgResponse response) throws Exception {
         ResponseHolder holder = responseMap.get(response.getMessageId());
         if (holder != null) {
             responseMap.remove(response.getMessageId());
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

     public MsgResponse.msgResponse invoke(MsgRequest.msgRequest request) throws Exception {
         ResponseHolder<MsgResponse.msgResponse> holder = new ResponseHolder<MsgResponse.msgResponse>();
         responseMap.put(request.getMessageId(), holder);
         channel.writeAndFlush(request);
         return holder.getResponse();
     }

 }
