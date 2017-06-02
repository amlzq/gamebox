package com.gamebox_idtkown.views.adpaters;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.core.db.greendao.GoodChange;
import com.gamebox_idtkown.utils.RoundedTransformation;
import com.gamebox_idtkown.utils.StateUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangkai on 16/11/15.
 */
public class GBGoodChangeAdapter extends GBBaseAdapter<GoodChange> {
    public ListView listView;

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public GBGoodChangeAdapter(Context context) {
        super(context);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_good_change, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GoodChange goodChange = dataInfos.get(position);
        holder.rlItem.setTag(goodChange);
        holder.tvTitle.setText(goodChange.getGoods_name());

        holder.tvPrize.setText(Html.fromHtml("<font androidColor=#0da914>" + goodChange.getGoods_price() + "</font>" + "积分"));
        Picasso.with(context).load(goodChange.getGoods_img()).transform(new RoundedTransformation(rouned, 0)).placeholder(R.mipmap.icon_default).into(holder.ivIcon);
        String desc = goodChange.getTrade_time_text();

        if (goodChange.getGift_code() != null && !goodChange.getGift_code().isEmpty()) {
            desc += "  礼包码: " + goodChange.getGift_code()+"(点击复制)";
        }

        if (goodChange.getGoods_type_id().equals("2")) {
            holder.tvVal.setText(Html.fromHtml(goodChange.getGoods_desp()));
        } else {
            holder.tvVal.setText(Html.fromHtml("价值<font color=#ff0000>" + goodChange.getGoods_type_val()
                    + "元</font>"));
        }
        holder.tvDesc.setText(Html.fromHtml(desc));
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.icon)
        ImageView ivIcon;

        @BindView(R.id.title)
        TextView tvTitle;

        @BindView(R.id.title_sub)
        TextView tvTitleSub;

        @BindView(R.id.desc)
        TextView tvDesc;

        @BindView(R.id.prize)
        TextView tvPrize;

        @BindView(R.id.item)
        RelativeLayout rlItem;

        @BindView(R.id.val)
        TextView tvVal;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            StateUtil.setRipple(rlItem);
        }

        @OnClick({R.id.item})
        public void onClick(View view) {
            onItemClickListener.onClick(view);
            return;
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