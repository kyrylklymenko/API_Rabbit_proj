package com.example.demo.api.GRPC.ServiceImpl;

import com.example.demo.api.dto.workerDTO.WorkerDataFormat;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.example.demo.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;


public class WorkerGrpcController extends WorkerGatewayServiceGrpc.WorkerGatewayServiceImplBase {


    @Override
    public void getAll(GetAllWorkersRequest request, StreamObserver<GetAllWorkersResponse> responseObserver) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9091).
                usePlaintext().build();
        WorkerServiceGrpc.WorkerServiceBlockingStub client = WorkerServiceGrpc.newBlockingStub(channel);

        GetAllWorkersResponse workersResponse = client.getAll(request);

        responseObserver.onNext(workersResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void get(GetOneWorkerRequest request, StreamObserver<WorkerResponse> responseObserver) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9091).
                usePlaintext().build();
        WorkerServiceGrpc.WorkerServiceBlockingStub client = WorkerServiceGrpc.newBlockingStub(channel);

        WorkerResponse response = client.get(request);
    }

    @Override
    public void create(WorkerRequest request, StreamObserver<WorkerResponse> responseObserver) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9091).
                usePlaintext().build();
        WorkerServiceGrpc.WorkerServiceBlockingStub client = WorkerServiceGrpc.newBlockingStub(channel);

        WorkerResponse response = client.create(request);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
