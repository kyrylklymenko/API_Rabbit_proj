package com.example.demo.api;

import com.example.demo.services.user_service.UserConfig;
import com.example.demo.services.user_service.model.User;
import com.example.demo.services.user_service.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import javassist.NotFoundException;
import org.example.demo.*;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserConfig userService;

    @Autowired
    private UserRepository repository;

    @Override
    public void getAll(GetAllRequest request, StreamObserver<GetAllResponse> responseObserver) {
//        List<User> users = userService.getAllUsers();
//        List<UserResponse> userResponses = new ArrayList<>();
//        for (User user: users) userResponses.add(UserResponse.newBuilder().
//                setId(user.getUserId().toString()).
//                setName(user.getUserName()).
//                setSurname(user.getUserSurname()).
//                setPhoneNum(user.getUserPhoneNumber()).
//                setBirthdayDate(new SimpleDateFormat("yyyy-mm-dd").format(user.getBirthdayDate())).
//                setPassportId(user.getUserPassportId()).
//                setUserTin(user.getUserTin()).
//                setUserEmail(user.getUserEmail()).build());
//
//        GetAllResponse response = GetAllResponse.newBuilder().
//                addAllUser(userResponses).build();

        List<UserResponse> userResponses = new ArrayList<>();
        userResponses.add(UserResponse.newBuilder().
                setId("fb6214cd-65e8-4cbb-9f13-adb55a7ca17e").
                setName("Abel").
                setSurname("Tesfaye").
                setPhoneNum("12345").
                setBirthdayDate("2020-01-01").
                setPassportId("888666").
                setUserTin("51233").
                setUserEmail("Weekend@gmail.com").build());

        GetAllResponse response = GetAllResponse.newBuilder().
                addAllUser(userResponses).build();


        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void get(GetOneRequest request, StreamObserver<UserResponse> responseObserver){
        UserResponse response;

        try{
            User user = userService.getUserById(UUID.fromString(request.getId()));

//           response = UserResponse.newBuilder().
//                    setId(user.getUserId().toString()).
//                    setName(user.getUserName()).
//                    setSurname(user.getUserSurname()).
//                    setPhoneNum(user.getUserPhoneNumber()).
//                    setBirthdayDate(new SimpleDateFormat("yyyy-mm-dd").format(user.getBirthdayDate())).
//                    setPassportId(user.getUserPassportId()).
//                    setUserTin(user.getUserTin()).
//                    setUserEmail(user.getUserEmail()).build();

            response = UserResponse.newBuilder().
                    setId("fb6214cd-65e8-4cbb-9f13-adb55a7ca17e").
                    setName("Abel").
                    setSurname("Tesfaye").
                    setPhoneNum("12345").
                    setBirthdayDate("2020-01-01").
                    setPassportId("888666").
                    setUserTin("51233").
                    setUserEmail("Weekend@gmail.com").build();

        } catch (NotFoundException e) {
            response = UserResponse.newBuilder().
                    setId("User not found").build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void create(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse response;

        try{
            User user = new User(request.getName(),
                    request.getSurname(),
                    request.getPhoneNum(),
                    request.getBirthdayDate(),
                    request.getPassportId(),
                    request.getUserTin(),
                    request.getUserEmail());
            System.out.println(user.getUserName()+ " "+
                    user.getUserSurname()+ " " +
                    user.getUserPhoneNumber()+ " " +
                    user.getBirthdayDate()+ " " +
                    user.getUserPassportId()+ " " +
                    user.getUserTin()+ " " +
                    user.getUserEmail()) ;
//           User user = userService.addUser(new User(request.getName(),
//                   request.getSurname(),
//                   request.getPhoneNum(),
//                   request.getBirthdayDate(),
//                   request.getPassportId(),
//                   request.getUserTin(),
//                   request.getUserEmail()));
            //userService.addUser(user);

            response = UserResponse.newBuilder().
                   setId(user.getUserId().toString()).
                   setName(user.getUserName()).
                   setSurname(user.getUserSurname()).
                   setPhoneNum(user.getUserPhoneNumber()).
                   setBirthdayDate(new SimpleDateFormat("yyyy-mm-dd").format(user.getBirthdayDate())).
                   setPassportId(user.getUserPassportId()).
                   setUserTin(user.getUserTin()).
                   setUserEmail(user.getUserEmail()).build();
       } catch (ParseException e) {
            response = UserResponse.newBuilder().
                    setId("Incorrect date format (must be yyyy-mm-dd").build();
       }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void addMoney(AddMoneyRequest request, StreamObserver<UserResponse> responseObserver){
        UserResponse response;
        try{
            User user = userService.changeBalance(UUID.fromString(request.getId()), request.getBalance());

            response = UserResponse.newBuilder().
                    setId(user.getUserId().toString()).
                    setName(user.getUserName()).
                    setSurname(user.getUserSurname()).
                    setPhoneNum(user.getUserPhoneNumber()).
                    setBirthdayDate(new SimpleDateFormat("yyyy-mm-dd").format(user.getBirthdayDate())).
                    setPassportId(user.getUserPassportId()).
                    setUserTin(user.getUserTin()).
                    setUserEmail(user.getUserEmail()).build();
        } catch (NotFoundException e) {
            response = UserResponse.newBuilder().
                    setId("User not found").build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void withdrawMoney(WithdrawMoneyRequest request, StreamObserver<UserResponse> responseObserver){
        UserResponse response;

        try{
            User user = userService.changeBalance(UUID.fromString(request.getId()), -request.getBalance());

            response = UserResponse.newBuilder().
                setId(user.getUserId().toString()).
                setName(user.getUserName()).
                setSurname(user.getUserSurname()).
                setPhoneNum(user.getUserPhoneNumber()).
                setBirthdayDate(new SimpleDateFormat("yyyy-mm-dd").format(user.getBirthdayDate())).
                setPassportId(user.getUserPassportId()).
                setUserTin(user.getUserTin()).
                setUserEmail(user.getUserEmail()).build();
        } catch (NotFoundException e) {
            response = UserResponse.newBuilder().
                    setId("User not found").build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void delete(GetOneRequest request, StreamObserver<DeleteResponse> responseObserver) {
        String deleteResponse;
        try{
            userService.removeUser(UUID.fromString(request.getId()));
            deleteResponse = "User deleted successfully";
        } catch (NotFoundException e) {
            deleteResponse = "User not found";
        }

        DeleteResponse response = DeleteResponse.newBuilder().
                setResponse(deleteResponse).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
