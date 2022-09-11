package cn.yours.web;

import cn.yours.elfinder.obs.AbstractCmdObserver;
import cn.yours.elfinder.obs.ObServerRequest;
import org.springframework.stereotype.Component;

import java.util.Observable;

/**
 * 命名执行后观察者测试
 */
@Component
public class UploadCmdObserver extends AbstractCmdObserver {

    @Override
    public boolean matching(ObServerRequest vo) {
        return "upload".equals(vo.getCmd());
    }

    @Override
    public void executor(ObServerRequest vo, Observable o) {
        System.out.println(vo.getResult().toString());
    }
}
