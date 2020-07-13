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
package cn.yours.elfinder.service;

import cn.yours.elfinder.core.Target;
import cn.yours.elfinder.core.Volume;
import cn.yours.elfinder.core.VolumeSecurity;

import cn.yours.elfinder.core.impl.aliyunoss.AliyunOssFileSystemVolume;
import cn.yours.elfinder.ElFinderConstants;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VolumeHandler implements Serializable{

    private final Volume volume;
    private Target target;
    private VolumeSecurity volumeSecurity;
    private final ElfinderStorage elfinderStorage;

    public VolumeHandler(Target target, ElfinderStorage elfinderStorage) {
        this.target = target;
        this.volume = target.getVolume();
        this.volumeSecurity = elfinderStorage.getVolumeSecurity(target);
        this.elfinderStorage = elfinderStorage;
    }

    public VolumeHandler(VolumeHandler parent, String name) throws IOException {
        this.volume = parent.volume;
        this.elfinderStorage = parent.elfinderStorage;
        String parentPath = volume.getPath(parent.target);
        if(parentPath.length() > 0 && !parentPath.endsWith(
            ElFinderConstants.ELFINDER_PARAMETER_FILE_SEPARATOR)){
            parentPath += ElFinderConstants.ELFINDER_PARAMETER_FILE_SEPARATOR;
        }
        this.target = volume.fromPath(parentPath + name);
        this.volumeSecurity = elfinderStorage.getVolumeSecurity(target);
    }

    public void createFile() throws IOException {
        volume.createFile(target);
    }

    public void createFolder() throws IOException {
        if(volume.getSource().equalsIgnoreCase(VolumeSources.ALIYUNOSS.name())){
            fixAliyunOSS((AliyunOssFileSystemVolume)volume);
        }
        volume.createFolder(target);
    }

    private void fixAliyunOSS(AliyunOssFileSystemVolume aliyunOssFileSystemVolume) throws IOException {
        //hack 阿里云没有文件夹的概念，模拟的文件夹必须以/结尾，所以单独处理一下
        String folderName = volume.getPath(target);
        folderName = aliyunOssFileSystemVolume.getAliyunOssService().fixOssFolderName(folderName);
        this.target = volume.fromPath(folderName);
        this.volumeSecurity = elfinderStorage.getVolumeSecurity(target);
    }

    public void createAndCopy(Target src, Target dst) throws IOException {
        volume.createAndCopy(src, dst);
        /*if(dst.getVolume().getSource().equalsIgnoreCase(ALIYUNOSS.name())){
            fixAliyunOSS();
        }*/
    }

    public void delete() throws IOException {
        if (volume.isFolder(target)) {
            volume.deleteFolder(target);
        } else {
            volume.deleteFile(target);
        }
    }

    public boolean exists() {
        return volume.exists(target);
    }

    public String getHash() throws IOException {
        return elfinderStorage.getHash(target);
    }

    public long getLastModified() throws IOException {
        return volume.getLastModified(target);
    }

    public String getMimeType() throws IOException {
        return volume.getMimeType(target);
    }

    public String getName() {
        return volume.getName(target);
    }

    public String getCsscls() {
        return volume.getCsscls(target);
    }

    public String getExternalUrl() {
        return volume.getExternalUrl(target);
    }

    public String[] getDisabledCmds() {
        return volume.getDisabledCmds(target);
    }

    public VolumeHandler getParent() {
        return new VolumeHandler(volume.getParent(target), elfinderStorage);
    }

    public long getSize() throws IOException {
        return volume.getSize(target);
    }

    public String getVolumeId() {
        return elfinderStorage.getVolumeId(volume);
    }

    public boolean hasChildFolder() throws IOException {
        return volume.hasChildFolder(target);
    }

    public boolean isFolder() {
        return volume.isFolder(target);
    }

    public boolean isLocked() {
        return this.volumeSecurity.getSecurityConstraint().isLocked();
    }

    public boolean isReadable() {
        return this.volumeSecurity.getSecurityConstraint().isReadable();
    }

    public boolean isWritable() {
        return this.volumeSecurity.getSecurityConstraint().isWritable();
    }

    public boolean isRoot() throws IOException {
        return volume.isRoot(target);
    }

    public List<VolumeHandler> listChildren() throws IOException {
        List<VolumeHandler> list = new ArrayList<>();
        for (Target child : volume.listChildren(target)) {
            list.add(new VolumeHandler(child, elfinderStorage));
        }
        return list;
    }

    public InputStream openInputStream() throws IOException {
        return volume.openInputStream(target);
    }

    public void renameTo(VolumeHandler dst) throws IOException {
        volume.rename(target, dst.target);
    }

    public String getVolumeAlias() {
        return volume.getAlias();
    }

    public Volume getVolume() {
        return volume;
    }

    public Target getTarget() {
        return target;
    }

    public VolumeSecurity getVolumeSecurity() {
        return volumeSecurity;
    }

    public ElfinderStorage getElfinderStorage() {
        return elfinderStorage;
    }

}
