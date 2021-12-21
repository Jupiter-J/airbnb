package com.example.demo.src.user.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
//프로필 조회
public class GetProfileRes {

    private int userIdx;
    private String uProfileTitle;
    private String uregister;
    private String uhabitation;
    private String ucertification;
    private String umail;
    private String uphone;
    private String ucompanyMail;
    private String hcreatedAt;
    private String hrContext;
    private String hname;
    private String hregister;
    private String himage;
    private String totalReivew;
}
