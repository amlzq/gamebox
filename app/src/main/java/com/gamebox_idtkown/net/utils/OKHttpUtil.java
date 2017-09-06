package com.gamebox_idtkown.net.utils;

import android.os.Build;

import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.net.contains.HttpConfig;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.net.entry.UpFileInfo;
import com.gamebox_idtkown.security.Encrypt;
import com.gamebox_idtkown.utils.EncryptUtil;
import com.gamebox_idtkown.utils.LogUtil;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zhangkai on 16/9/9.
 */
public final class OKHttpUtil {
    public static Request.Builder getRequestBuilder(String url) {
        if (GoagalInfo.channelInfo != null) {
            url += "/channel_id/" + GoagalInfo.channelInfo.agent_id;
        } else {
            url += "/channel_id/67";
        }
        LogUtil.msg("客户端请求url->" + url);
        Request.Builder builder = new Request.Builder()
                .tag(url)
                .url(url);

        return builder;
    }

    public static OkHttpClient.Builder getHttpClientBuilder() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(HttpConfig.TIMEOUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(HttpConfig.TIMEOUT, TimeUnit.MILLISECONDS);
        return builder;
    }

    public static Response setResponse(int code, String body) {
        Response response = new Response();
        response.code = code;
        response.body = body;
        return response;
    }

    public static FormBody.Builder setBuilder(Map<String, String> params) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        setDefaultParams(params, false);
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                builder.add(key, value);
            }
        }
        return builder;
    }

    private static void setDefaultParams(Map<String, String> params, boolean encryptResponse) {
        //params.put("channel", GoagalInfo.channel);
        if (GoagalInfo.channelInfo != null) {
            params.put("from_id", GoagalInfo.channelInfo.from_id);
            params.put("author", GoagalInfo.channelInfo.author);
        }
        params.put("timestamp", System.currentTimeMillis() + "");
        params.put("IMEI", GoagalInfo.uuid);
        params.put("sys_versoin", Build.VERSION.SDK_INT + "");
        if (GoagalInfo.packageInfo != null) {
            params.put("version", GoagalInfo.packageInfo.versionName);
        }
        if (encryptResponse) {
            params.put("encrypt-response", "true");
        }

    }

    public static MultipartBody.Builder setBuilder(UpFileInfo upFileInfo, Map<String, String> params) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        setDefaultParams(params, false);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                builder.addFormDataPart(key, value);
            }
        }
        builder.addFormDataPart(upFileInfo.name, upFileInfo.filename, RequestBody.create(MediaType.parse
                        ("multipart/form-data"),
                upFileInfo.file));
        return builder;
    }

    /**
     * Encode params data
     *
     * @param params          The params of http port
     * @param encryptResponse The response if encryptResponse by true, false.
     */
    public static byte[] encodeParams(Map params, boolean
            encryptResponse) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        setDefaultParams(params, encryptResponse);
        JSONObject jsonObject = new JSONObject(params);
        String jsonStr = jsonObject.toString();
        LogUtil.msg("客户端请求数据->" + jsonStr);
        jsonStr = EncryptUtil.rsa(jsonStr);
        return EncryptUtil.compress(jsonStr);
    }

    /**
     * compress data
     *
     * @param params The params of http port
     */
    public static byte[] encodeParams(Map params) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        setDefaultParams(params, true);
        JSONObject jsonObject = new JSONObject(params);
        String jsonStr = jsonObject.toString();
        LogUtil.msg("客户端请求数据->" + jsonStr);
        return EncryptUtil.compress(jsonStr);
    }
    
    ///< 解密返回值
    public static String decodeBody(InputStream in) {
        return Encrypt.decode(EncryptUtil.unzip(in));
    }

}
