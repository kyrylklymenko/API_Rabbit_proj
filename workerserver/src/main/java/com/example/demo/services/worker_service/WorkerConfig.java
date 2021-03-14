package com.example.demo.services.worker_service;

import com.example.demo.api.dto.WorkerWageDataFormat;
import com.example.demo.services.worker_service.model.Worker;
import com.example.demo.services.worker_service.repository.WorkerRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public final class WorkerConfig {

    private Map<Integer, String> positionMap;

    @Autowired
    private WorkerRepository repository;

    public WorkerConfig() {
        positionMap = new HashMap<>();
        this.positionMap.put(1, "Операционист");
        this.positionMap.put(2, "Менеджер по кредитам");
    }

    public Worker addWorker(Worker newWorker){
        return repository.save(newWorker);
    }

    public void removeWorker(UUID id) throws NotFoundException {
        Worker toRemove = getWorkerById(id);
        repository.delete(toRemove);
    }

    public Worker getWorkerById(UUID id) throws NotFoundException {
        Optional<Worker> worker = repository.findById(id);
        if (worker.isPresent()){
            return worker.get();
        }
        else {
            throw new NotFoundException(String.format("Worker ID %s does not exist", id));
        }
    }

    public List<Worker> getAllWorkers(){
        return (List<Worker>) repository.findAll();
    }

    private static boolean validateName(String name) {
        if (name.matches("[A-Z][a-z]*")) {
            return true;
        } else {
            throw new IllegalArgumentException("Incorrect name format (must contain 1-inf a-z characters, first capital");
        }
    }

    private static boolean validateSurname(String surname) {
        if (surname.matches("[A-Z][a-z]*")) {
            return true;
        } else {
            throw new IllegalArgumentException("Incorrect surname format (must contain 1-inf a-z characters, first capital");
        }
    }

    public void addWage(UUID workId, int money) throws NotFoundException {
        Worker worker = getWorkerById(workId);
        worker.setWage(worker.getWage() + money);
    }


    public void addWorkerWage(WorkerWageDataFormat wageBody) throws NotFoundException {
        Worker exec = getWorkerById(wageBody.getWorkerId());
        exec.setWage(exec.getWage() + wageBody.getWage());
        removeWorker(wageBody.getWorkerId());
        addWorker(exec);
    }
}