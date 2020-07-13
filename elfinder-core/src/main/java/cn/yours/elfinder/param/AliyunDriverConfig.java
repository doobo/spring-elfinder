package cn.yours.elfinder.param;


public class AliyunDriverConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bindedDomain;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBindedDomain() {
        return bindedDomain;
    }

    public void setBindedDomain(String bindedDomain) {
        this.bindedDomain = bindedDomain;
    }
}
