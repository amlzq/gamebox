package com.gamebox_idtkown.rpc;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.utils.PathUtil;

/**
 * Created by zhangkai on 2017/4/13.
 */

public class SDKContentProvider extends ContentProvider {

    private static final UriMatcher URI_MATCHER = new UriMatcher(
            UriMatcher.NO_MATCH);

    private static final int LOGIN_STATE = 1;

    static {
        URI_MATCHER.addURI("com.sdk.rpc.provide",
                "gamesdk", LOGIN_STATE);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        Bundle bundle = null;
        if (method.equals("search_login")) {
            if (GBApplication.isLogin()) {
                bundle = new Bundle();
                bundle.putString("username", GBApplication.userInfo.getName());
                bundle.putString("password", GBApplication.userInfo.getPwd());
                bundle.putString("phone", GBApplication.userInfo.getMobile());
            }
        }
        else if (method.equals("search_channel")) {
            bundle = new Bundle();
            String from_id = "0";
            GoagalInfo.setGoagalInfo(getContext(), PathUtil.getGolgalDir());
            if(GoagalInfo.channelInfo != null){
                from_id = GoagalInfo.channelInfo.from_id;
            }
            bundle.putString("channel", from_id);
        }
        return bundle;
    }
}
