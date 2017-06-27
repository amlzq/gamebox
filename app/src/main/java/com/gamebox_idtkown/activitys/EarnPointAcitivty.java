package com.gamebox_idtkown.activitys;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.cache.EarnPointTaskCache;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.core.db.greendao.DownloadInfo;
import com.gamebox_idtkown.core.db.greendao.EarnPointTaskInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.di.dagger2.components.DaggerEnginComponent;
import com.gamebox_idtkown.domain.GoagalInfo;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.EarnPointTaskEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.DialogShareUtil;
import com.gamebox_idtkown.utils.LogUtil;
import com.gamebox_idtkown.utils.ShareUtil;
import com.gamebox_idtkown.utils.TaskUtil;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.views.adpaters.GBEarnPointAdapter;
import com.gamebox_idtkown.views.widgets.GBActionBar5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by zhangkai on 16/9/26.
 */
public class EarnPointAcitivty extends BaseGameListActivity<EarnPointTaskInfo, GBActionBar5> {

    @Inject
    EarnPointTaskEngin earnPointTaskEngin;

    @BindView(R.id.earn_point_list)
    StickyListHeadersListView listView;

    GBEarnPointAdapter adapter;

    @Override
    public int getLayoutID() {
        return R.layout.activity_earn_point;
    }

    @Override
    public void initVars() {
        super.initVars();
        DaggerEnginComponent.create().injectEarnPointTask(this);
    }

    @Override
    public void initViews() {
        super.initViews();
        actionBar.setTitle("赚积分");
        actionBar.showMenuItem("积分明细");
        setBackListener();
        actionBar.setOnItemClickListener(new GBActionBar5.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                Intent intent = new Intent(EarnPointAcitivty.this, IntegralDetailsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onClose(View view) {

            }
        });

        adapter = new GBEarnPointAdapter(this);
        listView.setAdapter(adapter);

        adapter.setOnItemClickListener(new GBEarnPointAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view) {
                final EarnPointTaskInfo taskInfo = (EarnPointTaskInfo) view.getTag();
                LogUtil.msg(taskInfo.getShare_content() + "");

                try {
                    if (startLoginActivity()) {
                        if (taskInfo.getShare_content() != null && !taskInfo.getShare_content().isEmpty()) {
                            DialogShareUtil.show(EarnPointAcitivty.this, 1);
                            DialogShareUtil.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DialogShareUtil.dismiss();
                                    int type = Integer.parseInt(v.getTag() + "");
                                    if (type == 2) {
                                        ClipboardManager clipboard = (ClipboardManager) getBaseContext().getSystemService(Context
                                                .CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText("下载链接", taskInfo.getShare_content
                                                ());
                                        clipboard.setPrimaryClip(clip);
                                        ToastUtil.toast2(EarnPointAcitivty.this, "复制成功");
                                        return;
                                    } else if (type == 1) {
                                        ShareUtil.openWXShareWithImage(EarnPointAcitivty.this, taskInfo.getShare_content
                                                (), GoagalInfo.getInItInfo().launch_img, type);
                                    } else {
                                        ShareUtil.OpenWxShareText(EarnPointAcitivty.this, taskInfo.getShare_content
                                                ());
                                    }
                                }
                            });
                            return;
                        }
                        if (taskInfo.getLog_point() == null || taskInfo.getLog_point().isEmpty()) {
                            Class clazz = Class.forName("com.gamebox_idtkown.activitys." + taskInfo.getApp_active());
                            Intent intent = new Intent(getBaseContext(), clazz);
                            startActivity(intent);
                        } else {
                            ToastUtil.toast(getBaseContext(), "任务已完成");
                        }
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    @Override
    public void loadData() {
        super.loadData();

        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                bindCache(adapter, new Runnable() {
                    @Override
                    public void run() {
                        List<EarnPointTaskInfo> once = EarnPointTaskCache.getCache(getBaseContext(), "once");
                        List<EarnPointTaskInfo> repeat = EarnPointTaskCache.getCache(getBaseContext(), "repeat");
                        adapter.dataInfos = new ArrayList<>();
                        if (once != null && once.size() > 0) {
                            adapter.dataInfos.addAll(once);
                        }
                        if (repeat != null && repeat.size() > 0) {
                            adapter.dataInfos.addAll(repeat);
                        }
                        notifyDataSetChanged();
                    }
                });
                getTasks();
            }
        });
    }

    private void notifyDataSetChanged() {
        bindView(new Runnable() {
            @Override
            public void run() {
             removeNoView();
             adapter.notifyDataSetChanged();
            }
        });
    }

    private void getTasks() {
        earnPointTaskEngin.getTasks(GBApplication.userInfo.getUserId(), new Callback<HashMap<String, ArrayList<EarnPointTaskInfo>>>() {
            @Override
            public void onSuccess(final ResultInfo<HashMap<String, ArrayList<EarnPointTaskInfo>>> resultInfo) {
                stop();
                bindView(new Runnable() {
                    @Override
                    public void run() {
                        if (resultInfo != null && resultInfo.data != null) {
                            adapter.dataInfos = new ArrayList<>();
                            List once = resultInfo.data.get("once");
                            if (once != null && once.size() > 0) {
                                adapter.dataInfos.addAll(once);
                            }

                            List repeat = resultInfo.data.get("repeat");
                            if (repeat != null && repeat.size() > 0) {
                                adapter.dataInfos.addAll(repeat);
                            }
                            notifyDataSetChanged();

                            if (adapter.dataInfos.size() == 0) {
                                int i = listView.getCount();
                                if(i == 0) {
                                    showNoDataView();
                                }
                            }
                        }
                    }
                });

                if (resultInfo != null && resultInfo.data != null) {
                    if (!cache) {
                        boolean hasData = false;
                        List once = resultInfo.data.get("once");
                        if (once != null && once.size() > 0) {
                            EarnPointTaskCache.setCache(getBaseContext(), once, "once");
                            hasData = true;
                        }

                        List repeat = resultInfo.data.get("repeat");
                        if (repeat != null && repeat.size() > 0) {
                            EarnPointTaskCache.setCache(getBaseContext(), repeat, "repeat");
                            hasData = true;
                        }
                        if (hasData) {
                            cache = true;
                            LogUtil.msg(EarnPointAcitivty.this.getClass().getSimpleName() + "缓存方法setCache已运行");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Response response) {
                fail(adapter.dataInfos == null, getMessage(response.body, DescConstans.NET_ERROR));
            }
        });
    }

    @Override
    public void removeFooterView() {

    }

    @Override
    public void addFooterView() {

    }

    @Override
    public void update(DownloadInfo downloadInfo) {

    }
}
