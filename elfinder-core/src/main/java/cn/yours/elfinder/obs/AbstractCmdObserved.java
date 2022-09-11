package cn.yours.elfinder.obs;

import java.util.Observable;

/**
 * 被观察抽象类(可观察),事件触发者
 */
public abstract class AbstractCmdObserved extends Observable {

    public synchronized void notifyChange(ObServerRequest request) {
        this.setChanged();
        this.notifyObservers(request);
    }
}
