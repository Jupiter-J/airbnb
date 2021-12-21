package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
//프로필 수정
public class PatchProfileReq {
    private int userIdx;
    private String uimage;
    private String habitation;
    private String language;
}
