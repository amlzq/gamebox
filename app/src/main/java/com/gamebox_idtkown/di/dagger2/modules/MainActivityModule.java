package com.gamebox_idtkown.di.dagger2.modules;

import android.support.v4.app.Fragment;

import com.gamebox_idtkown.fragment.ChosenFragment;
import com.gamebox_idtkown.fragment.GiftsFragment;
import com.gamebox_idtkown.fragment.IndexFragment;
import com.gamebox_idtkown.fragment.MyFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zhangkai on 16/9/22.
 */
@Module
public class MainActivityModule {
    @Singleton
    @Provides
    Fragment provideIndexFragment(){
        return new IndexFragment();
    }

    @Singleton
    @Provides
    Fragment provideChosenFragment(){
        return new ChosenFragment();
    }

    @Singleton
    @Provides
    Fragment provideGiftsFragment(){
        return new GiftsFragment();
    }

    @Singleton
    @Provides
    Fragment provideMyFragment(){
        return new MyFragment();
    }


}
