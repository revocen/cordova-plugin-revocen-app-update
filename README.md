# cordova-plugin-revocen-app-update

# 模仿cordova热更新插件做的用于原生APK的版本检查更新的插件

安装插件后需要在cordova项目根目录的config.xml中添加以下配置
```

    <cavmp>
        <auto-download enabled="true" />
        <auto-install enabled="true" />
        <config-file url="http://com.example.com/revocen-app-version.json" />
        <native-interface version="1" />
    </cavmp>

```

其中config-file的url只要返回以下json内容即可

```
{
    name: 'app-name',
    androidDownloadUrl: 'http://com.example.com/android-app.apk',
    versionName: '1.0.0',
    versionCode: '2018100101',
    sha1: '',
    upgradeDate: '',
    upgradeDescription: '',
    upgradeLevel: 'force'
}
```

其他配置项暂时没有用到
