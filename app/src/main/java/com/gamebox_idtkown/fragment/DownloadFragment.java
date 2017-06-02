package com.gamebox_idtkown.fragment;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.ApkStatus;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.services.DownloadManagerService;
import com.gamebox_idtkown.utils.ApkStatusUtil;
import com.gamebox_idtkown.views.adpaters.GBDownloadAdapater;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by zhangkai on 16/9/29.
 */
public class DownloadFragment extends BaseFragment {
    @BindView(R.id.download_list)
    StickyListHeadersListView downloadListView;

    private GBDownloadAdapater downloadedAdapter = null;

    @Override
    public void initViews() {
        super.initViews();
        setExpandMenuClose();
        downloadedAdapter = new GBDownloadAdapater(getContext());
        downloadedAdapter.setListView(downloadListView.getWrappedList());
        downloadListView.setAdapter(downloadedAdapter);
        downloadedAdapter.setOnItemClickListener(new GBDownloadAdapater.OnItemClickListener() {
            @Override
            public void onClick(View view) {
                DownloadInfo downloadInfo = (DownloadInfo) view.getTag();
                ApkStatusUtil.actionByStatus2(getContext(), downloadInfo, (TextView) view);
            }

            @Override
            public void onMenuExpand(View view) {
                DownloadInfo downloadInfo = (DownloadInfo) view.getTag();
                downloadInfo.menuExpand = !downloadInfo.menuExpand;
                setExpandMenuClose(downloadedAdapter.dataInfos, downloadInfo);
                downloadedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDel(final View view) {
                final DownloadInfo downloadInfo = (DownloadInfo) view.getTag();

                new MaterialDialog.Builder(getContext())
                        .title("删除")
                        .content("确认删除这条记录？")
                        .positiveColor(GoagalInfo.getInItInfo().androidColor)
                        .negativeColorRes(R.color.gray_light)
                        .positiveText("确定")
                        .negativeText("取消")
                        .onAny(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if (which == DialogAction.POSITIVE) {
                                    DownloadManagerService.deleteDownloadInfo(downloadInfo, dialog.isPromptCheckBoxChecked());
                                    downloadedAdapter.notifyDataSetChanged();
                                    loadData();
                                }
                            }
                        })
                        .checkBoxPrompt("同时删除源文件", true, null)
                        .show();
            }
        });

        EventBus.getDefault().register(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventInit(Integer type) {
        if (type == EventBusMessage.RE_INIT){
            downloadedAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadData() {

        if ((DownloadManagerService.downloadedInfoList.size() == 0) && (DownloadManagerService.downloadingInfoList
                .size() == 0)) {
            removeNoView();
            showNoDataView2();
        }
    }

    public void setExpandMenuClose() {
        for (int i = 0; i < DownloadManagerService.downloadedInfoList.size(); i++) {
            DownloadInfo downloadInfo = DownloadManagerService.downloadedInfoList.get(i);
            downloadInfo.menuExpand = false;
        }

        for (int i = 0; i < DownloadManagerService.downloadingInfoList.size(); i++) {
            DownloadInfo downloadInfo = DownloadManagerService.downloadingInfoList.get(i);
            downloadInfo.menuExpand = false;
        }
    }

    public void setExpandMenuClose(List<DownloadInfo> downloadInfos, DownloadInfo dInfo) {
        for (int i = 0; i < downloadInfos.size(); i++) {
            DownloadInfo downloadInfo = downloadInfos.get(i);
            if (dInfo != null && DownloadManagerService.isSameDownloadInfo(downloadInfo, dInfo)) {
                continue;
            }
            downloadInfo.menuExpand = false;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final Integer type) {
        if (type == EventBusMessage.DOWNLIST_STATUS_CHANGE || type == EventBusMessage.REFRESH_INFO) {
            downloadedAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DownloadInfo downloadInfo) {
        if (downloadInfo.status == ApkStatus.DOWNLOADED || downloadInfo.status == ApkStatus.INSTALLED) {
            downloadedAdapter.notifyDataSetChanged();

        } else {
            downloadedAdapter.updateView(downloadInfo);
        }
    }



    @Override
    public int getLayoutID() {
        return R.layout.fragment_download;
    }




}
