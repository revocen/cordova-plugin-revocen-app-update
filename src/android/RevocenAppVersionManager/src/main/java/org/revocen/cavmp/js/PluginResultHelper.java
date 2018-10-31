package org.revocen.cavmp.js;


import com.alibaba.fastjson.JSONObject;
import org.revocen.cavmp.event.IPluginEvent;
import org.revocen.cavmp.model.CavmpError;

import org.apache.cordova.PluginResult;

import java.util.Map;
import java.util.Set;

/**
 *
 * Helper class to generate proper instance of PluginResult, which is send to the web side.
 *
 * @see PluginResult
 */
public class PluginResultHelper {

    // TODO: migrate to JSONObject

    // keywords for JSON string, that is send to JavaScript side
    private static class JsParams {
        private static class General {
            public static final String ACTION = "action";
            public static final String ERROR = "error";
            public static final String DATA = "data";
        }

        private static class Error {
            public static final String CODE = "code";
            public static final String DESCRIPTION = "description";
        }
    }

    /**
     * Create PluginResult instance from event.
     *
     * @param event hot-code-push plugin event
     *
     * @return cordova's plugin result
     * @see PluginResult
     * @see IPluginEvent
     */
    public static PluginResult pluginResultFromEvent(IPluginEvent event) {
        final String actionName = event.name();
        final Map<String, Object> data = event.data();
        final CavmpError error = event.error();

        return createPluginResult(actionName, data, error);
    }

    public static PluginResult createPluginResult(String actionName, Map<String, Object> data, CavmpError error) {
        JSONObject errorNode = null;
        JSONObject dataNode = null;

        if (error != null) {
            errorNode = createErrorNode(error.getErrorCode(), error.getErrorDescription());
        }

        if (data != null && data.size() > 0) {
            dataNode = createDataNode(data);
        }

        return getResult(actionName, dataNode, errorNode);
    }

    // region Private API

    private static JSONObject createDataNode(Map<String, Object> data) {

        JSONObject dataNode = new JSONObject();

        Set<Map.Entry<String, Object>> dataSet = data.entrySet();
        for (Map.Entry<String, Object> entry : dataSet) {
            Object value = entry.getValue();
            if (value == null) {
                continue;
            }

            dataNode.put(entry.getKey(), value.toString());
        }

        return dataNode;
    }

    private static JSONObject createErrorNode(int errorCode, String errorDescription) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(JsParams.Error.CODE, errorCode);
        jsonObject.put(JsParams.Error.DESCRIPTION, errorDescription);

        return jsonObject;
    }

    private static PluginResult getResult(String action, JSONObject data, JSONObject error) {
        JSONObject resultObject = new JSONObject();
        if (action != null) {
            resultObject.put(JsParams.General.ACTION, action);
        }

        if (data != null) {
            resultObject.put(JsParams.General.DATA, data);
        }

        if (error != null) {
            resultObject.put(JsParams.General.ERROR, error);
        }

        return new PluginResult(PluginResult.Status.OK, resultObject.toString());
    }

    // endregion

}
