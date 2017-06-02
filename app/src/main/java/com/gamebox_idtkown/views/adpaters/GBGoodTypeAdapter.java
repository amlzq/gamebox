package com.gamebox_idtkown.views.adpaters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.core.db.greendao.GoodType;
import com.gamebox_idtkown.utils.RoundedTransformation;
import com.gamebox_idtkown.utils.StateUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangkai on 16/11/14.
 */
public class GBGoodTypeAdapter extends GBBaseAdapter<GoodType> {

    public GBGoodTypeAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_good_type, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GoodType goodType = dataInfos.get(position);
        holder.rlItem.setTag(goodType);
        holder.tvTitle.setText(goodType.getTitle());
        holder.tvSize.setText("(可兑换" + goodType.getTotal_num() + "款商品)");
        holder.tvSize2.setText("已有" + goodType.getTotal_convert() + "人兑换");
        Picasso.with(context).load(goodType.getIco()).transform(new RoundedTransformation(rouned, 0)).placeholder(R.mipmap.icon_default).into(holder.ivIcon);
        return convertView;

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

        @BindView(R.id.size2)
        TextView tvSize2;

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
