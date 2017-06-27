package com.gamebox_idtkown.engin;

import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.listeners.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangkai on 2017/4/14.
 */

public class OrderPayMoneyEngin extends BaseEngin {
    @Override
    public String getUrl() {
        return Config.GetOrderPayMoney_URL;
    }

    public void getOrderPayMoney(String gameid, String paymoney, Callback<String> callback){
        Map<String, String> params = new HashMap<>();
        params.put("order_money", paymoney);
        if(gameid != null && !gameid.isEmpty()) {
            params.put("game_id", gameid);
        }
        agetResultInfo(true, String.class, params, callback);
    }
}
