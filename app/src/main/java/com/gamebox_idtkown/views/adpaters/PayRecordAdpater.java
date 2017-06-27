package com.gamebox_idtkown.views.adpaters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.domain.PayRecordInfo;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangkai on 2017/2/6.
 */

public class PayRecordAdpater extends GBBaseAdapter<PayRecordInfo> {
    private ListView listView;

    public PayRecordAdpater(Context context) {
        super(context);
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PayRecordAdpater.ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_pay_record, null);
            holder = new PayRecordAdpater.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (PayRecordAdpater.ViewHolder) convertView.getTag();
        }

//        if (position == dataInfos.size() - 1) {
//            holder.vOrderBg.getLayoutParams().height = ScreenUtil.dip2px(context, 1);
//        } else {
//            holder.vOrderBg.getLayoutParams().height = ScreenUtil.dip2px(context, 10);
//        }

        PayRecordInfo payRecordInfo = dataInfos.get(position);
        holder.tvOrderNumber.setText(payRecordInfo.order_sn);
        holder.tvState.setText(payRecordInfo.status_msg);
        String title = "平台币";
        if (payRecordInfo.desp != null && !payRecordInfo.desp.isEmpty() && !payRecordInfo.desp.equals("充值")) {
            title = payRecordInfo.desp;
        }
        holder.tvOrderTitle.setText(title);
        holder.tvOrderAllMoney.setText(payRecordInfo.money);

        holder.tvOrderTime.setText(payRecordInfo.finish_time);
        holder.tvOrderType.setText(payRecordInfo.pay_way_title);
        holder.tvOrderPayMoney.setText(payRecordInfo.rmb_money);
        return convertView;
    }


    class ViewHolder {
        @BindView(R.id.order_number)
        TextView tvOrderNumber;

        @BindView(R.id.state)
        TextView tvState;

        @BindView(R.id.order_title)
        TextView tvOrderTitle;

        @BindView(R.id.order_all_money)
        TextView tvOrderAllMoney;

        @BindView(R.id.order_time)
        TextView tvOrderTime;

        @BindView(R.id.order_type)
        TextView tvOrderType;

        @BindView(R.id.order_pay_money)
        TextView tvOrderPayMoney;

        @BindView(R.id.order_bg)
        View vOrderBg;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
