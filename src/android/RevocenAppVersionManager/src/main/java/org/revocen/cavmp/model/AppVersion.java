package org.revocen.cavmp.model;

import java.io.Serializable;

public class AppVersion implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String androidDownloadUrl;
	private String versionName;
	private String versionCode;
	private String sha1;
	private String upgradeDate;
	private String upgradeDescription;
	private String upgradeLevel;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAndroidDownloadUrl() {
		return androidDownloadUrl;
	}

	public void setAndroidDownloadUrl(String androidDownloadUrl) {
		this.androidDownloadUrl = androidDownloadUrl;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getSha1() {
		return sha1;
	}

	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}

	public String getUpgradeDate() {
		return upgradeDate;
	}

	public void setUpgradeDate(String upgradeDate) {
		this.upgradeDate = upgradeDate;
	}

	public String getUpgradeDescription() {
		return upgradeDescription;
	}

	public void setUpgradeDescription(String upgradeDescription) {
		this.upgradeDescription = upgradeDescription;
	}

	public String getUpgradeLevel() {
		return upgradeLevel;
	}

	public void setUpgradeLevel(String upgradeLevel) {
		this.upgradeLevel = upgradeLevel;
	}
}
