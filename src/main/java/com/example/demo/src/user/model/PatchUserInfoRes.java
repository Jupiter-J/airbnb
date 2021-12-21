package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
//개인정보수정 응답
public class PatchUserInfoRes {
    private String message;
//    private int userIdx;
}
