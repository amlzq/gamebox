package com.gamebox_idtkown.domain;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.alibaba.fastjson.annotation.JSONField;
import com.gamebox_idtkown.utils.LogUtil;

/**
 * Created by zhangkai on 16/9/20.
 */
public class InItInfo {
    @JSONField(name = "color")
    public String themeColor;
    public String logo;
    @JSONField(name = "name")
    public String title;

    public String launch_img;

    public String publicKey;

    public int androidColor;
    public Bitmap logoBitmp;

    public boolean is_update;
    public boolean is_strong;
    public VersionInfo update_info;
    public String agent_info;

    private String type;
    private String typeValue;
    private String gameId;
    private String gameType;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public void setThemeColor() {
        try {
            androidColor = Color.parseColor(themeColor);
        } catch (Exception e) {
            androidColor = Color.parseColor("#2AB1F2");
            themeColor = "#2AB1F2";
            LogUtil.msg("初始化信息有误->" + themeColor);
        }
    }
}
