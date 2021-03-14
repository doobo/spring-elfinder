package cn.yours.elfinder.param;

import cn.yours.elfinder.service.ElfinderStorage;
import cn.yours.elfinder.service.VolumeHandler;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.Map;

import static cn.yours.elfinder.command.AbstractCommand.findTarget;

/**
 * 广播信息载体
 */
public class ObServerVO implements Serializable {

    /**
     * 命令类型
     */
    private String cmd;

    /**
     * 目标目录
     */
    private String target;

    /**
     * 执行结构
     */
    private JSONObject result;

    /**
     * 存储库信息
     */
    private ElfinderStorage elfinderStorage;

    /**
     * request请求入参
     */
    private Map<String, String[]> params;
    
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

    public ElfinderStorage getElfinderStorage() {
        return elfinderStorage;
    }

    public ObServerVO setElfinderStorage(ElfinderStorage elfinderStorage) {
        this.elfinderStorage = elfinderStorage;
        return this;
    }

    /**
     * 通过target的code获取文件信息
     * @param code
     */
    public VolumeHandler getVolumeHandlerByHash(String code){
        return findTarget(elfinderStorage, code);
    }

    public Map<String, String[]> getParams() {
        return params;
    }

    public ObServerVO setParams(Map<String, String[]> params) {
        this.params = params;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public ObServerVO setTarget(String target) {
        this.target = target;
        return this;
    }
}
