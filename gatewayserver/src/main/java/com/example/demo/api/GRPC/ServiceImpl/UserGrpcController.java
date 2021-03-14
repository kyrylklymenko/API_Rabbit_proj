package com.example.demo.api.GRPC.ServiceImpl;


import com.example.demo.api.dto.userDTO.UserBalanceChangeDataFormat;
import com.example.demo.api.dto.userDTO.UserDataFormat;
import com.example.demo.services.user_service.model.User;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import javassist.NotFoundException;
import lombok.With;
import org.example.demo.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.UUID;


public class UserGrpcController extends UserGatewayServiceGrpc.UserGatewayServiceImplBase {

    @Override
    public void getAll(GetAllUsersRequest request, StreamObserver<GetAllUsersResponse> responseObserver) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).
                usePlaintext().build();
        UserServiceGrpc.UserServiceBlockingStub client = UserServiceGrpc.newBlockingStub(channel);

        GetAllUsersRequest usersRequest = GetAllUsersRequest.newBuilder().
                setDummyRequest("Hi! :)").build();

        GetAllUsersResponse usersResponse = client.getAll(usersRequest);

        responseObserver.onNext(usersResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void get(GetOneUserRequest request, StreamObserver<UserResponse> responseObserver) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).
                usePlaintext().build();

        UserServiceGrpc.UserServiceBlockingStub client = UserServiceGrpc.newBlockingStub(channel);

        UserResponse userResponse = client.get(request);

        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void create(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).
                usePlaintext().build();

        UserServiceGrpc.UserServiceBlockingStub client = UserServiceGrpc.newBlockingStub(channel);

        UserResponse userResponse = client.create(request);

        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void delete(GetOneUserRequest request, StreamObserver<UserDeleteResponse> responseObserver) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).
                usePlaintext().build();

        UserServiceGrpc.UserServiceBlockingStub client = UserServiceGrpc.newBlockingStub(channel);

        UserDeleteResponse response = client.delete(request);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
