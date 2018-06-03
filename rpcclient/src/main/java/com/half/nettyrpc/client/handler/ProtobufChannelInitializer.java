package com.half.nettyrpc.client.handler;

import com.half.nettyRpc.protobuf.MsgResponse;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;


public class ProtobufChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeLine = ch.pipeline();

        pipeLine.addLast("encoder1", new ProtobufVarint32LengthFieldPrepender());
        pipeLine.addLast("encoder2", new ProtobufEncoder());
        pipeLine.addLast("decoder1", new ProtobufVarint32FrameDecoder());
        pipeLine.addLast("decoder2", new ProtobufDecoder(MsgResponse.msgResponse.getDefaultInstance()));
        pipeLine.addLast("handler", new ProtobufHandler());
    }


}
