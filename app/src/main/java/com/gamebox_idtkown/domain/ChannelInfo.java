package com.gamebox_idtkown.domain;

import com.gamebox_idtkown.core.db.greendao.GameInfo;

import java.util.List;

/**
 * Created by zhangkai on 16/9/20.
 */
public class ChannelInfo {
    public String author;
    public String from_id;
    public String agent_id = "default";
    public String gameInfos = "";
    public String jumpInfos = "";
    public String iconInfos = "";

    public List<GameInfo> gameList = null;
    public List<GameInfo> jumpList = null;
    public List<ShortCutInfo> iconList = null;
}
