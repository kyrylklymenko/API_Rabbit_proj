package com.example.demo.api;

import com.example.demo.DemoApplication;
import com.example.demo.api.dto.*;
import com.example.demo.card_service.CardService;
import com.example.demo.card_service.model.CreditCard;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;
import java.util.MissingResourceException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/banking/creditcard")
@ComponentScan
public class CardController {

    @Autowired
    private CardService cardService;

    final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    private String patchUserURL = "http://userserver:8090/banking/user/change_balance";
    private String patchWorkerURL = "http://workerserver:8091/banking/worker/add_wage";

    @GetMapping
    public ResponseEntity<List<CreditCard>> getAllCreditCards(){
        try{
            final List<CreditCard> userList = cardService.getAllCreditCards();

            return new ResponseEntity<>(userList, HttpStatus.OK);
        } catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<CreditCard> getCard(@PathVariable("id") UUID cardId){

        try{
            final CreditCard user = cardService.getCardById(cardId);

            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
        catch (MissingResourceException | NotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @PostMapping(consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCard(@Valid @RequestBody CardDataFormat cardBody){
        try{

            RestTemplate workerPatchRequest = new RestTemplate();
            WorkerWageDTO workerReqBody = new WorkerWageDTO(cardBody.getWorkerId(), 1000);
            workerPatchRequest.exchange(patchWorkerURL, HttpMethod.PUT, new HttpEntity<>(workerReqBody), Void.class);


            CreditCard newCard = new CreditCard(cardBody.getRegMonth(), cardBody.getRegYear(), cardBody.getCVC(),
                    cardBody.getCardNum(), cardBody.getOwnerId());

            cardService.openCard(newCard);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value="/change_balance", consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeCreditCardBalance(@Valid @RequestBody CardOperationDataFormat cardBody) throws NotFoundException {

        RestTemplate workerPatchRequest = new RestTemplate();
        WorkerWageDTO workerReqBody = new WorkerWageDTO(cardBody.getWorkerId(), 1000);
        workerPatchRequest.exchange(patchWorkerURL, HttpMethod.PUT, new HttpEntity<>(workerReqBody), Void.class);

        CreditCard card = cardService.getCardById(cardBody.getCardId());
        RestTemplate userPatchRequest = new RestTemplate();



        UserBalChangeDTO userReqBody = null;

        if (cardBody.getBalanceChange() > 0){
                userReqBody = new UserBalChangeDTO(card.getOwnerId(), - cardBody.getBalanceChange());
                cardService.putMoney(cardBody.getCardId(), cardBody.getBalanceChange());
            }
        else {
            if (cardService.getCardBalance(cardBody.getCardId()) >= -1*cardBody.getBalanceChange()){
                userReqBody = new UserBalChangeDTO(card.getOwnerId(), - cardBody.getBalanceChange());
                cardService.withdrawMoney(cardBody.getCardId(), cardBody.getBalanceChange());
            }

        }
        userPatchRequest.exchange(patchUserURL, HttpMethod.PUT, new HttpEntity<>(userReqBody), Void.class);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> closeCard(@RequestBody CardCloseDataFormat cardCloseBody) throws NotFoundException {
        cardService.closeCC(cardCloseBody.getCardId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}