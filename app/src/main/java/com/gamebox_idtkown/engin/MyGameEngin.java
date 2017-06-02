package com.gamebox_idtkown.engin;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.net.impls.OKHttpRequest;
import com.gamebox_idtkown.net.listeners.OnHttpResonseListener;
import com.gamebox_idtkown.utils.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangkai on 16/10/26.
 */
public class MyGameEngin extends BaseEngin<GameInfo> {

    private static MyGameEngin myGameEngin;

    public MyGameEngin(Context context) {
        super(context);
    }

    public static MyGameEngin getImpl(Context context) {
        if (myGameEngin == null) {
            synchronized (MyGameEngin.class) {
                myGameEngin = new MyGameEngin(context);
            }
        }
        return myGameEngin;
    }

    @Override
    public String getUrl() {
        return Config.MY_GAME_URL;
    }

    public void getMyGame(int page, int limit, Callback<List<GameInfo>> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", GBApplication.userInfo.getUserId());
        params.put("page", page + "");
        params.put("limit", limit + "");
        agetResultInfo(true, params, callback);
    }


    public void agetResultInfo(final boolean encodeResponse, Map<String, String> params, final
    Callback<List<GameInfo>> callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        final Map<String, String> finalParams = params;

        try {
            OKHttpRequest.getImpl().apost2(getUrl(), params, new OnHttpResonseListener() {
                @Override
                public void onSuccess(Response response) {
                    ResultInfo<List<GameInfo>> resultInfo = null;
                    try {
                        resultInfo = JSON.parseObject(response.body, new
                                TypeReference<ResultInfo<List<GameInfo>>>
                                        () {
                                });
                        if (publicKeyError(resultInfo, response.body)) {
                            agetResultInfo(encodeResponse, finalParams, callback);
                            return;
                        }
                        callback.onSuccess(resultInfo);

                    } catch (Exception e) {
                        response.body = DescConstans.SERVICE_ERROR;
                        callback.onFailure(response);
                        e.printStackTrace();
                        LogUtil.msg("agetResultInfo异常->JSON解析错误（服务器返回数据格式不正确）");
                    }

                }

                @Override
                public void onFailure(Response response) {
                    callback.onFailure(response);
                }
            }, encodeResponse);
        } catch (Exception e) {
            LogUtil.msg("agetResultInfo异常->" + e.getMessage(), LogUtil.W);
        }
    }
}