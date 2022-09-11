package cn.yours.elfinder.core.impl.aliyunoss;

import cn.yours.elfinder.obs.ElfinderConfigurationUtils;
import cn.yours.elfinder.core.Target;
import cn.yours.elfinder.core.Volume;
import cn.yours.elfinder.ElFinderConstants;
import com.aliyun.oss.model.OSSObjectSummary;
import java.io.IOException;
import java.nio.file.Paths;

public class AliyunOssFileSystemTarget implements Target {

    private final OSSObjectSummary ossObject;
    private final Volume volume;
    private final AliyunOssService aliyunOssService;

    public AliyunOssFileSystemTarget(AliyunOssFileSystemVolume volume, OSSObjectSummary ossObject) {
        this.ossObject = ossObject;
        this.volume = volume;
        this.aliyunOssService = volume.getAliyunOssService();
    }

    @Override
    public Volume getVolume() {
        return volume;
    }

    public String getKey() {
        return this.ossObject.getKey();
    }

    public String getName() {
        if(isRoot()){
            return "";
        }
        String name = Paths.get(ElfinderConfigurationUtils.toURI(getKey())).getFileName().toString();
        return name;
    }

    public String getCsscls(Target target) {
        if(isRoot()){
            return "elfinder-navbar-root-aliyun";
        }
        return null;
    }

    public String[] getDisabledCmds(Target target) {
        return new String[]{"archive", "rename"};
    }

    public long getSize(){
        return ossObject.getSize();
    }

    public long getLastModified(){
        return ossObject.getLastModified().getTime();
    }

    public boolean isFolder(){
        return this.aliyunOssService.isFolder(ossObject.getKey());
    }

    public boolean isRoot(){
        return this.aliyunOssService.isRoot(ossObject.getKey());
    }

    public void delete() throws IOException {
        if(isFolder()){
            this.aliyunOssService.deleteFolder(getBucketName(), getKey());
        } else {
            this.aliyunOssService.deleteFile(getBucketName(), getKey());
        }
    }

    public String getBucketName() {
        return ((AliyunOssFileSystemVolume)volume).getBucketName();
    }

    public String getParentPath() {
        String parent = Paths.get(ElfinderConfigurationUtils.toURI(getKey())).getParent().toString();
        if(parent.equalsIgnoreCase(ElFinderConstants.ELFINDER_PARAMETER_FILE_SEPARATOR)){
            return "";
        }

        parent = this.aliyunOssService.fixOssFolderName(parent);

        return parent;
    }

    public boolean exists() {
        boolean exists = this.aliyunOssService.exists(getBucketName(), getKey());
        return exists;
    }

    public String getMimeType() throws IOException {
        if (isFolder()) {
            return "directory";
        }
        return this.aliyunOssService.getMimeType(getBucketName(), getKey());
    }

}
