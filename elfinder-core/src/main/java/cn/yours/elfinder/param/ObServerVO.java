package cn.yours.elfinder.param;

import org.json.JSONObject;

/**
 * 广播信息载体
 */
public class ObServerVO {

    /**
     * 命令类型
     */
    private String cmd;

    /**
     * 执行结构
     */
    private JSONObject result;
    
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
}
