package com.gamebox_idtkown.views.adpaters;

import android.content.Context;
import android.graphics.Color;
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
import com.gamebox_idtkown.core.db.greendao.GameInfo;
import com.gamebox_idtkown.domain.GameOpenServiceInfo;
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
 * Created by zhangkai on 2017/3/28.
 */

public class GBGameOpenServiceAdapter extends GBBaseAdapter<GameOpenServiceInfo> {
    private ListView listView;

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public GBGameOpenServiceAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_game_open_service, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GameOpenServiceInfo gameInfo = dataInfos.get(position);
        updateInfo(gameInfo, holder);
        return convertView;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private void updateInfo(GameOpenServiceInfo gameOpenServiceInfo, ViewHolder holder) {

        holder.rlItem.setTag(gameOpenServiceInfo);
        holder.btnDownload.setTag(gameOpenServiceInfo);
        holder.tvTitle.setText(gameOpenServiceInfo.getGame_name());
        holder.tvTitle2.setText(gameOpenServiceInfo.getServer());
        holder.tvTime.setText(gameOpenServiceInfo.getOpen_time());

        Picasso.with(context).load(gameOpenServiceInfo.getIco()).transform(new RoundedTransformation(rouned, 0)).placeholder(R.mipmap
                .icon_default).into(holder.ivIcon);
    }

    class ViewHolder {
        @BindView(R.id.item)
        RelativeLayout rlItem;

        @BindView(R.id.icon)
        ImageView ivIcon;

        @BindView(R.id.title)
        TextView tvTitle;

        @BindView(R.id.title2)
        TextView tvTitle2;

        @BindView(R.id.time)
        TextView tvTime;


        @BindView(R.id.download)
        TextView btnDownload;


        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
            StateUtil.setRipple(rlItem);
        }

        @OnClick({R.id.download, R.id.item, R.id.time})
        public void onClick(View view) {
            if (view instanceof RelativeLayout) {
                onItemClickListener.onSearch(view);
                return;
            }

            if (view.getId() == btnDownload.getId()) {
                onItemClickListener.onDownload((TextView) view);
                return;
            }

            if (view.getId() == tvTime.getId()) {
                onItemClickListener.OnClikeTime((TextView) view);
                return;
            }
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onSearch(View view);

        void onDownload(TextView view);

        void OnClikeTime(TextView view);
    }
}
