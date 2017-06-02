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
import com.gamebox_idtkown.utils.ScreenUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangkai on 16/10/8.
 */
public class GameDetailPPTAdapter extends RecyclerView.Adapter<GameDetailPPTAdapter.ViewHolder> {

    public Context context;
    protected LayoutInflater inflater;
    public List<GameImage> dataInfos;

    public int imgW = 0;
    public int imgH = 0;
    public int leftMargin = 0;
    public int rightMargin = 0;

    public GameDetailPPTAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        imgW = ScreenUtil.dip2px(context, 122.5f);
        imgH = ScreenUtil.dip2px(context, 220);
        leftMargin = ScreenUtil.dip2px(context, 16);
        rightMargin = ScreenUtil.dip2px(context, 5);

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_game_detail_ppt,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final GameImage gameImage = dataInfos.get(position);
        holder.image.setTag(position);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.image.getLayoutParams();
        if (position == 0) {
            layoutParams.setMargins(leftMargin, 0, 0, 0);
        } else if (position == dataInfos.size() - 1) {
            layoutParams.setMargins(rightMargin, 0, leftMargin, 0);
        } else {
            layoutParams.setMargins(rightMargin, 0, 0, 0);
        }
        holder.image.setLayoutParams(layoutParams);
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
