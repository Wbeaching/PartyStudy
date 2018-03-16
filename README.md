# PartyStudy
掌上党建

本App使用了Bmob后端云的Android SDK和C# SDK

【1、android客户端中】

在ConfigApp.java中填写你自己的AppID

```
public class ConfigApp {
    private String AppID = "填写你自己的APPID";

    public String getAppID() {
        return AppID;
    }

    public void setAppID(String appID) {
        AppID = appID;
    }
}
```
