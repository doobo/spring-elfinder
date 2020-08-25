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
    
    private static CmdObserved INSTANCE;
    
    private static boolean IS_OBSERVE;

    @Autowired(required = false)
    private List<Observer> observerList;
    
    @Autowired
    private CmdObserved observed;

    @PostConstruct
    public void observerRegister() {
        if(observerList != null && !observerList.isEmpty()) {
            observerList.forEach(this::addObserver);
            IS_OBSERVE = true;
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

    /**
     * 获取被观察者实例
     */
    public static CmdObserved getInstance(){
        return INSTANCE;
    }

    /**
     * 是否有观察者
     */
    public static boolean isObserver(){
        return IS_OBSERVE;
    }
}
