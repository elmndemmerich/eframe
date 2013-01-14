package eFrame.container;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import eFrame.annotations.Wired;
import eFrame.annotations.db.TableBean;
import eFrame.container.base.BaseClassContainer;
import eFrame.db.BaseDao;
import eFrame.utils.ReflectionUtil;

/**
 * 存放实体类bean的地方
 * <br>
 * @date 2012-12-27
 * @author LiangRL
 * @alias E.E.
 */
public class EntityContainer {
	/** 单例容器 */
	private static EntityContainer instance;

	/** 待扫描的包的目录 */
	private String[] resourcePackages;
	
	/** 因为容器只存放了类型信息class，这个beans存放类型信息class 的实例 */
	private static Map<String, Object> beans = new ConcurrentHashMap<String, Object>();		
	
	/** 私有构造器 */
	private EntityContainer(){}

	/** 单例容器 */
	public static EntityContainer getInstance(){
		if(instance==null){
			instance = new EntityContainer();
		}
		return instance;
	}
	
	/**
	 * 包扫描和依赖注入
	 * @param resourcePackages	输入包的目录
	 * @throws Exception
	 */
	public void invoke(final ArrayList<String> resourcePackages) throws Exception{
		invoke((String[])resourcePackages.toArray());
	}
	
	/**
	 * 包扫描和依赖注入
	 * @param resourcePackages	输入包的目录
	 * @throws Exception
	 */	
	public void invoke(final String[] resourcePackages) throws Exception{
		this.resourcePackages = resourcePackages;
		this.init();
		this.di();
		this.diDaoClass();
	}	
	
	/**
	 * 因为java默认是浅拷贝滴
	 * @param bean 有注入需求的bean的实例（action）
	 * @param methodName set方法的名字
	 * @param toInvokeBean 被注入的bean（service）
	 * @throws Exception
	 */
	private void invokeMethod(Object bean, String _methodName, Object toInvokeBean) throws Exception{
		for(Method m: bean.getClass().getMethods()){
			String methodName = m.getName();
			if(methodName.equals(_methodName)){
				m.invoke(bean, toInvokeBean);
			}
		}
	}
	
	/**
	 * 初始化时注入dao的泛型类型信息
	 * 这些dao必须继承了抽象类BaseDao
	 * @throws Exception 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	private void diDaoClass() throws SecurityException, NoSuchMethodException, Exception{
		//遍历所有继承了BaseDao的dao Bean
		for(String beanName:beans.keySet()){
			Object bean = beans.get(beanName);
			if(bean instanceof BaseDao){
				ParameterizedType t = (ParameterizedType)bean.getClass().getGenericSuperclass();
				//如果这个dao有继承BaseDao但是没有泛型参数，跳过这个dao
				if(t.getActualTypeArguments().length==0){
					continue;
				}
				Type genericType = t.getActualTypeArguments()[0];
				Class<?> clazz = (Class<?>)genericType;		
				//如果这个泛型没有TableBean 略过
				if(!clazz.isAnnotationPresent(TableBean.class)){
					continue;
				}
				BaseDao.class.getMethod("setGenericClass", Class.class).invoke(bean, clazz);
				bean.getClass().getMethod("setGenericClass", Class.class).invoke(bean, clazz);
			}
		}		
	}
	
	/** 
	 * 依赖注入
	 * */
	private void di() throws Exception{
		for(String beanName:beans.keySet()){
			Object bean = beans.get(beanName);
			for(Field f:bean.getClass().getDeclaredFields()){
				if(!f.isAnnotationPresent(Wired.class)){
					continue;
				}
				Wired w = f.getAnnotation(Wired.class);
				String beanname = w.name();
				String fieldName = f.getName();
				String setMethodName = ReflectionUtil.generateSetMethodName(fieldName);
				invokeMethod(bean, setMethodName, beans.get(beanname));
			}
		}
	}
	
	/** 
	 * 包扫描，把类型信息存放到classContainer
	 * */
	private void init() throws Exception{
		BaseClassContainer container = ClassContainer.getInstance();
		for(String p:resourcePackages){
			container.setBeans(p);			
		}
		Map<String, Class<?>> classMap = container.getClasses();
		for(String key:classMap.keySet()){
			//无参构造，实例化
			setBean(key, classMap.get(key).getConstructor().newInstance());
		}
	}	
	
	/**
	 * 给予外部调用， 获取对象
	 * @param key
	 * @return
	 */
	public Object getBean(String key) {
		return beans.get(key);
	}

	/**
	 * 内部调用， 设置对象到Map中。如果对象已存在， 则报错。
	 * @param beanName
	 * @param obj
	 */
	private void setBean(String beanName, Object obj) {
		if(beans.containsKey(beanName)){
			throw new RuntimeException(beanName+" is already exists!");
		}else{
			beans.put(beanName, obj);
		}
	}	
}
