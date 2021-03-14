package com.example.demo.api;

import com.example.demo.services.deposit_service.DepositService;
import com.example.demo.services.deposit_service.model.DepositAccount;
import io.grpc.stub.StreamObserver;
import javassist.NotFoundException;
import org.example.demo.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DepositServiceImpl extends DepositServiceGrpc.DepositServiceImplBase {

    DepositService depositService = new DepositService();

    @Override
    public void getAll(GetAllRequest request, StreamObserver<GetAllResponse> responseObserver) {
        List<DepositAccount> deposits = depositService.getAllDeposits();
        List<DepositResponse> depositResponses = new ArrayList<>();
        for (DepositAccount deposit: deposits){
            String date = new SimpleDateFormat("yyyy-mm-dd").format(deposit.getOpeningDate());

            depositResponses.add(DepositResponse.newBuilder().
                setId(deposit.getDepositId().toString()).
                setBalance(deposit.getBalance()).
                setOpeningDate(date).
                setOwnerId(deposit.getOwnerId().toString()).build());
        }

        GetAllResponse response = GetAllResponse.newBuilder().
                addAllDeposit(depositResponses).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void get(GetOneRequest request, StreamObserver<DepositResponse> responseObserver) {
        DepositResponse response;
        try{
            DepositAccount deposit = depositService.getDepositById(UUID.fromString(request.getId()));
            response = DepositResponse.newBuilder().
                    setId(deposit.getDepositId().toString()).
                    setBalance(deposit.getBalance()).
                    setOpeningDate(new SimpleDateFormat("yyyy-mm-dd").format(deposit.getOpeningDate())).
                    setOwnerId(deposit.getOwnerId().toString()).build();
        } catch (NotFoundException e) {
            response = DepositResponse.newBuilder().
                    setId("Deposit not found").build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void create(DepositRequest request, StreamObserver<DepositResponse> responseObserver){
        String date = request.getOpeningDate();
        Date openingDate = new Date();
        DepositResponse response;
        try{
            openingDate = (new SimpleDateFormat("yyyy-mm-dd").parse(date));
            DepositAccount deposit = depositService.addDeposit(new DepositAccount(
                    request.getBalance(),
                    openingDate,
                    UUID.fromString(request.getOwnerId())
            ));

            response = DepositResponse.newBuilder().
                    setId(deposit.getDepositId().toString()).
                    setBalance(deposit.getBalance()).
                    setOpeningDate(new SimpleDateFormat("yyyy-mm-dd").format(deposit.getOpeningDate())).
                    setOwnerId(deposit.getOwnerId().toString()).build();
        } catch (ParseException e) {
            response = DepositResponse.newBuilder().
                    setId("Deposit not found").build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void delete(WithdrawRequest request, StreamObserver<WithdrawResponse> responseObserver) {
        String date = request.getWithdrawDate();
        Date withdrawDate = new Date();
        WithdrawResponse response;
        try
        {
            withdrawDate = (new SimpleDateFormat("yyyy-mm-dd").parse(date));
            Map<String, Object> withdrawInfo = depositService.getWithdrawMoney(UUID.fromString(request.getId()), withdrawDate);

            int money = (int) withdrawInfo.get("Withdraw value");
            String ownerId = (String) withdrawInfo.get("Owner ID");
            response = WithdrawResponse.newBuilder().
                    setAddedBalance(money).
                    setOwnerId(ownerId).build();
        } catch (ParseException| NotFoundException e) {
            response = WithdrawResponse.newBuilder()
                    .setAddedBalance(0).build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
