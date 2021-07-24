package com.priyam.transactionalredis.controller;

import com.priyam.transactionalredis.model.User;
import com.priyam.transactionalredis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<?> addDetails(@RequestBody User user) {
        userService.addDetails(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<?> getDetails(@PathVariable("userId") Integer userId) {
        return new ResponseEntity<>(userService.getDetails(userId), HttpStatus.OK);
    }

    @GetMapping("nonTransactional")
    public ResponseEntity<?> persistDetailsNonTransactional() {
        userService.persistNonTransactional();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("transactional")
    public ResponseEntity<?> persistDetailsTransactional() {
        userService.persistTransactional();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
