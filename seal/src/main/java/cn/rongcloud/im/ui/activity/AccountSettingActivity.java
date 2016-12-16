package cn.rongcloud.im.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.File;

import cn.rongcloud.im.R;
import cn.rongcloud.im.SealConst;
import cn.rongcloud.im.server.broadcast.BroadcastManager;
import cn.rongcloud.im.server.utils.NToast;
import cn.rongcloud.im.server.widget.DialogWithYesOrNoUtils;

/**
 * Created by AMing on 16/6/23.
 * Company RongCloud
 */
public class AccountSettingActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_set);
        setTitle(R.string.account_setting);
        initViews();
    }

    private void initViews() {
        RelativeLayout mPassword = (RelativeLayout) findViewById(R.id.ac_set_change_pswd);
        RelativeLayout mPrivacy = (RelativeLayout) findViewById(R.id.ac_set_privacy);
        RelativeLayout mNewMessage = (RelativeLayout) findViewById(R.id.ac_set_new_message);
        RelativeLayout mClean = (RelativeLayout) findViewById(R.id.ac_set_clean);
        RelativeLayout mExit = (RelativeLayout) findViewById(R.id.ac_set_exit);
        mPassword.setOnClickListener(this);
        mPrivacy.setOnClickListener(this);
        mNewMessage.setOnClickListener(this);
        mClean.setOnClickListener(this);
        mExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ac_set_change_pswd:
                startActivity(new Intent(this, UpdatePasswordActivity.class));
                break;
            case R.id.ac_set_privacy:
                startActivity(new Intent(this, PrivacyActivity.class));
                break;
            case R.id.ac_set_new_message:
                startActivity(new Intent(this, NewMessageRemindActivity.class));
                break;
            case R.id.ac_set_clean:
                DialogWithYesOrNoUtils.getInstance().showDialog(mContext, "是否清除缓存?", new DialogWithYesOrNoUtils.DialogCallBack() {
                    @Override
                    public void executeEvent() {
                        File file = new File(Environment.getExternalStorageDirectory().getPath() + getPackageName());
                        deleteFile(file);
                        NToast.shortToast(mContext, "清除成功");
                    }

                    @Override
                    public void executeEditEvent(String editText) {

                    }

                    @Override
                    public void updatePassword(String oldPassword, String newPassword) {

                    }
                });
                break;
            case R.id.ac_set_exit:
                DialogWithYesOrNoUtils.getInstance().showDialog(mContext, "是否退出登录?", new DialogWithYesOrNoUtils.DialogCallBack() {
                    @Override
                    public void executeEvent() {
                        BroadcastManager.getInstance(mContext).sendBroadcast(SealConst.EXIT);
                    }

                    @Override
                    public void executeEditEvent(String editText) {

                    }

                    @Override
                    public void updatePassword(String oldPassword, String newPassword) {

                    }
                });
                break;
        }
    }


    public void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            file.delete();
        }
    }

}
