package cn.yours.web.config;

import cn.yours.elfinder.param.Node;
import cn.yours.elfinder.param.Thumbnail;

import java.util.List;

public class ElfinderConfiguration {
    
    private Boolean start;

    private Thumbnail thumbnail;

    private List<Node> volumes;

    /**
     * 默认不限制
     */
    private Long maxUploadSize = -1L;

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<Node> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<Node> volumes) {
        this.volumes = volumes;
    }

    public Long getMaxUploadSize() {
        return maxUploadSize;
    }

    public void setMaxUploadSize(Long maxUploadSize) {
        this.maxUploadSize = maxUploadSize;
    }

    public Boolean getStart() {
        return start;
    }

    public void setStart(Boolean start) {
        this.start = start;
    }
}
