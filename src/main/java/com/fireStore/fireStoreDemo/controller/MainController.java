package com.fireStore.fireStoreDemo.controller;


import com.fireStore.fireStoreDemo.entity.Users;
import com.fireStore.fireStoreDemo.service.UsersService;
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

    @GetMapping("/getUserById/{id}")
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

}
