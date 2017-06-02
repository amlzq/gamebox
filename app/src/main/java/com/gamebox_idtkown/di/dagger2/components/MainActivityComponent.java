package com.gamebox_idtkown.di.dagger2.components;

import com.gamebox_idtkown.activitys.MainActivity;
import com.gamebox_idtkown.di.dagger2.modules.MainActivityModule;

import dagger.Component;

/**
 * Created by zhangkai on 16/9/22.
 */
@Component(modules = MainActivityModule.class)
public interface MainActivityComponent {
    void inject(MainActivity activity);
}
