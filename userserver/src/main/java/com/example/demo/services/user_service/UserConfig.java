package com.example.demo.services.user_service;

import com.example.demo.api.dto.UserBalanceChangeDataFormat;
import com.example.demo.api.dto.UserDataFormat;
import com.example.demo.services.user_service.repository.UserRepository;
import com.example.demo.services.user_service.model.User;
import javassist.NotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public final class UserConfig implements UserInterface {

    @Autowired
    private UserRepository repository;

    private RabbitTemplate template;

    public User addUser(User newUser){
        System.out.println(Objects.isNull(repository));
        return repository.save(newUser);
    }

    public void addUserGrpc(User newUser){
        repository.save(newUser);
    }


    public void removeUser(UUID id) throws NotFoundException {
        User toRemove = getUserById(id);
        repository.delete(toRemove);
    }

    public User getUserById(UUID id) throws NotFoundException {
        System.out.println("\n\n\n\n\n"+id + "\n\n\n\n\n");
        Optional<User> user = repository.findById(id);
        if (user.isPresent()){
            return user.get();
        }
        else {
            throw new NotFoundException(String.format("User ID %s does not exist", id));
        }
    }

    public List<User> getAllUsers(){
        List<User> userList = (List<User>) repository.findAll();

        return userList;
    }

    @Override
    public ResponseEntity<Void> createUser(UserDataFormat userBody) throws ParseException {
        try{
            //Date userDate = new SimpleDateFormat("yyyy-MM-dd").parse(userBody.getBirthdayDate());
            User newUser = new User(userBody.getUserName(), userBody.getUserSurname(), userBody.getPhoneNumber(),
                    userBody.getBirthdayDate(), userBody.getPassportId(), userBody.getUserTin(), userBody.getUserEmail());
            addUser(newUser);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void createDummyUser() throws ParseException {
        Date birthdayDate = new GregorianCalendar(2000, Calendar.JANUARY, 22).getTime();
        User dummyUser = new User( "Otto",
                "Bismarck", "+3801234511", "2000-01-22","5123",
                "2961293318", "bism@arck.com");
        addUser(dummyUser);
    }

    @Override
    public void changeUserBalance(UserBalanceChangeDataFormat userBody) throws NotFoundException {
        User user = getUserById(userBody.getUserId());
        user.setUserBalance(user.getUserBalance() + userBody.getBalanceChange());

        removeUser(userBody.getUserId());
        addUser(user);
    }

    public User changeBalance(UUID id, int diff) throws NotFoundException {
        User user = getUserById(id);
        user.setUserBalance(user.getUserBalance() + diff);
        return user;
    }

}
