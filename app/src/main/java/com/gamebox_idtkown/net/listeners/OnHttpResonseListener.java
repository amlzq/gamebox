package com.gamebox_idtkown.net.listeners;

import com.gamebox_idtkown.net.entry.Response;

/**
 * Created by zhangkai on 16/8/30.
 */
public interface OnHttpResonseListener {
    void onSuccess(Response response);
    void onFailure(Response response);
}
