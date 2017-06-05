package com.gamebox_idtkown.views.adpaters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.domain.IntegralDetailInfo;
import com.gamebox_idtkown.domain.PayRecordInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangkai on 2017/6/5.
 */

public class IntegralDetailAdapter extends GBBaseAdapter<IntegralDetailInfo> {
    private ListView listView;

    public IntegralDetailAdapter(Context context) {
        super(context);
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_integral_detail, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        IntegralDetailInfo integralDetailInfo = dataInfos.get(position);
        holder.tvPointTitleDesc.setText(integralDetailInfo.getPoint());
        holder.tvActionTitleDesc.setText(integralDetailInfo.getAction_title());
        holder.tvActionTimeDesc.setText(integralDetailInfo.getAdd_time());
        return convertView;
    }


    class ViewHolder {
        @BindView(R.id.action_time)
        TextView tvActionTime;

        @BindView(R.id.action_time_desc)
        TextView tvActionTimeDesc;

        @BindView(R.id.action_title)
        TextView tvActionTitle;

        @BindView(R.id.action_title_desc)
        TextView tvActionTitleDesc;

        @BindView(R.id.point_title)
        TextView tvPointTitle;

        @BindView(R.id.point_title_desc)
        TextView tvPointTitleDesc;

        @BindView(R.id.order_bg)
        View vOrderBg;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
