package com.example.demo.api.REST;

import com.example.demo.api.dto.cardDTO.CardCloseDataFormat;
import com.example.demo.api.dto.cardDTO.CardDataFormat;
import com.example.demo.api.dto.cardDTO.CardOperationDataFormat;
import com.example.demo.services.card_service.CreditCard;
import javassist.NotFoundException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;
import java.util.MissingResourceException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/creditcard")
public class CardController {
    private final String cardURL = "http://cardserver:8094/banking/creditcard";
    private final RestTemplate template = new RestTemplate();

    @GetMapping
    public ResponseEntity<List<CreditCard>> getAllCreditCards(){
        try{
            HttpEntity<List<CreditCard>> response = template.exchange(cardURL, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CreditCard>>() {});
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        } catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<CreditCard> getCard(@PathVariable("id") UUID cardId){

        try{


            return template.getForEntity(cardURL+"/"+cardId.toString(), CreditCard.class);
        }
        catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @PostMapping(consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCard(@Valid @RequestBody CardDataFormat cardBody){
        try{
            template.exchange(cardURL, HttpMethod.POST, new HttpEntity<>(cardBody), Void.class);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value="/change_balance", consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeCreditCardBalance(@Valid @RequestBody CardOperationDataFormat cardBody) throws NotFoundException {

        template.exchange(cardURL+"/change_balance", HttpMethod.POST, new HttpEntity<>(cardBody), Void.class);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> closeCard(@RequestBody CardCloseDataFormat cardCloseBody) throws NotFoundException {
        template.exchange(cardURL, HttpMethod.POST, new HttpEntity<>(cardCloseBody), Void.class);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
