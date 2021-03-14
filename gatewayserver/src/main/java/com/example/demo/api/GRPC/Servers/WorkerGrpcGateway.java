package com.example.demo.api.GRPC.Servers;

import com.example.demo.api.GRPC.ServiceImpl.WorkerGrpcController;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class WorkerGrpcGateway {
    public static void main(String[] args)  throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9096).
                addService(new WorkerGrpcController()).build();

        server.start();
        server.awaitTermination();
    }
}
