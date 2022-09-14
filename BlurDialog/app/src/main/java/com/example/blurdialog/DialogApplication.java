package com.example.blurdialog;

import android.app.Application;


/**
 * Created by vincent on 2018/4/26.
 */

public class DialogApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
//        AppWatcher.config = AppWatcher.config.copy(watchFragmentViews = false)

    }
}
