package com.example.demo.api;

import com.example.demo.DemoApplication;
import com.example.demo.api.dto.UserBalChangeDTO;
import com.example.demo.api.dto.WorkerWageDTO;
import com.example.demo.services.deposit_service.model.DepositAccount;
import com.example.demo.api.dto.DepositDataFormat;
import com.example.demo.services.deposit_service.DepositService;
import com.example.demo.api.dto.DepositWithdrawDataFormat;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/banking/deposit")
@ComponentScan
public class DepositController {

    @Autowired
    private DepositService depositService;

    private String patchUserURL = "http://userserver:8090/banking/user/change_balance";
    private String patchWorkerURL = "http://workerserver:8091/banking/worker/add_wage";

    final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    @GetMapping
    public ResponseEntity<List<DepositAccount>> getAllDeposits(){
        try{
            final List<DepositAccount> depositList = depositService.getAllDeposits();

            return new ResponseEntity<>(depositList, HttpStatus.OK);
        } catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<DepositAccount> getDeposit(@PathVariable("id") UUID depositId){

        try{
            final DepositAccount user = depositService.getDepositById(depositId);

            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
        catch (MissingResourceException | NotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @PostMapping(consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createDeposit(@Valid @RequestBody DepositDataFormat depositBody) throws ParseException {
        try{



            Date openingDate = new SimpleDateFormat("yyyy-MM-dd").parse(depositBody.getOpeningDate());

            RestTemplate userPatchRequest = new RestTemplate();
            UserBalChangeDTO userReqBody = new UserBalChangeDTO(depositBody.getOwnerId(), -depositBody.getBalance());
            userPatchRequest.exchange(patchUserURL, HttpMethod.PUT, new HttpEntity<>(userReqBody), Void.class);

            RestTemplate workerPatchRequest = new RestTemplate();
            WorkerWageDTO workerReqBody = new WorkerWageDTO(depositBody.getWorkerId(), 1000);
            workerPatchRequest.exchange(patchWorkerURL, HttpMethod.PUT, new HttpEntity<>(workerReqBody), Void.class);


            DepositAccount newDeposit = new DepositAccount(depositBody.getBalance(), openingDate,
            depositBody.getOwnerId());

            depositService.addDeposit(newDeposit);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public ResponseEntity<Void> withdrawDeposit(@Valid @RequestBody DepositWithdrawDataFormat depositBody) throws NotFoundException, ParseException {
        Date withdrawDate = new SimpleDateFormat("yyyy-MM-dd").parse(depositBody.getWithdrawDate());

        DepositAccount deposit = depositService.getDepositById(depositBody.getDepositId());

        RestTemplate workerPatchRequest = new RestTemplate();
        WorkerWageDTO workerReqBody = new WorkerWageDTO(depositBody.getWorkerId(), 1000);
        workerPatchRequest.exchange(patchWorkerURL, HttpMethod.PUT, new HttpEntity<>(workerReqBody), Void.class);


        int withdrawMoney = depositService.getWithdrawMoney(depositBody.getDepositId(), withdrawDate);

        RestTemplate userPatchRequest = new RestTemplate();
        UserBalChangeDTO userReqBody = new UserBalChangeDTO(deposit.getOwnerId(), withdrawMoney);
        userPatchRequest.exchange(patchUserURL, HttpMethod.PUT, new HttpEntity<>(userReqBody), Void.class);
        depositService.closeDeposit(depositBody.getDepositId());


        return new ResponseEntity<>(HttpStatus.OK);

    }
}