package org.revocen.cavmp.config;

import android.content.Context;

import org.apache.cordova.ConfigXmlParser;
import org.xmlpull.v1.XmlPullParser;

/**
 * Created by Nikolay Demyankov on 06.08.15.
 * <p/>
 * XML parser for Cordova's config.xml.
 * Used to read plugin specific preferences.
 *
 * @see CavmpXmlConfig
 */
class CavmpXmlConfigParser extends ConfigXmlParser {

    private CavmpXmlConfig cavmpConfig;

    private boolean isInsideChcpBlock;
    private boolean didParseChcpBlock;

    /**
     * Parse config.xml.
     * Result is set into passed ChcpXmlConfig instance.
     *
     * @param context    current context
     * @param cavmpConfig config instance to which we will set preferences from config.xml
     */
    public void parse(final Context context, final CavmpXmlConfig cavmpConfig) {
        this.cavmpConfig = cavmpConfig;

        isInsideChcpBlock = false;
        didParseChcpBlock = false;

        super.parse(context);
    }

    @Override
    public void handleStartTag(final XmlPullParser xml) {
        if (didParseChcpBlock) {
            return;
        }

        final String name = xml.getName();
        if (XmlTags.MAIN_TAG.equals(name)) {
            isInsideChcpBlock = true;
            return;
        }

        // proceed only if we are parsing our plugin preferences
        if (!isInsideChcpBlock) {
            return;
        }

        // parse configuration file preference
        if (XmlTags.CONFIG_FILE_TAG.equals(name)) {
            processConfigFileBlock(xml);
            return;
        }

        // parse auto download preference
        if (XmlTags.AUTO_DOWNLOAD_TAG.equals(name)) {
            processAutoDownloadBlock(xml);
            return;
        }

        // parse auto installation preference
        if (XmlTags.AUTO_INSTALLATION_TAG.equals(name)) {
            processAutoInstallationBlock(xml);
            return;
        }

        // parse native navigation interface version
        if (XmlTags.NATIVE_INTERFACE_TAG.equals(name)) {
            processNativeInterfaceBlock(xml);
        }
    }

    @Override
    public void handleEndTag(final XmlPullParser xml) {
        if (didParseChcpBlock) {
            return;
        }

        final String name = xml.getName();
        if (XmlTags.MAIN_TAG.equals(name)) {
            didParseChcpBlock = true;
            isInsideChcpBlock = false;
        }
    }

    private void processConfigFileBlock(final XmlPullParser xml) {
        String configUrl = xml.getAttributeValue(null, XmlTags.CONFIG_FILE_URL_ATTRIBUTE);

        cavmpConfig.setConfigUrl(configUrl);
    }

    private void processAutoDownloadBlock(final XmlPullParser xml) {
        boolean isEnabled = xml.getAttributeValue(null, XmlTags.AUTO_DOWNLOAD_ENABLED_ATTRIBUTE).equals("true");
        cavmpConfig.allowUpdatesAutoDownload(isEnabled);
    }

    private void processAutoInstallationBlock(final XmlPullParser xml) {
        boolean isEnabled = xml.getAttributeValue(null, XmlTags.AUTO_INSTALLATION_ENABLED_ATTRIBUTE).equals("true");
        cavmpConfig.allowUpdatesAutoInstall(isEnabled);
    }

    private void processNativeInterfaceBlock(final XmlPullParser xml) {
        final String nativeVersionStr = xml.getAttributeValue(null, XmlTags.NATIVE_INTERFACE_VERSION_ATTRIBUTE);
        final int nativeVersion = Integer.parseInt(nativeVersionStr);

        cavmpConfig.setNativeInterfaceVersion(nativeVersion);
    }
}
