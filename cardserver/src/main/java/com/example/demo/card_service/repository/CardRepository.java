package com.example.demo.card_service.repository;

import com.example.demo.card_service.model.CreditCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CardRepository extends CrudRepository<CreditCard, UUID> {

}
