package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.gamebox_idtkown.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangkai on 16/9/22.
 */
public class GBTabBar extends BaseView {

    @BindView(R.id.item_index)
    GBTabBarItem indexItem;

    @BindView(R.id.item_chosen)
    GBTabBarItem chosenItem;

    @BindView(R.id.item_gifts)
    GBTabBarItem giftsItem;

    @BindView(R.id.item_my)
    GBTabBarItem myItem;

    public GBTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        indexItem.setTag(0);
        chosenItem.setTag(1);
        giftsItem.setTag(2);
        myItem.setTag(3);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_tabbar;
    }

    @OnClick({R.id.item_index, R.id.item_chosen, R.id.item_gifts, R.id.item_my})
    public void OnClick(GBTabBarItem item){
       if(onTabSelectedListener == null) throw  new NullPointerException("listener == null");

       int index = (int)item.getTag();
       clearSelectedItem();
       item.selected(getSelectedIconDrawable(index));
       onTabSelectedListener.onSelected(index);
    }

    public void tab(int idx){
        clearSelectedItem();
        getTabItem(idx).selected(getSelectedIconDrawable(idx));
        onTabSelectedListener.onSelected(idx);
    }

    private GBTabBarItem getTabItem(int idx){
        switch (idx){
            case 0:
                return indexItem;
            case 1:
                return chosenItem;
            case 2:
                return giftsItem;
            case 3:
                return myItem;
        }
        return indexItem;
    }

    private void clearSelectedItem(){
        indexItem.normal(getIconDrawable(0));
        chosenItem.normal(getIconDrawable(1));
        giftsItem.normal(getIconDrawable(2));
        myItem.normal(getIconDrawable(3));
    }

    private Drawable getIconDrawable(int idx){
        switch (idx){
            case 0:
                return ContextCompat.getDrawable(getContext(),R.mipmap.tab_index);
            case 1:
                return ContextCompat.getDrawable(getContext(),R.mipmap.tab_chosen);
            case 2:
                return ContextCompat.getDrawable(getContext(),R.mipmap.tab_gifts);
            case 3:
                return ContextCompat.getDrawable(getContext(), R.mipmap.tab_my);
        }
        return ContextCompat.getDrawable(getContext(), R.mipmap.tab_index);
    }

    private Drawable getSelectedIconDrawable(int idx){
        switch (idx){
            case 0:
                return ContextCompat.getDrawable(getContext(),R.mipmap.tab_index_selected);
            case 1:
                return ContextCompat.getDrawable(getContext(),R.mipmap.tab_chosen_selected);
            case 2:
                return ContextCompat.getDrawable(getContext(),R.mipmap.tab_gifts_selected);
            case 3:
                return ContextCompat.getDrawable(getContext(),R.mipmap.tab_my_selected);
        }
        return ContextCompat.getDrawable(getContext(), R.mipmap.tab_index_selected);
    }

    private OnTabSelectedListener onTabSelectedListener;

    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener){
        this.onTabSelectedListener = onTabSelectedListener;
    }

    public interface OnTabSelectedListener {
        void onSelected(int idx);
    }


}
