package com.example.demo.src.house;

import com.example.demo.src.house.model.*;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/app/houses")
public class HouseController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final HouseProvider houseProvider;
    @Autowired
    private final HouseService houseService;
    @Autowired
    private final JwtService jwtService;


    public HouseController(HouseProvider houseProvider, HouseService houseService, JwtService jwtService) {
        this.houseProvider = houseProvider;
        this.houseService = houseService;
        this.jwtService = jwtService;
    }


    @ResponseBody
    @GetMapping("/test/{houseIdx}")
    public BaseResponse<TestHouse> testHouseBaseResponse(@PathVariable("houseIdx") int houseIdx){
        TestHouse testHouse = houseProvider.testHouse(houseIdx);
        return new BaseResponse<>(testHouse);
    }



    @ResponseBody
    @GetMapping("/{houseIdx}")
    //숙소조회
    public BaseResponse<GetHouseRes> getHouseRes(@PathVariable("houseIdx") int houseIdx) throws BaseException{

       try {
           GetHouseRes getHouseRes = houseProvider.getHouseRes(houseIdx);
           return new BaseResponse<>(getHouseRes);
       }catch (BaseException exception){
           return new BaseResponse<>((exception.getStatus()));
       }
    }

    @ResponseBody
    @GetMapping("")
    //숙소검색
    public BaseResponse<List<GetAllHousesRes>> getAllhouse(@RequestParam(required = false) String city, int bedNum) throws BaseException{

        try {
            List<GetAllHousesRes> getAllHousesRes = houseProvider.getAllhouse(city, bedNum);
            return new BaseResponse<>(getAllHousesRes);

        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }



    }




    @ResponseBody
    @GetMapping("/{houseIdx}/reivew")
    //유저리뷰조회 1-2.
    public BaseResponse<List<GetUserReivewRes>> getUserReivew2 (@PathVariable("houseIdx") int houseIdx) throws BaseException{

        try {
            List<GetUserReivewRes> getUsereiveww = houseProvider.getUserReivew2(houseIdx);
            return new BaseResponse<>(getUsereiveww);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }









}