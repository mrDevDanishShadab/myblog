package com.myblog7.controller;

import com.myblog7.entity.User;
import com.myblog7.payload.JWTResponse;
import com.myblog7.payload.LoginDto;
import com.myblog7.payload.SignUpDto;
import com.myblog7.repository.UserRepository;
import com.myblog7.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    //http://localhost:8080/api/auth/signup
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){

        Boolean emailExist = userRepository.existsByEmail(signUpDto.getEmail());
        if(emailExist){
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }

        Boolean usernameExists = userRepository.existsByUsername(signUpDto.getUsername());
        if (usernameExists){
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setName(signUpDto.getName());
        user.setEmail(signUpDto.getEmail());
        user.setUsername(signUpDto.getUsername());
        user.setPassword(BCrypt.hashpw(signUpDto.getPassword(), BCrypt.gensalt(10)));
        userRepository.save(user);
        return new ResponseEntity<>("User is successfully registered", HttpStatus.CREATED);
    }


    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        String token = userService.verifyLogin(loginDto);
        if (token!=null){
            JWTResponse response = new JWTResponse();
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/profile")
    public User getCurrentProfile(@AuthenticationPrincipal User user){
        return user;
    }

    }


