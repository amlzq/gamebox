package com.gamebox_idtkown.views.widgets;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.activitys.UserInfoActivity;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.ApkStatus;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.fragment.BaseFragment;
import com.gamebox_idtkown.services.DownloadManagerService;
import com.gamebox_idtkown.utils.ScreenUtil;
import com.gamebox_idtkown.utils.TaskUtil;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/9/22.
 */
public class GBActionBar extends GBBaseActionBar {
    @BindView(R.id.search)
    GBImageButton btnSearch;

    @BindView(R.id.download)
    GBImageButton btnDownload;

    @BindView(R.id.share)
    GBImageButton btnShare;

    @BindView(R.id.title)
    TextView tvTitle;

    @BindView(R.id.badge)
    TextView tvBadge;

    public static int mBadge = 0;

    public GBActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        btnSearch.setTag(1);
        btnDownload.setTag(2);
        btnSearch.setIcon(getDrawable(R.mipmap.actionbar_search));
        btnDownload.setIcon(getDrawable(R.mipmap.actionbar_download));
        try {
            setBadge();
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventBus.getDefault().register(this);

        btnShare.setIcon(getDrawable(R.mipmap.share));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final Integer type) {
        if (EventBusMessage.BADGE == type) {
            setBadge();
        }
    }

    public void setBadge() {
        mBadge = DownloadManagerService.downloadingInfoList.size();
        for (int i = 0; i < DownloadManagerService.downloadedInfoList.size(); i++) {
            DownloadInfo downloadInfo = DownloadManagerService.downloadedInfoList.get(i);
            if (downloadInfo.status == ApkStatus.DOWNLOADED) {
                mBadge++;
            }
        }
        setBadge(mBadge + "");
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_actionbar;
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setLogoWH(final BaseFragment activity) {
        if (GBApplication.isLogin()) {
            if (GBApplication.userInfo.avatarBitmp == null) {
                if (GBApplication.avatarLoaded) {
                    GBApplication.getUserBitmap(activity.getContext());
                }
            } else {
                setAvatar(GBApplication.userInfo.avatarBitmp);
            }
        } else {
            setAvatar(null);
        }
        setAvatarOnClickListner(new Runnable() {
            @Override
            public void run() {
                if (activity.startLoginActivity()) {
                    Intent intent = new Intent(getContext(), UserInfoActivity.class);
                    activity.startActivity(intent);
                }
            }
        });

    }

    public void setBadge(String badge) {
        if (mBadge <= 0) {
            tvBadge.setVisibility(View.GONE);
        } else {
            if (btnDownload.getVisibility() == View.VISIBLE) {
                tvBadge.setVisibility(View.VISIBLE);
                tvBadge.setText(badge);
            }
        }
    }

    public void hideMenuItem() {
        btnSearch.setVisibility(View.GONE);
        btnDownload.setVisibility(View.GONE);
        tvBadge.setVisibility(View.GONE);
    }

    public void showShare() {
        hideMenuItem();
        btnShare.setVisibility(View.VISIBLE);
        btnShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onShareClickLister.onClick(v);
            }
        });

    }

    public void setOnShareClickLister(OnShareClickLister onShareClickLister) {
        this.onShareClickLister = onShareClickLister;
    }

    private OnShareClickLister onShareClickLister;

    public interface OnShareClickLister {
        void onClick(View v);
    }

    public void setOnActionBarItemClickListener(OnActionBarItemClickListener _onActionBarItemClickListener) {
        this.onActionBarItemClickListener = _onActionBarItemClickListener;

        btnSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onActionBarItemClickListener.onSearchClick(v);
            }
        });
        btnDownload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onActionBarItemClickListener.onDownloadClick(v);
            }
        });
    }


}
