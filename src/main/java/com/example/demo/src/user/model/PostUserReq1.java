package com.example.demo.src.user.model;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
//회원가입 요청
public class PostUserReq1 {
    private String name;
    private String phone;
}
