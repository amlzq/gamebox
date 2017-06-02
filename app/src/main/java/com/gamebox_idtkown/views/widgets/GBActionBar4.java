package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.utils.StateUtil;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/10/24.
 */
public class GBActionBar4 extends GBBaseActionBar {
    @BindView(R.id.search_et)
    EditText searchEt;

    @BindView(R.id.search_tv)
    TextView searchBtn;

    public GBActionBar4(Context context, AttributeSet attrs) {
        super(context, attrs);

        StateUtil.setCursorDrawableColor(getContext(),searchEt, GoagalInfo.getInItInfo().androidColor);
    }

    public String getKeyWord() {
        return searchEt.getText().toString();
    }

    public void setKeyWord(String keyWord) {
        searchEt.setText(keyWord);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_actionbar4;
    }

    private OnSearchListener onSearchListener;

    public void setOnSearchListener(OnSearchListener _onSearchListener) {
        onSearchListener = _onSearchListener;
        searchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchListener.onSearch(v);
            }
        });
    }

    public interface OnSearchListener {
        void onSearch(View view);
    }

    public void setClickable(boolean flag){
        searchBtn.setClickable(flag);
    }

}
