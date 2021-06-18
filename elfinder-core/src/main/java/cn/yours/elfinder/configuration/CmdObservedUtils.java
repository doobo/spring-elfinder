package cn.yours.elfinder.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 被观察者工具类
 */
@Component
public class CmdObservedUtils {
    
    private static CmdObserved INSTANCE;
    
    /**
     * 获取被观察者实例
     */
    public static CmdObserved getInstance(){
        if(INSTANCE == null){
            throw new IllegalArgumentException("CmdObserved is Undefined");
        }
        return INSTANCE;
    }

    @Autowired
    public void setObserved(CmdObserved observed) {
        INSTANCE = observed;
    }
}
