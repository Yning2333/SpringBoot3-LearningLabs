package com.example.nettyclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NettyClient {

    private final String host = "localhost";

    @Value("${server.netty.port:8888}")
    private int port;

    private volatile Channel channel; // 确保线程安全
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    public void start() {
        // 使用单独的线程启动 Netty 客户端
        new Thread(() -> {
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.SO_KEEPALIVE, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new StringDecoder(), new StringEncoder(), new ClientHandler());
                                channel = ch; // 初始化时设置 channel
                            }
                        });

                ChannelFuture f = b.connect(host, port).sync();
                channel = f.channel(); // 确保在连接成功后设置 channel
                System.out.println("Connected to server: " + host + ":" + port);
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }
        }).start(); // 启动异步线程
    }

    @Scheduled(fixedRate = 5000) // 每5秒发送一次消息
    public void sendMessage() {
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush("Hello from Netty Client at " + System.currentTimeMillis());
        } else {
            System.out.println("Channel is not active or is null");
            reconnect(); // 如果连接断开，尝试重连
        }
    }

    private void reconnect() {
        System.out.println("Attempting to reconnect...");
        start(); // 异步重连
    }

}
