package com.gamebox_idtkown.views.adpaters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.core.ApkStatus;
import com.gamebox_idtkown.core.db.greendao.ChosenInfo;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.services.DownloadManagerService;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.CheckUtil;
import com.gamebox_idtkown.utils.RoundedTransformation;
import com.gamebox_idtkown.utils.ScreenUtil;
import com.gamebox_idtkown.utils.StateUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangkai on 16/9/26.
 */
public class GBChosenAdapter extends GBBaseAdapter<GameInfo> {
    private ListView listView;
    private int width;

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public GBChosenAdapter(Context context) {
        super(context);
        width = ScreenUtil.getWidth(context) - ScreenUtil.dip2px(context, 181);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GameInfo gameInfo = dataInfos.get(position);
        ViewHolder holder = null;
        HeaderViewHolder headerViewHolder = null;

        switch (getItemViewType(position)) {
            case 1:
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.item_chosen_header, parent, false);
                    headerViewHolder = new HeaderViewHolder(convertView);
                    convertView.setTag(headerViewHolder);
                } else {
                    headerViewHolder = (HeaderViewHolder) convertView.getTag();
                }
                ChosenInfo chosenInfo = dataInfos.get(position).chosenInfo;
                headerViewHolder.ivIcon.setTag(chosenInfo);
                Picasso.with(context).load(chosenInfo.getImg()).transform(new RoundedTransformation(ScreenUtil.dip2px
                        (context, 5), 0))
                        .into
                        (headerViewHolder.ivIcon);
                break;
            case 0:
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.view_h_gamelist_item, null);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                updateInfo(gameInfo, holder);
                break;
        }
        return convertView;

    }

    @Override
    public int getItemViewType(int position) {
        GameInfo gameInfo = dataInfos.get(position);
        if (gameInfo.chosenInfo == null) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    public void updateView(DownloadInfo downloadInfo) {
        if (dataInfos == null) {
            return;
        }
        int position = -1;
        GameInfo tmpGameInfo = null;
        for (int i = 0; i < dataInfos.size(); i++) {
            tmpGameInfo = dataInfos.get(i);
            if (DownloadManagerService.isSameDownloadInfo(tmpGameInfo, downloadInfo)) {
                tmpGameInfo.setStatus(downloadInfo.status);
                tmpGameInfo.setPackageName(downloadInfo.packageName);
                position = i;
                break;
            }
        }

        int hcount = listView.getHeaderViewsCount();
        int fcount = listView.getFooterViewsCount();
        int fvisiblePos = listView.getFirstVisiblePosition() - hcount;
        int lvisiblePos = listView.getLastVisiblePosition() - fcount;

        View view = null;
        if (position >= fvisiblePos && position <= lvisiblePos) {
            view = (listView.getChildAt(position - fvisiblePos));
        }

        if (view == null) {
            return;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            return;
        }
        updateInfo(tmpGameInfo, holder);
    }

    public void updateView(GameInfo gameInfo) {
        if (dataInfos == null) {
            return;
        }

        int position = -1;

        for (int i = 0; i < dataInfos.size(); i++) {
            GameInfo tmpGameInfo = dataInfos.get(i);
            if (DownloadManagerService.isSameDownloadInfo(tmpGameInfo, gameInfo)) {
                position = i;
                break;
            }
        }

        if (position == -1) {
            return;
        }

        int hcount = listView.getHeaderViewsCount();
        int fcount = listView.getFooterViewsCount();
        int fvisiblePos = listView.getFirstVisiblePosition() - hcount;
        int lvisiblePos = listView.getLastVisiblePosition() - fcount;

        View view = null;
        if (position >= fvisiblePos && position <= lvisiblePos) {
            view = (listView.getChildAt(position - fvisiblePos));
        }

        if (view == null) {
            return;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            return;
        }
        updateInfo(gameInfo, holder);
    }

    @Override
    public void notifyDataSetChanged() {
        ApkStatusUtil.getStatuss(context, dataInfos);
        super.notifyDataSetChanged();
    }

    private void updateInfo(GameInfo gameInfo, ViewHolder holder) {
        int status = gameInfo.getStatus();
        ApkStatusUtil.setButtonStatus(context, holder.btnDownload, status);

        holder.rlItem.setTag(gameInfo);
        holder.btnDownload.setTag(gameInfo);
        holder.tvType.setText(gameInfo.getCateName());
        holder.tvTitle.setText(gameInfo.getName());

        if (gameInfo.getHasGift() > 0) {
            holder.tvGift.setVisibility(View.VISIBLE);
        } else {
            holder.tvGift.setVisibility(View.GONE);
        }

        DownloadInfo downloadInfo = DownloadManagerService.getDownloadInfo(gameInfo);
        if (downloadInfo != null && (downloadInfo.status != ApkStatus.DOWNLOADED && downloadInfo.status != ApkStatus
                .INSTALLED && downloadInfo.status != ApkStatus.UNDOWNLOAD)
                ) {
            holder.tvSize.setVisibility(View.GONE);
            holder.tvDesc.setVisibility(View.GONE);
            holder.rl_process_wraper.setVisibility(View.VISIBLE);
            holder.rl_size.setVisibility(View.VISIBLE);

            holder.tvSize2.setText(CheckUtil.checkStr(downloadInfo.size, "0M/0M"));
            holder.tvSpeed.setText(CheckUtil.checkStr(downloadInfo.speed, "等待中"));
            if (downloadInfo.precent == null) {
                downloadInfo.precent = 0.0f;
            }
            setProcess(holder.processView, downloadInfo.precent);

        } else {
            holder.tvSize.setVisibility(View.VISIBLE);
            holder.tvDesc.setVisibility(View.VISIBLE);
            holder.rl_process_wraper.setVisibility(View.GONE);
            holder.rl_size.setVisibility(View.GONE);
            holder.tvSize.setText(gameInfo.getDownloadTimes() + "次下载  " + gameInfo.getSize_text());
            holder.tvDesc.setText(CheckUtil.checkDesc(gameInfo.getDesc()));
        }
        Picasso.with(context).load(gameInfo.getIconUrl()).placeholder(R.mipmap.icon_default).into(holder.ivIcon);
    }

    private void setProcess(View processView, float precent) {
        try {
            FrameLayout.LayoutParams l = (FrameLayout.LayoutParams) processView.getLayoutParams();
            l.width = (int) (width * precent);
            processView.setLayoutParams(l);
        } catch (Exception e) {
        }
    }

    class HeaderViewHolder {
        @BindView(R.id.icon)
        ImageView ivIcon;

        public HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
            StateUtil.setDrawable(context, ivIcon, 5f, ContextCompat.getColor(context, R.color.gray));
        }

        @OnClick({R.id.icon})
        public void onClick(View view) {
            onItemClickListener.onImage(view);
        }
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

        @BindView(R.id.type)
        TextView tvType;

        @BindView(R.id.gift)
        TextView tvGift;

        @BindView(R.id.desc)
        TextView tvDesc;

        @BindView(R.id.download)
        TextView btnDownload;

        @BindView(R.id.rl_process_wraper)
        RelativeLayout rl_process_wraper;

        @BindView(R.id.rl_size)
        RelativeLayout rl_size;

        @BindView(R.id.process_wraper)
        FrameLayout process_wraper;

        @BindView(R.id.speed)
        TextView tvSpeed;

        @BindView(R.id.size2)
        TextView tvSize2;

        @BindView(R.id.process)
        View processView;


        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            StateUtil.setDrawable(context, tvGift, 1.5f, Color.parseColor("#ffc000"));
            StateUtil.setDrawable(context, tvType, 1.5f, Color.parseColor("#ff5555"));
            StateUtil.setDrawable(context, processView, 4);
            StateUtil.setRipple(rlItem);
        }

        @OnClick({R.id.download, R.id.item})
        public void onClick(View view) {
            if (view instanceof RelativeLayout) {
                onItemClickListener.onDetail(view);
                return;
            }
            if (view instanceof TextView) {
                onItemClickListener.onDownload((TextView) view);
                return;
            }
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onDetail(View view);

        void onDownload(TextView view);

        void onImage(View view);
    }
}
