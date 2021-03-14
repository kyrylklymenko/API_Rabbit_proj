package com.example.demo.services.user_service.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@EnableAutoConfiguration
@Table(name="users", schema = "public")
@NoArgsConstructor
@AllArgsConstructor

public final class User {

    @Id
    @Column(name="uid")
    private UUID userId;


    @Column(name = "balance",
            nullable = false)
    private int userBalance;

    @Column(name = "name")
    private String userName;
    @Column(name="surname")
    private String userSurname;
    @Column(name="phone_num")
    private String userPhoneNumber;

    @Type(type="date")
    @Column(name="birthday_date")
    private Date birthdayDate;

    @Column(name="passport_id")
    private String userPassportId;
    @Column(name="user_tin")
    private String userTin;
    @Column(name="email")
    private String userEmail;


    public User(String name, String surname, String phoneNum, String birthdayDate, String passId,
                String TIN, String email) throws ParseException {
        this.userId = UUID.randomUUID();
        this.userBalance = 100000;
        this.userName = name;
        this.userSurname = surname;
        this.userPhoneNumber = phoneNum;
        this.birthdayDate = new SimpleDateFormat("yyyy-mm-dd").parse(birthdayDate);
        this.userPassportId = passId;
        this.userTin = TIN;
        this.userEmail = email;
    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return this.userId;
    }

    void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getUserSurname() {
        return this.userSurname;
    }

    void setUserPassportId(String userPassportId) {
        this.userPassportId = userPassportId;
    }

    public String getUserPassportId() {
        return this.userPassportId;
    }

    void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserPhoneNumber() {
        return this.userPhoneNumber;
    }

    void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    void setBirthdayDate(Date birthdayDate) {
        this.birthdayDate = birthdayDate;
    }

    public Date getBirthdayDate() {
        return this.birthdayDate;
    }


    void setUserTin(String userTin) {
        this.userTin = userTin;
    }

    public String getUserTin() {
        return this.userTin;
    }

    public void setUserBalance(int userBalance) {
        this.userBalance = userBalance;
    }

    public int getUserBalance() {
        return this.userBalance;
    }


    public String toString() {
        return String.format("Имя: %s %s, UUID: %s баланс: %d", this.userName, this.userSurname, this.userId, this.userBalance);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof User)) return false;
        User extUser = (User) obj;

        return Objects.equals(this.userId, extUser.getUserId());
    }
}