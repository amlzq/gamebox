package com.gamebox_idtkown.core.db.greendao;

import android.graphics.Bitmap;

import com.alibaba.fastjson.annotation.JSONField;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "USER_INFO".
 */
@Entity
public class UserInfo {

    @Id
    private Long id;
    @JSONField(name = "id")
    private String userId;
    private String name;
    private String nick_name;
    private String mobile;
    @JSONField(name = "face")
    private String avatar;
    private String pwd;
    private String email;
    private String qq;
    private String sex;
    private String birth;
    private String area_id;
    private String point;
    private String money;
    private Boolean is_vali_mobile;
    private String checkTime;
    private Boolean signed;
    private Integer num;

    public Bitmap avatarBitmp;
    public boolean sign_access = false;

    @Generated
    public UserInfo() {
    }

    public UserInfo(Long id) {
        this.id = id;
    }

    @Generated
    public UserInfo(Long id, String userId, String name, String nick_name, String mobile, String avatar, String pwd, String email, String qq, String sex, String birth, String area_id, String point, String money, Boolean is_vali_mobile, String checkTime, Boolean signed, Integer num) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.nick_name = nick_name;
        this.mobile = mobile;
        this.avatar = avatar;
        this.pwd = pwd;
        this.email = email;
        this.qq = qq;
        this.sex = sex;
        this.birth = birth;
        this.area_id = area_id;
        this.point = point;
        this.money = money;
        this.is_vali_mobile = is_vali_mobile;
        this.checkTime = checkTime;
        this.signed = signed;
        this.num = num;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Boolean getIs_vali_mobile() {
        if (is_vali_mobile == null) {
            is_vali_mobile = false;
        }
        return is_vali_mobile;
    }

    public void setIs_vali_mobile(Boolean is_vali_mobile) {
        this.is_vali_mobile = is_vali_mobile;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public Boolean getSigned() {
        if (signed == null) {
            signed = false;
        }
        return signed;
    }

    public void setSigned(Boolean signed) {
        this.signed = signed;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

}
