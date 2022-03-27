package com.example.kasq.Controller;

import com.example.kasq.Model.User;

import java.util.Random;

public class UserController {
    private static String generateId(){
        Random rand = new Random();
        int a1 =rand.nextInt(9);
        int a2 =rand.nextInt(9);
        int a3 =rand.nextInt(9);
        return "US" + a1 + a2 + a3;
    }
    public static User createUser(String userName, String userPassword){
        String userId = generateId();
        return new User(userId,userName,userPassword);
    }
}
