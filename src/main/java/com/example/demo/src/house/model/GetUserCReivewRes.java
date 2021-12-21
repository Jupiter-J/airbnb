package com.example.demo.src.house.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
//공통 model
public class GetUserCReivewRes {

    private int houseIdx;
    private double totalGrade;
    private String totalReivew;
    private String accuranct;
    private String communication;
    private String checkIn;
    private String priceC;
    private String clean;
    private String location;
    private String name;
    private String uimage;
    private String date;
    private String urContext;

}
