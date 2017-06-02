package com.gamebox_idtkown.engin;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.db.greendao.ChosenInfo;
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
 * Created by zhangkai on 16/10/18.
 */
public class ChosenEngin extends BaseEngin<ChosenInfo> {

    private static ChosenEngin chosenEngin;

    public ChosenEngin(Context context) {
        super(context);
    }

    public static ChosenEngin getImpl(Context context) {
        if (chosenEngin == null) {
            synchronized (GameListEngin.class) {
                chosenEngin = new ChosenEngin(context);
            }
        }
        return chosenEngin;
    }

    @Override
    public String getUrl() {
        return Config.GCHOSEN_LIST_URL;
    }

    public void getChosenList(Callback<List<ChosenInfo>> callback) {
        agetResultInfo(true, null, callback);
    }

    public void agetResultInfo(final boolean encodeResponse, Map<String, String> params, final
    Callback<List<ChosenInfo>> callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        final Map<String, String> finalParams = params;
        try {
            OKHttpRequest.getImpl().apost2(getUrl(), params, new OnHttpResonseListener() {
                @Override
                public void onSuccess(Response response) {
                    ResultInfo<List<ChosenInfo>> resultInfo = null;
                    try {
                        resultInfo = JSON.parseObject(response.body, new
                                TypeReference<ResultInfo<List<ChosenInfo>>>
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
                    if (callback != null) {
                        callback.onFailure(response);
                    }
                }
            }, encodeResponse);
        } catch (Exception e) {
            LogUtil.msg("agetResultInfo异常->" + e.getMessage(), LogUtil.W);
        }
    }
}
