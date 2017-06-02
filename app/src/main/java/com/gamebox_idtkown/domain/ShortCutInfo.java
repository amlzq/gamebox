package com.gamebox_idtkown.domain;

import android.graphics.Bitmap;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by zhangkai on 16/12/7.
 */
public class ShortCutInfo {
    public String url;

    @JSONField(name = "ico")
    public String iconUrl;
    public String name;

    public Bitmap icon;

}
