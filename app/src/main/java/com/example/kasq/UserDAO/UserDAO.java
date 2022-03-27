package com.example.kasq.UserDAO;

import com.example.kasq.Model.User;

public interface UserDAO {
    public Boolean registerUser(User user);
    public User loginUser(String username, String password);
}
