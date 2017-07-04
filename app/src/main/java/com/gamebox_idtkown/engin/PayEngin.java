package com.gamebox_idtkown.engin;

import android.content.Context;

import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.utils.DeviceUtil;
import com.gamebox_idtkown.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangkai on 16/10/28.
 */
public class PayEngin extends BaseEngin<HashMap> {

    private static PayEngin payEngin;

    public PayEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.ORDER_URL;
    }

    public void pay(String gameid, String type, String amount, String md5signstr, Callback<HashMap> callback){
        Map<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("user_id", GBApplication.userInfo.getUserId());
        params.put("amount", amount);
        params.put("imeil", DeviceUtil.getDeviceIMEI(context));
        params.put("productname", "平台币");
        if(!type.equals("zfb")){
            params.put("md5signstr", md5signstr);
            LogUtil.msg(md5signstr+"");
        }
        if(gameid != null && !gameid.isEmpty()) {
            params.put("game_id", gameid);
        }
        agetResultInfo(true, HashMap.class, params, callback);
    }


    public static PayEngin getImpl(Context context) {
        if (payEngin == null) {
            synchronized (PayEngin.class) {
                payEngin = new PayEngin(context);
            }
        }
        return payEngin;
    }
}