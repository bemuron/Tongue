package viola1.agrovc.com.tonguefinal.view;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();

    }
}