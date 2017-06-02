package com.gamebox_idtkown.engin;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.Config;
import com.gamebox_idtkown.core.db.greendao.GameDetail;
import com.gamebox_idtkown.core.db.greendao.GameImage;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.net.impls.OKHttpRequest;
import com.gamebox_idtkown.net.listeners.OnHttpResonseListener;
import com.gamebox_idtkown.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangkai on 16/10/20.
 */
public class GameDetailEngin extends BaseEngin<GameDetail> {
    private static GameDetailEngin gameInfoEngin;

    public GameDetailEngin(Context context) {
        super(context);
    }

    public static GameDetailEngin getImpl(Context context) {
        if (gameInfoEngin == null) {
            synchronized (GameDetailEngin.class) {
                gameInfoEngin = new GameDetailEngin(context);
            }
        }
        return gameInfoEngin;
    }

    public void getGameDetail(String type, String gameId, Callback<GameDetail> callback) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("game_id", gameId);
        agetResultInfo(true, params, callback);
    }

    public void agetResultInfo(final boolean encodeResponse, Map<String, String> params, final
    Callback<GameDetail> callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        final Map<String, String> finalParams = params;
        try {
            OKHttpRequest.getImpl().apost2(getUrl(), params, new OnHttpResonseListener() {
                @Override
                public void onSuccess(Response response) {
                    ResultInfo<GameDetail> resultInfo = null;
                    try {
                        resultInfo = new ResultInfo();
                        Map map = JSON.parseObject(response.body);
                        resultInfo.code = Integer.parseInt(map.get("code") + "");

                        if (publicKeyError(resultInfo, response.body)) {
                            agetResultInfo(encodeResponse, finalParams, callback);
                            return;
                        }

                        GameDetail gameDetail = new GameDetail();
                        List<GameImage> list = new ArrayList<GameImage>();
                        Map imgsMap = (Map) map.get("data");
                        gameDetail.setBody(imgsMap.get("body") + "");
                        gameDetail.setShareUrl(imgsMap.get("share_url") + "");
                        gameDetail.setHasGift(Boolean.parseBoolean(imgsMap.get("has_gift") + ""));
                        List imgs = (List) imgsMap.get("imgs");
                        if (imgs != null) {
                            for (int i = 0; i < imgs.size(); i++) {
                                String imgUrl = (String) imgs.get(i);
                                GameImage gameImage = new GameImage();
                                gameImage.setImgUrl(imgUrl);
                                list.add(gameImage);
                            }
                        }
                        gameDetail.imgs = list;
                        resultInfo.data = gameDetail;

                        callback.onSuccess(resultInfo);

                    } catch (Exception e) {
                        response.body = DescConstans.SERVICE_ERROR;
                        callback.onFailure(response);
                        e.printStackTrace();
                        LogUtil.msg("agetResultInfo异常->JSON解析错误（服务器返回数据格式不正确）");
                    }

                }

                @Override
                public void onFailure(Response response) {
                    callback.onFailure(response);
                }
            }, encodeResponse);
        } catch (Exception e) {
            LogUtil.msg("agetResultInfo异常->" + e.getMessage(), LogUtil.W);
        }
    }

    @Override
    public String getUrl() {
        return Config.CHOSEN_GAME_INFO_URL;
    }

}
