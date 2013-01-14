package eFrame.container;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import eFrame.container.base.BaseClassContainer;

/**
 * 简单容器实现类。
 * 存放容器用一个简单的map
 * @date 2012-12-24
 * @author LiangRL
 * @alias E.E.
 */
public class ClassContainer extends BaseClassContainer{
	
	/** 
	 * <br>简单容器, 
	 * <br>key是bean的名字， value是bean的类型信息
	 * <br>这里只存放类型信息
	 * */
	private Map<String, Class<?>> classContainer = new ConcurrentHashMap<String, Class<?>>();
	
	private ClassContainer(){}

	private static ClassContainer instance;
	
	public static synchronized BaseClassContainer getInstance(){
		if(instance==null){
			instance = new ClassContainer();
		}
		return instance;
	}
	
	/**
	 * 设置类型信息
	 * 父类的方法setBeans 
	 * 需要调用这个之类实现了的方法。
	 * @param name
	 * @param obj
	 */
	public void setClass(String name, Class<?> obj){
		if(classContainer.containsKey(name)){
			throw new RuntimeException("set bean error! bean name:'"+name+"' already exitst！");
		}else{
			classContainer.put(name, obj);			
		}
	}
	
	/**
	 * 获取类型信息
	 * @param name
	 * @return
	 */
	public Class<?> getClass(String name){
		return classContainer.get(name);
	}

	/**
	 * 取得所有已加载的类型信息
	 */
	@Override
	public Map<String, Class<?>> getClasses() {
		return classContainer;
	}
}
