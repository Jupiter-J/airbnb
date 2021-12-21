package com.example.demo.src.house;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.house.model.*;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import com.sun.tools.javac.comp.Resolve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class HouseProvider {

    private final HouseDao houseDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public HouseProvider(HouseDao houseDao, JwtService jwtService) {
        this.houseDao = houseDao;
        this.jwtService = jwtService;
    }


    public TestHouse testHouse (int houseIdx){
        TestHouse testHouse = houseDao.testHouse(houseIdx);
        return testHouse;
    }



    //숙소정보
    public GetHouseRes getHouseRes (int houseIdx) throws BaseException{
        try {
            GetHouseRes getHouseRes = houseDao.getHouseRes(houseIdx);
            return getHouseRes;

        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //숙소검색
    public List<GetAllHousesRes> getAllhouse(String city, int bedNum) throws BaseException{
        List<GetAllHousesRes> getAllHousesRes = houseDao.getAllhouse(city, bedNum);
        return getAllHousesRes;
    }



    /**
     쿼리 2개 합치기
     */

    //유저리뷰 1-1.
    public GetUserReivewRes1 getUserReivew1(int houseIdx) throws BaseException{
        try {
            GetUserReivewRes1 getUserReivew1 = houseDao.getUserReivew1(houseIdx);
            return getUserReivew1;

        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //유저리뷰조회 1-2.
    public List<GetUserReivewRes> getUserReivew2 (int houseIdx) throws BaseException{
        //1.
        List<GetUserReivewRes> getUserReivewRes = houseDao.getUserReivew2(houseIdx);
        System.out.println(getUserReivewRes.get(0).getName());

        try {

            List<GetUserReivewRes> getUserReivew2 = houseDao.getUserReivew2(houseIdx);
            return getUserReivew2;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }










}