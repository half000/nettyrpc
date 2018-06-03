package com.half.nettyrpc.client;

import com.half.nettyRpc.objectMsg.RpcRequest;
import com.half.nettyRpc.objectMsg.RpcResponse;
import com.half.nettyRpc.protobuf.MsgRequest;
import com.half.nettyRpc.protobuf.MsgResponse;
import com.half.nettyrpc.client.handler.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class RpcClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);

    //已连接主机的缓存
    private static Map<String, RpcClient> clientMap = new ConcurrentHashMap<String, RpcClient>();

    private Channel channel;

    private EventLoopGroup group;

    private String ip;

    private int port;

    private ChannelInitializer<SocketChannel> channelChannelInitializer;

    public ChannelInitializer<SocketChannel> getChannelChannelInitializer() {
        return channelChannelInitializer;
    }

    public void setChannelChannelInitializer(ChannelInitializer<SocketChannel> channelChannelInitializer) {
        this.channelChannelInitializer = channelChannelInitializer;
    }



    private RpcClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static RpcClient getConnect(String host, int port) throws InterruptedException {
        RpcClient con;
        if (clientMap.containsKey(host + port)) {
            con= clientMap.get(host + port);
        }
        synchronized (host + port){
            if (clientMap.containsKey(host + port)) {
                con=clientMap.get(host + port);
            }
            con=new RpcClient(host, port);
            //con.setChannelChannelInitializer(new ObjectChannelInitializer()); //对象解码
            con.setChannelChannelInitializer(new ProtobufChannelInitializer());//protobuf解码
            con = con.connect();
            clientMap.put(host + port, con);
        }
        return con;
    }

    private  RpcClient connect() throws InterruptedException {
        return connect(ip,port);
    }

    private  RpcClient connect(String host, int port) throws InterruptedException {
        return connect(host,port,channelChannelInitializer);
    }

    private  RpcClient connect(String host, int port,ChannelInitializer<SocketChannel> channelChannelInitializer) throws InterruptedException {

        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(channelChannelInitializer);

        ChannelFuture future = bootstrap.connect(host, port).sync();
        LOGGER.info("client connect to " + host + ":" + port);
        Channel c = future.channel();

        this.setChannel(c);
        this.setGroup(group);
        return this;
    }

    public RpcResponse invoke(RpcRequest request) throws Exception {
        ClientHandler handle = channel.pipeline().get(ObjectHandler.class);
        Assert.notNull(handle);
        return (RpcResponse) handle.invoke(request);
    }

    public MsgResponse.msgResponse invoke(MsgRequest.msgRequest request) throws Exception {
        ClientHandler handle = channel.pipeline().get(ProtobufHandler.class);
        Assert.notNull(handle);
        return (MsgResponse.msgResponse) handle.invoke(request);
    }

    public void closeConnect() {
        this.group.shutdownGracefully();
    }


    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setGroup(EventLoopGroup group) {
        this.group = group;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}  