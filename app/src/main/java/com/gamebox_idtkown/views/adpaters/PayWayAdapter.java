package com.gamebox_idtkown.views.adpaters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.core.db.greendao.PayTypeInfo;
import com.gamebox_idtkown.utils.CheckUtil;
import com.gamebox_idtkown.utils.StateUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangkai on 16/10/27.
 */
public class PayWayAdapter extends GBBaseAdapter<PayTypeInfo> {
    private ListView listView;

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public PayWayAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_pay, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PayTypeInfo payTypeInfo = dataInfos.get(position);
        updateInfo(payTypeInfo, holder);
        return convertView;

    }

    private void updateInfo(PayTypeInfo payTypeInfo, ViewHolder holder) {
        String type = "zfb";
        String name = payTypeInfo.getName();
        holder.rlItem.setTag(payTypeInfo);

        holder.tvTitle.setText(payTypeInfo.getName());
        if (name.contains("支付宝")) {
            type = "zfb";
            boolean isinstall = CheckUtil.checkAliPayInstalled(context);
            holder.tvOther.setText(isinstall ? "已绑定" : "未绑定");
            if (!isinstall) {
                holder.tvOther.setTextColor(Color.parseColor("#DC554C"));
            }
            holder.tvTitle.setText(Html.fromHtml(payTypeInfo.getName()+"(<font color=#ff6600>推荐</font>)") );
            holder.ivIcon.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.alpay));
        } else if (name.contains("微信")) {
            type = "wxpay";
            boolean isinstall = CheckUtil.isWeixinAvilible(context);
            holder.tvOther.setText(isinstall ? "已绑定" : "未绑定");
            if (!isinstall) {
                holder.tvOther.setTextColor(Color.parseColor("#DC554C"));
            }
            holder.ivIcon.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.webcat));
        } else if (name.contains("银联")) {
            type = "bank";
            holder.ivIcon.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.bank));
        }
        payTypeInfo.setType(type);
    }

    class ViewHolder {
        @BindView(R.id.item)
        RelativeLayout rlItem;

        @BindView(R.id.icon)
        ImageView ivIcon;

        @BindView(R.id.title)
        TextView tvTitle;

        @BindView(R.id.other)
        TextView tvOther;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            StateUtil.setRipple(rlItem);
        }

        @OnClick({R.id.item})
        public void onClick(View view) {
            onItemClickListener.onClick(view);
        }
    }


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(View view);
    }
}