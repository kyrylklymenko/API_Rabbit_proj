package com.example.demo;

import com.example.demo.api.UserServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserGrpcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(9090).
                addService(new UserServiceImpl()).build();

        server.start();
        server.awaitTermination();
    }
}
