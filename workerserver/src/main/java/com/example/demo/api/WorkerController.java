package com.example.demo.api;

import com.example.demo.DemoApplication;
import com.example.demo.api.dto.WorkerWageDataFormat;
import com.example.demo.services.worker_service.model.Worker;
import com.example.demo.services.worker_service.WorkerConfig;
import com.example.demo.api.dto.WorkerDataFormat;
import javassist.NotFoundException;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;
import java.util.MissingResourceException;
import java.util.UUID;

@RestController
@RequestMapping("/banking/worker")
@ComponentScan
public class WorkerController {

    @Autowired
    private WorkerConfig workerService;

    final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    @GetMapping
    public ResponseEntity<List<Worker>> getAllWorkers(){
        try{
            final List<Worker> userList = workerService.getAllWorkers();

            return new ResponseEntity<>(userList, HttpStatus.OK);
        } catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Worker> getWorker(@PathVariable("id") UUID workerId){

        try{
            final Worker user = workerService.getWorkerById(workerId);

            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
        catch (MissingResourceException | NotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @PostMapping(consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createWorker(@Valid @RequestBody WorkerDataFormat workerBody) throws ParseException {
        try{
            Worker newWorker = new Worker(workerBody.getWorkerName(), workerBody.getWorkerSurname());
            workerService.addWorker(newWorker);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/add_wage", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addWage(@Valid @RequestBody WorkerWageDataFormat wageBody) throws NotFoundException {
        workerService.addWorkerWage(wageBody);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
