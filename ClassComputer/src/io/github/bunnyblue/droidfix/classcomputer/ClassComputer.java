/*
 * DroidFix Project
 * file ClassComputer.java  is  part of DroidFix
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

package io.github.bunnyblue.droidfix.classcomputer;

import io.github.bunnyblue.droidfix.classcomputer.cache.Configure;
import io.github.bunnyblue.droidfix.classcomputer.classes.ClassInject;
import io.github.bunnyblue.droidfix.classcomputer.proguard.MappingMapper;

/**
 * Created by BunnyBlue on 11/10/15.
 */
public class ClassComputer {
    public static void main(String[] args) {
        String action = args[0];
        String buildRootDir = args[1];
        String buildType = args[2];
        Configure.getInstance().setBuildRootDir(buildRootDir);
        Configure.getInstance().setBuildType(buildType);
        Configure.getInstance().init();
        if ("injectCode".equals(action)) {
            injectClasses();
        } else if ("cacheClassesTable".equals(action)) {
            writeClassCache();
        } else if ("diffPatch".equals(action)) {
            computerDiffClases();
        }

    }

    public static void injectClasses() {

        ClassInject.injectConstructionClasses();
    }

    public static void writeClassCache() {

        MappingMapper mappingMapper = new MappingMapper();
        mappingMapper.processRawClasses();
        mappingMapper.writeNewClassCache(mappingMapper.processRawClasses());

    }

    public static void computerDiffClases() {

        MappingMapper mappingMapper = new MappingMapper();
        mappingMapper.processRawClasses();

        mappingMapper.computeDiffObject();

    }
}
