package com.example.demo.services.worker_service.repository;

import com.example.demo.services.worker_service.model.Worker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkerRepository extends CrudRepository<Worker, UUID> {
}
