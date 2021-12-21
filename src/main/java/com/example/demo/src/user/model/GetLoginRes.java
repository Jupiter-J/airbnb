package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
//로그인 jwt 비교
public class GetLoginRes {
    private String name;
    private String phone;
}
