package com.gamebox_idtkown.views.adpaters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gamebox_idtkown.core.db.greendao.GameImage;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.gamebox_idtkown.R;

/**
 * Created by zhangkai on 16/10/9.
 */
public class GBGameDetailGalleryAdapter extends PagerAdapter {
    public Context context;
    protected LayoutInflater inflater;


    public List<GameImage> dataInfos;

    public GBGameDetailGalleryAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.item_game_detail_gallery, null);
        container.addView(view);
        ViewHolder holder = new ViewHolder(view);
        GameImage gameImage = dataInfos.get(position);
        Picasso.with(context).load(gameImage.getImgUrl()).into(holder.ivImage);
        return view;
    }

    @Override
    public int getCount() {
        return dataInfos != null ? dataInfos.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    class ViewHolder {
        @BindView(R.id.image)
        ImageView ivImage;

        public ViewHolder(View view) {
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