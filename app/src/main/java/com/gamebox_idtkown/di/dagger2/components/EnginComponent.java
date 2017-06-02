package com.gamebox_idtkown.di.dagger2.components;


import android.support.v4.app.Fragment;

import com.gamebox_idtkown.activitys.BaseActivity;
import com.gamebox_idtkown.activitys.EarnPointAcitivty;
import com.gamebox_idtkown.activitys.GameDetailActivity;
import com.gamebox_idtkown.activitys.GiftDetailActivity;
import com.gamebox_idtkown.activitys.GoodChangeActivity;
import com.gamebox_idtkown.activitys.GoodListActivity;
import com.gamebox_idtkown.activitys.GoodTypeActivity;
import com.gamebox_idtkown.activitys.PayRecordActivity;
import com.gamebox_idtkown.di.dagger2.Scopes.PerActivity;
import com.gamebox_idtkown.di.dagger2.modules.EnginModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by zhangkai on 16/11/11.
 */
@PerActivity
@Singleton
@Component(modules = EnginModule.class)
public interface EnginComponent {
    void injectEarnPointTask(EarnPointAcitivty activity);
    void injectGoodType(GoodTypeActivity activity);
    void injectGoodList(GoodListActivity activity);
    void injectGoodChange(GoodChangeActivity activity);
    void injectGameInfo(GameDetailActivity activity);
    void injectGiftDetail(GiftDetailActivity activity);
    void injectPayRecord(PayRecordActivity activity);

    void injectSlide(Fragment activity);
}
