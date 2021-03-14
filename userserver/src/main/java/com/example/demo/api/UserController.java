package com.example.demo.api;

import com.example.demo.DemoApplication;
import com.example.demo.api.dto.UserBalanceChangeDataFormat;
import com.example.demo.services.user_service.model.User;
import com.example.demo.services.user_service.UserConfig;
import com.example.demo.api.dto.UserDataFormat;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping(value = "/banking/user")
@ComponentScan
public class UserController {

    @Autowired
    private UserConfig userService;

    final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){

        try{
            final List<User> userList = userService.getAllUsers();

            return new ResponseEntity<>(userList, HttpStatus.OK);
        } catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") UUID userId){

        try{
            final User user = userService.getUserById(userId);

            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
        catch (MissingResourceException | NotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @PostMapping(consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserDataFormat userBody) throws ParseException {
        return userService.createUser(userBody);
    }

    @PostMapping("/dummy")
    public ResponseEntity<Void> createDummyUser() throws ParseException {
        userService.createDummyUser();

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/change_balance", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeUserBalance(@Valid @RequestBody UserBalanceChangeDataFormat userBody) throws NotFoundException {
        userService.changeUserBalance(userBody);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RabbitListener(queues = "banking_user_create_queue")
    void listenCreate(UserDataFormat userBody) throws ParseException {
        userService.createUser(userBody);
    }

}
