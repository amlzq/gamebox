package com.gamebox_idtkown.core.db.greendao;

import com.alibaba.fastjson.annotation.JSONField;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.
/**
 * Entity mapped to table "PAY_TYPE_INFO".
 */
@Entity
public class PayTypeInfo {

    @Id
    private Long id;
    @JSONField(name = "title")
    private String name;
    @JSONField(name = "desp")
    private String desc;
    private String type;

    @Generated
    public PayTypeInfo() {
    }

    public PayTypeInfo(Long id) {
        this.id = id;
    }

    @Generated
    public PayTypeInfo(Long id, String name, String desc, String type) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
