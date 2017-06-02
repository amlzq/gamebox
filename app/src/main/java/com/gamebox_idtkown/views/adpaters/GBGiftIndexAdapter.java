package com.gamebox_idtkown.views.adpaters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.core.db.greendao.GiftIndex;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.CheckUtil;
import com.gamebox_idtkown.utils.RoundedTransformation;
import com.gamebox_idtkown.utils.StateUtil;
import com.gamebox_idtkown.views.widgets.GBDownloadBtn;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangkai on 16/10/24.
 */
public class GBGiftIndexAdapter extends GBBaseAdapter<GiftIndex> {

    public GBGiftIndexAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gift_index, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GiftIndex giftIndex = dataInfos.get(position);
        updateInfo(giftIndex, holder);
        return convertView;
    }

    private void updateInfo(GiftIndex giftIndex, ViewHolder holder) {
        holder.rlItem.setTag(giftIndex);
        holder.btnView.setTag(giftIndex);
        ApkStatusUtil.enableButtonState(context, holder.btnView, GoagalInfo.getInItInfo().androidColor);
        holder.tvTitle.setText(giftIndex.getGameName());
        holder.tvSize.setText("礼包种类：" + giftIndex.getNum());
        holder.tvLastGiftName.setText(CheckUtil.checkStr(giftIndex.getLastGiftName(), "无"));
        Picasso.with(context).load(giftIndex.getIconUrl()).transform(new RoundedTransformation(rouned, 0)).placeholder(R.mipmap.icon_default).into(holder.ivIcon);
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

        @BindView(R.id.last_gift_name)
        TextView tvLastGiftName;

        @BindView(R.id.view)
        GBDownloadBtn btnView;

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
