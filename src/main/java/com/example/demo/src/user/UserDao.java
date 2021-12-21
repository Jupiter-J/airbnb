package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //t - 전체 유저 정보 조회
    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select * from UserInfo";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password"))
                );
    }
    //t - 이메일 정보조회
    public List<GetUserRes> getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from UserInfo where email =?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUsersByEmailParams);
    }
    //t - 유저 정보 조회
    public GetUserRes getUser(int userIdx){
        String getUserQuery = "select * from UserInfo where userIdx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUserParams);
    }
    
    //t - 회원가입
    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into UserInfo (userName, ID, password, email) VALUES (?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getUserName(), postUserReq.getId(), postUserReq.getPassword(), postUserReq.getEmail()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    //t - 이메일 확인
    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from UserInfo where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    //t - 유저정보 변경
    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update UserInfo set userName = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserName(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }





    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userIdx, password,email,userName,ID from UserInfo where ID = ?";
        String getPwdParams = postLoginReq.getId();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("ID"),
                        rs.getString("userName"),
                        rs.getString("password"),
                        rs.getString("email")
                ),
                getPwdParams
                );

    }


    /**
     Aribnb API
     */

    //로그인 login =jwt 비교
    public GetLoginRes getLogin (int userIdx){
        String getLoginQuery = "select name, phone from User where userIdx=? ";
        return this.jdbcTemplate.queryForObject(getLoginQuery,
                (rs, rowNum) -> new GetLoginRes(
                        rs.getString("name"),
                        rs.getString("phone")), userIdx
        );
    }


    //회원가입 createUser =jwt 생성
    public int createUser1 (PostUserReq1 postUserReq1){
        String createUserQuery = "insert into User (phone , name) values ( ? , ?) ";
        Object[] createUserParams = new Object[]{postUserReq1.getPhone(), postUserReq1.getName()};
                this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }



    //개인정보 수정
    public int patchInfo(PatchUserInfoReq patchUserInfoReq){
        String patchInfoQuery = "update User set name=?, firstname=?, gender=?\n" +
                "              , birth=?, mail=?, phone=?" +
                "where userIdx= ?";

        Object[] patchInfoParams = new Object[]{patchUserInfoReq.getName(), patchUserInfoReq.getFirstname()
                , patchUserInfoReq.getGender() ,patchUserInfoReq.getBirth(),patchUserInfoReq.getMail(),patchUserInfoReq.getPhone()
                , patchUserInfoReq.getUserIdx()};
        System.out.println(patchUserInfoReq.getUserIdx());
        return this.jdbcTemplate.update(patchInfoQuery, patchInfoParams);
    }



    //프로필 수정
    public int patchProfile(PatchProfileReq patchProfileReq){
        String patchProfileQuery = "update User set uimage=?, habitation= ? \n" +
                "              ,language= ?\n" +
                "where userIdx = ? ";
        Object[] patchProfileParams = new Object[]{ patchProfileReq.getUimage(),patchProfileReq.getHabitation()
                ,patchProfileReq.getLanguage(), patchProfileReq.getUserIdx()};
        return this.jdbcTemplate.update(patchProfileQuery, patchProfileParams);
    }

    //유저프로필 조회
    public GetProfileRes getProfileRes (int userIdx) {
        String getProfileResQuery = "select userIdx\n" +
                "       , concat('안녕하세요. ', name,'입니다') as uProfileTitle\n" +
                "       , date_format(register, '%Y년 가입') as uregister\n" +
                "       , concat('거주지:',habitation) as uhabitation\n" +
                "       , if(certification is not null, 'V', certification) as ucertification\n" +
                "       , if(mail is not null, 'V', mail ) as umail\n" +
                "       , if(phone is not null, 'V', phone ) as uphone\n" +
                "       , if(companyMail is not null, 'V', companyMail ) as ucompanyMail\n" +
                "       , date_format(a.createdAt,'%Y년 %m월' ) as hcreatedAt\n" +
                "       , a.hrContext\n" +
                "       , b.hname\n" +
                "       , concat('회원가입:', date_format(b.hregister, '%Y년') ) as hregister\n" +
                "       , b.himage\n" +
                "       , concat(count(a.hrContext),'개' ) as totalReivew\n" +
                "from User\n" +
                "left join (\n" +
                "    select hostReivewIdx,createdAt, hrContext\n" +
                "    from HostReivew) as a\n" +
                "left join (\n" +
                "    select hostIdx, himage, hname, hregister\n" +
                "    from Host) as b\n" +
                "on a.hostReivewIdx = b.hostIdx\n" +
                "on userIdx = a.hostReivewIdx\n" +
                "where userIdx=? ";
        return this.jdbcTemplate.queryForObject(getProfileResQuery,
                (rs, rowNum) -> new GetProfileRes(
                        rs.getInt("userIdx"),
                        rs.getString("uProfileTitle"),
                        rs.getString("uregister"),
                        rs.getString("uhabitation"),
                        rs.getString("ucertification"),
                        rs.getString("umail"),
                        rs.getString("uphone"),
                        rs.getString("ucompanyMail"),
                        rs.getString("hcreatedAt"),
                        rs.getString("hrContext"),
                        rs.getString("hname"),
                        rs.getString("hregister"),
                        rs.getString("himage"),
                        rs.getString("totalReivew")),userIdx
        );
    }

    //유저정보 조회
    public GetUserInfoRes getUserInfoRes(int userIdx){
        String getUserInfoResQuery = "select userIdx,name, firstname, gender, birth, mail, phone\n" +
                "from User\n" +
                "where userIdx=?";

        return this.jdbcTemplate.queryForObject(getUserInfoResQuery,
                (rs, rowNum) -> new GetUserInfoRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("firstname"),
                        rs.getString("gender"),
                        rs.getString("birth"),
                        rs.getString("mail"),
                        rs.getString("phone")), userIdx
        );
    }



    //회원탈퇴
    public int deleteUser(DeleteUserReq deleteUserReq){
        String deleteUserQuery = "update User set status = 'INACTIVE', name =? where userIdx = ? ";
        Object[] deleteUserParams = new Object[]{deleteUserReq.getName() ,deleteUserReq.getUserIdx()};
        return this.jdbcTemplate.update(deleteUserQuery, deleteUserParams);
    }

    //리뷰생성
    public int createReivew( PostUserReivewReq postUserReivewReq, int userIdx, int houseIdx){
        String createReivewQuery = "insert into UserReview\n" +
                "(accuranct, communication, checkIn, priceC, clean, location, urContext, userIdx, houseIdx )\n" +
                "values ( ? , ? , ?, ?,? ,? , ?, ?, ?) ";
        Object[] createReivewParams = new Object[]{postUserReivewReq.getAccuranct(), postUserReivewReq.getCommunication()
                ,postUserReivewReq.getCheckIn() ,postUserReivewReq.getPriceC(), postUserReivewReq.getClean()
                ,postUserReivewReq.getLocation() ,postUserReivewReq.getUrContext()
                ,userIdx, houseIdx};
         return this.jdbcTemplate.update( createReivewQuery, createReivewParams);

    }

    //위시리스트 추가
    public int patchwishList( int userIdx, int houseIdx){
        String patchwishListQuery = "update House set isLiked = 'V', userIdx = ? \n" +
                "where houseIdx = ? ";
        Object[] patchwishListParams = new Object[]{ userIdx, houseIdx };
        return this.jdbcTemplate.update(patchwishListQuery, patchwishListParams);

    }

    //위시리스트 삭제
    public int deltwishList( int userIdx, int houseIdx){
        String deltwishListQuery = "update House set isLiked = null, userIdx = ? \n" +
                "where houseIdx = ? ";
        Object[] deltwishListParams = new Object[]{ userIdx, houseIdx };
        return this.jdbcTemplate.update(deltwishListQuery, deltwishListParams);

    }

//    //리뷰수정
//    public int patchReivew (PatchUserReivewReq patchUserReivewReq, int userIdx, int userReivewIdx){
//        String patchReivewQuery = "update UserReview set accuranct= ? " +
//                ", communication= ?, checkIn= ?, priceC= ?, clean = ? ,location= ?\n" +
//                ", urContext = ? \n" +
//                "where userReiviewIdx = ? ";
//        Object[] patchReivewParams = new Object[]{patchUserReivewReq.getAccuranct()
//                , patchUserReivewReq.getCommunication() ,patchUserReivewReq.getCheckIn() ,patchUserReivewReq.getPriceC()
//                , patchUserReivewReq.getClean() ,patchUserReivewReq.getLocation() ,patchUserReivewReq.getUrContext()
//                , userIdx, userReivewIdx};
//        return this.jdbcTemplate.update(patchReivewQuery, patchReivewParams);
//    }








}
