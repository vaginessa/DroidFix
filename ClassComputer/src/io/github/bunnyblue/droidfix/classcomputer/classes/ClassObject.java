/*
 * DroidFix Project
 * file ClassObject.java  is  part of DroidFix
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

package io.github.bunnyblue.droidfix.classcomputer.classes;

/**
 * Created by BunnyBlue on 11/11/15.
 */
public class ClassObject   implements Comparable<ClassObject>{

    public ClassObject(String md5, String className) {
        super();
        this.md5 = md5;
        this.className = className.replaceAll("\\\\",".").replaceAll("/",".");
    }
    String  md5;
    String  injectedMD5;
    public String getInjectedMD5() {
        return injectedMD5;
    }
    public void setInjectedMD5(String injectedMD5) {
        this.injectedMD5 = injectedMD5;
    }
    @Override
    public String toString() {
        return "ClassObject [md5=" + md5 + ", className=" + className + ", localPath=" + localPath
                + ", proguardedClassName=" + proguardedClassName + ", proguardedClassPath=" + proguardedClassPath + "]";
    }
    String className;
    String localPath;
    public String getProguardedClassName() {
        return proguardedClassName;
    }
    public void setProguardedClassName(String proguardedClassName) {
        this.proguardedClassName = proguardedClassName;
    }
    public String getProguardedClassPath() {
        return proguardedClassPath;
    }
    public void setProguardedClassPath(String proguardedClassPath) {
        this.proguardedClassPath = proguardedClassPath;
    }
    String proguardedClassName;
    String proguardedClassPath;
    public String getMd5() {
        return md5;
    }
    public void setMd5(String md5) {
        this.md5 = md5;
    }
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public String getLocalPath() {
        return localPath;
    }
    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }


    @Override
    public int compareTo(ClassObject o) {
        return className.compareTo(o.className);
        //return 0;
    }

}
