package com.gamebox_idtkown.engin;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.alibaba.fastjson.JSON;
import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.domain.InItInfo;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.net.contains.HttpConfig;
import com.gamebox_idtkown.utils.FileUtil;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.PathUtil;
import com.gamebox_idtkown.utils.PreferenceUtil;


import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangkai on 16/9/20.
 */
public class InitEngin extends BaseEngin<InItInfo> {
    public String key = "";
    public Context mcontext;
    public InitEngin(Context context) {
        super(context);
        mcontext = context;
        key = "6071_init";
    }

    @Override
    public String getUrl() {
        return Config.INIT_URL;
    }

    public void run(String userId) {
        String cUserId  = userId;
        if(userId == null || userId.isEmpty()){
            cUserId = PreferenceUtil.getImpl(context).getString("lastLogin", "");
        }
        Map<String, String> params = new HashMap<>();
        params.put("user_id", cUserId);

        ResultInfo<InItInfo> resultInfo = getResultInfo(true, InItInfo.class, params);
        if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK) {
            GoagalInfo.setInitInfo(mcontext, resultInfo.data);
            save(GoagalInfo.getInItInfo());
        } else {
            GoagalInfo.setInitInfo(mcontext, get());
        }
    }

    public void run() {
        run(null);
    }

    private void save(InItInfo inItInfo) {
        saveInitInfo(inItInfo);
        //saveLogoBitmap(inItInfo);
    }

    public InItInfo get() {
        InItInfo inItInfo = getInitInfo();

        //getLogoBitmap(inItInfo);
        return inItInfo;
    }

    private void saveInitInfo(InItInfo inItInfo) {
        if (inItInfo != null) {
            String initInfoStr = JSON.toJSONString(inItInfo);
            LogUtil.msg("saveInitInfo->" + initInfoStr);
            try {
                PreferenceUtil.getImpl(this.context).putString(key, initInfoStr);
            } catch (Exception e) {
                LogUtil.msg(e.getMessage());
            }
        }
    }

    private void saveLogoBitmap(InItInfo inItInfo) {
        if (inItInfo != null && inItInfo.logo != null) {
            try {
                boolean cache = true;

                getLogoBitmap(inItInfo);

                if (inItInfo.logoBitmp == null) {
                    URL url = new URL(inItInfo.logo);
                    inItInfo.logoBitmp = BitmapFactory.decodeStream(url.openConnection()
                            .getInputStream());
                    cache = false;
                }

                if (inItInfo.logoBitmp != null && !cache) {
                    FileUtil.writeImageInSDCard(context, inItInfo.logoBitmp, PathUtil.getThemeDir(), inItInfo.logo);
                    LogUtil.msg("w logo in cache ->" + inItInfo.logoBitmp);
                }

            } catch (Exception e) {
                LogUtil.msg(e.getMessage());
            }
        }
    }


    private InItInfo getInitInfo() {
        InItInfo inItInfo = null;
        try {
            String initInfoStr = PreferenceUtil.getImpl(this.context).getString(key, "");
            LogUtil.msg("getInitInfo->" + initInfoStr);
            inItInfo = JSON.parseObject(initInfoStr, InItInfo.class);
        } catch (Exception e) {
            LogUtil.msg("getInitInfo->" + e);
        }
        return inItInfo;
    }


    private void getLogoBitmap(InItInfo inItInfo) {
        if (inItInfo != null) {
            inItInfo.logoBitmp = FileUtil.getImageFromSDCard(context, PathUtil.getThemeDir(), inItInfo.logo);
            LogUtil.msg("r logo in cache ->" + inItInfo.logoBitmp);
        }
    }
}
