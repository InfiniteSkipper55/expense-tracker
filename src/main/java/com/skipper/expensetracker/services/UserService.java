package com.skipper.expensetracker.services;

import com.skipper.expensetracker.entities.User;

import java.util.List;

public interface UserService {
    User getUserByUserId(Long userId);

    List<User> getAllUsers();

    User createUser(User user);

    void deleteUser(Long userId);
}
