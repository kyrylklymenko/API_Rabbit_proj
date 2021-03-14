package com.example.demo.api.REST;

import com.example.demo.api.dto.depositDTO.DepositDataFormat;
import com.example.demo.api.dto.depositDTO.DepositWithdrawDataFormat;
import com.example.demo.services.deposit_service.DepositAccount;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;
import java.util.MissingResourceException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/deposit")
public class DepositController {

    private final RestTemplate template = new RestTemplate();
    private final String depositURL = "http://depositserver:8093/banking/deposit";

    @GetMapping
    public ResponseEntity<List<DepositAccount>> getAllDeposits(){
        try{
            HttpEntity<List<DepositAccount>> response = template.exchange(depositURL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<DepositAccount>>() {});
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        } catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<DepositAccount> getDeposit(@PathVariable("id") UUID depositId){

        try{
            return template.getForEntity(depositURL+"/"+depositId.toString(), DepositAccount.class);
        }
        catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @PostMapping(consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createDeposit(@Valid @RequestBody DepositDataFormat depositBody){
        try{
            template.exchange(depositURL, HttpMethod.POST, new HttpEntity<>(depositBody), Void.class);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public ResponseEntity<Void> withdrawDeposit(@Valid @RequestBody DepositWithdrawDataFormat depositBody){
        template.exchange(depositURL, HttpMethod.PUT, new HttpEntity<>(depositBody), Void.class);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}