package com.myblog7.payload;

import lombok.Data;

@Data
public class LoginDto {

    private String username;
    private String email;
    private String password;

}
