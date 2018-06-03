package com.half.nettyrpc.server.hanlder;

import com.half.nettyRpc.protobuf.MsgRequest;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
  * @author wangwei
  * @date 2018-05-24 23:33
  */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeLine = ch.pipeline();

        pipeLine.addLast("encoder1", new ProtobufVarint32LengthFieldPrepender());
        pipeLine.addLast("encoder2", new ProtobufEncoder());
        pipeLine.addLast("decoder1", new ProtobufVarint32FrameDecoder());
        pipeLine.addLast("decoder2", new ProtobufDecoder(MsgRequest.msgRequest.getDefaultInstance()));
        pipeLine.addLast("handler", new ServerHandler());
    }
}
