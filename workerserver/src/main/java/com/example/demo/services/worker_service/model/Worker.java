package com.example.demo.services.worker_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Data
@Table(name="workers", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@EnableAutoConfiguration
@ComponentScan
public final class Worker {
    @Id
    @Column(name="worker_id")
    private UUID workerId;

    @Column(name="name")
    private String workerName;

    @Column(name="surname")
    private String workerSurname;

    @Column(name = "wage")
    private int wage;

    @Column(name="position_name")
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
