package cn.yours.elfinder.obs;


import java.util.List;
import java.util.Objects;
import java.util.Observer;

/**
 * 文件命令事件触发者
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-09-09 15:14
 */
public class ElfinderCmdObserved extends AbstractCmdObserved {

	private List<Observer> list;

	public ElfinderCmdObserved() {
	}

	public ElfinderCmdObserved(List<Observer> list) {
		this.list = list;
		if(Objects.nonNull(list) && !list.isEmpty()){
			list.forEach(this::addObserver);
		}
	}

	public List<Observer> getList() {
		return list;
	}

	public void setList(List<Observer> list) {
		this.list = list;
	}
}
