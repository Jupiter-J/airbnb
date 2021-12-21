package com.example.demo.src.house;



import com.example.demo.config.BaseResponse;
import com.example.demo.src.house.model.*;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class HouseDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public TestHouse testHouse(int houseIdx) {
        String testHouseQuery = " select House.houseTitle from House where houseIdx = ? ";
        return this.jdbcTemplate.queryForObject(testHouseQuery,
                (rs, rwNum) -> new TestHouse(
                        rs.getString("houseTitle")), houseIdx
        );
    }




    //숙소조회
    public  GetHouseRes getHouseRes(int houseIdx){
        String getHouseQuery = "select  houseTitle\n" +
                "       , if(isLiked is not null, 'V', isLiked) as isLiked\n" +
                "       , houseImage\n" +
                "       , city\n" +
                "       , houseType, concat('호스트:', hname) as hostname\n" +
                "       , himage\n" +
                "       , maxPeople , bedNum, bedroomNum, bathNum\n" +
                "       , if(superHost is not null, 'V', superHost) as superHost\n" +
                "       , round(sum(UR.accuranct + UR.clean + UR.communication + UR.checkIn+ UR.priceC+ UR.location )\n" +
                "            / count(userReiviewIdx) /6,2) as totalGrade\n" +
                "       , concat('후기 ',count(UR.urContext),'개') as totalReivew\n" +
                "       from House h\n" +
                "join (select hostIdx, hname, himage from Host) as v\n" +
                "on h.houseIdx = v.hostIdx\n" +
                "left join UserReview UR on h.houseIdx = UR.houseIdx\n" +
                "where hostIdx =?";
        return this.jdbcTemplate.queryForObject(getHouseQuery,
                (rs, rowNum) -> new GetHouseRes(
                        rs.getString("houseTitle"),
                        rs.getString("isLiked"),
                        rs.getString("houseImage"),
                        rs.getString("city"),
                        rs.getString("houseType"),
                        rs.getString("hostname"),
                        rs.getString("himage"),
                        rs.getInt("maxPeople"),
                        rs.getInt("bedNum"),
                        rs.getInt("bedroomNum"),
                        rs.getInt("bathNum"),
                        rs.getString("superHost"),
                        rs.getDouble("totalGrade"),
                        rs.getString("totalReivew")) , houseIdx
        );

    }

    //숙소 검색 ******
    public List<GetAllHousesRes> getAllhouse(String city, int bedNum){
        String getAllhousesQuery = "select  houseTitle\n" +
                "       , if(isLiked is not null, 'V', isLiked) as isLiked\n" +
                "       , houseImage\n" +
                "       , city\n" +
                "       , maxPeople , bedNum, bedroomNum, bathNum\n" +
                "       , if(superHost is not null, 'V', superHost) as superHost\n" +
                "       , concat('$', format(dayPrice, 0), '/박') as dayPrice\n" +
                "from House\n" +
                "join (select hostIdx, hname, himage from Host) as v\n" +
                "on houseIdx = v.hostIdx\n" +
                "where city like ? and bedNum >= ? ";
        return this.jdbcTemplate.query(getAllhousesQuery,
                (rs, rowNum) -> new GetAllHousesRes(
                        rs.getString("houseTitle"),
                        rs.getString("isLiked"),
                        rs.getString("houseImage"),
                        rs.getString("city"),
                        rs.getInt("maxPeople"),
                        rs.getInt("bedNum"),
                        rs.getInt("bedroomNum"),
                        rs.getInt("bathNum"),
                        rs.getString("superHost"),
                        rs.getString("dayPrice")) , city , bedNum
                );
    }


    //유저리뷰 1-1.
    public GetUserReivewRes1 getUserReivew1(int houseIdx){
        String getUserReivewRes1Query = "select H.houseIdx,\n" +
                "       round(sum(accuranct + UserReview.clean + communication + UserReview.checkIn+ priceC+ location )\n" +
                "           / count(userReiviewIdx) /6,2)                as totalGrade\n" +
                "        ,concat('후기 ',count(urContext),'개')             as totalReivew\n" +
                "        ,concat('정확성: ',round(avg(accuranct),1))        as accuranct\n" +
                "        , concat('의사소통: ', round(avg(communication),1)) as communication\n" +
                "        , concat('체크인: ',round(avg(checkIn),1))         as checkIn\n" +
                "        , concat('가격 대비 만족도: ',round(avg(priceC),1))    as priceC\n" +
                "        , concat('청결도: ',round(avg(clean),1))           as clean\n" +
                "        , concat('위치: ',round(avg(location),1))         as location\n" +
                "\n" +
                "from UserReview\n" +
                "left join House H on H.houseIdx = UserReview.houseIdx\n" +
                "join User U on UserReview.userIdx = U.userIdx\n" +
                "where H.houseIdx = ? ";
        return this.jdbcTemplate.queryForObject(getUserReivewRes1Query,
                (rs, rowNum) -> new GetUserReivewRes1(
                        rs.getInt("houseIdx"),
                        rs.getDouble("totalGrade"),
                        rs.getString("totalReivew"),
                        rs.getString("accuranct"),
                        rs.getString("communication"),
                        rs.getString("checkIn"),
                        rs.getString("priceC"),
                        rs.getString("clean"),
                        rs.getString("location")), houseIdx
        );
    }
    //유저리뷰조회 1-2.
    public List<GetUserReivewRes> getUserReivew2(int houseIdx ){
        String getUserReivewResQuery = "select U.name, U.uimage\n" +
                "     ,date_format(UserReview.updatedAt,'%Y년 %m월') as date\n" +
                "     , urContext from UserReview\n" +
                "join User U on UserReview.userIdx = U.userIdx\n" +
                "where houseIdx = ? ";
        return this.jdbcTemplate.query(getUserReivewResQuery,
                (rs, rowNum) -> new GetUserReivewRes(
                        rs.getString("name"),
                        rs.getString("uimage"),
                        rs.getString("date"),
                        rs.getString("urContext")), houseIdx

        );
    }








}