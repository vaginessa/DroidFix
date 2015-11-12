/*
 * DroidFix Project
 * file GradleImpl15.java  is  part of DroidFix
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

package io.github.bunnyblue.droidfix.classcomputer.gradleImpl;

import io.github.bunnyblue.droidfix.classcomputer.cache.Configure;
import io.github.bunnyblue.droidfix.classcomputer.classes.ClassInject;
import io.github.bunnyblue.droidfix.classcomputer.proguard.MappingMapper;
import org.apache.commons.io.FileUtils;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Created by BunnyBlue on 11/12/15.
 */
public class GradleImpl15 {
    public  static  void extract(){
        File srcDir=new File(Configure.getInstance().getProguardJarFolder());
     Collection<File> jars=FileUtils.listFiles(srcDir, new String[]{"jar"}, true);
        List<File> jarsList= (List<File>) jars;
        File jar= jarsList.get(0);
       String extractClasses= jar.getParentFile().getAbsolutePath()+File.separator+ jar.getName().substring(0,jar.getName().indexOf(".jar"));
        Configure.getInstance().setTransformedClassDir(extractClasses);
File targetFile=new File(extractClasses);
        try {
            FileUtils.deleteDirectory(targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ZipUtil.unpack(jar,targetFile);
    }
    public  static  void main(String[]args){
        Configure.getInstance().setBuildRootDir("/Users/BunnyBlue/Downloads/ProguardDroid/app");
        Configure.getInstance().setBuildType("debug");
        Configure.getInstance().init();
        extract();

        ClassInject.injectConstructionClasses();
        MappingMapper mappingMapper = new MappingMapper();
        mappingMapper.processRawClasses();
        mappingMapper.writeNewClassCache(mappingMapper.processRawClasses());

    }
}
