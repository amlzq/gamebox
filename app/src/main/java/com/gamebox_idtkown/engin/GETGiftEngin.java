package com.gamebox_idtkown.engin;

import android.content.Context;

import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.listeners.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangkai on 16/10/26.
 */
public class GETGiftEngin extends BaseEngin<HashMap>  {
    private static GETGiftEngin getGiftEngin;

    public GETGiftEngin(Context context) {
        super(context);
    }

    public static GETGiftEngin getImpl(Context context){
        if(getGiftEngin == null){
            synchronized (GETGiftEngin.class) {
                getGiftEngin = new GETGiftEngin(context);
            }
        }
        return getGiftEngin;
    }

    public void getGift(String giftId, Callback<HashMap> callback){
        Map<String, String> params = new HashMap<>();
        params.put("user_id", GBApplication.userInfo.getUserId());
        params.put("gift_id", giftId);
        agetResultInfo(true, HashMap.class, params, callback);
    }

    @Override
    public String getUrl() {
        return Config.GET_GFIT_URL;
    }
}
