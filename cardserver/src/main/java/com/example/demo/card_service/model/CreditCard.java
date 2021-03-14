package com.example.demo.card_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "credit_cards")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EnableAutoConfiguration
public final class CreditCard {
    @Id
    @Column(name="card_id")
    private UUID cardId;

    @Column(name="card_num")
    private String ccNum;

    @Column(name = "exp_month")
    private int expMonth;

    @Column(name = "exp_year")
    private int expYear;

    @Column(name="cvc")
    private int CVC;

    @Column(name="balance")
    private int balance;

    @Column(name="owner_id_string")
    private UUID ownerId;


    public CreditCard(int month, int year, int cvc, String cardNum, UUID ownerId) {
        this.cardId = UUID.randomUUID();
        this.expMonth = month;
        this.expYear = year + 2;
        this.CVC = cvc;
        this.ccNum = cardNum;
        this.balance = 0;
        this.ownerId = ownerId;
    }

    public void setCcNum(String ccNum) {
        this.ccNum = this.ccNum;
    }

    public String getCcNum() {
        return this.ccNum;
    }

    public int getExpMonth() {
        return this.expMonth;
    }

    public void setExpMonth(int expMonth) {
        this.expMonth = expMonth;
    }

    public int getExpYear() {
        return this.expYear;
    }

    public void setExpYear(int expYear) {
        this.expYear = expYear;
    }

    public int getCVC() {
        return this.CVC;
    }

    void setCVC(int CVC) {
        this.CVC = CVC;
    }

    public int getBalance() {
        return this.balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public UUID getOwnerId() {
        return this.ownerId;
    }
}
