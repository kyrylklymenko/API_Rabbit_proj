package com.example.demo.api.GRPC.ServiceImpl;

import com.example.demo.api.dto.depositDTO.DepositDataFormat;
import com.example.demo.api.dto.depositDTO.DepositWithdrawDataFormat;
import com.example.demo.services.deposit_service.DepositAccount;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.example.demo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;
import java.util.MissingResourceException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/grpc/deposit")
public class DepositGrpcController extends DepositGatewayServiceGrpc.DepositGatewayServiceImplBase {

    public String getRandomWorkerId(WorkerServiceGrpc.WorkerServiceBlockingStub workerClient){
        GetAllWorkersRequest workersRequest = GetAllWorkersRequest.newBuilder().
                setDummyRequest("Hi :").build();
        GetAllWorkersResponse workersResponse = workerClient.getAll(workersRequest);

        return workersResponse.getWorker(0).getId();
    }

    @Override
    public void getAll(GetAllDepositsRequest request, StreamObserver<GetAllDepositsResponse> responseObserver) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9093).
                usePlaintext().build();
        DepositServiceGrpc.DepositServiceBlockingStub client = DepositServiceGrpc.newBlockingStub(channel);

        GetAllDepositsResponse response = client.getAll(request);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void get(GetOneDepositRequest request, StreamObserver<DepositResponse> responseObserver) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9093).
                usePlaintext().build();
        DepositServiceGrpc.DepositServiceBlockingStub client = DepositServiceGrpc.newBlockingStub(channel);

        DepositResponse response = client.get(request);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void create(DepositRequest request, StreamObserver<DepositResponse> responseObserver) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9093).
                usePlaintext().build();
        DepositServiceGrpc.DepositServiceBlockingStub client = DepositServiceGrpc.newBlockingStub(channel);

        ManagedChannel userChannel = ManagedChannelBuilder.forAddress("localhost", 9090).
                usePlaintext().build();
        UserServiceGrpc.UserServiceBlockingStub userClient = UserServiceGrpc.newBlockingStub(userChannel);

        ManagedChannel workerChannel = ManagedChannelBuilder.forAddress("localhost", 9091).
                usePlaintext().build();
        WorkerServiceGrpc.WorkerServiceBlockingStub workerClient = WorkerServiceGrpc.newBlockingStub(workerChannel);

        WithdrawMoneyRequest withdrawRequest = WithdrawMoneyRequest.newBuilder().
                setId(request.getOwnerId()).
                setBalance(request.getBalance()).build();

        userClient.withdrawMoney(withdrawRequest);

        WorkerWage wageRequest = WorkerWage.newBuilder().
                setWorkerId(getRandomWorkerId(workerClient)).
                setWage(1000).build();

        workerClient.addWage(wageRequest);

        DepositResponse response = client.create(request);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void delete(DepositWithdrawRequest request, StreamObserver<DepositWithdrawResponse> responseObserver) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9093).
                usePlaintext().build();
        DepositServiceGrpc.DepositServiceBlockingStub client = DepositServiceGrpc.newBlockingStub(channel);

        ManagedChannel userChannel = ManagedChannelBuilder.forAddress("localhost", 9090).
                usePlaintext().build();
        UserServiceGrpc.UserServiceBlockingStub userClient = UserServiceGrpc.newBlockingStub(userChannel);

        ManagedChannel workerChannel = ManagedChannelBuilder.forAddress("localhost", 9091).
                usePlaintext().build();
        WorkerServiceGrpc.WorkerServiceBlockingStub workerClient = WorkerServiceGrpc.newBlockingStub(workerChannel);

        WorkerWage wageRequest = WorkerWage.newBuilder().
                setWorkerId(getRandomWorkerId(workerClient)).
                setWage(1000).build();

        workerClient.addWage(wageRequest);

        DepositWithdrawRequest withdrawRequest = DepositWithdrawRequest.newBuilder().
                setId(request.getId()).
                setWithdrawDate(request.getWithdrawDate()).build();

        DepositWithdrawResponse response = client.delete(withdrawRequest);

        userClient.addMoney(
                AddMoneyRequest.newBuilder().
                        setId(response.getOwnerId()).
                        setBalance(response.getAddedBalance()).build()
        );

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
