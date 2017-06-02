package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.utils.StateUtil;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/10/8.
 */
public class GBGameDetailDownView extends  BaseView {

    @BindView(R.id.download)
    public TextView btnDownload;

    public GBGameDetailDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
            StateUtil.setDrawable(context, btnDownload, 20);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setTitle(String title){
        btnDownload.setText(title);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_game_detail_down_bar;
    }
}
