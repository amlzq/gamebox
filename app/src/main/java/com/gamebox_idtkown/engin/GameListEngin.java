package com.gamebox_idtkown.engin;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
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
 * Created by zhangkai on 16/9/21.
 */
public class GameListEngin extends BaseEngin<GameInfo> {

    private static GameListEngin gameListEngin;

    public GameListEngin(Context context) {
        super(context);
    }

    public static GameListEngin getImpl(Context context) {
        if (gameListEngin == null) {
            synchronized (GameListEngin.class) {
                gameListEngin = new GameListEngin(context);
            }
        }
        return gameListEngin;
    }

    @Override
    public String getUrl() {
        return Config.GAME_LIST_URL;
    }

    public static class ParamsInfo {
        public String type;
        public int cate_id = 0;
        public int page = 1;
        public int limit;
    }

    public final static class ParamTypeInfo {
        public final static String COMM = "comm";
        public final static String HOT = "hot";
        public final static String CATE = "cate";
        public final static String GOOD = "good";
    }

    public HashMap<String, String> getParams(ParamsInfo paramsInfo) {
        return JSON.parseObject(JSON.toJSONString(paramsInfo), new TypeReference<HashMap<String,
                String>>() {
        });
    }


    /**
     * 获取精选游戏信息
     *
     * @param page     page 信息的页数
     * @param limit    limit 一次请求拉取的条数
     * @param cate_id  cate_id 一次请求拉取的条数
     * @param callback 回调
     */
    public void getCateInfo(int page, int limit, int cate_id, Callback<List<GameInfo>> callback) {
        ParamsInfo paramsInfo = new ParamsInfo();
        paramsInfo.type = ParamTypeInfo.CATE;
        paramsInfo.page = page;
        paramsInfo.limit = limit;
        paramsInfo.cate_id = cate_id;
        Map<String, String> params = getParams(paramsInfo);
        this.agetResultInfo(true, params, callback);
    }


    public void getTypeInfo(int page, int limit, String type, Callback<List<GameInfo>> callback) {
        ParamsInfo paramsInfo = new ParamsInfo();
        paramsInfo.type = type;
        paramsInfo.page = page;
        paramsInfo.limit = limit;
        paramsInfo.cate_id = 0;
        Map<String, String> params = getParams(paramsInfo);
        this.agetResultInfo(true, params, callback);
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
