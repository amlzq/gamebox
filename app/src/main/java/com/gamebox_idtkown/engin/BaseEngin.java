package com.gamebox_idtkown.engin;

import android.content.Context;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.TypeReference;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.domain.InItInfo;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.net.contains.HttpConfig;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.net.impls.OKHttpRequest;
import com.gamebox_idtkown.net.listeners.OnHttpResonseListener;
import com.gamebox_idtkown.utils.FileUtil;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.PathUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by zhangkai on 16/9/20.
 */
public abstract class BaseEngin<T> {
    protected Context context;

    public BaseEngin() {
    }

    public BaseEngin(Context context) {
        this.context = context;
    }

    public ResultInfo<T> getResultInfo(boolean encodeResponse, Class<T> type, Map<String, String> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        ResultInfo<T> resultInfo = null;
        try {
            Response response = OKHttpRequest.getImpl().post2(getUrl(), params, encodeResponse);
            resultInfo = JSON.parseObject(response.body, new TypeReference<ResultInfo<T>>(type) {
            });
            if (publicKeyError(resultInfo, response.body)) {
                return getResultInfo(encodeResponse, type, params);
            }
        } catch (Exception e) {
            resultInfo = new ResultInfo<>();
            resultInfo.code = -1000;
            LogUtil.msg("getResultInfo异常:" + e.getMessage(), LogUtil.W);
        }
        return resultInfo;
    }

    public void agetResultInfo(final Class<T> type, Map<String, String> params, final
    Callback<T> callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        final Map<String, String> finalParams = params;
        try {
            OKHttpRequest.getImpl().apost(getUrl(), params, new OnHttpResonseListener() {
                @Override
                public void onSuccess(Response response) {
                    ResultInfo<T> resultInfo = null;
                    try {
                        resultInfo = JSON.parseObject(response.body, new TypeReference<ResultInfo<T>>(type) {
                        });
                        if (publicKeyError(resultInfo, response.body)) {
                            agetResultInfo(type, finalParams, callback);
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
            });
        } catch (Exception e) {
            LogUtil.msg("agetResultInfo异常->" + e.getMessage(), LogUtil.W);
        }
    }

    public void agetResultInfo(final boolean encodeResponse, final Class<T> type, Map<String, String> params, final
    Callback<T> callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        final Map<String, String> finalParams = params;
        try {
            OKHttpRequest.getImpl().apost2(getUrl(), params, new OnHttpResonseListener() {
                @Override
                public void onSuccess(Response response) {
                    ResultInfo<T> resultInfo = null;
                    try {
                        resultInfo = JSON.parseObject(response.body, new TypeReference<ResultInfo<T>>(type) {
                        });

                        if (publicKeyError
                                (resultInfo, response.body)) {
                            agetResultInfo(encodeResponse, type, finalParams, callback);
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

    public abstract String getUrl();

    public boolean publicKeyError(ResultInfo resultInfo, String body) {
        if(resultInfo != null && resultInfo.code == HttpConfig.PUBLICKEY_ERROR) {
            ResultInfo<InItInfo> resultInfoPE = JSON.parseObject(body, new
                    TypeReference<ResultInfo<InItInfo>>
                            (InItInfo.class) {
                    });
            if (resultInfoPE.data != null && resultInfoPE.data.publicKey != null) {
                GoagalInfo.publicKey = GoagalInfo.getPublicKey(resultInfoPE.data.publicKey);
                LogUtil.msg("公钥出错->" + GoagalInfo.publicKey);
                String name = "rsa_public_key.pem";
                FileUtil.writeInfoInSDCard(context, GoagalInfo.publicKey, PathUtil.getGolgalDir(), name);
                return true;
            }
        }
        return false;
    }
}
