package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserInfoRes {

    private int userIdx;
    private String name;
    private String firstname;
    private String gender;
    private String birth;
    private String mail;
    private String phone;
}
