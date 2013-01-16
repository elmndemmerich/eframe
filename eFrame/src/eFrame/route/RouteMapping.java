package eFrame.route;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 路由映射
 * 1、读取路由配置。
 * 2、请求的路由映射到对应的action
 * <br>
 * @date 2013-1-14
 * @author LiangRL
 * @alias E.E.
 */
public class RouteMapping {
	/**路由*/
	private static RouteMapping instance = new RouteMapping();
	/**是否全匹配*/
	private boolean isCatchAll = false;
	
	/** url--route */
	private Map<String, Route> map = new HashMap<String, Route>();
	
	/**
	 * 配置设定
	 * 目前只有是否全部匹配的设定
	 * @param baseNode
	 */
	private void settingConf(Node baseNode){
		if(!"configuration".equalsIgnoreCase(baseNode.getNodeName())){
			return;
		}
		NamedNodeMap attr = baseNode.getAttributes();
		if(attr==null || attr.getLength()==0){
			return;
		}
		Node catchAll = attr.getNamedItem("catchAll");
		String value = catchAll.getNodeValue();
		if("true".equals(value)){
			isCatchAll = true;			
		}
	}	
	
	/**
	 * 路由设定
	 * 读取配置文件中的route节点
	 * @param baseNode
	 */
	private void settingRoute(Node baseNode){
		if(!"route".equalsIgnoreCase(baseNode.getNodeName())){
			return;
		}
		NamedNodeMap attr = baseNode.getAttributes();
		if(attr==null || attr.getLength()==0){
			return;
		}
		Node method = attr.getNamedItem("method");
		Node requsetType = attr.getNamedItem("requsetType");
		Node url = attr.getNamedItem("url");
		Route route = new Route(getNodeValue(requsetType), getNodeValue(url), getNodeValue(method));
		addRoute(route);
	}	
	
	/**
	 * 获取参数的时候， 如果遇到异常抛出之
	 * @param node
	 * @return
	 */
	private String getNodeValue(Node node){
		if(node==null){
			throw new RuntimeException("route node null!");
		}else if(node.getNodeName()==null){
			throw new RuntimeException("route node name null!");
		}else if(node.getNodeValue()==null){
			throw new RuntimeException("route node:' "+node.getNodeName()+"' null!");
		}
		return node.getNodeValue();
	}
	
	/** 读取xml，做读取路由映射设定 */
	private RouteMapping(){
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();		
			URL url = Thread.currentThread().getContextClassLoader().getResource("route.xml");			
			File f = new File(url.getFile());		
			Document doc = db.parse(f);		
	        Element elmtInfo = doc.getDocumentElement();
	        NodeList nodes = elmtInfo.getChildNodes();
	        for(int i = 0; i < nodes.getLength(); i++){
	        	Node result = nodes.item(i);
	        	String nodeType = result.getNodeName();
	        	if("configuration".equalsIgnoreCase(nodeType)){
	        		settingConf(result);
	        	}else if("route".equalsIgnoreCase(nodeType)){
	        		settingRoute(result);
	        	}
	        }	        
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static RouteMapping getInstance(){
		return instance;
	}
	
	public boolean isCatchAll() {
		return isCatchAll;
	}

	public void setCatchAll(boolean isCatchAll) {
		this.isCatchAll = isCatchAll;
	}	
	
	/**
	 * 
	 * @param uri 请求的地址
	 * @param methodType 请求类型
	 * @return
	 */
	public boolean containRoute(String uri, String methodType){
		/*String temp = uri.replaceAll("\\", "/");
		//去掉最后一个 '/'
		if(temp.endsWith("/") && temp.length()>1){
			temp = temp.substring(0, temp.length()-1);
		}*/
		
		return false;
	}
	
	public boolean containRoute(String url){
		if(url==null||"".equals(url.trim())){
			return false;
		}		
		if(this.map.containsKey(url)){
			return true;
		}
		return false;
	}	
	
	public Route getRoute(String url){
		if(containRoute(url)){
			return map.get(url);
		}else{
			return null;
		}
	}
	
	private void addRoute(Route route){
		if(map.containsKey(route.getUrl())){
			throw new RuntimeException("route already exists!! route url:"+route.getUrl());
		}else{
			this.map.put(route.getUrl(), route);
		}
	}
}
