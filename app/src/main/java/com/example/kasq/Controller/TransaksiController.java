package com.example.kasq.Controller;

import com.example.kasq.Model.Transaksi;
import com.example.kasq.Model.User;

import java.util.ArrayList;
import java.util.Random;

public class TransaksiController {
    private static String generateId(){
        Random rand = new Random();
        int a1 =rand.nextInt(9);
        int a2 =rand.nextInt(9);
        int a3 =rand.nextInt(9);
        return "US" + a1 + a2 + a3;
    }
    public Transaksi createTransaksi(String date, String type, String category, String value, String description, User user){
        String id = generateId();
        return new Transaksi(id,date,type,category,value,description,user);
    }
}
