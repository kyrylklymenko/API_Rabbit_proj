package com.example.demo.api.REST;

import com.example.demo.RabbitConfig.BrokerConfig;
import com.example.demo.api.dto.userDTO.UserBalanceChangeDataFormat;
import com.example.demo.services.user_service.model.User;
import com.example.demo.api.dto.userDTO.UserDataFormat;
import javassist.NotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(value = "/user")
@ComponentScan
public class UserController {

    private final String userURL = "http://userserver:8090/banking/user";
    private final RestTemplate template = new RestTemplate();

    @Autowired
    private RabbitTemplate brokerTemplate;

    @GetMapping
    public ResponseEntity<List<User>> getAll(){

        try{
            HttpEntity<List<User>> response = template.exchange(userURL, HttpMethod.GET, null,  new ParameterizedTypeReference<List<User>>() {});
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        } catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable("id") UUID userId){

        try{
            return template.getForEntity(userURL+"/"+userId.toString(), User.class);
        }
        catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @PostMapping(consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserDataFormat userBody) {
        try{
            template.exchange(userURL, HttpMethod.POST, new HttpEntity<>(userBody), Void.class);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/dummy")
    public ResponseEntity<Void> createDummyUser() {
        template.exchange(userURL+"/dummy", HttpMethod.POST, new HttpEntity<>(Void.class), Void.class);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/queue/create")
    public String queueCreateUser (@Valid @RequestBody UserDataFormat userBody){
        brokerTemplate.convertAndSend(BrokerConfig.USER_EXCHANGE, BrokerConfig.USERS_ROUTING_KEY, "Hi :)");
        return "Message sent!";
    }

    @PostMapping("/queue/string_test")
    public String queueStringTestUser (@Valid @RequestBody String string){
        brokerTemplate.convertAndSend(BrokerConfig.USER_EXCHANGE, BrokerConfig.USERS_ROUTING_KEY, string);
        return "Test string message sent!";
    }

    @PostMapping("/queue/balance")
    public String queueChangeUserBalance (@Valid @RequestBody UserBalanceChangeDataFormat userBody) throws NotFoundException {
        //brokerTemplate.convertAndSend(BrokerConfig.USER_EXCHANGE, BrokerConfig.USERS_ROUTING_KEY, userBody);
        brokerTemplate.convertAndSend(BrokerConfig.USER_EXCHANGE, BrokerConfig.USERS_ROUTING_KEY, "Hello :)");
        return "Message sent!";
    }

    @PutMapping(value = "/change_balance", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeUserBalance(@Valid @RequestBody UserBalanceChangeDataFormat userBody) throws NotFoundException {
        template.exchange(userURL+"/change_balance", HttpMethod.PUT, new HttpEntity<>(userBody), Void.class);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
