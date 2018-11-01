package org.revocen.cavmp.util;

import java.io.IOException;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.revocen.cavmp.R;
import org.revocen.cavmp.model.AppVersion;
import org.revocen.cavmp.network.ApplicationVersionConfigDownloader;
import org.revocen.cavmp.network.DownloadTask;
import org.revocen.cavmp.widget.RevocenAppUpdateProgressBar;
import org.revocen.cavmp.widget.RevocenAppUpdateProgressBarContainerAlertDialog;

import android.content.Context;
import android.widget.Toast;

public class UpdateManagerController {

    private Context context;

    private static UpdateManagerController CONTROLLER;

    private UpdateManagerController() {

    }

    public static UpdateManagerController getInstance() {
        if (CONTROLLER == null) {
            CONTROLLER = new UpdateManagerController();
        }
        return CONTROLLER;
    }

    public void checkUpdate(Context context, final String serverConfigUrl) {
        this.context = context;
        new Thread(new Runnable() {
            @Override
            public void run() {
                ApplicationVersionConfigDownloader applicationVersionConfigDownloader = new ApplicationVersionConfigDownloader(serverConfigUrl, null);
                try {
                    String result = applicationVersionConfigDownloader.download();
                    if (StringUtils.isBlank(result)) {
                        Toast.makeText(UpdateManagerController.this.context, "检查更新失败,没有找到更新信息！", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        AppVersion lastAppVersion = new Gson().fromJson(result, AppVersion.class);
                        EventBus.getDefault().post(lastAppVersion);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void compareVersion(AppVersion last) {
        try {
            int lastVersionCode = Integer.valueOf(last.getVersionCode());
            if (isUpdate(lastVersionCode)) {
                showDownloadDialog(last);
            } else {
                // Toast.makeText(this.context, "已经是最新版本", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查软件是否有更新版本
     *
     * @return true:有更新版本 false:没有可更新版本
     */
    private boolean isUpdate(int newVersionCode) {
        // 获取当前软件版本
        int versionCode = AppHelper.applicationVersionCode(context);
        // 版本判断
        if (newVersionCode > versionCode) {
            return true;
        }
        return false;
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog(AppVersion last) {

        final RevocenAppUpdateProgressBarContainerAlertDialog alertDialog = new RevocenAppUpdateProgressBarContainerAlertDialog(this.context);
        alertDialog.show();
        DownloadTask downloadUtil = new DownloadTask(last.getAndroidDownloadUrl(), "download");
        downloadUtil.setOnDownloadListener(new DownloadTask.OnDownloadListener() {

            @Override
            public void onDownloadingListener(int progress) {
                ((RevocenAppUpdateProgressBar) alertDialog.findViewById(R.id.cpb_progresbar)).setProgress(progress);
            }

            @Override
            public void onDownloadException(Exception ex) {
                alertDialog.dismiss();
                Toast.makeText(context, "更新失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadFinishedListener(String filePath, String fileName) {
                alertDialog.dismiss();
                AppHelper.installApk(UpdateManagerController.this.context, filePath, fileName);
            }
        });
        downloadUtil.start();
    }
}
