package cn.yours.elfinder.param;

public class Node {
    private String source;
    private String alias;
    private String path;
    private Boolean isDefault;
    private String locale;
    private Constraint constraint;
    private AliyunDriverConfig aliyunDriverConfig;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Constraint getConstraint() {
        return constraint;
    }

    public void setConstraint(Constraint constraint) {
        this.constraint = constraint;
    }

    public AliyunDriverConfig getAliyunDriverConfig() {
        return aliyunDriverConfig;
    }

    public void setAliyunDriverConfig(AliyunDriverConfig aliyunDriverConfig) {
        this.aliyunDriverConfig = aliyunDriverConfig;
    }
}
