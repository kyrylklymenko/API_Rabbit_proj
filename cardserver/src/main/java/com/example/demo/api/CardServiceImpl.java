package com.example.demo.api;

import com.example.demo.card_service.CardService;
import com.example.demo.card_service.model.CreditCard;
import io.grpc.stub.StreamObserver;
import javassist.NotFoundException;
import org.example.demo.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardServiceImpl extends CardServiceGrpc.CardServiceImplBase {

    CardService cardService = new CardService();

    @Override
    public void getAll(GetAllRequest request, StreamObserver<GetAllResponse> responseObserver) {
        List<CreditCard> cards = cardService.getAllCreditCards();
        List<CardResponse> cardResponses= new ArrayList<>();
        for (CreditCard card : cards) {
            cardResponses.add(
                    CardResponse.newBuilder().
                            setId(card.getCardId().toString()).
                            setExpMonth(card.getExpMonth()).
                            setExpYear(card.getExpYear()).
                            setCvc(card.getCVC()).
                            setCardNum(card.getCcNum()).
                            setOwnerId(card.getOwnerId().toString()).
                            setBalance(card.getBalance()).build()

            );
        }

        GetAllResponse response = GetAllResponse.newBuilder().
                addAllCard(cardResponses).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void get(GetOneRequest request, StreamObserver<CardResponse> responseObserver) {
        CardResponse response;

        try{
            CreditCard card = cardService.getCardById(UUID.fromString(request.getId()));
            response = CardResponse.newBuilder().
                    setId(card.getCardId().toString()).
                    setExpMonth(card.getExpMonth()).
                    setExpYear(card.getExpYear()).
                    setCvc(card.getCVC()).
                    setCardNum(card.getCcNum()).
                    setOwnerId(card.getOwnerId().toString()).
                    setBalance(card.getBalance()).build();
        } catch (NotFoundException e) {
            response = CardResponse.newBuilder().setId("Card not found")
                    .build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void create(CardRequest request, StreamObserver<CardResponse> responseObserver) {
        CardResponse response;
        CreditCard card = new CreditCard(
                request.getRegMonth(),
                request.getRegYear(),
                request.getCvc(),
                request.getCardNum(),
                UUID.fromString(request.getOwnerId())
        );
        cardService.openCard(card);

        response = CardResponse.newBuilder().
                setId(card.getCardId().toString()).
                setExpMonth(card.getExpMonth()).
                setExpYear(card.getExpYear()).
                setCvc(card.getCVC()).
                setCardNum(card.getCcNum()).
                setOwnerId(card.getOwnerId().toString()).
                setBalance(card.getBalance()).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void replenish(ReplenishRequest request, StreamObserver<OperationResponse> responseObserver) {
        CardResponse cardResponse;

        try{
            CreditCard card = cardService.getCardById(UUID.fromString(request.getId()));
            cardService.putMoney(UUID.fromString(request.getId()), request.getBalance());
            cardResponse = CardResponse.newBuilder().
                    setId(card.getCardId().toString()).
                    setExpMonth(card.getExpMonth()).
                    setExpYear(card.getExpYear()).
                    setCvc(card.getCVC()).
                    setCardNum(card.getCcNum()).
                    setOwnerId(card.getOwnerId().toString()).
                    setBalance(card.getBalance()).build();
        } catch (NotFoundException e){
            cardResponse = CardResponse.newBuilder().
                    setId("Card not found").build();
        }

        OperationResponse response = OperationResponse.newBuilder().
                setCard(cardResponse).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<OperationResponse> responseObserver)
    {
        CardResponse cardResponse;

        try{
            CreditCard card = cardService.getCardById(UUID.fromString(request.getId()));
            cardService.withdrawMoney(UUID.fromString(request.getId()), request.getBalance());
            cardResponse = CardResponse.newBuilder().
                    setId(card.getCardId().toString()).
                    setExpMonth(card.getExpMonth()).
                    setExpYear(card.getExpYear()).
                    setCvc(card.getCVC()).
                    setCardNum(card.getCcNum()).
                    setOwnerId(card.getOwnerId().toString()).
                    setBalance(card.getBalance()).build();
        } catch (NotFoundException e){
            cardResponse = CardResponse.newBuilder().
                    setId("Card not found").build();
        }

        OperationResponse response = OperationResponse.newBuilder().
            setCard(cardResponse).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void close(GetOneRequest request, StreamObserver<CloseResponse> responseObserver) {
        String closeResponse;

        try{
            cardService.closeCC(UUID.fromString(request.getId()));
            closeResponse = "Card closed successfully";
        } catch (NotFoundException e) {
            closeResponse = "Card not found";
        }

        CloseResponse response = CloseResponse.newBuilder().
                setResponse(closeResponse).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
