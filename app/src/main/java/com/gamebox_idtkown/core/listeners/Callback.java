package com.gamebox_idtkown.core.listeners;

import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.net.entry.Response;

/**
 * Created by zhangkai on 16/9/20.
 */
public interface Callback<T> {
    public void onSuccess(ResultInfo<T> resultInfo);
    public void onFailure(Response response);
}
