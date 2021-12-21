package com.example.demo.src.house.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetAllHousesRes {

    private String houseTitle;
    private String isLiked;
    private String houseImage;
    private String city;
    private int maxPeople;
    private int bedNum;
    private int bedroomNum;
    private int bathNum;
    private String superHost;
    private String dayPrice;

}
