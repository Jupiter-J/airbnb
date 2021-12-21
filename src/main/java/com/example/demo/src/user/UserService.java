package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //중복
        if(userProvider.checkEmail(postUserReq.getEmail()) ==1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        String pwd;
        try{
            //암호화
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int userIdx = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt,userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserName(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.modifyUserName(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }





    /**
     Aribnb API
     */

    //회원가입(유저생성)
    public PostUserRes1 createUser1(PostUserReq1 postUserReq1) throws BaseException{

        try {
            String status = "유저확인";
            int userIdx = userDao.createUser1(postUserReq1);
            System.out.println(userIdx);
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes1(status, jwt, userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //개인정보 수정
    public PatchUserInfoRes patchInfo(PatchUserInfoReq patchUserInfoReq) throws BaseException{

        try {
            int result = userDao.patchInfo(patchUserInfoReq);
            System.out.println(result);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERINFO);
            }
            String message = "개인정보수정 완료";
            return new PatchUserInfoRes(message);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }



    //프로필 수정
    public PatchProfileRes patchProfile (PatchProfileReq patchProfileReq ) throws BaseException{
        try {
            int result = userDao.patchProfile(patchProfileReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERPROFILE);
            }
            String message = "프로필 수정 완료";
            return new PatchProfileRes (message);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //회원 삭제
    public DeleteUserRes deleteUser (DeleteUserReq deleteUserReq) throws BaseException{
        try {
            int result = userDao.deleteUser(deleteUserReq);
            if(result == 0){
                throw new BaseException( DELETE_FAIL_USER);
            }
            String message = "유저 정보 삭제";
            return new DeleteUserRes(message);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //리뷰생성
    public PostUserReivewRes createReivew (PostUserReivewReq postUserReivewReq, int userIdx, int houseIdx) throws BaseException {

        try {
//                userDao.createReivew(postUserReivewReq, userIdx, houseIdx);
            int result = userDao.createReivew(postUserReivewReq, userIdx, houseIdx);
            if(result == 0){
                throw new BaseException( DELETE_FAIL_USER);
            }
            String message = "리뷰 생성 성공";
            return new PostUserReivewRes(message);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }

    //위시리스트 추가
    public PatchWishListRes patchwishList (int userIdx, int houseIdx) throws BaseException{
        try {
            int result = userDao.patchwishList( userIdx, houseIdx);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_WISHLIST);
            }
            String message = "위시리스트 추가";
            return new PatchWishListRes(message);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //위시리스트 삭제
    public DeltWishListRes deltWishListRes (int userIdx, int houseIdx) throws BaseException{
        try {
            int result = userDao.deltwishList( userIdx, houseIdx);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_WISHLIST);
            }
            String message = "위시리스트 삭제";
            return new DeltWishListRes(message);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

//    //리뷰수정
//    public PatchUserReivewRes patchReivew (PatchUserReivewReq patchUserReivewReq, int userIdx, int userReivewIdx)
//            throws BaseException{
//        try {
//            int result = userDao.patchReivew(patchUserReivewReq, userIdx, userReivewIdx);
//            if(result == 0){
//                throw new BaseException(MODIFY_FAIL_WISHLIST);
//            }
//            String message = "리뷰수정 성공";
//            return new PatchUserReivewRes(message);
//
//        }catch (Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }






    }




