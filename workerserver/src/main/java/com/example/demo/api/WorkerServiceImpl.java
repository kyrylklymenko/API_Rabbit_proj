package com.example.demo.api;

import com.example.demo.services.worker_service.WorkerConfig;
import com.example.demo.services.worker_service.model.Worker;
import io.grpc.stub.StreamObserver;
import javassist.NotFoundException;
import net.bytebuddy.description.field.FieldDescription;
import org.example.demo.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkerServiceImpl extends WorkerServiceGrpc.WorkerServiceImplBase {

    private WorkerConfig workerService = new WorkerConfig();

    @Override
    public void getAll(GetAllRequest request, StreamObserver<GetAllResponse> responseObserver) {
        List<Worker> workers = workerService.getAllWorkers();
        List<WorkerResponse> workerResponses = new ArrayList<>();
        for (Worker worker: workers) workerResponses.add(WorkerResponse.newBuilder().
                setId(worker.getWorkerId().toString()).
                setName(worker.getWorkerName()).
                setSurname(worker.getWorkerSurname()).
                setWage(worker.getWage()).build());

        GetAllResponse response = GetAllResponse.newBuilder().
                 addAllWorker(workerResponses).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void get(GetOneRequest request, StreamObserver<WorkerResponse> responseObserver) throws NotFoundException {
        Worker worker = workerService.getWorkerById(UUID.fromString(request.getId()));

        WorkerResponse response = WorkerResponse.newBuilder().
                setId(worker.getWorkerId().toString()).
                setName(worker.getWorkerName()).
                setSurname(worker.getWorkerSurname()).
                setWage(worker.getWage()).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void create(WorkerRequest request, StreamObserver<WorkerResponse> responseObserver) {
        Worker worker = workerService.addWorker(new Worker(request.getName(), request.getSurname()));

        WorkerResponse response = WorkerResponse.newBuilder().
                setId(worker.getWorkerId().toString()).
                setName(worker.getWorkerName()).
                setSurname(worker.getWorkerSurname()).
                setWage(worker.getWage()).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void delete(GetOneRequest request, StreamObserver<DeleteResponse> responseObserver) {
        String deleteResponse = "";
        try{
            workerService.removeWorker(UUID.fromString(request.getId()));

            deleteResponse = "Successfully deleted the worker";
        } catch(NotFoundException e){
           deleteResponse = "Couldn't delete the requested worker";
        }

        DeleteResponse response = DeleteResponse.newBuilder().
                setResponse(deleteResponse).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void addWage(workerWage request, StreamObserver<WorkerResponse> responseObserver) {
        String id = "", name = "", surmane = "";
        int wage = 0;
        try{
            Worker worker = workerService.getWorkerById(UUID.fromString(request.getWorkerId()));
            id = worker.getWorkerId().toString();
            name = worker.getWorkerName();
            surmane = worker.getWorkerSurname();
            wage = worker.getWage();
        } catch (NotFoundException e) {
            id = "Worker not found";
        }

        WorkerResponse response = WorkerResponse.newBuilder().
                setId(id).
                setWage(wage).
                setSurname(surmane).
                setName(name).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
