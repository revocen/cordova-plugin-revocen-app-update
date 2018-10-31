package org.revocen.cavmp.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Toast;

import org.revocen.cavmp.R;

public class RevocenAppUpdateProgressBarContainerAlertDialog extends Dialog {

    private Context context;
    /* 更新进度条 */
    private RevocenAppUpdateProgressBar progressBar;

    public RevocenAppUpdateProgressBarContainerAlertDialog(Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.revocen_app_update_softupdate_progress);
        progressBar = (RevocenAppUpdateProgressBar) findViewById(R.id.cpb_progresbar);
        progressBar.setOnFinishedListener(new RevocenAppUpdateProgressBar.OnFinishedListener() {
            @Override
            public void onFinish() {
                Toast.makeText(RevocenAppUpdateProgressBarContainerAlertDialog.this.context, "正在准备安装", Toast.LENGTH_SHORT).show();
            }
        });
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onBackPressed() {

    }

}
