package cn.yours.elfinder.configuration;

import cn.yours.elfinder.param.ObServerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 被观察者
 */
@Component
public class CmdObserved extends Observable {
    
    public static CmdObserved INSTANCE;

    @Autowired(required = false)
    private List<Observer> observerList;
    
    @Autowired
    private CmdObserved observed;

    @PostConstruct
    public void observerRegister() {
        if(observerList != null && !observerList.isEmpty()) {
            observerList.forEach(this::addObserver);
        }
        INSTANCE = observed;
    }

    /**
     * 广播信息
     * @param vo
     */
    public void sendCmdResult(ObServerVO vo){
        this.setChanged();
        this.notifyObservers(vo);
    }

}
