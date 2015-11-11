package io.github.bunnyblue.droidfix;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import io.github.bunnyblue.droidfix.dexloader.DroidFix;

/**
 * Created by BunnyBlue on 11/11/15.
 */
public class DroidApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {

        super.attachBaseContext(base);
        DroidFix.install(this);
        File file =new File("/sdcard/patch.apk");
        if (!file.exists()){
            Toast.makeText(this, "file not found", Toast.LENGTH_SHORT).show();
            return;
        }

        File dest=new File(getApplicationInfo().dataDir, DroidFix.DROID_CODE_CACHE+File.separator+file.getName());
        try {
            DroidFix.copyFile(file,dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DroidFix.installPatch(this, dest);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
