package cn.yours.elfinder.core.impl.aliyunoss;

import static cn.yours.elfinder.service.VolumeSources.ALIYUNOSS;

import cn.yours.elfinder.core.Target;
import cn.yours.elfinder.core.Volume;
import cn.yours.elfinder.core.VolumeBuilder;
import cn.yours.elfinder.param.AliyunDriverConfig;
import cn.yours.elfinder.param.Node;
import cn.yours.elfinder.ElFinderConstants;
import com.aliyun.oss.model.OSSObjectSummary;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aliyun OSS Volume Implementation.
 *
 * @author Van
 */
public class AliyunOssFileSystemVolume implements Volume {

    private static final Logger logger = LoggerFactory.getLogger(AliyunOssFileSystemVolume.class);

    private final String alias;
    private final String source;
    private final String bucketName;
    private final AliyunDriverConfig aliyunDriverConfig;
    private final Target rootTarget;
    private final AliyunOssService aliyunOssService;

    private AliyunOssFileSystemVolume(Builder builder, Node nodeConfig) {

        this.aliyunDriverConfig = nodeConfig.getAliyunDriverConfig();
        if(aliyunDriverConfig == null) {
            throw new RuntimeException("Please config your aliyun driver");
        }

        this.aliyunOssService = new AliyunOssService(aliyunDriverConfig.getEndpoint(),aliyunDriverConfig.getAccessKeyId(),aliyunDriverConfig.getAccessKeySecret());
        this.alias = builder.alias;
        this.bucketName = builder.bucketName;
        this.rootTarget = new AliyunOssFileSystemTarget(this, this.aliyunOssService.getRootOSSObjectSummary(bucketName));
        this.source = ALIYUNOSS.name();

        checkBucket();
    }

    public AliyunOssService getAliyunOssService() {
        return this.aliyunOssService;
    }

    public String getBucketName() {
        return this.bucketName;
    }

    private void checkBucket() {
        if(!this.aliyunOssService.isBucketExists(bucketName)){
            throw new RuntimeException("Unable to create root dir folder");
        }
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public void createFile(Target target) throws IOException {
        this.aliyunOssService.createFile(getBucketName(), getPath(target));
    }

    @Override
    public void createFolder(Target target) throws IOException {
        String path = getPath(target);
        path = this.aliyunOssService.fixOssFolderName(path);
        this.aliyunOssService.createFile(getBucketName(), path);
    }

    @Override
    public void deleteFile(Target target) throws IOException {
        ((AliyunOssFileSystemTarget)target).delete();
    }

    @Override
    public void deleteFolder(Target target) throws IOException {
        ((AliyunOssFileSystemTarget)target).delete();
    }

    @Override
    public boolean exists(Target target) {
        boolean exists = ((AliyunOssFileSystemTarget)target).exists();
        return exists;
    }

    @Override
    public Target fromPath(String ossKey) {
        if(ossKey.startsWith(ElFinderConstants.ELFINDER_PARAMETER_FILE_SEPARATOR)){
            ossKey = ossKey.substring(1);
        }
        if(ossKey.length() == 0){
            return this.rootTarget;
        }
        OSSObjectSummary ossObjectSummary = this.aliyunOssService.getOssObjectSummary(bucketName, ossKey);
        AliyunOssFileSystemTarget target = new AliyunOssFileSystemTarget(this, ossObjectSummary);
        return target;
    }

    public Target fromOSSObjectSummary(OSSObjectSummary ossObjectSummary) {
        AliyunOssFileSystemTarget target = new AliyunOssFileSystemTarget(this, ossObjectSummary);
        return target;
    }

    @Override
    public long getLastModified(Target target) throws IOException {
        return ((AliyunOssFileSystemTarget)target).getLastModified();
    }

    @Override
    public String getMimeType(Target target) throws IOException {
        return ((AliyunOssFileSystemTarget)target).getMimeType();
    }

    @Override
    public String getName(Target target) {
        return ((AliyunOssFileSystemTarget)target).getName();
    }

    @Override
    public String getCsscls(Target target) {
        return ((AliyunOssFileSystemTarget)target).getCsscls(target);
    }

    @Override
    public String getExternalUrl(Target target) {
        String protocol = "https://";
        if(aliyunDriverConfig.getBindedDomain() != null){
            return protocol + aliyunDriverConfig.getBindedDomain() + ElFinderConstants.ELFINDER_PARAMETER_FILE_SEPARATOR + ((AliyunOssFileSystemTarget)target).getKey();
        } else {
            return protocol + bucketName + "." + aliyunDriverConfig.getEndpoint() + ElFinderConstants.ELFINDER_PARAMETER_FILE_SEPARATOR + ((AliyunOssFileSystemTarget)target).getKey();
        }
    }

    @Override
    public String[] getDisabledCmds(Target target) {
        return ((AliyunOssFileSystemTarget)target).getDisabledCmds(target);
    }

    @Override
    public Target getParent(Target target) {
        String parentPath = ((AliyunOssFileSystemTarget)target).getParentPath();
        Target parent = fromPath(parentPath);
        return parent;
    }

    @Override
    public String getPath(Target target) throws IOException {
        return ((AliyunOssFileSystemTarget)target).getKey();
    }

    @Override
    public Target getRoot() {
        return rootTarget;
    }

    @Override
    public long getSize(Target target) throws IOException {
        return ((AliyunOssFileSystemTarget)target).getSize();
    }

    @Override
    public boolean isFolder(Target target) {
        try {
            return this.aliyunOssService.isFolder(getPath(target));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isRoot(Target target) {
        return ((AliyunOssFileSystemTarget)target).isRoot();
    }

    @Override
    public boolean hasChildFolder(Target target) throws IOException {
        return this.aliyunOssService.hasChildFolder(bucketName, getPath(target));
    }

    @Override
    public Target[] listChildren(Target target) throws IOException {

        List<OSSObjectSummary> childrenResultList = this.aliyunOssService.listChildren(bucketName, getPath(target));
        List<Target> targets = new ArrayList<>(childrenResultList.size());
        for (OSSObjectSummary ossObjectSummary : childrenResultList) {
            targets.add(fromPath(ossObjectSummary.getKey()));
        }
        return targets.toArray(new Target[targets.size()]);
    }

    @Override
    public InputStream openInputStream(Target target) throws IOException {
        return this.aliyunOssService.openInputStream(bucketName, getPath(target));
    }

    @Override
    public void rename(Target origin, Target destination) throws IOException {
        //AliyunOssHelper.rename(getPath(origin), getPath(destination));
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Target> search(String target) throws IOException {
        List<Target> targets = new ArrayList<>(0);
        return Collections.unmodifiableList(targets);
    }

    @Override
    public void createAndCopy(Target src, Target dst) throws IOException {
        if (src.getVolume().isFolder(src)) {
            createAndCopyFolder(src, dst);
        } else {
            createAndCopyFile(src, dst);
        }
    }

    private void createAndCopyFile(Target src, Target dst) throws IOException {
        InputStream is = src.getVolume().openInputStream(src);
        putFile(dst, is);
        is.close();
    }

    private void createAndCopyFolder(Target src, Target dst) throws IOException {
        dst.getVolume().createFolder(dst);

        String dstPath = dst.getVolume().getPath(dst);
        dstPath = this.aliyunOssService.fixOssFolderName(dstPath);
        for (Target c : src.getVolume().listChildren(src)) {
            createAndCopy(c, dst.getVolume().fromPath(dstPath + c.getVolume().getName(c)));
        }
    }

    @Override
    public void putFile(Target target, String content, String encoding) {
        try {
            deleteFile(target);
            this.aliyunOssService.createFile(bucketName, getPath(target), content.getBytes(encoding));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void putFile(Target target, InputStream inputStream) {
        try {
            deleteFile(target);
            this.aliyunOssService.createFile(bucketName, getPath(target), inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a Builder for creating a new AliyunOssFileSystemVolume instance.
     *
     * @return a new Builder for AliyunOssFileSystemVolume.
     */
    public static Builder builder(String alias, String bucketName, Node nodeConfig) {
        return new AliyunOssFileSystemVolume.Builder(alias, bucketName, nodeConfig);
    }

    public static class Builder implements VolumeBuilder<AliyunOssFileSystemVolume> {
        // required fields
        private final String alias;
        private final String bucketName;
        private final Node nodeConfig;

        public Builder(String alias, String bucketName, Node nodeConfig) {
            this.alias = alias;
            this.bucketName = bucketName;
            this.nodeConfig = nodeConfig;
        }

        @Override
        public AliyunOssFileSystemVolume build() {
            return new AliyunOssFileSystemVolume(this, nodeConfig);
        }
    }

}