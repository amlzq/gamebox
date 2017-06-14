package com.gamebox_idtkown.core;

import android.os.Environment;

import com.gamebox_idtkown.domain.GoagalInfo;

/**
 * Created by zhangkai on 16/9/12.
 */
public class Config {
    /**
     * The path of this application cache.
     */
    public static final String PATH = Environment.getExternalStorageDirectory() + "/" + GoagalInfo.TAG;

    public static final boolean DEBUG = false;

    private static String baseUrl = "http://api.box.6071.com/box";
    private static String debugBaseUrl = "http://sdk.289.com/Api/box";

    /**
     * The url of init.
     */
    public static final String INIT_URL = getBaseUrl() + "/init";

    /**
     * The url of game list info
     */
    public static final String GAME_LIST_URL = getBaseUrl() + "/gameList";

    /**
     * The url of game detail info
     */
    public static final String GAME_DETAIL_URL = getBaseUrl() + "/gameInfo";

    /**
     * The url of game detail info
     */
    public static final String GAME_INFO_URL = getBaseUrl() + "/gameInfoFull";


    /**
     * The category of game info
     */
    public static final String GAME_CATE_URL = getBaseUrl() + "/cateList";


    /**
     * The url of game search info
     */
    public static final String GAME_SEARCH_URL = getBaseUrl() + "/search";

    /**
     * The url of search info tag
     */
    public static final String GAME_SEARCH_TAG_URL = getBaseUrl() + "/searchTag";


    //专题
    public static final String GCHOSEN_LIST_URL = getBaseUrl() + "/specialList";

    //专题游戏
    public static final String CHOSEN_GAMES_URL = getBaseUrl() + "/specialGames";

    //游戏详情
    public static final String CHOSEN_GAME_INFO_URL = getBaseUrl() + "/gameInfo";

    //礼品首页
    public static final String GIFT_INDEX = getBaseUrl() + "/giftIndex";


    //礼品列表
    public static final String GIFT_LIST = getBaseUrl() + "/giftList";

    //注册
    public static final String REGISTER_URL = getBaseUrl() + "/reg";

    //登录
    public static final String LOGIN_URL = getBaseUrl() + "/login";

    //发送验证码
    public static final String SEND_CODE_URL = getBaseUrl() + "/sendcode";

    //验证验证码
    public static final String CHECK_CODE_URL = getBaseUrl() + "/checkcode";

    //获取礼包
    public static final String GET_GFIT_URL = getBaseUrl() + "/convertGift";

    //我的礼包
    public static final String MY_GFIT_URL = getBaseUrl() + "/myGift";

    //我的游戏
    public static final String MY_GAME_URL = getBaseUrl() + "/userGame";

    //忘记密码
    public static final String FORGOT_URL = getBaseUrl() + "/forgetPwd";

    //礼品详情
    public static final String GIFT_DETAIL_URL = getBaseUrl() + "/myGIftDetail";

    //签到
    public static final String SIGN_URL = getBaseUrl() + "/sign";

    //支付方式
    public static final String PAY_WAY_URL = getBaseUrl() + "/payWayList";

    //订单
    public static final String ORDER_URL = getBaseUrl() + "/order";

    //更新图像
    public static final String AVATAR_URL = getBaseUrl() + "/uploadUserInavatar";

    //更新用户信息
    public static final String UPDATE_URL = getBaseUrl() + "/updateUser";

    //修改密码
    public static final String MPASSWORD_URL = getBaseUrl() + "/resetpwd";

    //版本更新
    public static final String VERSION_URL = getBaseUrl() + "/checkUpdate";

    //游戏更新查询
    public static final String GAMES_UPDATE = getBaseUrl() + "/gameUpdate";


    //积分任务列表
    public static final String EARN_TASK_URL = getBaseUrl() + "/tasks";

    //商品分类接口
    public static final String GOOD_TYPE_URL = getBaseUrl() + "/goodsType";

    //商品列表接口
    public static final String GOOD_LIST_URL = getBaseUrl() + "/goodsList";

    //商品详细接口
    public static final String GOOD_DETAIL_URL = getBaseUrl() + "/goodsInfo";

    //我的购买记录
    public static final String MY_GOOD_URL_APP = getBaseUrl() + "/myGoods";

    //商品兑换
    public static final String GOOD_CONVERT_URL = getBaseUrl() + "/goodsConvert";

    //安装
    public static final String INSTALL_URL = getBaseUrl() + "/install";

    //卸载
    public static final String UNINSTALL_URL = getBaseUrl() + "/uninstall";

    //更新图像
    public static final String DOWNLOAD_STAT_URL = getBaseUrl() + "/apiGameDown";

    //礼包详情
    public static final String GIFT_INFO_URL = getBaseUrl() + "/giftInfo";

    //ppt
    public static final String SLIDE_URL = getBaseUrl() + "/slide";

    //pay record
    public static final String PAY_RECORD_URL = getBaseUrl() + "/userRecord";

    //开服信息
    public static final String OPEN_SERVICE = getBaseUrl() + "/gameKaifuList";

    public static String getBaseUrl() {
        return (DEBUG ? debugBaseUrl : baseUrl);
    }

    //充值选项
    public static final String PAYOPT_URL = getBaseUrl() + "/payopt";

    //获取应付金额
    public static final String GetOrderPayMoney_URL = getBaseUrl() + "/getOrderPayMoney";


    //用户积分记录
    public static final String POINT_URL = getBaseUrl() + "/pointLog";

}
