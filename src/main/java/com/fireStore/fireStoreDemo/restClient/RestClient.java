package com.fireStore.fireStoreDemo.restClient;


import com.fireStore.fireStoreDemo.entity.Users;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class RestClient {

    private static RestTemplate restTemplate = new RestTemplate();




    private static String getUser = "http://localhost:8080/firestore/api/getUserById/2";


    public static void main(String[] args) {

        callGetAllUsersAPI();

    }

    private static void callGetAllUsersAPI() {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> parameters = new HttpEntity<>("parameters", headers);


        ResponseEntity<Users> result = restTemplate.getForEntity(getUser, Users.class);

        System.out.println(result.getBody());
    }

}

