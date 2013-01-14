package eFrame.container.base;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import eFrame.annotations.ActionBean;
import eFrame.annotations.DaoBean;
import eFrame.annotations.ServiceBean;
import eFrame.constants.Encoding;

/**
 * 容器基类。
 * <br>附带一些辅助包扫描的工具类
 * @date 2012-12-24
 * @author LiangRL
 * @alias E.E.
 */
public abstract class BaseClassContainer {
	
	public abstract void setClass(String name, Class<?> obj);

	public abstract Class<?> getClass(String name);
	
	/**
	 * 取得所有已加载的类型信息
	 * @return
	 */
	public abstract Map<String, Class<?>> getClasses();
	
	/**
	 * 包扫描，并且设置到容器中</br>
	 * 这里调用了抽象方法setClass，说明了子类必须实现才能调用setBeans方法。
	 * @param packageName
	 * @throws Exception 
	 */
	public void setBeans(String packageName) throws Exception{
		//判定输入的包目录是否合法
		if(!hasPackage(packageName)){
			throw new RuntimeException("set beans error! Is package: '"+packageName+"' ok?");
		}
		File packageFolder = package2Folder(packageName);
		if(packageFolder==null){
			throw new RuntimeException("set beans error! Is packageFolder: '"+packageName+"' exists?");
		}
		
		//递归遍历文件
		Set<File> files = new HashSet<File>();
		files = recusiveDir(packageFolder, files);
		
		//file to class
		Set<String> set = file2Set(files, packageFolder.getAbsolutePath(), packageName);
		
		//loading class into container
		for(String str:set){
			Class<?> clazz = Class.forName(str);
			if(clazz.isAnnotationPresent(ActionBean.class)){
				ActionBean b = clazz.getAnnotation(ActionBean.class);
				setClass(b.name(), (Class<?>)clazz);
			}else if(clazz.isAnnotationPresent(ServiceBean.class)){
				ServiceBean b = clazz.getAnnotation(ServiceBean.class);
				setClass(b.name(), (Class<?>)clazz);
			}else if(clazz.isAnnotationPresent(DaoBean.class)){
				DaoBean b = clazz.getAnnotation(DaoBean.class);
				setClass(b.name(), (Class<?>)clazz);
			}
		}
	}
	
	/**
	 * 返回该目录
	 * 如果不是目录类型， 返回空
	 * @param packagePath
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	protected File package2Folder(final String packagePath) throws UnsupportedEncodingException{
		String classesDir = packagePath.replace('.', '/');
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL u = loader.getResource(classesDir);
		String filePath = URLDecoder.decode(u.getFile(), Encoding.UTF_8);
		File dir = new File(filePath);
		if(dir.isDirectory()){
			return dir;
		}else{
			return null;			
		}
	}
	
	/**
	 * 判定输入的包目录，是否正确
	 * @param packagePath
	 * @return
	 */
	protected boolean hasPackage(final String packagePath){
		String classesDir = packagePath.replace('.', '/');
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL u = loader.getResource(classesDir);
		if(u==null){
			return false;
		}
		//如果不是file类型，退出
		if(!"file".equalsIgnoreCase(u.getProtocol())){
			return false;
		}
		return true;
	}

	/**
	 * 文件名变成可以通过classLoader加载的形式: com.a
	 * @param files
	 * @param folderPath
	 * @param basePackage
	 * @return
	 */
	protected Set<String> file2Set(Set<File> files, String folderPath, String basePackage){
		Set<String> result = new HashSet<String>();
		for(File f:files){
			String filePath = f.getAbsolutePath();
			filePath = filePath.replace(folderPath, "");
			//去掉开头的一个 '\'
			filePath = filePath.substring(1);	
			filePath = filePath.replace('\\', '.');
			filePath = filePath.replaceAll(".class", "");
			result.add(basePackage+"."+filePath);
		}
		return result;
	}
	
	/**
	 * 递归收集目录下的文件
	 * @param dir
	 * @return
	 */
	protected Set<File> recusiveDir(File dir, final Set<File> files){
		for(File f : dir.listFiles()){
			if(f.isFile()){
				files.add(f);
			}else{
				recusiveDir(f, files);
			}
		}
		return files;
	}
}