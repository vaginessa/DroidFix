package io.github.bunnyblue.droidfix;

import android.app.Application;
import android.content.Context;

import io.github.bunnyblue.droidfix.dexloader.DroidFix;

/**
 * Created by BunnyBlue on 11/11/15.
 */
public class DroidApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {

        super.attachBaseContext(base);
        DroidFix.install(this);
    }
}
