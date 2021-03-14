package com.example.demo.api.GRPC.Servers;

import com.example.demo.api.GRPC.ServiceImpl.UserGrpcController;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class UserGrpcGateway {
    public static void main(String[] args)  throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9095).
                addService(new UserGrpcController()).build();

        server.start();
        server.awaitTermination();
    }
}
