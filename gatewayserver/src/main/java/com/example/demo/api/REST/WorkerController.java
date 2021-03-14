package com.example.demo.api.REST;

import com.example.demo.api.dto.workerDTO.WorkerDataFormat;
import com.example.demo.api.dto.workerDTO.WorkerWageDataFormat;
import com.example.demo.services.worker_service.Worker;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;
import java.util.MissingResourceException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/worker")
public class WorkerController {

    private final String workerURL = "http://workerserver:8091/banking/worker";
    private final RestTemplate template = new RestTemplate();

    @GetMapping
    public ResponseEntity<List<Worker>> getAllWorkers(){
        try{
            HttpEntity<List<Worker>> response = template.exchange(workerURL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Worker>>() {});
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
        } catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Worker> getWorker(@PathVariable("id") UUID workerId){

        try{
            return template.getForEntity(workerURL+"/"+workerId.toString(), Worker.class);
        }
        catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @PostMapping(consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createWorker(@Valid @RequestBody WorkerDataFormat workerBody){
        try{
            template.exchange(workerURL, HttpMethod.POST, new HttpEntity<>(workerBody), Void.class);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/add_wage", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addWage(@Valid @RequestBody WorkerWageDataFormat wageBody){
        template.exchange(workerURL+"/add_wage", HttpMethod.PUT, new HttpEntity<>(wageBody), Void.class);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
