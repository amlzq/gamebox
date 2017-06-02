package com.gamebox_idtkown.views.adpaters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.core.db.greendao.GiftDetail;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.CheckUtil;
import com.gamebox_idtkown.utils.RoundedTransformation;
import com.gamebox_idtkown.utils.StateUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangkai on 16/10/24.
 */
public class GBGiftListAdapter extends GBBaseAdapter<GiftDetail> {
    public GBGiftListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gift_list, null);
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
        ApkStatusUtil.enableButtonState(context, holder.btnView, GoagalInfo.getInItInfo().androidColor);
        holder.tvTitle.setText(giftDetail.getName());
        holder.tvSize.setText("剩余：" + CheckUtil.checkStr(giftDetail.getSurplusNum(), "0"));
        holder.tvDesc.setText(giftDetail.getContent());
        if (giftDetail != null && giftDetail.fixisPay.equals("1")) {
            holder.btnView.setText("兑换");
        } else {
            holder.btnView.setText("免费");
        }
        Picasso.with(context).load(giftDetail.getImgUrl()).transform(new RoundedTransformation(rouned, 0)).placeholder(R.mipmap.icon_default).into(holder.ivIcon);
    }

    class ViewHolder {
        @BindView(R.id.item)
        RelativeLayout rlItem;

        @BindView(R.id.icon)
        ImageView ivIcon;

        @BindView(R.id.title)
        TextView tvTitle;

        @BindView(R.id.size)
        TextView tvSize;

        @BindView(R.id.desc)
        TextView tvDesc;

        @BindView(R.id.view)
        TextView btnView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            StateUtil.setRipple(rlItem);
        }

        @OnClick({R.id.item, R.id.view})
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
