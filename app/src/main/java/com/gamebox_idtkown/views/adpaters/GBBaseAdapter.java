package com.gamebox_idtkown.views.adpaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.gamebox_idtkown.utils.ScreenUtil;

import java.util.List;

/**
 * Created by zhangkai on 16/9/22.
 */
public abstract class GBBaseAdapter<T> extends BaseAdapter {
    public Context context;
    protected LayoutInflater inflater;
    public int rouned = 0;

    public List<T> dataInfos;


    public GBBaseAdapter(Context context){
        this.context = context;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rouned = ScreenUtil.dip2px(context, 8);
    }

    @Override
    public int getCount() {
        return dataInfos != null ? dataInfos.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return dataInfos != null ? dataInfos.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
