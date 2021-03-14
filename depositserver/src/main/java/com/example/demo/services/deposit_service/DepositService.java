package com.example.demo.services.deposit_service;

import com.example.demo.services.deposit_service.model.DepositAccount;
import com.example.demo.services.deposit_service.repository.DepositRepository;
import javassist.NotFoundException;
import org.graalvm.compiler.hotspot.phases.OnStackReplacementPhase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public final class DepositService {

    @Autowired
    private DepositRepository repository;



    public DepositAccount openDeposit(UUID userId, Date openingDate, int depositMoney) throws NotFoundException {
        DepositAccount deposit = new DepositAccount(depositMoney, openingDate, userId);
        return repository.save(deposit);
    }

    public DepositAccount addDeposit(DepositAccount newDeposit){
        return repository.save(newDeposit);
    }

    public List<DepositAccount> getAllDeposits(){
        List<DepositAccount> depositList = (List<DepositAccount>) repository.findAll();

        return depositList;
    }


    public DepositAccount getDepositById(UUID depositID) throws NotFoundException {
        Optional<DepositAccount> deposit = repository.findById(depositID);
        if (deposit.isPresent()){
            return deposit.get();
        }
        else {
            throw new NotFoundException(String.format("Deposit ID %s does not exist", depositID));
        }
    }

    public void closeDeposit(UUID depositID) throws NotFoundException {
        DepositAccount toRemove = getDepositById(depositID);
        repository.delete(toRemove);
    }

    public Map<String, Object> getWithdrawMoney(UUID depositId, Date withdrawDate) throws NotFoundException {
        DepositAccount deposit = getDepositById(depositId);
        String ownerId = deposit.getOwnerId().toString();
        int withdrawValue = getDepositById(depositId).withdrawBalance(withdrawDate);
        Map<String, Object> withdrawInfo = new HashMap<>();
        withdrawInfo.put("Withdraw value", withdrawValue);
        withdrawInfo.put("Owner ID", ownerId);
        return withdrawInfo;
    }

}