/*
 * DroidFix Project
 * file MappingMapper.java  is  part of DroidFix
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;


import java.util.List;

import io.github.bunnyblue.droidfix.classcomputer.cache.Configure;
import io.github.bunnyblue.droidfix.classcomputer.cache.HashUtil;
import io.github.bunnyblue.droidfix.classcomputer.classes.ClassObject;
import io.github.bunnyblue.droidfix.classcomputer.gradleImpl.GradleImpl15;
import org.apache.commons.io.FileUtils;


public class MappingMapper {


    HashMap<String, String> hashtable=new HashMap<String, String>();
    //	HashMap<K, V>
    public void  produce(String path) {
        try {
            Collection<String>lines=FileUtils.readLines(new File(path));
            for (String string : lines) {//android.support.design.internal.NavigationMenuPresenter -> android.support.design.internal.b:
                if (string.contains(" -> ")&&string.endsWith(":")) {
                    String originClass=string.substring(0, string.indexOf(" ")).replaceAll(" ", "");
                    String proguardClass=string.substring(string.lastIndexOf(" "), string.length()-1).replaceAll(" ", "");
                    hashtable.put(proguardClass,originClass);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public ArrayList<ClassObject> processRawClasses() {
        File pathRoot=new File(Configure.getInstance().getTransformedClassDir());
        if (Configure.getInstance().isJar()){
          return   processRawClassesJarFormat();
        }else {
          return   processRawClassesFolder();
        }

//      }

    }
     ArrayList<ClassObject> processRawClassesJarFormat() {
         GradleImpl15.extract();
        ArrayList<ClassObject> cacheClasses = new ArrayList<ClassObject>();
        produce(Configure.getInstance().getPatchMapping());
        File pathRoot=new File(Configure.getInstance().getTransformedClassDir());

        Collection<File> clses=FileUtils.listFiles(pathRoot, new String[]{"class"}, true);
        for (File file : clses) {
            String path = file.getAbsolutePath();
            String md5 = HashUtil.getClassMd5(path);


            String tmp = "classes" + File.separator + "debug";
            path = path.replaceAll(pathRoot.getAbsolutePath(), "");
            path = path.substring(1, path.indexOf(".class"));
            String classname = path.replaceAll(File.separator, ".");
            ClassObject classObject = new ClassObject(md5, classname);
            classObject.setProguardedClassName(hashtable.get(classname));
            classObject.setLocalPath(file.getAbsolutePath());
            cacheClasses.add(classObject);


        }
        int  index=0;
        for (ClassObject file : cacheClasses) {
            index++;
            //System.out.println(file.toString()+index);
        }
        return cacheClasses;

    }
    private ArrayList<ClassObject> processRawClassesFolder() {
        ArrayList<ClassObject> cacheClasses = new ArrayList<ClassObject>();
        produce(Configure.getInstance().getPatchMapping());
        File pathRoot=new File(Configure.getInstance().getTransformedClassDir());

        Collection<File> clses=FileUtils.listFiles(pathRoot, new String[]{"class"}, true);
        for (File file : clses) {
            String path = file.getAbsolutePath();
            String md5 = HashUtil.getClassMd5(path);


            String tmp = "classes" + File.separator + "debug";
            path = path.replaceAll(pathRoot.getAbsolutePath(), "");
            path = path.substring(1, path.indexOf(".class"));
            String classname = path.replaceAll(File.separator, ".");
            ClassObject classObject = new ClassObject(md5, classname);
            classObject.setProguardedClassName(hashtable.get(classname));
            classObject.setLocalPath(file.getAbsolutePath());
            cacheClasses.add(classObject);


        }
        int  index=0;
        for (ClassObject file : cacheClasses) {
            index++;
            //System.out.println(file.toString()+index);
        }
        return cacheClasses;

    }
    public  void writeNewClassCache(ArrayList<ClassObject> cacheClasses ) {

        Collections.sort(cacheClasses);
        ArrayList<String> md5s = new ArrayList<String>();

        for (ClassObject classObject : cacheClasses) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(classObject.getClassName());
            stringBuffer.append(" ");
            stringBuffer.append(classObject.getMd5());
            md5s.add(stringBuffer.toString());


        }
        try {
            FileUtils.writeLines(new File(Configure.getInstance().getPatchClassMD5()), md5s);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public  ArrayList<ClassObject> computeDiffObject() {
        ArrayList<ClassObject> diffClass=new ArrayList<ClassObject>();
        ArrayList<ClassObject>mClassMappings=	processRawClasses();//最新的class列表
        ArrayList<ClassObject> cacheClasses=readCacheClasses();//上次的缓存列表
        for (ClassObject classObject : mClassMappings) {

            boolean isNewClass=true;
            for (ClassObject classObjectCache : cacheClasses) {
                if (classObject.getClassName().equals(classObjectCache.getClassName())) {
                    isNewClass=false;
                    if (!classObject.getMd5().equals(classObjectCache.getMd5())) {
                        diffClass.add(classObject);
                    }
                    break;

                }
            }
            if(isNewClass)
            {diffClass.add(classObject);//新的类

            }
        }
        DiffClassUtil.copyDiffClasses(diffClass, Configure.getInstance().getDiffClassesDir());
        return diffClass;

    }
    public  ArrayList<ClassObject> readCacheClasses() {
        ArrayList<ClassObject> cacheClasses = new ArrayList<ClassObject>();
        File cache = new File(Configure.getInstance().getPatchClassMD5());
        try {
            List<String> caches = FileUtils.readLines(cache);
            for (String string : caches) {
                String[] maps = string.split(" ");
                ClassObject classObject = new ClassObject(maps[1], maps[0]);
                cacheClasses.add(classObject);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return cacheClasses;
    }

}

