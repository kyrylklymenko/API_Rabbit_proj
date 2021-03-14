package com.example.demo.services.worker_service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Worker {
    private UUID workerId;
    private String workerName;
    private String workerSurname;
    private int wage;
    private String positionName;

    public Worker(String name, String surname) {
        this.workerId = UUID.randomUUID();
        this.wage = 0;
        this.positionName = "Operation worker";
        this.workerName = name;
        this.workerSurname = surname;
    }

    public String getWorkerName() {
        return this.workerName;
    }

    void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getWorkerSurname() {
        return this.workerSurname;
    }

    void setWorkerSurname(String workerSurname) {
        this.workerSurname = workerSurname;
    }

    public UUID getWorkerId() {
        return this.workerId;
    }

    void setWorkerId() {
        this.workerId = UUID.randomUUID();
    }

    void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionName() {
        return this.positionName;
    }

    public void setWage(int wage) {
        this.wage = wage;
    }

    public int getWage() {
        return this.wage;
    }
}

