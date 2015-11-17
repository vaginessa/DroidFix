/*
 * DroidFix Project
 * file DiffClassUtil.java  is  part of DroidFix
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

package io.github.bunnyblue.droidfix.classcomputer.proguard;

/**
 * Created by BunnyBlue on 11/11/15.
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import io.github.bunnyblue.droidfix.classcomputer.classes.ClassObject;
import org.apache.commons.io.FileUtils;



public class DiffClassUtil {
    public static void copyDiffClasses(ArrayList<ClassObject> diffClasses,String rootPath) {
        File  rootDir=new File(rootPath);
        try {
            FileUtils.deleteDirectory(rootDir);
        } catch (IOException e1) {

            e1.printStackTrace();
        }
        rootDir.mkdirs();
        for (ClassObject classObject : diffClasses) {
            classObject.getClassName().replaceAll(".", "/");

            String subPath=classObject.getClassName().replaceAll("\\.","/");
            if ( subPath.lastIndexOf("/")!=-1) {
                subPath=subPath.substring(0, subPath.lastIndexOf("/"));
                subPath=rootPath+"/"+subPath;
                subPath=subPath.replaceAll("\\\\","/");
                File subDir=new File(subPath);
                subDir.mkdirs();
                File localClass=new File(classObject.getLocalPath());
                try {
                    FileUtils.copyFileToDirectory(localClass, subDir);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.err.println("Copy diff class "+localClass.getAbsolutePath());



            }else {
                File localClass=new File(classObject.getLocalPath());

                try {
                    FileUtils.copyFileToDirectory(localClass, rootDir);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.err.println("Copy diff class "+localClass.getAbsolutePath());
            }


        }
    }


}
