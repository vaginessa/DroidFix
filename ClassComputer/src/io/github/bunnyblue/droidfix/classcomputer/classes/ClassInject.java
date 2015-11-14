/*
 * DroidFix Project
 * file ClassInject.java  is  part of DroidFix
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import io.github.bunnyblue.droidfix.classcomputer.cache.Configure;
import io.github.bunnyblue.droidfix.classcomputer.proguard.MappingMapper;
import javassist.CannotCompileException;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;
import org.apache.commons.io.FileUtils;
import org.zeroturnaround.zip.ZipUtil;

public class ClassInject {
    public static String getConstructionCode() {
        String insert = "if (io.github.bunnyblue.droidfix.ClassVerifier.CLASS_ISPREVERIFIED) {\n"
                + "			System.out.println(io.github.bunnyblue.droidfix.AntilazyLoad.class);\n" + "		}";
        return insert;

    }
    public static void injectConstructionClasses() {

        MappingMapper mappingMapper = new MappingMapper();
        ArrayList<ClassObject> classObjects = mappingMapper.processRawClasses();
        try {
           File cp= new File(Configure.getInstance().getTransformedClassDir());

            ClassPath mClassPath = ClassPool.getDefault()
                    .appendClassPath(cp.getAbsolutePath());
            System.err.println("add classpath : "+Configure.getInstance().getTransformedClassDir()+cp.isDirectory());
            //System.err.println("update class "+Configure.getInstance().getTransformedClassDir());
        } catch (NotFoundException e1) {
            e1.printStackTrace();
        }
        for (ClassObject classObject : classObjects) {
            try {
                CtClass ctClass = ClassPool.getDefault().get(classObject.getClassName());
                CtConstructor conts[] = ctClass.getDeclaredConstructors();
                if (conts==null||conts.length==0) {
                    CtConstructor ctConstructor = new CtConstructor(new CtClass[]{}, ctClass);
                    StringBuffer buffer2 = new StringBuffer();
                    buffer2.append("{\n\n")
                            .append(ClassInject.getConstructionCode())

                            .append("}");
                    ctConstructor.setBody(buffer2.toString());
                    ctClass.addConstructor(ctConstructor);
                   // System.out.println("add new constructor "+ctClass.getName());
                }else {
                    for (CtConstructor ctConstructor : conts) {
                        //System.err.println(ctConstructor.toString());
                        ctConstructor.insertBeforeBody(ClassInject.getConstructionCode());

                    }
                }
                //ctClass.writeFile();
                ctClass.writeFile(Configure.getInstance().getTransformedClassDir());
                // ctClass.debugWriteFile();
                ctClass.detach();
            } catch (NotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } // .getConstructors()[0].in
            catch (CannotCompileException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (Configure.getInstance().isJar()){
            repackJar();//如果混淆后的是jar 先解压然后重新打包
        }


    }
    private static void repackJar(){
     File src= new File(Configure.getInstance().getTransformedClassDir());
        File target= new File(Configure.getInstance().getTransformedClassDir()+".jar");
        ZipUtil.pack(src,target);
        try {
            FileUtils.deleteDirectory(src);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
