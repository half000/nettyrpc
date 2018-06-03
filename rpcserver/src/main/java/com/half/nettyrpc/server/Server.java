package com.half.nettyrpc.server;

import com.half.nettyrpc.server.hanlder.ServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.channel.socket.nio.NioServerSocketChannel;
/**
 * @author wangwei
 * @create 2018-04-28 23:40
 **/
public class Server {

    public static void main(String[] args) {
        // 服务类,用于启动netty 在netty5中同样使用这个类来启动
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 新建两个线程池  boss线程监听端口，worker线程负责数据读写
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 200) //Socket参数，服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝。默认值，Windows为200，其他为128。
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 60000) //超时时间 ms 默认30000
                .childHandler(new ServerChannelInitializer());
        // Start the server.
        ChannelFuture f = null;
        try {
            f = bootstrap.bind(10100).sync();
            System.out.println("start!!!");
            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

}
