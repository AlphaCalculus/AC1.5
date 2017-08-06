package io.github.alphacalculus.alphacalculus;

import android.app.Application;

public class TheApp extends Application {
    private static TheApp app = null;

    public static TheApp getInstance() {
        return app;
    }

    ;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

}
