package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
//회원가입 응답
public class PostUserRes1 {
    private String status;
    private String jwt;
    private int userIdx;

}
