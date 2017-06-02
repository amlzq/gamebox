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
import com.gamebox_idtkown.core.db.greendao.GoodList;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.RoundedTransformation;
import com.gamebox_idtkown.utils.StateUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangkai on 16/11/14.
 */
public class GBGoodListAdapter extends GBBaseAdapter<GoodList> {
    public ListView listView;

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public GBGoodListAdapter(Context context) {
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
            convertView = inflater.inflate(R.layout.item_good_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GoodList goodList = dataInfos.get(position);
        holder.rlItem.setTag(goodList);
        holder.btnDownload.setTag(goodList);
        ApkStatusUtil.enableButtonState(context, holder.btnDownload, GoagalInfo.getInItInfo().androidColor);
        holder.tvTitle.setText(goodList.getName());
        String desc = "价值<font color=#ff0000>" + goodList.getType_val() + "元</font>";
        if (goodList.getType_id().equals("2")) {
            desc = goodList.getDesp();
        }
        if (goodList.getType_id().equals("1")) {
            holder.tvUseMoney.setText("满" + goodList.getUc_money() + "元可用");
        }
        holder.tvDesc.setText(Html.fromHtml(desc));
        holder.tvPrize.setText(Html.fromHtml("<font color=#0da914>" + goodList.getPrice() + "</font>" + "积分"));
        Picasso.with(context).load(goodList.getImg()).transform(new RoundedTransformation(rouned, 0)).placeholder(R.mipmap.icon_default).into(holder.ivIcon);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.icon)
        ImageView ivIcon;

        @BindView(R.id.title)
        TextView tvTitle;

        @BindView(R.id.use_money)
        TextView tvUseMoney;

        @BindView(R.id.title_sub)
        TextView tvTitleSub;

        @BindView(R.id.desc)
        TextView tvDesc;

        @BindView(R.id.prize)
        TextView tvPrize;

        @BindView(R.id.download)
        TextView btnDownload;

        @BindView(R.id.item)
        RelativeLayout rlItem;


        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            StateUtil.setRipple(rlItem);
        }

        @OnClick({R.id.download, R.id.item})
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
