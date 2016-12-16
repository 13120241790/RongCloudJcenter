package cn.rongcloud.im;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.facebook.stetho.inspector.database.DefaultDatabaseConnectionProvider;
import com.facebook.stetho.inspector.protocol.ChromeDevtoolsDomain;

import cn.rongcloud.im.message.TestMessage;
import cn.rongcloud.im.message.provider.ContactNotificationMessageProvider;
import cn.rongcloud.im.message.provider.TestMessageProvider;
import cn.rongcloud.im.server.utils.NLog;
import cn.rongcloud.im.utils.SharedPreferencesContext;
import io.rong.imageloader.core.DisplayImageOptions;
import io.rong.imageloader.core.display.FadeInBitmapDisplayer;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.RealTimeLocationMessageProvider;

import io.rong.imlib.ipc.RongExceptionHandler;
import io.rong.push.RongPushClient;
import io.rong.push.common.RongException;


public class App extends MultiDexApplication {

    private static DisplayImageOptions options;

    @Override
    public void onCreate() {

        super.onCreate();
        Stetho.initialize(new Stetho.Initializer(this) {
            @Override
            protected Iterable<DumperPlugin> getDumperPlugins() {
                return new Stetho.DefaultDumperPluginsBuilder(App.this).finish();
            }

            @Override
            protected Iterable<ChromeDevtoolsDomain> getInspectorModules() {
                Stetho.DefaultInspectorModulesBuilder defaultInspectorModulesBuilder = new Stetho.DefaultInspectorModulesBuilder(App.this);
                defaultInspectorModulesBuilder.provideDatabaseDriver(new RongDatabaseDriver(App.this, new RongDatabaseFilesProvider(App.this), new DefaultDatabaseConnectionProvider()));
                return defaultInspectorModulesBuilder.finish();
            }
        });

        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {

//            LeakCanary.install(this);//内存泄露检测
            RongPushClient.registerHWPush(this);
            RongPushClient.registerMiPush(this, "2882303761517473625", "5451747338625");
            try {
                RongPushClient.registerGCM(this);
            } catch (RongException e) {
                e.printStackTrace();
            }

            /**
             * 注意：
             *
             * IMKit SDK调用第一步 初始化
             *
             * context上下文
             *
             * 只有两个进程需要初始化，主进程和 push 进程
             */
            //RongIM.setServerInfo("nav.cn.ronghub.com", "img.cn.ronghub.com");
            RongIM.init(this);
            NLog.setDebug(true);//Seal Module Log 开关
            SealAppContext.init(this);
            SharedPreferencesContext.init(this);
            Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));

            try {
                RongIM.registerMessageTemplate(new ContactNotificationMessageProvider());
                RongIM.registerMessageTemplate(new RealTimeLocationMessageProvider());
                RongIM.registerMessageType(TestMessage.class);
                RongIM.registerMessageTemplate(new TestMessageProvider());
            } catch (Exception e) {
                e.printStackTrace();
            }

            openSealDBIfHasCachedToken();

            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.de_default_portrait)
                    .showImageOnFail(R.drawable.de_default_portrait)
                    .showImageOnLoading(R.drawable.de_default_portrait)
                    .displayer(new FadeInBitmapDisplayer(300))
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();

        }
    }

    public static DisplayImageOptions getOptions() {
        return options;
    }

    private void openSealDBIfHasCachedToken() {
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        String cachedToken = sp.getString("loginToken", "");
        if (!TextUtils.isEmpty(cachedToken)) {
            String current = getCurProcessName(this);
            String mainProcessName = getPackageName();
            if (mainProcessName.equals(current)) {
                SealUserInfoManager.getInstance().openDB();
            }
        }
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

}
