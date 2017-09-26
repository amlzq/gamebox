package com.gamebox_idtkown.views.adpaters;

import android.content.Context;
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
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.services.DownloadManagerService;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.utils.CheckUtil;
import com.gamebox_idtkown.utils.RoundedTransformation;
import com.gamebox_idtkown.utils.ScreenUtil;
import com.gamebox_idtkown.utils.StateUtil;
import com.gamebox_idtkown.views.widgets.GBTitleView3;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by zhangkai on 16/9/30.
 */
public class GBDownloadAdapater extends GBBaseAdapter<DownloadInfo> implements StickyListHeadersAdapter {
    public ListView listView;
    private int width;
    private int rouned = 0;

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public GBDownloadAdapater(Context context) {
        super(context);
        width = ScreenUtil.getWidth(context) - ScreenUtil.dip2px(context, 183);
        dataInfos = new ArrayList<>();
        setDataInfos();
    }

    @Override
    public void notifyDataSetChanged() {
        setDataInfos();
        super.notifyDataSetChanged();
    }

    public void setDataInfos() {
        if (dataInfos != null && dataInfos.size() > 0) {
            dataInfos.clear();
            dataInfos = new ArrayList<>();
        }
        dataInfos.addAll(DownloadManagerService.downloadingInfoList);
        dataInfos.addAll(DownloadManagerService.downloadedInfoList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_download, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DownloadInfo downloadInfo = dataInfos.get(position);
        updateInfo(downloadInfo, holder);
        return convertView;
    }

    private void updateInfo(DownloadInfo downloadInfo, ViewHolder holder) {
        if (downloadInfo.menuExpand) {
            holder.ivExpandIcon.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.expand_up));
            holder.menu.setVisibility(View.VISIBLE);
        } else {
            holder.ivExpandIcon.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.expand_down));
            holder.menu.setVisibility(View.GONE);
        }
        holder.btnDownload.setTag(downloadInfo);
        holder.ivExpand.setTag(downloadInfo);
        holder.menu.setTag(downloadInfo);

        holder.tvTitle.setText(downloadInfo.name);
        holder.tvSize.setText(CheckUtil.checkStr(downloadInfo.size, "0M/0M"));
        holder.tvSpeed.setText(CheckUtil.checkStr(downloadInfo.speed, "等待中"));

        if (downloadInfo.iconUrl != null && downloadInfo.iconUrl.equals("{self}")) {
            Picasso.with(context).load(R.mipmap.logo).into(holder.ivIcon);
        } else {
            Picasso.with(context).load(downloadInfo.iconUrl).transform(new RoundedTransformation(rouned, 0)).placeholder(R.mipmap.icon_default).into(holder.ivIcon);
        }

        ApkStatusUtil.setButtonStatus(context, holder.btnDownload, downloadInfo.status);

        if (downloadInfo.status == ApkStatus.DOWNLOADED || downloadInfo.status == ApkStatus.INSTALLED) {
            try {
                String[] size = downloadInfo.getSize().split("/");
                if (size.length > 1) {
                    holder.tvSpeed.setText(size[1]);
                } else {
                    holder.tvSpeed.setText(downloadInfo.getSize() + "");
                }
            } catch (Exception e) {
                holder.tvSpeed.setText(downloadInfo.getSize() + "");
            }
            holder.tvSize.setVisibility(View.GONE);
            holder.process_wraper.setVisibility(View.GONE);
        } else {
            holder.tvSize.setVisibility(View.VISIBLE);
            holder.process_wraper.setVisibility(View.VISIBLE);
            if (downloadInfo.precent == null) {
                downloadInfo.precent = 0.0f;
            }
            setProcess(holder.processView, downloadInfo.precent);
        }
    }


    public int getPosition(DownloadInfo downloadInfo) {
        int poistion = -1;
        for (int i = 0; i < dataInfos.size(); i++) {
            DownloadInfo dInfo = dataInfos.get(i);
            if (DownloadManagerService.isSameDownloadInfo(downloadInfo, dInfo)) {
                poistion = i;
                break;
            }
        }
        return poistion;
    }

    public void updateView(DownloadInfo downloadInfo) {
        setDataInfos();
        int position = getPosition(downloadInfo);
        if (position == -1) {
            return;
        }
        int visiblePos = listView.getFirstVisiblePosition();

        View view = null;
        if (position >= visiblePos && position <= listView.getLastVisiblePosition()) {
            view = (listView.getChildAt(position - visiblePos));
        }

        if (view == null) {
            return;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        updateInfo(downloadInfo, holder);
    }


    public void updateView(int position) {
        int visiblePos = listView.getFirstVisiblePosition();

        View view = null;
        if (position >= visiblePos && position <= listView.getLastVisiblePosition()) {
            view = (listView.getChildAt(position - visiblePos));
        }
        if (view == null) {
            return;
        }
        DownloadInfo downloadInfo = dataInfos.get(position);
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder(view);
        }
        updateInfo(downloadInfo, holder);
    }

    private void setProcess(View processView, float precent) {
        try {
            if (precent < 0.01f) {
                return;
            }
            FrameLayout.LayoutParams l = (FrameLayout.LayoutParams) processView.getLayoutParams();
            l.width = (int) (width * precent);
            processView.setLayoutParams(l);
        } catch (Exception e) {

        }
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
        DownloadInfo downloadInfo = dataInfos.get(position);
        if (downloadInfo.status == ApkStatus.INSTALLED || downloadInfo.status == ApkStatus.DOWNLOADED) {
            holder.tvTitle.setText("已完成(" + DownloadManagerService.downloadedInfoList.size() + ")");
        } else {
            holder.tvTitle.setText("待下载(" + DownloadManagerService.downloadingInfoList.size() + ")");
        }
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        DownloadInfo downloadInfo = dataInfos.get(position);
        if (downloadInfo.status == ApkStatus.DOWNLOADED || downloadInfo.status == ApkStatus.INSTALLED) {
            return 1;
        }
        return 0;
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

        @BindView(R.id.size)
        TextView tvSize;

        @BindView(R.id.download)
        TextView btnDownload;

        @BindView(R.id.speed)
        TextView tvSpeed;

        @BindView(R.id.item)
        RelativeLayout ivExpand;

        @BindView(R.id.expand)
        ImageView ivExpandIcon;

        @BindView(R.id.process)
        View processView;

        @BindView(R.id.del)
        GBTitleView3 btnDel;

        @BindView(R.id.expanded_menu)
        RelativeLayout menu;

        @BindView(R.id.process_wraper)
        FrameLayout process_wraper;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);

            btnDel.setColor("#E75053");
            StateUtil.setDrawable(context, processView, 4);

        }

        @OnClick({R.id.download, R.id.item})
        public void onClick(View view) {
            if (view instanceof TextView) {
                onItemClickListener.onClick(view);
                return;
            }

            if (view instanceof RelativeLayout) {
                onItemClickListener.onMenuExpand(view);
            }
        }

        @OnClick({R.id.expanded_menu})
        public void onDel(View view) {
            onItemClickListener.onDel(view);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(View view);

        void onMenuExpand(View view);

        void onDel(View view);
    }
}
