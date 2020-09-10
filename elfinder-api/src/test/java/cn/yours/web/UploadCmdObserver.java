package cn.yours.web;

import cn.yours.elfinder.configuration.CmdObserver;
import cn.yours.elfinder.param.ObServerVO;
import org.springframework.stereotype.Component;

/**
 * 命名执行后观察者测试
 */
@Component
public class UploadCmdObserver extends CmdObserver {
    @Override
    public void handleObserver(ObServerVO vo) {
        System.out.println(vo.getResult().toString());
    }
}
