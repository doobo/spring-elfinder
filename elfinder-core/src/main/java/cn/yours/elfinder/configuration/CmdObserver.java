package cn.yours.elfinder.configuration;

import cn.yours.elfinder.param.ObServerVO;

import java.util.Observable;
import java.util.Observer;

/**
 * 命名执行后的观察者
 */
public abstract class CmdObserver implements Observer {

    /**
     * 匹配规则,返回true才执行handleObserver方法
     */
    public boolean matching(ObServerVO vo){
        return Boolean.TRUE;
    }
    
    @Override
    public void update(Observable o, Object arg) {
        if(!(o instanceof CmdObserved) || !(arg instanceof ObServerVO)){
            return;
        }
        ObServerVO vo = (ObServerVO) arg;
        if(matching(vo)) {
            handleObserver(vo);
        }
    }
    
    public abstract void handleObserver(ObServerVO vo);
}
