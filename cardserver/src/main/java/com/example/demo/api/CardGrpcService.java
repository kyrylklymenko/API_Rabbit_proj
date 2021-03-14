package com.example.demo.api;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class CardGrpcService {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9094).
                addService(new CardServiceImpl()).build();

        server.start();
        server.awaitTermination();
    }


}
