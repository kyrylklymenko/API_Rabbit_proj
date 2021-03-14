package com.example.demo.card_service;

import com.example.demo.card_service.model.CreditCard;
import com.example.demo.card_service.repository.CardRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public final class CardService {
    @Autowired
    private CardRepository repository;

//    public CreditCard openCC(UUID ccOpener, String ccNum, int month, int year) {
//        CreditCard newCard = new CreditCard(month, year);
//        newCard.setCcNum(ccNum);
//        newCard.setCVC(ThreadLocalRandom.current().nextInt(100, 1000));
//        newCard.setOwnerId(ccOpener);
//        repository.save(newCard);
//        return newCard;
//    }

    public CreditCard openCard(CreditCard newCard){
        return repository.save(newCard);
    }

    public CreditCard getCardById(UUID cardId) throws NotFoundException {
        Optional<CreditCard> card = repository.findById(cardId);
        if (card.isPresent()){
            return card.get();
        }
        else {
            throw new NotFoundException(String.format("Card ID %s does not exist", cardId));
        }
    }

    public List<CreditCard> getAllCreditCards(){
        List<CreditCard> cardList = (List<CreditCard>) repository.findAll();

        return cardList;
    }

    public void closeCC(UUID cardId) throws NotFoundException {
        CreditCard toRemove = getCardById(cardId);
        repository.delete(toRemove);
    }

    public void putMoney(UUID cardId, int money) throws NotFoundException {
        CreditCard card = getCardById(cardId);
        card.setBalance(getCardBalance(cardId) + money);
    }

    public void withdrawMoney(UUID cardId, int withdrawMoney) throws NotFoundException {
        CreditCard card = getCardById(cardId);
        card.setBalance(getCardBalance(cardId) - withdrawMoney);
    }

    public int getCardBalance(UUID cardId) throws NotFoundException {
        return getCardById(cardId).getBalance();
    }

}