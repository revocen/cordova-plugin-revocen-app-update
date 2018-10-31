package org.revocen.cavmp.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;


import java.io.File;

public class AppHelper {

    /**
     * Getter for application build version.
     *
     * @param context application context
     * @return build version
     */
    public static int applicationVersionCode(final Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("APP UPDATE", "Can't get version code", e);
        }

        return versionCode;
    }

    /**
     * Getter for application version name.
     *
     * @param context application context
     * @return version name
     */
    public static String applicationVersionName(final Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("APP UPDATE", "Can't get version name", e);
        }

        return versionName;
    }

    /**
     * 安装APK文件
     */
    public static void installApk(Context context, String path, String apkName) {
        try{
            File apkFile = new File(path, apkName);
            if (!apkFile.exists()) {
                Toast.makeText(context,"没有找到apk文件",Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);

            if (Build.VERSION.SDK_INT >= 24) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, "org.revocen.cavmp.fileprovider", apkFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
