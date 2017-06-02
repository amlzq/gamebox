package com.gamebox_idtkown.domain;

import com.gamebox_idtkown.core.db.greendao.DownloadInfo;

import java.util.List;

/**
 * Created by zhangkai on 16/12/23.
 */

public class BatInfo {
    public DownloadInfo app;
    public List<DownloadInfo> per_local_apps;
    public List<DownloadInfo> per_net_apps;
}
