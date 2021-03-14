package com.example.demo.api.GRPC.ServiceImpl;

import com.example.demo.api.dto.cardDTO.CardCloseDataFormat;
import com.example.demo.api.dto.cardDTO.CardDataFormat;
import com.example.demo.api.dto.cardDTO.CardOperationDataFormat;
import com.example.demo.services.card_service.CreditCard;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import javassist.NotFoundException;
import lombok.With;
import org.example.demo.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.MissingResourceException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/grpc/creditcard")
public class CardGrpcController extends CardGatewayServiceGrpc.CardGatewayServiceImplBase {

    public String getRandomWorkerId(WorkerServiceGrpc.WorkerServiceBlockingStub workerClient){
        GetAllWorkersRequest workersRequest = GetAllWorkersRequest.newBuilder().
                setDummyRequest("Hi :").build();
        GetAllWorkersResponse workersResponse = workerClient.getAll(workersRequest);

        return workersResponse.getWorker(0).getId();
    }

    @Override
    public void getAll(GetAllCardsRequest request, StreamObserver<GetAllCardsResponse> responseObserver) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9094).
                usePlaintext().build();
        CardServiceGrpc.CardServiceBlockingStub client = CardServiceGrpc.newBlockingStub(channel);

        GetAllCardsResponse response = client.getAll(request);
        List<CardResponse> cards = response.getCardList();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void get(GetOneCardRequest request, StreamObserver<CardResponse> responseObserver) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9094).
                usePlaintext().build();
        CardServiceGrpc.CardServiceBlockingStub client = CardServiceGrpc.newBlockingStub(channel);

        CardResponse response = client.get(request);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    @Override
    public void create(CardRequest request, StreamObserver<CardResponse> responseObserver) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9094).
                usePlaintext().build();
        CardServiceGrpc.CardServiceBlockingStub client = CardServiceGrpc.newBlockingStub(channel);

        ManagedChannel workerChannel = ManagedChannelBuilder.forAddress("localhost", 9091).
                usePlaintext().build();
        WorkerServiceGrpc.WorkerServiceBlockingStub workerClient = WorkerServiceGrpc.newBlockingStub(workerChannel);

        CardRequest createRequest = CardRequest.newBuilder().
                setRegMonth(request.getRegMonth()).
                setRegYear(request.getRegYear()).
                setCvc(request.getCvc()).
                setCardNum(request.getCardNum()).
                setOwnerId(request.getOwnerId()).
                build();

        CardResponse response = client.create(createRequest);

        GetAllWorkersRequest workersRequest = GetAllWorkersRequest.newBuilder().
                setDummyRequest("Hi :").build();
        GetAllWorkersResponse workersResponse = workerClient.getAll(workersRequest);

        WorkerWage wageRequest = WorkerWage.newBuilder().
                setWorkerId(getRandomWorkerId(workerClient)).
                setWage(1000).build();

        workerClient.addWage(wageRequest);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    @Override
    public void replenish(ReplenishRequest request, StreamObserver<CardOperationResponse> responseObserver) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9094).
                usePlaintext().build();
        CardServiceGrpc.CardServiceBlockingStub client = CardServiceGrpc.newBlockingStub(channel);

        ManagedChannel workerChannel = ManagedChannelBuilder.forAddress("localhost", 9091).
                usePlaintext().build();
        WorkerServiceGrpc.WorkerServiceBlockingStub workerClient = WorkerServiceGrpc.newBlockingStub(workerChannel);

        ManagedChannel userChannel = ManagedChannelBuilder.forAddress("localhost", 9090).
                usePlaintext().build();
        UserServiceGrpc.UserServiceBlockingStub userClient = UserServiceGrpc.newBlockingStub(userChannel);

        WorkerWage wageRequest = WorkerWage.newBuilder().
                setWorkerId(getRandomWorkerId(workerClient)).
                setWage(1000).build();

        workerClient.addWage(wageRequest);

        CardOperationResponse response = client.replenish(request);

        WithdrawMoneyRequest withdrawMoneyRequest = WithdrawMoneyRequest.newBuilder().
                setId(response.getCard().getOwnerId()).
                setBalance(request.getBalance()).build();

        userClient.withdrawMoney(withdrawMoneyRequest);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void withdraw(CardWithdrawRequest request, StreamObserver<CardOperationResponse> responseObserver) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9094).
                usePlaintext().build();
        CardServiceGrpc.CardServiceBlockingStub client = CardServiceGrpc.newBlockingStub(channel);

        ManagedChannel workerChannel = ManagedChannelBuilder.forAddress("localhost", 9091).
                usePlaintext().build();
        WorkerServiceGrpc.WorkerServiceBlockingStub workerClient = WorkerServiceGrpc.newBlockingStub(workerChannel);

        ManagedChannel userChannel = ManagedChannelBuilder.forAddress("localhost", 9090).
                usePlaintext().build();
        UserServiceGrpc.UserServiceBlockingStub userClient = UserServiceGrpc.newBlockingStub(userChannel);

        WorkerWage wageRequest = WorkerWage.newBuilder().
                setWorkerId(getRandomWorkerId(workerClient)).
                setWage(1000).build();

        workerClient.addWage(wageRequest);

        CardOperationResponse response = client.withdraw(request);

        AddMoneyRequest addMoneyRequest = AddMoneyRequest.newBuilder().
                setId(response.getCard().getOwnerId()).
                setBalance(request.getBalance()).build();

        userClient.addMoney(addMoneyRequest);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void close(GetOneCardRequest request, StreamObserver<CardCloseResponse> responseObserver) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9094).
                usePlaintext().build();
        CardServiceGrpc.CardServiceBlockingStub client = CardServiceGrpc.newBlockingStub(channel);

        ManagedChannel workerChannel = ManagedChannelBuilder.forAddress("localhost", 9091).
                usePlaintext().build();
        WorkerServiceGrpc.WorkerServiceBlockingStub workerClient = WorkerServiceGrpc.newBlockingStub(workerChannel);

        WorkerWage wageRequest = WorkerWage.newBuilder().
                setWorkerId(getRandomWorkerId(workerClient)).
                setWage(1000).build();

        workerClient.addWage(wageRequest);

        CardCloseResponse response = client.close(request);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
