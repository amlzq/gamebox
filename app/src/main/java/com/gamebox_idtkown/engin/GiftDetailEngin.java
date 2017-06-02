package com.gamebox_idtkown.engin;

import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.listeners.Callback;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by zhangkai on 16/12/21.
 */

public  class GiftDetailEngin extends BaseEngin<String> {

    @Inject
    public GiftDetailEngin(){}

    public void getGiftInfo(String gift_id, Callback<String> callback){
        Map<String, String> params = new HashMap<>();
        params.put("gift_id", gift_id);
        agetResultInfo(true, String.class, params, callback);
    }

    @Override
    public String getUrl() {
        return Config.GIFT_INFO_URL;
    }
}
