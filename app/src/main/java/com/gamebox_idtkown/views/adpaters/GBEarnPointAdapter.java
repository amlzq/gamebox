package com.gamebox_idtkown.views.adpaters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.core.db.greendao.EarnPointTaskInfo;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.utils.StateUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by zhangkai on 16/11/14.
 */
public class GBEarnPointAdapter extends GBBaseAdapter<EarnPointTaskInfo> implements StickyListHeadersAdapter {
    public ListView listView;


    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public GBEarnPointAdapter(Context context) {
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
            convertView = inflater.inflate(R.layout.item_earn_point, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        EarnPointTaskInfo taskInfo = dataInfos.get(position);

        holder.rlItem.setTag(taskInfo);
        holder.btnDownload.setTag(taskInfo);
        if (taskInfo.getLog_point() == null || taskInfo.getLog_point().isEmpty()) {
            holder.btnDownload.setText("进行中");
        } else {
            holder.btnDownload.setText("已完成");
            StateUtil.setDrawable(context, holder.btnDownload, 2.5f, Color.parseColor("#999999"));
        }
        holder.tvTitleSub.setText(taskInfo.getTitle_sub());
        holder.tvTitle.setText(taskInfo.getTitle());
        holder.tvDesc.setText(taskInfo.getDesp());
        holder.tvPrize.setText("奖励：" + taskInfo.getPoint());
        Picasso.with(context).load(taskInfo.getIco()).placeholder(R.mipmap.icon_default).into(holder.ivIcon);
        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_download_header, parent, false);
            holder = new HeaderViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.tvTitle.setText("新手任务");
        } else {
            holder.tvTitle.setText("每日任务");
        }
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        String type = dataInfos.get(position).getType();
        long hType = 1;
        try {
            hType = Long.parseLong(dataInfos.get(position).getType());
        } catch (Exception e) {
            if ( type!=null && type.equals("repeat")) {
                hType = 0;
            }
        }
        return hType;
    }

    class HeaderViewHolder {
        @BindView(R.id.icon)
        ImageView ivIcon;

        @BindView(R.id.title)
        TextView tvTitle;

        public HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
            ivIcon.setBackgroundColor(GoagalInfo.getInItInfo().androidColor);
        }
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
