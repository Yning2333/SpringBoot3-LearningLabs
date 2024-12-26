package com.example.nettyserver;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class NettyServerRunner implements CommandLineRunner {

    private final NettyServer nettyServer;

    public NettyServerRunner(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    @Override
    public void run(String... args) {
        new Thread(nettyServer::start).start();
    }
}
