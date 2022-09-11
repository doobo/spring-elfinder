package cn.yours.elfinder.factory;


import cn.yours.elfinder.obs.AbstractCmdObserver;
import cn.yours.elfinder.obs.ElfinderCmdObserved;
import cn.yours.elfinder.obs.ObServerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Observer;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 平台观察者模型工厂
 *
 * @Description: elfinder-core
 * @User: doobo
 * @Time: 2022-09-09 13:30
 */
@Component
public class ElfinderObservedFactory {

    /**
     * 导入处理器列表
     */
    private static List<Observer> handlerList;

	/**
	 * 平台消息触发器
	 */
	private static ElfinderCmdObserved observed;

    /**
     * 添加处理器
     */
    public static synchronized void addHandler(Observer handler){
        if(Objects.isNull(handlerList)){
            handlerList = new CopyOnWriteArrayList<>();
        }
        if(Objects.nonNull(handler) && handler instanceof AbstractCmdObserver){
            handlerList.add(handler);
        }
    }

    /**
     * 添加理器列表
     */
    public static synchronized void addHandlerList(List<Observer> handlers){
        if(Objects.isNull(handlerList)){
            handlerList = new CopyOnWriteArrayList<>();
        }
        if(Objects.nonNull(handlers) && !handlers.isEmpty()){
			List<Observer> collect = handlers.stream().filter((f) -> f instanceof AbstractCmdObserver).collect(Collectors.toList());
			if(!collect.isEmpty()){
				handlerList.addAll(collect);
			}
        }
    }

    /**
     * 移除指定类型的处理器
     */
    public static synchronized <T> boolean removeHandler(Class<T> cls){
		Observer handler = null;
        for(Observer item : handlerList){
            if(Objects.nonNull(item) && item.getClass().getName().equals(cls.getName())){
                handler = item;
                break;
            }
        }
        if(Objects.nonNull(handler)){
			if(handler instanceof AbstractCmdObserver){
				((AbstractCmdObserver) handler).setLeave(true);
			}
            return handlerList.remove(handler);
        }
        return false;
    }

    /**
     * 获取处理器
     */
    public static AbstractCmdObserver getInstanceHandler(ObServerRequest request){
        if(isEmpty(handlerList)){
            return null;
        }
        for(Observer item : handlerList){
            if (item instanceof AbstractCmdObserver ){
				AbstractCmdObserver platformObserver = (AbstractCmdObserver) item;
				if(matching(request, platformObserver::matching)){
					return platformObserver;
				}
            }
        }
        return null;
    }

    /**
     * 匹配执行器
     */
    private static boolean matching(ObServerRequest request, Predicate<ObServerRequest> predicate){
        return predicate.test(request);
    }

    public static List<Observer> getHandlerList() {
        return handlerList;
    }

	/**
	 * 初始化事件触发者
	 */
	@Bean
	@ConditionalOnMissingBean(ElfinderCmdObserved.class)
	public ElfinderCmdObserved ElfinderCmdObserved(@Autowired(required = false) List<Observer> list) {
		ElfinderObservedFactory.addHandlerList(list);
		ElfinderObservedFactory.observed = new ElfinderCmdObserved(getHandlerList());
		return ElfinderObservedFactory.observed;
	}

    public static ElfinderCmdObserved getObserved() {
        return observed;
    }

    public static void setObserved(ElfinderCmdObserved observed) {
        ElfinderObservedFactory.observed = observed;
    }

    public static boolean isEmpty(Collection<?> coll) {
		return coll == null || coll.isEmpty();
	}

	/**
	 * SPI注册所有实现类
	 */
	public static synchronized void registerHandlerList() {
		ServiceLoader<Observer> filtersImplements = ServiceLoader.load(Observer.class);
		List<Observer> receiptHandlerList = new ArrayList<>();
		//把找到的所有的Filter的实现类放入List中
		for (Observer filtersImplement : filtersImplements) {
			receiptHandlerList.add(filtersImplement);
		}
		if(isEmpty(receiptHandlerList)){
			return;
		}
		addHandlerList(receiptHandlerList);
	}

	//注册处理器
	static {
		registerHandlerList();
	}
}
