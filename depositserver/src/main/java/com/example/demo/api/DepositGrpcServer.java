package com.example.demo.api;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class DepositGrpcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9093).
                addService(new DepositServiceImpl()).build();

        server.start();
        server.awaitTermination();
    }
}
