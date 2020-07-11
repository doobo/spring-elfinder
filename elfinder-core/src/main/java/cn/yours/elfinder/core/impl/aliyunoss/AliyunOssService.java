package cn.yours.elfinder.core.impl.aliyunoss;

import static cn.yours.elfinder.ElFinderConstants.ELFINDER_PARAMETER_FILE_SEPARATOR;
import static cn.yours.elfinder.support.locale.LocaleUtils.isEmpty;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.internal.OSSUtils;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for NIO file system operations.
 *
 * @author Van
 */
public final class AliyunOssService {

    private static final Logger logger = LoggerFactory.getLogger(AliyunOssService.class);

    private volatile OSS client;

    private volatile static OSSClientBuilder ossClientBuilder;

    public AliyunOssService(String endpoint, String accessKeyId, String accessKeySecret) {
        this.client = getOSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
    }

    private static OSSClientBuilder getOSSClientBuilder(){
        if(ossClientBuilder == null){
            synchronized(AliyunOssService.class){
                if(ossClientBuilder==null){
                    ossClientBuilder = new OSSClientBuilder();
                }
            }
        }
        return ossClientBuilder;
    }

    public boolean isBucketExists(String bucketName) {
        boolean exists = client.doesBucketExist(bucketName);
        return exists;
    }

    public boolean isFolder(String path){
        boolean isFolder = path.endsWith(ELFINDER_PARAMETER_FILE_SEPARATOR);
        return isRoot(path) || isFolder;
    }

    public boolean isRoot(String path){
        boolean isRoot = path.length() == 0;
        return isRoot;
    }

    public void createBucket(String bucketName) {
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
        client.createBucket(createBucketRequest);
    }

    public void createFile(String bucketName, String objectName) throws IOException {
        createFile(bucketName, objectName, new byte[0]);
    }

    public void createFile(String bucketName, String objectName, byte[] content) throws IOException {
        testName(objectName);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new ByteArrayInputStream(content));
        client.putObject(putObjectRequest);
    }

    public void createFile(String bucketName, String objectName, InputStream inputStream) throws IOException {
        testName(objectName);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
        client.putObject(putObjectRequest);
    }

    public void deleteFile(String bucketName, String objectName) throws IOException {
        client.deleteObject(bucketName, objectName);
    }

    public void deleteFolder(String bucketName, String objectName) throws IOException {
        client.deleteObject(bucketName, objectName);

        // 列举所有包含指定前缀的文件并删除。
        String nextMarker = null;
        ObjectListing objectListing = null;
        do {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName)
                .withPrefix(objectName)
                .withMarker(nextMarker);

            objectListing = client.listObjects(listObjectsRequest);
            if (objectListing.getObjectSummaries().size() > 0) {
                List<String> keys = new ArrayList<String>();
                for (OSSObjectSummary s : objectListing.getObjectSummaries()) {
                    keys.add(s.getKey());
                }
                logger.info("delete folder key name: " + keys);
                DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName).withKeys(keys);
                client.deleteObjects(deleteObjectsRequest);
            }

            nextMarker = objectListing.getNextMarker();
        } while (objectListing.isTruncated());

    }

    public boolean exists(String bucketName, String objectName) {
        boolean found = client.doesObjectExist(bucketName, objectName);
        return found;
    }

    public String getMimeType(String bucketName, String objectName) {
        //logger.info("getMimeType objectName: "+objectName);
        ObjectMetadata metadata = getMeta(bucketName, objectName);
        if (metadata != null) {
            return metadata.getContentType();
        }
        return null;
    }

    private ObjectMetadata getMeta(String bucketName, String objectName) {
        ObjectMetadata metadata = null;

        //logger.info(objectName);
        try {
            metadata = client.getObjectMetadata(bucketName, objectName);
        } catch (OSSException e) {
            logger.error("aliyun bucket "+ bucketName + " can not find object "+ objectName);
            //e.printStackTrace();
        } catch (ClientException e) {
            logger.error(e.getMessage());
            //e.printStackTrace();
        }

        return metadata;
    }

    public long getTotalSizeInBytes(String bucketName, String objectName) {
        ObjectMetadata metadata = getMeta(bucketName, objectName);
       return metadata.getContentLength();
    }

    public boolean hasChildFolder(String bucketName, String prefix) throws IOException {
        // MaxKey默认值为100，最大值为1000。
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
        listObjectsRequest.setDelimiter(ELFINDER_PARAMETER_FILE_SEPARATOR);
        listObjectsRequest.setPrefix(prefix);
        listObjectsRequest.setMaxKeys(2);
        ObjectListing objectListing = client.listObjects(listObjectsRequest);

        //找文件夹
        List<String> folders = objectListing.getCommonPrefixes();
        return folders.size() > 0;
    }

    public InputStream openInputStream(String bucketName, String objectName) throws IOException {
        OSSObject ossObject = client.getObject(bucketName, objectName);
        return ossObject.getObjectContent();
    }

    public List<OSSObjectSummary> listChildren(String bucketName, String prefix) throws IOException {

        // 列举文件。 如果不设置KeyPrefix，则列举存储空间下所有的文件。KeyPrefix，则列举包含指定前缀的文件。
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
        listObjectsRequest.setDelimiter(ELFINDER_PARAMETER_FILE_SEPARATOR);
        listObjectsRequest.setPrefix(prefix);
        ObjectListing objectListing = client.listObjects(listObjectsRequest);

        List<OSSObjectSummary> list = new ArrayList<>();

        //先找文件夹
        List<String> folders = objectListing.getCommonPrefixes();
        List<String> folderList = new ArrayList<>();
        for (String folder : folders) {
            //比如aaa/目录下会返回 /aaa// 和 /aaa/bbb/两个目录，需要去掉
            if(!folder.equalsIgnoreCase(prefix + ELFINDER_PARAMETER_FILE_SEPARATOR)){
                folderList.add(folder);
            }
        }
        for(String folder : folderList){
            list.add(getOssObjectSummary(bucketName, folder));
        }
        //logger.info(list.toString());
        //再找文件
        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
        for (OSSObjectSummary s : sums) {
            //会把本身的目录也当成文件列出来，需要去掉.
            String key = s.getKey();
            if(!key.equalsIgnoreCase(prefix)){
                list.add(s);
            }
        }
        //logger.info(list.toString());
        return Collections.unmodifiableList(list);
    }

    private OSSObjectSummary createObjectSummary(String bucketName, String key, long size){
        OSSObjectSummary ossObjectSummary = new OSSObjectSummary();
        ossObjectSummary.setBucketName(bucketName);
        ossObjectSummary.setSize(size);
        ossObjectSummary.setKey(key);
        ossObjectSummary.setLastModified(new Date());
        return ossObjectSummary;
    }

    public OSSObjectSummary getRootOSSObjectSummary(String bucketName){
        //return createObjectSummary(bucketName, "", calculate(bucketName, ""));
        //优化性能
        return createObjectSummary(bucketName, "", 0);
    }

    public OSSObjectSummary getOssObjectSummary(String bucketName, String key){

        if(isFolder(key)){
            //return createObjectSummary(bucketName, key, calculate(bucketName, key));
            //优化性能
            return createObjectSummary(bucketName, key, 0);
        }

        ObjectMetadata objectMetadata = getMeta(bucketName, key);
        if(objectMetadata == null){
            return createObjectSummary(bucketName, key, calculate(bucketName, key));
        }

        OSSObjectSummary ossObjectSummary = new OSSObjectSummary();
        ossObjectSummary.setBucketName(bucketName);
        ossObjectSummary.setSize(objectMetadata.getContentLength());
        ossObjectSummary.setKey(key);
        ossObjectSummary.setLastModified(objectMetadata.getLastModified());

        return ossObjectSummary;
    }

    // 获取bucket下某个文件夹大小。
    private long calculateFolderLength(String bucketName, String folder) {
        long size = 0L;
        ObjectListing objectListing = null;
        do {
            // MaxKey默认值为100，最大值为1000。
            ListObjectsRequest request = new ListObjectsRequest(bucketName).withPrefix(folder).withMaxKeys(1000);
            if (objectListing != null) {
                request.setMarker(objectListing.getNextMarker());
            }
            objectListing = client.listObjects(request);
            List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
            for (OSSObjectSummary s : sums) {
                size += s.getSize();
            }
        } while (objectListing.isTruncated());
        return size;
    }

    public long calculate(String bucketName, String keyPrefix) {
        long size = 0L;
        // 指定前缀，若希望遍历主目录文件夹，则将该值置空。
        //final String keyPrefix = "";
        ObjectListing objectListing = null;
        do {
            // 默认情况下，每次列举100个文件或目录。
            ListObjectsRequest request = new ListObjectsRequest(bucketName).withDelimiter("/").withPrefix(keyPrefix);
            if (objectListing != null) {
                request.setMarker(objectListing.getNextMarker());
            }
            objectListing = client.listObjects(request);
            List<String> folders = objectListing.getCommonPrefixes();
            for (String folder : folders) {
                long ss = calculateFolderLength(bucketName, folder);
                //System.out.println(folder + " : " + (ss / 1024) + "KB");
                size += ss;
            }
            List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
            for (OSSObjectSummary s : sums) {
                long ss = s.getSize();
                //System.out.println(s.getKey() + " : " + (ss / 1024) + "KB");
                size += ss;
            }
        } while (objectListing.isTruncated());

        return size;
        //getOSSClient().shutdown();
    }

    private void testName(String objectName){
        OSSUtils.ensureObjectKeyValid(objectName);
    }

    public String fixOssFolderName(String folderName){
        if(isEmpty(folderName)){
            return folderName;
        }
        if(!folderName.endsWith(ELFINDER_PARAMETER_FILE_SEPARATOR)){
            folderName += ELFINDER_PARAMETER_FILE_SEPARATOR;
        }
        if(folderName.startsWith(ELFINDER_PARAMETER_FILE_SEPARATOR)){
            folderName = folderName.substring(1);
        }
        return folderName;
    }
}
