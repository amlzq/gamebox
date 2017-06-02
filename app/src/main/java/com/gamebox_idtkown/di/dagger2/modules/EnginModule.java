package com.gamebox_idtkown.di.dagger2.modules;

import com.gamebox_idtkown.engin.EarnPointTaskEngin;
import com.gamebox_idtkown.engin.GameInfoEngin;
import com.gamebox_idtkown.engin.GiftDetailEngin;
import com.gamebox_idtkown.engin.GoodChangeEngin;
import com.gamebox_idtkown.engin.GoodConvertEngin;
import com.gamebox_idtkown.engin.GoodDetailEngin;
import com.gamebox_idtkown.engin.GoodListEngin;
import com.gamebox_idtkown.engin.GoodTypeEngin;
import com.gamebox_idtkown.engin.PayRecordEngin;
import com.gamebox_idtkown.engin.SlideEngin;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhangkai on 16/11/11.
 */

@Module
public class EnginModule {
    @Singleton
    @Provides
    EarnPointTaskEngin provideEarnPointTask(){
        return new EarnPointTaskEngin();
    }

    @Singleton
    @Provides
    GoodTypeEngin provideGoodType(){
        return new GoodTypeEngin();
    }

    @Singleton
    @Provides
    GoodListEngin provideGoodList(){
        return new GoodListEngin();
    }

    @Singleton
    @Provides
    GoodChangeEngin provideGoodChange(){
        return new GoodChangeEngin();
    }

    @Singleton
    @Provides
    GoodConvertEngin provideGoodConvert(){
        return new GoodConvertEngin();
    }

    @Singleton
    @Provides
    GameInfoEngin provideGameInfo(){
        return new GameInfoEngin();
    }

    @Singleton
    @Provides
    GiftDetailEngin provideGiftDetail(){
        return new GiftDetailEngin();
    }

    @Singleton
    @Provides
    SlideEngin provideSlideEngin(){
        return new SlideEngin();
    }

    @Provides
    PayRecordEngin providePayRecordEngin(){
        return new PayRecordEngin();
    }

    @Provides
    GoodDetailEngin provideGoodDetailEngin(){
        return new GoodDetailEngin();
    }
}
