package com.gamebox_idtkown.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;


import com.gamebox_idtkown.R;
import com.gamebox_idtkown.domain.AppModel;
import com.gamebox_idtkown.utils.VUiKit;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.proto.AppSetting;
import com.squareup.picasso.Picasso;

import org.jdeferred.DoneCallback;

/**
 * @author Lody
 */

public class LoadingActivity extends AppCompatActivity {

	public static final String MODEL_ARGUMENT = "MODEL_ARGUMENT";
	public static final String KEY_INTENT = "KEY_INTENT";
	public static final String KEY_USER = "KEY_USER";
	public static final String APP_ICON_PATH = "APP_ICON_PATH";
	public AppModel appModel;

	public static void launch(Context context, AppModel model, int userId) {
		Intent intent = VirtualCore.get().getLaunchIntent(model.packageName, userId);
		if (intent != null) {
			Intent loadingPageIntent = new Intent(context, LoadingActivity.class);
			loadingPageIntent.putExtra(MODEL_ARGUMENT, model);
			loadingPageIntent.putExtra(APP_ICON_PATH, model.iconPath);
			loadingPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			loadingPageIntent.putExtra(KEY_INTENT, intent);
			loadingPageIntent.putExtra(KEY_USER, userId);
			context.startActivity(loadingPageIntent);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);

		appModel = getIntent().getParcelableExtra(MODEL_ARGUMENT);
		final String iconPath = getIntent().getStringExtra(APP_ICON_PATH);
		final int userId = getIntent().getIntExtra(KEY_USER, -1);

		VUiKit.defer().when(new Runnable() {
			@Override
			public void run() {
				AppSetting appSetting = VirtualCore.get().findApp(appModel.packageName);
				if (appSetting != null) {
					appModel = new AppModel(LoadingActivity.this, appSetting);
				}
			}
		}).done(new DoneCallback<Void>() {
			@Override
			public void onDone(Void result) {
				ImageView iconView = (ImageView) findViewById(R.id.app_icon);
				if (iconView != null) {
					Picasso.with(LoadingActivity.this).load(iconPath).placeholder(R.mipmap.icon_default).into
							(iconView);
				}
			}
		});

		TextView nameView = (TextView) findViewById(R.id.app_name);
		if (nameView != null) {
			nameView.setText(appModel.name);
		}

		final Intent intent = getIntent().getParcelableExtra(KEY_INTENT);
		VirtualCore.get().setLoadingPage(intent, this);
		if (intent != null) {
			VUiKit.defer().when(new Runnable() {
				@Override
				public void run() {
					long startTime = System.currentTimeMillis();
					if (!appModel.fastOpen) {
						try {
							VirtualCore.get().preOpt(appModel.packageName);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					long spend = System.currentTimeMillis() - startTime;
					if (spend < 500) {
						try {
							Thread.sleep(500 - spend);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).done(new DoneCallback<Void>() {
				@Override
				public void onDone(Void result) {
					VActivityManager.get().startActivity(intent, userId);
				}
			});
		}
	}

}
