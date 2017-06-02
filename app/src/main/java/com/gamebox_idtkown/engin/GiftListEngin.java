package com.gamebox_idtkown.engin;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.db.greendao.GiftDetail;
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
 * Created by zhangkai on 16/10/24.
 */
public class GiftListEngin extends BaseEngin<GiftDetail> {
    private static GiftListEngin giftListEngin;

    public GiftListEngin(Context context) {
        super(context);
    }

    public static GiftListEngin getImpl(Context context) {
        if (giftListEngin == null) {
            synchronized (GiftListEngin.class) {
                giftListEngin = new GiftListEngin(context);
            }
        }
        return giftListEngin;
    }

    public void getGiftListInfo(int page, int limit, String game_id, Callback<List<GiftDetail>> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("limit", limit + "");
        params.put("game_id", game_id + "");
        agetResultInfo(true, params, callback);
    }

    public void agetResultInfo(final boolean encodeResponse, Map<String, String> params, final
    Callback<List<GiftDetail>> callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        final Map<String, String> finalParams = params;
        try {
            OKHttpRequest.getImpl().apost2(getUrl(), params, new OnHttpResonseListener() {
                @Override
                public void onSuccess(Response response) {
                    ResultInfo<List<GiftDetail>> resultInfo = null;
                    try {
                        resultInfo = JSON.parseObject(response.body, new
                                TypeReference<ResultInfo<List<GiftDetail>>>
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

    @Override
    public String getUrl() {
        return Config.GIFT_LIST;
    }
}
