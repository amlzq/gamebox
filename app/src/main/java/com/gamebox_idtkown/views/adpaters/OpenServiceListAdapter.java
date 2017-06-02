package com.gamebox_idtkown.views.adpaters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.domain.GameOpenServiceInfo;
import com.gamebox_idtkown.utils.StateUtil;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by zhangkai on 2017/2/24.
 */

public class OpenServiceListAdapter extends GBBaseAdapter<GameOpenServiceInfo> {

    public OpenServiceListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_fragment_service, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GameOpenServiceInfo openServiceInfo = dataInfos.get(i);
        holder.tvTitle.setText(openServiceInfo.getServer() + "   \n" + openServiceInfo.getOpen_time());
        if (i + 1 < dataInfos.size()) {
            GameOpenServiceInfo openServiceInfo2 = dataInfos.get(i + 1);
            holder.tvTitle2.setText(openServiceInfo2.getServer() + "   \n" + openServiceInfo2.getOpen_time());
            holder.tvTitle2.setVisibility(View.VISIBLE);
        } else {
            holder.tvTitle2.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return dataInfos != null ? (dataInfos.size() / 2 + dataInfos.size() % 2 > 0 ? 1 : 0) : 0;
    }

    class ViewHolder {

        @BindView(R.id.title)
        TextView tvTitle;

        @BindView(R.id.title2)
        TextView tvTitle2;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            StateUtil.setStorke(context, tvTitle, 40);
            StateUtil.setStorke(context, tvTitle2, 40);
        }
    }


}
