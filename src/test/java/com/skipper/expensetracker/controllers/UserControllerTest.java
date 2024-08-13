package com.skipper.expensetracker.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.skipper.expensetracker.entities.User;
import com.skipper.expensetracker.services.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_NullUser() {
        ResponseEntity<User> response = userController.registerUser(null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testRegisterUser_ValidUser() {
        User user = new User();
        User createdUser = new User();
        when(userService.createUser(user)).thenReturn(createdUser);

        ResponseEntity<User> response = userController.registerUser(user);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdUser, response.getBody());
    }

    @Test
    void testRegisterUser_UserServiceReturnsNull() {
        User user = new User();
        when(userService.createUser(user)).thenReturn(null);

        ResponseEntity<User> response = userController.registerUser(user);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetUserById_NullUserId() {
        ResponseEntity<User> response = userController.getUserById(null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetUserById_ValidUserId_UserFound() {
        Long userId = 1L;
        User user = new User();
        when(userService.getUserByUserId(userId)).thenReturn(user);

        ResponseEntity<User> response = userController.getUserById(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testGetUserById_ValidUserId_UserNotFound() {
        Long userId = 1L;
        when(userService.getUserByUserId(userId)).thenReturn(null);

        ResponseEntity<User> response = userController.getUserById(userId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetUserById_ExceptionThrown() {
        Long userId = 1L;
        when(userService.getUserByUserId(userId)).thenThrow(new RuntimeException());

        ResponseEntity<User> response = userController.getUserById(userId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}