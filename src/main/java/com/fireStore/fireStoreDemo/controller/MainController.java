package com.fireStore.fireStoreDemo.controller;


import com.fireStore.fireStoreDemo.entity.Users;
import com.fireStore.fireStoreDemo.service.UsersService;
import com.google.cloud.firestore.WriteBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("firestore/api")
public class MainController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/getUsers")
    public List<Users> getUser() throws ExecutionException, InterruptedException {
        return usersService.getUsers();
    }

    @GetMapping(value = "/getUserById/{id}")
    public Users getUserById(@PathVariable("id") String id) throws ExecutionException, InterruptedException {
        return usersService.getUserById(id);
    }


    @GetMapping("/getUpdatedTime/{id}")
    public String getUpdatedTime(@PathVariable("id") String id) throws ExecutionException, InterruptedException {
        return usersService.getUpdatedTime(id);
    }

    @PostMapping("/saveUser")
    public String saveUser(@RequestBody Users users) throws ExecutionException, InterruptedException {

        return usersService.saveUser(users);

    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestBody Users users) throws ExecutionException, InterruptedException {

        return usersService.saveUser(users);

    }

    @DeleteMapping("deleteUserById/{id}")
    public String deleteUserById(@PathVariable("id") String id) throws ExecutionException, InterruptedException {
        return usersService.deleteUserById(id);
    }

    @GetMapping("onch/{id}")
    public void onch(@PathVariable("id") String id) throws ExecutionException, InterruptedException {
        usersService.onChange(id);
    }

    @GetMapping("onchange")
    public void onchange() throws ExecutionException, InterruptedException {
        usersService.onChange();
    }

    @GetMapping("batchWrite")
    public WriteBatch batchWrite() throws ExecutionException, InterruptedException {
        return usersService.batchWrite();
    }

    @GetMapping("batchOperations")
    public WriteBatch batchOperations() throws ExecutionException, InterruptedException {
        return usersService.batchOperations();
    }


    @GetMapping("batchTask")
    public String batchTask() throws ExecutionException, InterruptedException {
        return usersService.batchTask();
    }


    @GetMapping("deleteRecreate")
    public String deleteRecreate() throws ExecutionException, InterruptedException {
        return usersService.deleteAndRecreate();
    }


}
