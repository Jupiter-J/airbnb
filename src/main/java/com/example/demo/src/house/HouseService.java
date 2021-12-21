package com.example.demo.src.house;

import com.example.demo.config.BaseResponse;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

// Service Create, Update, Delete 의 로직 처리
@Service
public class HouseService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final HouseDao houseDao;
    private final HouseProvider houseProvider;
    private final JwtService jwtService;


    @Autowired
    public HouseService(HouseDao houseDao, HouseProvider houseProvider, JwtService jwtService) {
        this.houseDao = houseDao;
        this.houseProvider = houseProvider;
        this.jwtService = jwtService;
    }













}