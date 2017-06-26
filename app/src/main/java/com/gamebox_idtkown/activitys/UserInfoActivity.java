package com.gamebox_idtkown.activitys;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;

import com.gamebox_idtkown.GBApplication;
import com.gamebox_idtkown.R;
import com.gamebox_idtkown.constans.DescConstans;
import com.gamebox_idtkown.constans.EventBusMessage;
import com.gamebox_idtkown.core.db.greendao.UserInfo;
import com.gamebox_idtkown.core.listeners.Callback;
import com.gamebox_idtkown.domain.ResultInfo;
import com.gamebox_idtkown.engin.UpdateAvatarEngin;
import com.gamebox_idtkown.net.entry.Response;
import com.gamebox_idtkown.utils.CircleTransform;
import com.gamebox_idtkown.utils.LoadingUtil;
import com.gamebox_idtkown.utils.ToastUtil;
import com.gamebox_idtkown.views.widgets.GBActionBar;
import com.gamebox_idtkown.views.widgets.GBUserInfoItem;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;

/**
 * Created by zhangkai on 16/10/25.
 */
public class UserInfoActivity extends BaseActionBarActivity<GBActionBar> {
    @BindView(R.id.avatar)
    GBUserInfoItem avatarItem;

    @BindView(R.id.nickname)
    GBUserInfoItem nicknameItem;

    @BindView(R.id.sex)
    GBUserInfoItem sexItem;

    @BindView(R.id.email)
    GBUserInfoItem emailItem;

    @BindView(R.id.qq)
    GBUserInfoItem qqItem;

    @BindView(R.id.phone)
    GBUserInfoItem phoneItem;

    @BindView(R.id.password)
    GBUserInfoItem passwordItem;

    @Override
    public int getLayoutID() {
        return R.layout.activity_user_info;
    }

    @Override
    public boolean isNeedLogin() {
        return true;
    }

    @Override
    public void initViews() {
        super.initViews();

        setBackListener();
        actionBar.setTitle("个人信息");
        actionBar.hideMenuItem();

        avatarItem.showIcon();
        setInfo();

        EventBus.getDefault().register(this);
    }

    public void setInfo() {
        if (!GBApplication.isLogin()) {
            return;
        }
        UserInfo userInfo = GBApplication.userInfo;
        if (GBApplication.userInfo.getAvatar() != null && !GBApplication.userInfo.getAvatar().isEmpty()) {
            Picasso.with(this)
                    .load(GBApplication.userInfo.getAvatar()).placeholder(R.mipmap.avatar_default).transform(new
                    CircleTransform())
                    .into(avatarItem.ivIcon);
        }
        nicknameItem.setDesc(getMessage(userInfo.getNick_name(), DescConstans.NICKNAME));
        String sexStr = "未知";
        try {
            int sex = Integer.parseInt(userInfo.getSex());
            if (sex == 1) {
                sexStr = "男";
            } else if (sex == 2) {
                sexStr = "女";
            }
        } catch (Exception e) {

        }

        sexItem.setDesc(sexStr);
        emailItem.setDesc(getMessage(userInfo.getEmail(), "还没有填写邮箱"));
        qqItem.setDesc(getMessage(userInfo.getQq(), "还没有填写QQ"));
        if (userInfo.getIs_vali_mobile()) {
            String phone = userInfo.getMobile();
            if (phone.length() >= 11) {
                phone = phone.replace(phone.substring(3, 7), "****");
            }
            phoneItem.setTitle("更换手机号");
            phoneItem.setDesc(phone);
        } else {
            phoneItem.setDesc("还没有绑定手机号");
        }

        avatarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAvatar();
            }
        });

        nicknameItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, MUserInfoActivity.class);
                intent.putExtra("title", "昵称");
                intent.putExtra("data", getMessage(GBApplication.userInfo.getNick_name(), ""));
                intent.putExtra("type", "0");
                startActivity(intent);
            }
        });

        emailItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, MUserInfoActivity.class);
                intent.putExtra("title", "邮箱");
                intent.putExtra("data", getMessage(GBApplication.userInfo.getEmail(), ""));
                intent.putExtra("type", "1");
                startActivity(intent);
            }
        });

        qqItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, MUserInfoActivity.class);
                intent.putExtra("title", "QQ");
                intent.putExtra("data", getMessage(GBApplication.userInfo.getQq(), ""));
                intent.putExtra("type", "2");
                startActivity(intent);
            }
        });

        passwordItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, MPasswordActivity.class);
                startActivity(intent);
            }
        });

        phoneItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GBApplication.userInfo.getIs_vali_mobile()) {
                    Intent intent = new Intent(UserInfoActivity.this, BindPhoneActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(UserInfoActivity.this, BindPhone2Activity.class);
                    startActivity(intent);
                }
            }
        });

        sexItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, MSexActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && null != data) {
            try {
                Bundle extras = data.getExtras();
                Bitmap photo = null;
                if (extras != null) {
                    photo = extras.getParcelable("data");
                }

                if (photo == null) {
                    Uri selectedImage = data.getData();

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    String picturePath = "";
                    if(cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        picturePath = cursor.getString(columnIndex);
                        cursor.close();
                    } else {
                        picturePath = selectedImage.getPath();
                    }
                    if(requestCode == 1) {
                        Intent intent = new Intent("com.android.camera.action.CROP");
                        intent.setDataAndType(selectedImage, "image/*");
                        intent.putExtra("crop", "true");
                        intent.putExtra("aspectX", 1);
                        intent.putExtra("aspectY", 1);
                        intent.putExtra("outputX", 160);
                        intent.putExtra("outputY", 160);
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, 2);
                        return;
                    }
                    photo = BitmapFactory.decodeFile(picturePath);
                }

                if(photo == null){
                    ToastUtil.toast2(getBaseContext(), "获取图片失败");
                    return;
                }

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0 - 100)压缩文件
                byte[] byteArray = stream.toByteArray();
                String streamStr = Base64.encodeToString(byteArray, Base64.DEFAULT);
                String image = "data:image/png;base64," + streamStr;
                LoadingUtil.show(this, "正在上传图像...");
                UpdateAvatarEngin.getImpl(this).updateAvatar(image, new Callback<String>() {
                    @Override
                    public void onSuccess(final ResultInfo<String> resultInfo) {
                        bindView(new Runnable() {
                            @Override
                            public void run() {
                                LoadingUtil.dismiss();
                                if (resultInfo == null) {
                                    ToastUtil.toast2(getBaseContext(), DescConstans.SERVICE_ERROR);
                                    return;
                                }
                                if (resultInfo.code == 1) {
                                    GBApplication.userInfo.setAvatar(resultInfo.data);
                                    GBApplication.getUserBitmap(getBaseContext());
                                    GBApplication.updateUserInfo(getBaseContext());
                                    ToastUtil.toast2(getBaseContext(), getMessage(resultInfo.message, "修改成功"));
                                } else {
                                    ToastUtil.toast2(getBaseContext(), getMessage(resultInfo.message, "修改失败"));
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Response response) {
                        error();
                    }
                });

            }catch (Exception e){
                ToastUtil.toast2(getBaseContext(), "修改失败" + e);
            }
        }
    }

    public void updateAvatar() {
        Intent intent = new Intent(Intent.ACTION_PICK); // 打开相册
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        this.startActivityForResult(intent, 1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserInfo userInfo) {
        setInfo();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventUpdateUserInfo(Integer type) {
        if (type == EventBusMessage.UPDATE_USER_INFO)
            GBApplication.login(getBaseContext());
    }
}
