package com.gamebox_idtkown.views.adpaters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.domain.PayOptInfo;
import com.gamebox_idtkown.utils.ScreenUtil;
import com.gamebox_idtkown.utils.StateUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangkai on 2017/4/15.
 */

public class PayOptAdpater extends GBBaseAdapter<PayOptInfo> {

    public PayOptAdpater(Context context) {
        super(context);
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
        holder.tvMoney.setText(payOptInfo.real_money + "元");
        holder.tvRMoney.setText("实付"+payOptInfo.pay_money + "元");
        if (payOptInfo.isSelected) {
            setStorke(context, holder.rlItem, Color.parseColor(GoagalInfo.getInItInfo().themeColor));
        } else {
            setStorke(context, holder.rlItem, Color.parseColor("#E9EAEB"));
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
