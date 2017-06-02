package com.gamebox_idtkown.views.adpaters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.utils.StateUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangkai on 16/9/28.
 */
public class GBTGridViewAdapter extends GBBaseAdapter<String> {
    public GBTGridViewAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView =  inflater.inflate(R.layout.item_tgrid, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        String tag = dataInfos.get(position);
        holder.rlItem.setTag(tag);
        holder.tvTitle.setText(tag);
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.item)
        RelativeLayout rlItem;

        @BindView(R.id.title)
        TextView tvTitle;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
            StateUtil.setRipple(rlItem);
        }

        @OnClick({R.id.item})
        public void onClick(View view){
            onItemClickListener.onClick(view);
        }
    }

    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onClick(View view);
    }
}
