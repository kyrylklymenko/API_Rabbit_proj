package com.example.demo.services.deposit_service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.services.deposit_service.model.DepositAccount;

import java.util.UUID;

@Repository
public interface DepositRepository extends CrudRepository<DepositAccount, UUID> {

}
