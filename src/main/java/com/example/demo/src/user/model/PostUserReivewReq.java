package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReivewReq {
    private double accuranct;
    private double communication;
    private double checkIn;
    private double priceC;
    private double clean;
    private double location;
    private String urContext;

}
