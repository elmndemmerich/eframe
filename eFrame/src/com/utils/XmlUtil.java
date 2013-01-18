package com.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 测试工具类
 * <br>
 * @date 2013-1-18
 * @author LiangRL
 * @alias E.E.
 */
public class XmlUtil {
	
	static void out(Node baseNode){
		NamedNodeMap attr = baseNode.getAttributes();
		if(attr==null || attr.getLength()==0){
			return;
		}
		System.out.println(baseNode.getNodeName()+":");
		for(int i=0; i<attr.getLength(); i++){
			Node node = attr.item(i);
			System.out.print(node.toString()+"\t");
		}
		System.out.println("\n——————————————————————————————————————————————");
	}
	
	static  void outConf(Node baseNode){
		if(!"configuration".equalsIgnoreCase(baseNode.getNodeName())){
			return;
		}
		NamedNodeMap attr = baseNode.getAttributes();
		if(attr==null || attr.getLength()==0){
			return;
		}
		Node catchAll = attr.getNamedItem("catchAll");
		System.out.println(baseNode.getNodeName()+
				": catchAll:"+catchAll.getNodeValue());
		System.out.println("——————————————————————————————————————————————");
	}	
	
	static  void outRoute(Node baseNode){
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
		System.out.println(baseNode.getNodeName()+
				": method:"+method.getNodeValue()+"\trequestType:"+requsetType.getNodeValue()+"\turl:"+url.getNodeValue());
		System.out.println("——————————————————————————————————————————————");
	}	
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException{
		//测试读取xml
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = factory.newDocumentBuilder();
        Document doc = db.parse(new File("D:\\eframe\\eFrame\\resource\\route.xml"));
        Element elmtInfo = doc.getDocumentElement();
        NodeList nodes = elmtInfo.getChildNodes();
        for(int i = 0; i < nodes.getLength(); i++){
        	Node result = nodes.item(i);
        	String nodeType = result.getNodeName();
        	if("configuration".equalsIgnoreCase(nodeType)){
        		outConf(result);
        	}else if("route".equalsIgnoreCase(nodeType)){
        		outRoute(result);
        	}
        }
	}
}
