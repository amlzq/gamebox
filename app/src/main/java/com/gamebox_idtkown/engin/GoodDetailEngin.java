package com.gamebox_idtkown.engin;

import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.db.greendao.GoodList;
import com.gamebox_idtkown.core.listeners.Callback;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by zhangkai on 2017/5/12.
 */

public class GoodDetailEngin extends BaseEngin {

    @Inject
    public GoodDetailEngin() {
        super();
    }

    @Override
    public String getUrl() {
        return Config.GOOD_DETAIL_URL;
    }

    public void getList(String good_id, Callback<GoodList> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("goods_id", good_id + "");
        agetResultInfo(true, GoodList.class, params, callback);
    }


}
