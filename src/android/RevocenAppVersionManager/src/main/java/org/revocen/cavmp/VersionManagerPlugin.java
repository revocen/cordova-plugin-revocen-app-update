package org.revocen.cavmp;

import android.Manifest;
import android.content.Context;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.revocen.cavmp.config.CavmpXmlConfig;
import org.revocen.cavmp.js.JSAction;
import org.revocen.cavmp.js.PluginResultHelper;
import org.revocen.cavmp.model.AppVersion;
import org.revocen.cavmp.util.AppHelper;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.revocen.cavmp.util.UpdateManagerController;

import java.util.HashMap;
import java.util.Map;

public class VersionManagerPlugin extends CordovaPlugin {

    private CavmpXmlConfig cavmpXmlConfig;
    private static final String [] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static final int REQ_CODE_PERMISSION = 1;

    @Override
    public void onStart() {
        super.onStart();

        final EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);

        super.onStop();
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        parseCordovaConfigXml();
    }

    /**
     * Read hot-code-push plugin preferences from cordova config.xml
     */
    private void parseCordovaConfigXml() {
        if (cavmpXmlConfig != null) {
            return;
        }

        cavmpXmlConfig = CavmpXmlConfig.loadFromCordovaConfig(cordova.getActivity());
    }

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) {
        boolean cmdProcessed = true;
        if (JSAction.INIT.equals(action)) {
            // jsInit(callbackContext);
        } else if (JSAction.CHECK_UPDATE.equals(action)) {
            jsCheckUpdate(callbackContext);
        } else if (JSAction.GET_VERSION_INFO.equals(action)) {
            jsGetVersionInfo(callbackContext, args);
        } else {
            cmdProcessed = false;
        }

        return cmdProcessed;
    }

    private void jsCheckUpdate(CallbackContext callbackContext) {
        checkPermission();
        final PluginResult pluginResult = PluginResultHelper.createPluginResult(JSAction.GET_VERSION_INFO, null, null);
        callbackContext.sendPluginResult(pluginResult);
    }

    private void checkPermission() {
        for (int i = 0; i < PERMISSIONS.length; i++) {
            if (cordova.hasPermission(PERMISSIONS[i])) {
                continue;
            } else {
                cordova.requestPermissions(this, REQ_CODE_PERMISSION, PERMISSIONS);
                return;
            }

        }
        doCheck();
    }

    /**
     * Get information about app and web versions.
     *
     * @param callback callback where to send the result
     */
    private void jsGetVersionInfo(final CallbackContext callback, CordovaArgs args) {
        final Context context = cordova.getActivity();
        final Map<String, Object> data = new HashMap<String, Object>();
        data.put("appVersion", AppHelper.applicationVersionName(context));
        data.put("buildVersion", AppHelper.applicationVersionCode(context));

        final PluginResult pluginResult = PluginResultHelper.createPluginResult(JSAction.GET_VERSION_INFO, data, null);
        callback.sendPluginResult(pluginResult);
    }

    public void doCheck() {
        String configURL = cavmpXmlConfig.getConfigUrl();
        UpdateManagerController.getInstance().checkUpdate(cordova.getContext(), configURL);
    }

    @Subscribe
    public void onEvent(final AppVersion appVersion) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UpdateManagerController.getInstance().compareVersion(appVersion);
            }
        });

    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        super.onRequestPermissionResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_CODE_PERMISSION){
            checkPermission();
        }
    }
}
