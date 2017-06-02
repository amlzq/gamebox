package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.AbsListView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.views.adpaters.GBBaseAdapter;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/9/26.
 */
public abstract class GBGameListView<D, T extends GBBaseTitleView,G extends AbsListView> extends BaseView {
    @BindView(R.id.titleView)
    T titleView;

    @BindView(R.id.gamelist)
    public G gamelist;

    public void setAdapter(GBBaseAdapter<D> adapter){
        gamelist.setAdapter(adapter);
    }

    public GBGameListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.tab_item);
        CharSequence title = a.getText(R.styleable.tab_item_text);
        if(title != null){
            titleView.setTitle(title.toString());
        }
        Drawable iconSrc = a.getDrawable(R.styleable.tab_item_src);
        if (iconSrc != null) {
            titleView.setIcon(iconSrc);
        }
    }

    public void setTitle(String title){
        titleView.setTitle(title);
    }

}
