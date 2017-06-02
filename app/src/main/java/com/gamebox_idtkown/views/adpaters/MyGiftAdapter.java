package com.gamebox_idtkown.views.adpaters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.core.db.greendao.GiftDetail;
import com.gamebox_idtkown.utils.RoundedTransformation;
import com.gamebox_idtkown.utils.StateUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangkai on 16/10/26.
 */
public class MyGiftAdapter extends GBBaseAdapter<GiftDetail> {
    public MyGiftAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_my_gift, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GiftDetail giftDetail = dataInfos.get(position);
        updateInfo(giftDetail, holder);
        return convertView;
    }

    private void updateInfo(GiftDetail giftDetail, ViewHolder holder) {
        holder.rlItem.setTag(giftDetail);
        holder.btnView.setTag(giftDetail);

        holder.tvTitle.setText(giftDetail.getName());
        holder.tvCode.setText("礼包码：" + giftDetail.getCode());
        Picasso.with(context).load(giftDetail.getImgUrl()).transform(new RoundedTransformation(rouned, 0)).placeholder(R.mipmap.icon_default).into(holder.ivIcon);
    }

    class ViewHolder {
        @BindView(R.id.item)
        RelativeLayout rlItem;

        @BindView(R.id.icon)
        ImageView ivIcon;

        @BindView(R.id.title)
        TextView tvTitle;

        @BindView(R.id.code)
        TextView tvCode;

        @BindView(R.id.view)
        TextView btnView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            StateUtil.setRipple(rlItem);
        }

        @OnClick({R.id.item, R.id.view})
        public void onClick(View view) {
            if(view instanceof RelativeLayout) {
                onItemClickListener.onDetail(view);
            } else {
                onItemClickListener.onClick(view);
            }
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onDetail(View view);
        void onClick(View view);
    }
}