package com.example.demo.Client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.demo.GetAllUsersRequest;
import org.example.demo.GetAllUsersResponse;
import org.example.demo.UserGatewayServiceGrpc;
import org.example.demo.UserServiceGrpc;

public class UserGrpcTest {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9095).
                usePlaintext().build();

        UserGatewayServiceGrpc.UserGatewayServiceBlockingStub client = UserGatewayServiceGrpc.newBlockingStub(channel);

        GetAllUsersRequest request = GetAllUsersRequest.newBuilder().
                setDummyRequest("Hi :)").build();

        GetAllUsersResponse response = client.getAll(request);

        System.out.println(response.getUser(0).getName() + " " + response.getUser(0).getSurname());
    }

}
