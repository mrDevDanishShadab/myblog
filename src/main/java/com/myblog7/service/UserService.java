package com.myblog7.service;

import com.myblog7.payload.LoginDto;

public interface UserService {

    public String verifyLogin(LoginDto loginDto);
}
