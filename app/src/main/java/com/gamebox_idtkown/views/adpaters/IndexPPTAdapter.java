package com.gamebox_idtkown.views.adpaters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.core.db.greendao.GameImage;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.ScreenUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangkai on 16/12/27.
 *
 */

public class IndexPPTAdapter extends RecyclerView.Adapter<IndexPPTAdapter.ViewHolder> {
    public Context context;
    protected LayoutInflater inflater;
    public List<GameImage> dataInfos;

    public int imgW = 0;
    public int imgH = 0;
    public IndexPPTAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        imgW = ScreenUtil.getWidth(context);
        imgH = ScreenUtil.dip2px(context, 210);
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_index_ppt,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GameImage gameImage = dataInfos.get(position);
        holder.image.setTag(position);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.image.getLayoutParams();

        holder.image.setLayoutParams(layoutParams);
        LogUtil.msg(gameImage.getImgUrl());
        Picasso.with(context).load(gameImage.getImgUrl()).resize(imgW, imgH).centerCrop().into(holder.image);
    }


    @Override
    public int getItemCount() {
        return dataInfos != null ? dataInfos.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.image)
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
