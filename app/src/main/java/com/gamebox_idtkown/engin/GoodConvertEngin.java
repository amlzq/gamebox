package com.gamebox_idtkown.engin;

import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.listeners.Callback;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by zhangkai on 16/11/15.
 */
public class GoodConvertEngin extends BaseEngin<String> {

    @Inject
    public GoodConvertEngin(){}

    public void change(String goods_id, String user_id, Callback<String> callback){
        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("goods_id", goods_id);
        agetResultInfo(true, String.class,  params, callback);
    }



    @Override
    public String getUrl() {
        return Config.GOOD_CONVERT_URL;
    }
}
