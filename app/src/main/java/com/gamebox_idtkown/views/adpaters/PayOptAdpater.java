package com.gamebox_idtkown.views.adpaters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.activitys.PayActivity;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.domain.PayOptInfo;
import com.gamebox_idtkown.utils.ScreenUtil;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangkai on 2017/4/15.
 */

public class PayOptAdpater extends GBBaseAdapter<PayOptInfo> {

    private EditText etzh;
    private float money;
    private boolean isEidt;

    public PayOptAdpater(Context context) {
        super(context);
    }

    private String formatOne(float number) {
        String result = new DecimalFormat("#.0").format(number);
        return result;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_pay_opt, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PayOptInfo payOptInfo = dataInfos.get(i);
        holder.rlItem.setTag(payOptInfo);
        holder.tvMoney.setText(formatOne(payOptInfo.pay_money) + "元");
        holder.tvRMoney.setText("实付" + formatOne(payOptInfo.real_money) + "元");
        if (payOptInfo.isSelected) {
            money = payOptInfo.pay_money;
            setStorke(context, holder.tvMoney, Color.parseColor(GoagalInfo.getInItInfo().themeColor));
            setStorke(context, holder.etzh, Color.parseColor(GoagalInfo.getInItInfo().themeColor));
        } else {
            setStorke(context, holder.tvMoney, Color.parseColor("#E9EAEB"));
            setStorke(context, holder.etzh, Color.parseColor("#E9EAEB"));
        }


        if (i == dataInfos.size() - 1) {
            holder.tvMoney.setVisibility(View.GONE);
            holder.rlzh.setVisibility(View.VISIBLE);
            etzh = holder.etzh;
            final PayActivity payActivity = (PayActivity) context;
            if (!isEidt) {
                etzh.setText(formatOne(money) + "");
            }
            isEidt = false;
            etzh.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    isEidt = true;
                    String tmpmoney = etzh.getText().toString();
                    String tmp = payActivity.money;
                    payActivity.money = tmpmoney;
                    if (payActivity.isMoney()) {
                        payActivity.setMoney(Float.parseFloat(tmpmoney));
                        if (dataInfos != null) {
                            for (PayOptInfo payOptInfo2 : dataInfos) {
                                payOptInfo2.isSelected = false;
                            }
                            for (PayOptInfo payOptInfo2 : dataInfos) {
                                if (payOptInfo2.pay_money == Float.parseFloat(tmpmoney)) {
                                    payOptInfo2.isSelected = true;
                                    break;
                                }
                            }
                            notifyDataSetChanged();
                        }
                    } else {
                        payActivity.money = tmp;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        } else {
            holder.tvMoney.setVisibility(View.VISIBLE);
            holder.rlzh.setVisibility(View.GONE);
        }

        return convertView;
    }


    class ViewHolder {
        @BindView(R.id.item)
        RelativeLayout rlItem;

        @BindView(R.id.money)
        TextView tvMoney;

        @BindView(R.id.rmoney)
        TextView tvRMoney;

        @BindView(R.id.rlzh)
        LinearLayout rlzh;

        @BindView(R.id.etzh)
        EditText etzh;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.item})
        public void onClick(View view) {
            onItemClickListener.onClick(view);
        }
    }

    private void setStorke(Context context, View view, int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setStroke(ScreenUtil.dip2px(context, 1), color);
        view.setBackground(drawable);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(View view);
    }
}
