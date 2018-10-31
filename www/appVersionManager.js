var exec = require('cordova/exec'),
    channel = require('cordova/channel'),

    PLUGIN_NAME = 'VersionManagerPlugin',

    pluginNativeMethod = {
        GET_INFO: 'jsGetVersionInfo',
        CHECK_UPDATE: 'jsCheckUpdate'

    };

channel.onCordovaReady.subscribe(function () {
    exec(nativeCallback, null, PLUGIN_NAME, pluginNativeMethod.CHECK_UPDATE, []);
});

/**
 * Method is called when native side sends us different events.
 * Those events can be about update download/installation process.
 *
 * @param {String} msg - JSON formatted string with call arguments
 */
function nativeCallback(msg) {
    // parse call arguments
    var resultObj = processMessageFromNative(msg);
    if (resultObj.action == null) {
        console.log('Action is not provided, skipping');
        return;
    }
}

/**
 * Parse arguments that were sent from the native side.
 * Arguments are a JSON string of type:
 * { action: "action identifier", error: {...error data...}, data: {...some additional data..} }
 * Some parameters may not exist, but the resulting object will have them all.
 *
 * @param {String} msg - arguments as JSON string
 * @return {Object} parsed string
 */
function processMessageFromNative(msg) {
    var errorContent = null,
        dataContent = null,
        actionId = null;

    try {
        var resultObj = JSON.parse(msg);
        if (resultObj.hasOwnProperty('error')) {
            errorContent = resultObj.error;
        }
        if (resultObj.hasOwnProperty('data')) {
            dataContent = resultObj.data;
        }
        if (resultObj.hasOwnProperty('action')) {
            actionId = resultObj.action;
        }
    } catch (err) {}

    return {
        action: actionId,
        error: errorContent,
        data: dataContent
    };
}

function callNativeMethod(methodName, options, callback) {
    var innerCallback = function (msg) {
        var resultObj = processMessageFromNative(msg);
        if (callback !== undefined && callback != null) {
            callback(resultObj.error, resultObj.data);
        }
    };

    var sendArgs = [];
    if (options !== null && options !== undefined) {
        sendArgs.push(options);
    }

    exec(innerCallback, null, PLUGIN_NAME, methodName, sendArgs);
}
var app = {

    /**
     * Get information about the current version like current release version, app build version and so on.
     * The "data" property of the callback will contain all the information.
     *
     * @param {Callback(error, data)} callback - called, when information is retrieved from the native side.
     */
    getVersionInfo: function (callback) {
        callNativeMethod(pluginNativeMethod.GET_INFO, null, callback);
    }
};

module.exports = app;