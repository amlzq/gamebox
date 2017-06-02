package com.gamebox_idtkown.engin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.PayRecordInfo;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.net.impls.OKHttpRequest;
import com.gamebox_idtkown.net.listeners.OnHttpResonseListener;
import com.gamebox_idtkown.utils.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by zhangkai on 2017/2/6.
 */

public class PayRecordEngin extends BaseEngin<PayRecordInfo> {
    @Inject
    public PayRecordEngin(){

    }

    @Override
    public String getUrl() {
        return Config.PAY_RECORD_URL;
    }

    public void getPayRecord(int page, int limit, Callback<List<PayRecordInfo>> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", GBApplication.userInfo.getUserId());
        params.put("page", page + "");
        params.put("limit", limit + "");
        agetResultInfo(true, params, callback);
    }


    public void agetResultInfo(final boolean encodeResponse, Map<String, String> params, final
    Callback<List<PayRecordInfo>> callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        final Map<String, String> finalParams = params;

        try {
            OKHttpRequest.getImpl().apost2(getUrl(), params, new OnHttpResonseListener() {
                @Override
                public void onSuccess(Response response) {
                    ResultInfo<List<PayRecordInfo>> resultInfo = null;
                    try {
                        resultInfo = JSON.parseObject(response.body, new
                                TypeReference<ResultInfo<List<PayRecordInfo>>>
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
