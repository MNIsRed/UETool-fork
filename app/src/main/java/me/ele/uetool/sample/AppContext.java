package me.ele.uetool.sample;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.yc.toollib.crash.CrashHandler;
import com.yc.toollib.crash.CrashListener;
import com.yc.toollib.crash.CrashToolUtils;

import me.ele.uetool.UETool;

public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        Fresco.initialize(this);

        UETool.putFilterClass(FilterOutView.class);
        UETool.putAttrsProviderClass(CustomAttribution.class);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            private int visibleActivityCount;
            private int uetoolDismissY = -1;

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                visibleActivityCount++;
                if (visibleActivityCount == 1 && uetoolDismissY >= 0) {
                    UETool.showUETMenu(uetoolDismissY);
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                visibleActivityCount--;
                if (visibleActivityCount == 0) {
                    uetoolDismissY = UETool.dismissUETMenu();
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

        CrashHandler.getInstance().init(this, new CrashListener() {
            /**
             * 重启app
             */
            @Override
            public void againStartApp() {
                CrashToolUtils.reStartApp1(AppContext.this, 1000);
                //CrashToolUtils.reStartApp2(App.this,1000, MainActivity.class);
                //CrashToolUtils.reStartApp3(AppManager.getAppManager().currentActivity());
            }

            /**
             * 自定义上传crash，支持开发者上传自己捕获的crash数据
             * @param ex                        ex
             */
            @Override
            public void recordException(Throwable ex) {
                //自定义上传crash，支持开发者上传自己捕获的crash数据
                //StatService.recordException(getApplication(), ex);
            }
        });
    }
}
