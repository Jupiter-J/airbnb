package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
//유저정보수정
public class PatchUserInfoReq {

    private int userIdx;
    private String name;
    private String firstname;
    private String gender;
    private String birth;
    private String mail;
    private String phone;

}
