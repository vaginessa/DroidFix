/*
 * DroidFix Project
 * file DroidApp.java  is  part of DroidFix
 * The MIT License (MIT)  Copyright (c) 2015 Bunny Blue.
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

package io.github.bunnyblue.droidfix;

import android.app.Application;
import android.content.Context;

import java.io.File;

import io.github.bunnyblue.droidfix.dexloader.DroidFix;

/**
 * Created by BunnyBlue on 11/11/15.
 */
public class DroidApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {

        super.attachBaseContext(base);
        DroidFix.install(this);
//        File file =new File("/sdcard/patch.apk");
//        if (!file.exists()){
//            Toast.makeText(this, "file not found", Toast.LENGTH_SHORT).show();
//            return ;
//        }

        File dest=new File(getFilesDir(), DroidFix.DROID_CODE_CACHE+File.separator+"patch.apk");
//        try {
//            DroidFix.copyFile(file,dest);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (dest.isFile()&&dest.exists())
        {  DroidFix.installPatch(this, dest);
        }


    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
