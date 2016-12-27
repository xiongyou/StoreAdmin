package com.store.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class StoreConfig {

	private String filePath;

	private Document document;
	private XPath xpath = XPathFactory.newInstance().newXPath();
	
	public StoreConfig(String filePath) {
		this.filePath = filePath;
		this.loadFromFile(this.filePath);
	}
	
	protected Map<String, String> platformConfigMap = new HashMap<String, String>();
	public boolean loadFromFile(String filePath) {
		// TODO Auto-generated method stub
		// 解析文件，生成document对象
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			document = builder.parse(new File(filePath));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 获取XPath路径所表示的值
	 * 
	 * @param elementPath
	 * @return
	 * @throws Exception
	 */
	public String getXpathText(String elementPath) throws Exception {
		String XpathText = (String) xpath.evaluate(elementPath, document,
				XPathConstants.STRING);
		
		return XpathText;
	}

	
	public Node getNodeObj(String elementPath) throws Exception {
		// 获取节点对象
		Node bookWeb = (Node) xpath.evaluate(elementPath, document,
				XPathConstants.NODE);
		System.out.println(bookWeb.getNodeName());

		System.out
				.println("===========================================================");
		return bookWeb;
	}

	/**
	 * 获取节点集合
	 * 
	 * @param elementPath
	 * @return
	 * @throws Exception
	 */
	public NodeList getNodeList(String elementPath) throws Exception {
		NodeList nodes = (NodeList) xpath.evaluate(elementPath, document,
				XPathConstants.NODESET);
		return nodes;
	}

	
	/**
	 * 获取网站下的所有配置节点
	 * @param platform
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getWebsiteConfigs(String platform) throws Exception {
		// TODO Auto-generated method stub
		this.platformConfigMap.clear();
		String elementPath = "webstoreConfig/websites/website[@name='"
				+ platform + "']/configs/*";
		NodeList nodes = getNodeList(elementPath);
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);				
			//String elementPathValue="webstoreConfig/websites/website[@name='"
				//+ platform + "']/configs/"+node.getNodeName()+"/text()";
			//String value=this.getXpathText(elementPathValue);
			String value=node.getTextContent();
			this.platformConfigMap.put( node.getNodeName(),value);
		}
		return platformConfigMap;
	}

}
