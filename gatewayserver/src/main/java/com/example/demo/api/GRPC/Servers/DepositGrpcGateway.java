package com.example.demo.api.GRPC.Servers;

import com.example.demo.api.GRPC.ServiceImpl.DepositGrpcController;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class DepositGrpcGateway {
    public static void main(String[] args)  throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9097).
                addService(new DepositGrpcController()).build();

        server.start();
        server.awaitTermination();
    }
}
