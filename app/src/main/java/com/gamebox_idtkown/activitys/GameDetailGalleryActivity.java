package com.gamebox_idtkown.activitys;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.gamebox_idtkown.R;
import com.gamebox_idtkown.views.adpaters.GBGameDetailGalleryAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by zhangkai on 16/10/9.
 */
public class GameDetailGalleryActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.indicator_unselected_background)
    CircleIndicator indicator;

    public int position = 0;

    @Override
    public int getLayoutID() {
        return R.layout.activity_game_detail_gallery;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void initViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void initVars() {
        super.initVars();
        Intent intent = this.getIntent();
        if (intent != null) {
            position = intent.getIntExtra("position", 0);
        }
    }

    @Override
    public void loadData() {
        GBGameDetailGalleryAdapter adapter = new GBGameDetailGalleryAdapter(this);
        adapter.dataInfos = GameDetailActivity.gameImages;
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());
        viewPager.setCurrentItem(position);
        indicator.setViewPager(viewPager);
        adapter.setOnItemClickListener(new GBGameDetailGalleryAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }
}
