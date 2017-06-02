package com.gamebox_idtkown.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.cache.PayTypeCache;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.DbUtil;
import com.gamebox_idtkown.core.db.greendao.PayTypeInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.domain.PayOptInfo;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.OrderPayMoneyEngin;
import com.gamebox_idtkown.engin.PayEngin;
import com.gamebox_idtkown.engin.PayOptEngin;
import com.gamebox_idtkown.engin.PayWayEngin;
import com.gamebox_idtkown.net.contains.HttpConfig;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.security.Rsa;
import com.gamebox_idtkown.utils.LoadingUtil;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.ScreenUtil;
import com.gamebox_idtkown.utils.StateUtil;
import com.gamebox_idtkown.utils.TaskUtil;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.views.adpaters.PayOptAdpater;
import com.gamebox_idtkown.views.adpaters.PayWayAdapter;
import com.gamebox_idtkown.views.widgets.GBActionBar5;
import com.ipaynow.plugin.api.IpaynowPlugin;
import com.ipaynow.plugin.utils.PreSignMessageUtil;

import org.apache.commons.lang.math.NumberUtils;
import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import mirror.android.widget.Toast;

/**
 * Created by zhangkai on 16/10/25.
 */
public class PayActivity extends BaseActionBarActivity<GBActionBar5> {
    @BindView(R.id.tvusername)
    TextView tvUsername;

    @BindView(R.id.tvmoney)
    TextView tvMoney;

    @BindView(R.id.et_money)
    EditText etMoney;

    @BindView(R.id.gridview)
    GridView gridView;

    @BindView(R.id.listView)
    ListView listView;

    private String money;

    PayOptEngin payOptEngin;
    PayOptAdpater payOptAdpater;

    private PayOptInfo mPayOptInfo;

    @Override
    public int getLayoutID() {
        return R.layout.activity_pay;
    }

    private PayWayAdapter adapter;


    @Override
    public boolean isNeedLogin() {
        return true;
    }

    @Override
    public void initViews() {
        super.initViews();
        view.setBackgroundColor(Color.WHITE);
        setBackListener();
        actionBar.setTitle("充值");
        actionBar.showMenuItem("充值记录");
        actionBar.setOnItemClickListener(new GBActionBar5.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                startActivity(new Intent(getBaseContext(), PayRecordActivity.class));
            }

            @Override
            public void onClose(View view) {

            }
        });

        payOptAdpater = new PayOptAdpater(this);
        gridView.setAdapter(payOptAdpater);
        payOptAdpater.setOnItemClickListener(new PayOptAdpater.OnItemClickListener() {
            @Override
            public void onClick(View view) {
                PayOptInfo payOptInfo = (PayOptInfo) view.getTag();
                payOptInfo.isSelected = true;
                etMoney.setText(payOptInfo.real_money + "");
            }
        });

        adapter = new PayWayAdapter(this);
        listView.setAdapter(adapter);

        if (GBApplication.userInfo.getIs_vali_mobile()) {
            String phone = GBApplication.userInfo.getMobile();
            if (phone.length() >= 11) {
                phone = phone.replace(phone.substring(3, 7), "****");
            }
            tvUsername.setText(phone);
        } else {
            tvUsername.setText(GBApplication.userInfo.getName());
        }

        adapter.setOnItemClickListener(new PayWayAdapter.OnItemClickListener() {
                                           @Override
                                           public void onClick(final View view) {
                                               if (!isMoney()) {
                                                   ToastUtil.toast2(getBaseContext(), "输入的金额不正确或小于最小支付额度");
                                                   return;
                                               }
                                               DecimalFormat decimalFormat = new DecimalFormat("0.00");
                                               //构造方法的字符格式这里如果小数不足2位,会以0补足.
                                               money = decimalFormat.format(Float.parseFloat(money));
                                               tvMoney.setText(money);
                                               etMoney.setText(money);
                                               PayTypeInfo payTypeInfo = (PayTypeInfo) view.getTag();

                                               getPayMoney(payTypeInfo, money);


                                           }
                                       }
        );

        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!flag) {
                    String tmpmoney = etMoney.getText().toString();
                    try{
                        Float.parseFloat(tmpmoney);
                    }catch (Exception e){
                        return;
                    }
                    money = tmpmoney;
                    if (payOptAdpater.dataInfos != null) {
                        for (PayOptInfo payOptInfo2 : payOptAdpater.dataInfos) {
                            payOptInfo2.isSelected = false;
                        }
                        for (PayOptInfo payOptInfo2 : payOptAdpater.dataInfos) {
                            if (payOptInfo2.real_money == Float.parseFloat(tmpmoney)) {
                                payOptInfo2.isSelected = true;
                                break;
                            }
                        }
                        payOptAdpater.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                flag = false;
            }
        });

        setStorke(getBaseContext(), etMoney, Color.parseColor("#E9EAEB"));


    }

    OrderPayMoneyEngin orderPayMoneyEngin;

    public void getPayMoney(final PayTypeInfo payTypeInfo, final String money) {
        if (orderPayMoneyEngin == null) {
            orderPayMoneyEngin = new OrderPayMoneyEngin();
        }
        LoadingUtil.show(this, "请稍后...");
        orderPayMoneyEngin.getOrderPayMoney(money, new Callback() {
            @Override
            public void onSuccess(final ResultInfo resultInfo) {
                PayActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoadingUtil.dismiss();
                        if(resultInfo == null || resultInfo.code != HttpConfig.STATUS_OK){
                            ToastUtil.toast2(PayActivity.this, getMessage(resultInfo.message, DescConstans
                                    .SERVICE_ERROR));
                            return;
                        }

                        try{
                            Float.parseFloat(resultInfo.data+"");
                        }catch (Exception e){
                            ToastUtil.toast2(PayActivity.this, getMessage(resultInfo.message, DescConstans
                                    .SERVICE_ERROR));
                            return;
                        }

                        new MaterialDialog.Builder(PayActivity.this)
                                .title("提示")
                                .content(Html.fromHtml("确认支付：<font color=red>" + resultInfo.data +
                                        "</font>元?<br/>实际到帐：<font color=red>" +
                                        money + "</font>元"))
                                .positiveColor(GoagalInfo.getInItInfo().androidColor)
                                .negativeColorRes(R.color.gray_light)
                                .positiveText("确定")
                                .negativeText("取消")
                                .onAny(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        if (which == DialogAction.POSITIVE) {
                                            PayActivity.this.money = resultInfo.data+"";
                                            pay(payTypeInfo);
                                        }
                                    }
                                })
                                .show();
                    }
                });
            }

            @Override
            public void onFailure(final Response response) {
                PayActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.toast2(PayActivity.this, getMessage(response.body, DescConstans.NET_ERROR));
                        LoadingUtil.dismiss();
                    }
                });
            }
        });

    }


    private IpaynowPlugin mIpaynowplugin;

    @Override
    public void initVars() {
        super.initVars();
        payOptEngin = new PayOptEngin();
        if (mIpaynowplugin == null) {
            mIpaynowplugin = IpaynowPlugin.getInstance().init(this);// 1.插件初始化
            mIpaynowplugin.unCkeckEnvironment();
        }
    }

    private void pay(final PayTypeInfo payTypeInfo) {

        String md5signstr = "";
        if (payTypeInfo!= null && payTypeInfo.getType() != null && !payTypeInfo.getType().equals("zfb")) {
            if (payTypeInfo.getType().equals("wxpay")) {
                prePayMessage("13");
            } else {
                prePayMessage("11");
            }
            LogUtil.msg(preSign.generatePreSignMessage() + "");
            md5signstr = preSign.generatePreSignMessage();
        }

        LoadingUtil.show(this, "正在创建订单...");
        PayEngin.getImpl(getBaseContext()).pay(payTypeInfo.getType(), money, md5signstr
                ,
                new Callback<HashMap>() {
                    @Override
                    public void onSuccess(final ResultInfo<HashMap> resultInfo) {
                        bindView(new Runnable() {
                            @Override
                            public void run() {
                                LoadingUtil.dismiss();
                                if (resultInfo == null) {
                                    ToastUtil.toast2(getBaseContext(), DescConstans.SERVICE_ERROR);
                                    return;
                                }
                                if (resultInfo.code == 1) {
                                    String notify_url = "";
                                    String orderId = "";
                                    String partnerid = "";
                                    String email = "";
                                    String privatekey = "";
                                    String wx_sign = "";
                                    String starttime = "";
                                    try {
                                        orderId = resultInfo.data.get("order_num") + "";
                                        notify_url = resultInfo.data.get("notify_url") + "";
                                        JSONObject params = (JSONObject) JSON.parse(resultInfo.data.get("params") + "");
                                        partnerid = params.get("partnerid") + "";
                                        if (payTypeInfo.getType().equals("zfb")) {
                                            email = params.get("email") + "";
                                            privatekey = params.get("privatekey") + "";
                                            payTask(partnerid, email, privatekey, orderId, notify_url);
                                        } else {
                                            starttime = resultInfo.data.get("starttime") + "";
                                            preSign.appId = partnerid;
                                            preSign.mhtOrderNo = orderId;
                                            preSign.notifyUrl = notify_url;
                                            preSign.mhtOrderStartTime = starttime;
                                            wx_sign = resultInfo.data.get("wx_sign") + "";
                                            String preSignStr = preSign.generatePreSignMessage();
                                            String mhtSignature = preSignStr + "&mhtSignature=" + wx_sign
                                                    + "&mhtSignType=MD5";
                                            mIpaynowplugin.setCallResultActivity(PayActivity.this).pay(mhtSignature);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        LogUtil.msg("支付宝支付出错->" + e);
                                    }
                                } else {
                                    ToastUtil.toast2(getBaseContext(), getMessage(resultInfo.message, DescConstans.SERVICE_ERROR));
                                }
                            }
                        });

                    }

                    @Override
                    public void onFailure(Response response) {
                        error();
                    }
                });


    }


    /**
     * 确定支付，将相关信息发送到支付宝服务端
     */
    private void payTask(String partnerId, String email, String privateKey, String orderid, String url) {
        try {
            privateKey = GoagalInfo.getPublicKey(privateKey);
            String info = getNewOrderInfo(partnerId, email, orderid, url);
            String sign = Rsa.sign(info, privateKey);
            sign = URLEncoder.encode(sign);
            info += "&sign=\"" + sign + "\"&" + getSignType();
            final String orderInfo = info;
            TaskUtil.getImpl().runTask(new Runnable() {
                                           @Override
                                           public void run() {
                                               // 构造PayTask 对象
                                               PayTask alipay = new PayTask(PayActivity.this);
                                               // 调用支付接口
                                               final String resultInfo = alipay.pay(orderInfo);

                                               bindView(new Runnable() {
                                                            @Override
                                                            public void run() {

                                                                String result = (String) resultInfo;

                                                                if (null != result) {
                                                                    String[] result_obj = result.split(";");
                                                                    int resultStatus;// 返回状态码
                                                                    String memo;// 提示信息
                                                                    resultStatus = Integer.parseInt(result_obj[0].substring(
                                                                            result_obj[0].indexOf("{") + 1,
                                                                            result_obj[0].lastIndexOf("}")));
                                                                    memo = result_obj[1].substring(
                                                                            result_obj[1].indexOf("{") + 1,
                                                                            result_obj[1].lastIndexOf("}"));
                                                                    if (resultStatus == 9000) {
                                                                        // 支付成功
                                                                        updateUserInfo();
                                                                    }
                                                                    ToastUtil.toast(getBaseContext(), getMessage(memo, "支付成功"));
                                                                } else {
                                                                    // 如果msg为null 是支付宝那边返回数据为null
                                                                    ToastUtil.toast(getBaseContext(),
                                                                            "无法判别支付是否成功，具体请查看后台数据");

                                                                }
                                                            }
                                                        }

                                               );
                                           }
                                       }
            );
        } catch (Exception ex) {
            ex.printStackTrace();
            ToastUtil.toast(getBaseContext(), "支付失败");
        }
    }

    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    private String getNewOrderInfo(String partnerId, String email, String orderid, String url) throws
            UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append("partner=\""); // 合作者身份id 等待服务端传递
        sb.append(partnerId);
        sb.append("\"&out_trade_no=\"");// 订单号，等待服务端传递
        sb.append(orderid);
        sb.append("\"&subject=\"");
        sb.append(DescConstans.PRODUCT_NAME);
        sb.append("\"&body=\"");
        sb.append(DescConstans.PRODUCT_NAME);
        sb.append("\"&total_fee=\"");
        sb.append(money);
        sb.append("\"&notify_url=\"");
        // 网址需要做URL编码

        sb.append(URLEncoder.encode(url, "UTF-8"));
        sb.append("\"&service=\"mobile.securitypay.pay");
        sb.append("\"&_input_charset=\"UTF-8");
        // sb.append("\"&return_url=\"");
        // sb.append(URLEncoder.encode("http://m.alipay.com"));
        sb.append("\"&payment_type=\"1");
        sb.append("\"&seller_id=\"");// 卖家支付宝账号，等待服务端传递
        sb.append(email);
        // 如果show_url值为空，可不传
        // sb.append("\"&show_url=\"");
        sb.append("\"&it_b_pay=\"1m");
        sb.append("\"");
        return new String(sb);
    }

    //微信支付
    private PreSignMessageUtil preSign = new PreSignMessageUtil();

    private void prePayMessage(String type) {
        preSign.appId = "{appid}";

        preSign.mhtCharset = "UTF-8";
        preSign.mhtCurrencyType = "156";
        // 支付金额
        preSign.mhtOrderAmt = Integer.toString((int) (Float.parseFloat(money) * 100));

        preSign.mhtOrderDetail = DescConstans.PRODUCT_NAME;
        preSign.mhtOrderName = DescConstans.PRODUCT_NAME;
        preSign.mhtOrderNo = "{orderid}";
        preSign.payChannelType = type;

        preSign.mhtOrderStartTime = "{starttime}";

        preSign.mhtOrderTimeOut = "3600";
        preSign.mhtOrderType = "01";
        preSign.notifyUrl = "{notify_url}";
        preSign.mhtReserved = "nowpay";

    }

    public boolean isMoney() {
        boolean flag = NumberUtils.isNumber(money);
        return flag && Float.parseFloat(money) >= 0.0099f;
    }

    @Override
    public void loadData() {
        super.loadData();
        showProcessView();
        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                final List<PayTypeInfo> dataInfos = PayTypeCache.getCache(getBaseContext());
                if (dataInfos != null && dataInfos.size() > 0) {
                    bindView(new Runnable() {
                        @Override
                        public void run() {
                            adapter.dataInfos = dataInfos;
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        PayWayEngin.getImpl(this).getPayType(new Callback<List<PayTypeInfo>>() {
            @Override
            public void onSuccess(final ResultInfo<List<PayTypeInfo>> resultInfo) {
                bindView(new Runnable() {
                    @Override
                    public void run() {
                        removeProcessView();
                        if (resultInfo != null) {
                            adapter.dataInfos = resultInfo.data;
                            adapter.notifyDataSetChanged();
                            PayTypeCache.setCache(getBaseContext(), adapter.dataInfos);
                        }
                    }
                });

            }

            @Override
            public void onFailure(Response response) {
                bindView(new Runnable() {
                    @Override
                    public void run() {
                        removeProcessView();
                        ToastUtil.toast2(getBaseContext(), DescConstans.SERVICE_ERROR);
                    }
                });
            }
        });

        payOptEngin.getPayOpt(new Callback<List<PayOptInfo>>() {
            @Override
            public void onSuccess(final ResultInfo<List<PayOptInfo>> resultInfo) {
                if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK) {
                    PayActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            payOptAdpater.dataInfos = resultInfo.data;
                            int n = 0;
                            if (resultInfo.data != null && resultInfo.data.size() > 0) {
                                payOptAdpater.dataInfos.get(0).isSelected = true;
                                etMoney.setText("" + payOptAdpater.dataInfos.get(0).real_money);
                                n = resultInfo.data.size() / 3 + (resultInfo.data.size() % 3 > 0 ? 1 : 0);
                                mPayOptInfo = payOptAdpater.dataInfos.get(0);
                            }
                            gridView.getLayoutParams().height = ScreenUtil.dip2px(getBaseContext(), n * 60 + (n - 1) * 10);
                            payOptAdpater.notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Response response) {

            }
        });
    }

    private void setStorke(Context context, View view, int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setStroke(ScreenUtil.dip2px(context, 1), color);
        view.setBackground(drawable);
    }


    private boolean flag = false;


    @Override
    protected void onActivityResult(int arg0, int arg1, Intent data) {
        // TODO Auto-generated method stub
        if (data == null) {
            return;
        }
        String bmsg = "";
        String msg = data.getExtras().getString("respMsg");
        String errorcode = data.getExtras().getString("errorCode");
        String respCode = data.getExtras().getString("respCode");
        if (respCode.equals("00")) {
            bmsg = "支付成功";
            updateUserInfo();
        } else {
            bmsg = "支付失败";
        }
        ToastUtil.toast2(this, getMessage(msg, bmsg));
    }

    private void updateUserInfo() {
        float total = Float.parseFloat(GBApplication.userInfo.getMoney()) + Float.parseFloat(money);
        GBApplication.userInfo.setMoney(total + "");
        DbUtil.getSession(getBaseContext()).update(GBApplication.userInfo);
        EventBus.getDefault().post(GBApplication.userInfo);
    }
}
