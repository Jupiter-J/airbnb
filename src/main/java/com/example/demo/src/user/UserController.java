package com.example.demo.src.user;

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
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;


    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService) {
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /users? Email=
     *
     * @return BaseResponse<List < GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String Email) {
        try {
            if (Email == null) {
                List<GetUserRes> getUsersRes = userProvider.getUsers();
                return new BaseResponse<>(getUsersRes);
            }
            // Get Users
            List<GetUserRes> getUsersRes = userProvider.getUsersByEmail(Email);
            return new BaseResponse<>(getUsersRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 1명 조회 API
     * [GET] /users/:userIdx
     *
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx11}") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") int userIdx) {
        // Get Users
        try {
            GetUserRes getUserRes = userProvider.getUser(userIdx);
            return new BaseResponse<>(getUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 회원가입 API
     * [POST] /users
     *
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("11")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        if (postUserReq.getEmail() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //이메일 정규표현
        if (!isRegexEmail(postUserReq.getEmail())) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        try {
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 로그인 API
     * [POST] /users/logIn
     *
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/logIn11")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq) {
        try {
            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저정보변경 API
     * [PATCH] /users/:userIdx
     *
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx11}")
    public BaseResponse<String> modifyUserName(@PathVariable("userIdx") int userIdx, @RequestBody User user) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저네임 변경
            PatchUserReq patchUserReq = new PatchUserReq(userIdx, user.getUserName());
            userService.modifyUserName(patchUserReq);

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * Aribnb API
     */


    //로그인
    @ResponseBody
    @GetMapping("/login/{userIdx}")
    public BaseResponse<GetLoginRes> getLoginRes(@PathVariable("userIdx") int userIdx) {

        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetLoginRes getLoginRes = userProvider.getLoginRes(userIdx);
            return new BaseResponse<>(getLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //회원가입(유저생성)
    @ResponseBody
    @PostMapping("/login/{userIdx}")
    public BaseResponse<PostUserRes1> createUser(@RequestBody PostUserReq1 postUserReq1) {
        try {
            PostUserRes1 postCreateUser = userService.createUser1(postUserReq1);
            return new BaseResponse<>(postCreateUser);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //개인정보 수정
    @ResponseBody
    @PatchMapping("/info/{userIdx}")
    public BaseResponse<PatchUserInfoRes> patchInfo(@PathVariable("userIdx") int userIdx,
                                                    @RequestBody PatchUserInfoReq patchUserInfoReq) {

        System.out.println("testtest");
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
//            if(patchUserInfoReq.getName().length() <= 10){
//                return new BaseResponse<>(PATCH_USERS_INVALNAME);
//            }
            PatchUserInfoRes patchUserInfo = userService.patchInfo(patchUserInfoReq);
            return new BaseResponse<>(patchUserInfo);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }




    //프로필 수정
    @ResponseBody
    @PatchMapping("/profile/{userIdx}")
    public BaseResponse<PatchProfileRes> patchProfile (@PathVariable("userIdx") int userIdx,
                                                       @RequestBody PatchProfileReq patchProfileReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PatchProfileRes patchUserProfile = userService.patchProfile(patchProfileReq);
            return new BaseResponse<>(patchUserProfile);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //유저프로필 조회
    @ResponseBody
    @GetMapping("/profile/{userIdx}")
    public BaseResponse<GetProfileRes> getprofileRes(@PathVariable("userIdx") int userIdx) throws BaseException{
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetProfileRes getProfileRes = userProvider.getprofileRes(userIdx);
            return new BaseResponse<>(getProfileRes);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //유저정보조회
    @ResponseBody
    @GetMapping("/info/{userIdx}")
    public BaseResponse<GetUserInfoRes> getUserInfoRes(@PathVariable("userIdx") int userIdx) throws BaseException{

        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetUserInfoRes getUserInfoRes = userProvider.getUserInfoRes(userIdx);
            return new BaseResponse<>(getUserInfoRes);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    //회원탈퇴
    @ResponseBody
    @DeleteMapping("/delt/{userIdx}")
    public BaseResponse<DeleteUserRes> deleteUser(@PathVariable("userIdx") int userIdx,
                                                  @RequestBody DeleteUserReq deleteUserReq ) throws BaseException{

        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            DeleteUserRes deleteUserRes = userService.deleteUser(deleteUserReq);
            return new BaseResponse<>(deleteUserRes);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //app/users/:userIdx/reivew - GET 조회
    //app/users/:userIdx/reivew/:houseIdx - POST 생성


    //리뷰생성
    @ResponseBody
    @PostMapping("/{userIdx}/reivew/{houseIdx}")
    public  BaseResponse<PostUserReivewRes> createReivew (@PathVariable("userIdx") int userIdx,
                                                          @PathVariable("houseIdx") int houseIdx,
                                                          @RequestBody PostUserReivewReq postUserReivewReq) throws BaseException{
        System.out.println("ttttest");

        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PostUserReivewRes postReivew = userService.createReivew(postUserReivewReq, userIdx, houseIdx);
            return new BaseResponse<>(postReivew);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    //위시리스트 추가
    //   app/users/:userIdx/wishList/{houseIdx}
    @ResponseBody
    @PatchMapping("/{userIdx}/wishList/{houseIdx}")
    public BaseResponse<PatchWishListRes> patchwishList(@PathVariable("userIdx") int userIdx,
                                                        @PathVariable("houseIdx") int houseIdx) throws BaseException {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PatchWishListRes patchWishListRes = userService.patchwishList(userIdx,houseIdx);
            return new BaseResponse<>(patchWishListRes);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    //5.0 이하의 소수를 입력하세요
//            if(postUserReivewReq.getAccuranct().size() <= 5){
//                return new BaseResponse<>(PATCH_REIVEW_INVALNUM);
//            }
    //위시리스트 삭제
    @ResponseBody
    @DeleteMapping("/{userIdx}/wishList/{houseIdx}")
    public BaseResponse<DeltWishListRes> deltwishList (@PathVariable("userIdx") int userIdx,
                                                        @PathVariable("houseIdx") int houseIdx) throws BaseException {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            DeltWishListRes deltWishlist = userService.deltWishListRes(userIdx,houseIdx);
            return new BaseResponse<>(deltWishlist);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

//    //리뷰수정
//    @ResponseBody
//    @PatchMapping("/{userIdx}/reivew/{userReivewIdx}")
//    public BaseResponse<PatchUserReivewRes> patchReivew (@PathVariable("userIdx") int userIdx,
//                                                       @PathVariable("userReivewIdx") int userReivewIdx ,
//                                                       @RequestBody PatchUserReivewReq patchUserReivewReq) throws BaseException{
//        try {
//            int userIdxByJwt = jwtService.getUserIdx();
//            if (userIdx != userIdxByJwt) {
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
//            PatchUserReivewRes patchReivews = userService.patchReivew(patchUserReivewReq, userIdx, userReivewIdx);
//            return new BaseResponse<>(patchReivews);
//        }catch (BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }






}