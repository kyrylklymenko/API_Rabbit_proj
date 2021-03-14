package com.example.demo.api.dto.userDTO;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@Builder
@ToString
public class UserDataFormat implements Serializable{

    @NotNull
    String userName;

    @NotNull
    String userSurname;

    @NotNull
    String phoneNumber;

    @NotNull
    String birthdayDate;

    @NotNull
    String passportId;

    @NotNull
    String userTin;

    @NotNull
    String userEmail;
}
