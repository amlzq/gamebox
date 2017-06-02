package com.gamebox_idtkown.engin;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.GameOpenServiceInfo;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.net.impls.OKHttpRequest;
import com.gamebox_idtkown.net.listeners.OnHttpResonseListener;
import com.gamebox_idtkown.utils.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangkai on 2017/3/15.
 */

public class GameOpenServiceEngin extends BaseEngin {

    private static GameOpenServiceEngin gameOpenServiceEngin;

    public static GameOpenServiceEngin getImpl(Context context){
        if(gameOpenServiceEngin == null){
            synchronized (GameListEngin.class) {
                gameOpenServiceEngin = new GameOpenServiceEngin(context);
            }
        }
        return gameOpenServiceEngin;
    }

    public GameOpenServiceEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.OPEN_SERVICE;
    }

    public void getOpenService(int page, int limit, String name, String time, Callback<List<GameOpenServiceInfo>>
            callback) {
        Map<String, String> params = new HashMap();
        params.put("game_name", name);
        params.put("time", time);
        params.put("page", page + "");
        params.put("limit", limit + "");
        this.agetResultInfo(true, params, callback);
    }

    public void agetResultInfo(final boolean encodeResponse, Map<String, String> params, final
    Callback<List<GameOpenServiceInfo>> callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        final Map<String, String> finalParams = params;
        try {
            OKHttpRequest.getImpl().apost2(getUrl(), params, new OnHttpResonseListener() {
                @Override
                public void onSuccess(Response response) {
                    ResultInfo<List<GameOpenServiceInfo>> resultInfo = null;
                    try {
                        resultInfo = JSON.parseObject(response.body, new
                                TypeReference<ResultInfo<List<GameOpenServiceInfo>>>
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
