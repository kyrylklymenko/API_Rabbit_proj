package com.example.demo.api;

import com.example.demo.api.WorkerServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.netty.bootstrap.ServerBootstrap;

import java.io.IOException;

public class WorkerGrpcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9091).
                addService(new WorkerServiceImpl()).build();

        server.start();
        server.awaitTermination();
    }
}
