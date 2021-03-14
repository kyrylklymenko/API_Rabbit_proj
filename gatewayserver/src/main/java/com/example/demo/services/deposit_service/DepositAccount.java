package com.example.demo.services.deposit_service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public final class DepositAccount {

    private UUID depositId;

    private int balance;
    private Date openingDate;

    private UUID ownerId;

    public DepositAccount(int balance, Date openingDate, UUID ownerId) {
        this.balance = balance;
        this.openingDate = openingDate;
        this.ownerId = ownerId;
    }

    void setBalance(int money) {
        this.balance = money;
    }

    public int getBalance() {
        return this.balance;
    }

    public Date getOpeningDate() {
        return this.openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public UUID getDepositId() {
        return this.depositId;
    }

    void setDepositId() {
        this.depositId = UUID.randomUUID();
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    double calculateMultiplier(Date withdraw_date) {
        Calendar cal = Calendar.getInstance();
        if (!this.openingDate.before(withdraw_date)) {
            throw new IllegalArgumentException("Wrong withdraw data (must always be after opening");
        } else {
            cal.setTime(this.openingDate);

            int c;
            for (c = 0; cal.getTime().before(withdraw_date); ++c) {
                cal.add(2, 1);
            }

            double percent = 0.095D;
            return Math.pow(1.0D + percent, (double) (c - 1));
        }
    }

    public int withdrawBalance(Date withdraw_date) {
        return (int) (this.balance * this.calculateMultiplier(withdraw_date));
    }
}
