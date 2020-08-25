package cn.yours.elfinder.param;

import cn.yours.elfinder.service.VolumeHandler;
import org.json.JSONObject;
import java.io.Serializable;

/**
 * 广播信息载体
 */
public class ObServerVO implements Serializable {

    /**
     * 命令类型
     */
    private String cmd;

    /**
     * 执行结构
     */
    private JSONObject result;

    /**
     * 添加目录信息
     */
    private VolumeHandler path;
    
    public ObServerVO() {
    }

    public JSONObject getResult() {
        return result;
    }

    public ObServerVO setResult(JSONObject result) {
        this.result = result;
        return this;
    }

    public String getCmd() {
        return cmd;
    }

    public ObServerVO setCmd(String cmd) {
        this.cmd = cmd;
        return this;
    }

    public VolumeHandler getPath() {
        return path;
    }

    public ObServerVO setPath(VolumeHandler path) {
        this.path = path;
        return this;
    }
}
