package com.stayfinder.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.stayfinder.backend.service.UserService;

import com.stayfinder.backend.dto.RegisterRequest;

import com.stayfinder.backend.dto.LoginRequest;

import com.stayfinder.backend.dto.LoginResponse;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.http.ResponseEntity;

import com.stayfinder.backend.User;

import java.util.List;

import com.stayfinder.backend.dto.RegisterResponse;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
       this.userService = userService;
    }

    @PostMapping("/register")
    public RegisterResponse registerUser(@RequestBody RegisterRequest request) {
       return userService.registerUser(request);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
       return userService.getAllUsers();
   }

   @GetMapping("/users/{id}")
   public ResponseEntity<User> getUserById(@PathVariable Long id) {
      User user = userService.getUserById(id);

      if (user != null){
         return ResponseEntity.ok(user);
      }

      return ResponseEntity.notFound().build();
   }

   @PutMapping("/users/{id}")
   public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {

    User updatedUser = userService.updateUser(id, user);

    if (updatedUser != null) {
        return ResponseEntity.ok(updatedUser);
    }

    return ResponseEntity.notFound().build();
   }

   @DeleteMapping("/users/{id}")
   public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

    boolean deleted = userService.deleteUser(id);

    if (deleted) {
        return ResponseEntity.noContent().build();
    }

    return ResponseEntity.notFound().build();
   }

   @PostMapping("/login")
   public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

    LoginResponse response = userService.loginUser(request);

    if (response != null) {
        return ResponseEntity.ok(response);
    }

    return ResponseEntity.status(401).build();
   }

   
}