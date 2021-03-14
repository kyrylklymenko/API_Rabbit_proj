package com.example.demo.services.user_service;

import com.example.demo.api.dto.UserBalanceChangeDataFormat;
import com.example.demo.api.dto.UserDataFormat;
import com.example.demo.services.user_service.model.User;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

public interface UserInterface {
    User addUser(User newUser);
    public void removeUser(UUID id) throws NotFoundException;
    public User getUserById(UUID id) throws NotFoundException;
    public List<User> getAllUsers();

    ResponseEntity<Void> createUser(UserDataFormat userBody) throws ParseException;

    void createDummyUser() throws ParseException;

    void changeUserBalance(UserBalanceChangeDataFormat userBody) throws NotFoundException;
}
