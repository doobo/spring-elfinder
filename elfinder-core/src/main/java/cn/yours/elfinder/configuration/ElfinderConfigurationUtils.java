/*
 * #%L
 * %%
 * Copyright (C) 2015 Trustsystems Desenvolvimento de Sistemas, LTDA.
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Trustsystems Desenvolvimento de Sistemas, LTDA. nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package cn.yours.elfinder.configuration;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * Utility class to handle the Elfinder Configuration file path.
 *
 * @author Thiago Gutenberg Carvalho da Costa
 */
public final class ElfinderConfigurationUtils {

    private ElfinderConfigurationUtils() {
        // suppress default constructor
        // for noninstantiability
        throw new AssertionError();
    }

    public static String treatPath(String path) {
        if (path != null && !path.trim().isEmpty()) {
            final String uriSuffix = "file:";
            final String slash = "/";
            path = path.trim();
            path = path.replaceAll("//", slash);
            path = path.replaceAll("\\\\", slash);
            path = path.replaceAll("\\s+", " ");
            path = path.replaceAll("[\\p{Z}]", "%20");

            StringBuilder sb = new StringBuilder();
            if (!path.startsWith(uriSuffix)) {
                if (path.startsWith(slash)) {
                    sb.append(uriSuffix).append(path);
                } else {
                    sb.append(uriSuffix).append(slash).append(path);
                }
            }
            return sb.toString();
        }
        return path;
    }

    public static URI toURI(String path) {
        return URI.create(treatPath(path));
    }

    /**
     * 创建目录以及文件
     */
    public static File createFile(String filePath, String fileName) {
        File folder = new File(filePath);
        //文件夹路径不存在
        if (!folder.exists()) {
            boolean mkdirs = folder.mkdirs();
        }
        filePath = filePath.replace("\\\\","/").replace("\\","/");
        if(!filePath.endsWith("/")){
            filePath += "/";
        }
        // 如果文件不存在就创建
        File file = new File(filePath + fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 如果目录不存在,则创建目录
     * @param path
     */
    public static File createDirIfAbsent(String path){
        File folder = new File(path);
        //文件夹路径不存在
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }
    
}
