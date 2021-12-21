package com.example.demo.src.house.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
//유저리뷰조회 1-2.
public class GetUserReivewRes {

    private String name;
    private String uimage;
    private String date;
    private String urContext;
}
