package com.gamebox_idtkown.engin;

import android.content.Context;

import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.utils.DeviceUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangkai on 16/10/26.
 */
public class SendCodeEngin extends BaseEngin<String> {
    private static SendCodeEngin sendCodeEngin;

    public SendCodeEngin(Context context) {
        super(context);
    }

    public static SendCodeEngin getImpl(Context context){
        if(sendCodeEngin == null){
            synchronized (SendCodeEngin.class) {
                sendCodeEngin = new SendCodeEngin(context);
            }
        }
        return sendCodeEngin;
    }

    public void send(String mobile, String user_name, String type, Callback<String> callback){
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("user_name", user_name);
        params.put("type", type);
        params.put("imeil", DeviceUtil.getDeviceIMEI(context));
        agetResultInfo(true,String.class, params, callback);
    }

    @Override
    public String getUrl() {
        return Config.SEND_CODE_URL;
    }
}
