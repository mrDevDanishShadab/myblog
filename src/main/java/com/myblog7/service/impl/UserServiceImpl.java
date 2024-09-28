package com.myblog7.service.impl;

import com.myblog7.entity.User;
import com.myblog7.payload.LoginDto;
import com.myblog7.repository.UserRepository;
import com.myblog7.service.UserService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private JWTService jwtService;

    private  UserRepository userRepository;

    public UserServiceImpl(JWTService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    public String verifyLogin(LoginDto loginDto) {
        Optional<User> opUser = userRepository.findByUsernameOrEmail(loginDto.getUsername(), loginDto.getEmail());
        if (opUser.isPresent()){
            User user = opUser.get();
            if (BCrypt.checkpw(loginDto.getPassword(), user.getPassword())){
               return jwtService.generateToken(user);
            }
        }
        return null;
    }
}
