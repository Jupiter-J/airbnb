package com.example.demo.src.house.model;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
//유저리뷰 1-1.
public class GetUserReivewRes1 {

    private int houseIdx;
    private double totalGrade;
    private String totalReivew;
    private String accuranct;
    private String communication;
    private String checkIn;
    private String priceC;
    private String clean;
    private String location;

}
