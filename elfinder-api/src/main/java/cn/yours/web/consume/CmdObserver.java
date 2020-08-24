package cn.yours.web.consume;

import cn.yours.elfinder.configuration.CmdObserved;
import cn.yours.elfinder.param.ObServerVO;

import java.util.Observable;
import java.util.Observer;

/**
 * cmd执行后处理
 */
public abstract class CmdObserver implements Observer {
    
    @Override
    public void update(Observable o, Object arg) {
        if(!(o instanceof CmdObserved) || !(arg instanceof ObServerVO)){
            return;
        }
        ObServerVO vo = (ObServerVO) arg;
        handleObserver(vo);
    }
    
    public abstract void handleObserver(ObServerVO vo);
}
