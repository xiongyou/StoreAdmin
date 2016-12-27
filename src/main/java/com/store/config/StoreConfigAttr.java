package com.store.config;

import java.util.HashMap;
import java.util.Map;

public class StoreConfigAttr extends StoreConfig {

	public StoreConfigAttr(String filePath) {
		super(filePath);
		// TODO Auto-generated constructor stub
	}

	private String mode;
	private String append;
	private String checkLoadCssSelector;
	private String productUrlReg;
	private String nextPageCssSelector;
	private String prefix;
	private String isSameUrl;

	/**
	 * 设置配置的属性
	 * 
	 * @param platform
	 * @throws Exception
	 */
	public void set(String platform) throws Exception {
		this.platformConfigMap = this.getWebsiteConfigs(platform);
		this.setMode();
		this.setCheckLoadCssSelector();
		this.setNextPageCssSelector();
		this.setProductUrlReg();
		this.setPrefix();
		if (this.getMode().equals("0")) {
			this.setAppend();
		}
		this.setIsSameUrl();
	}

	public String getMode() {
		return mode;
	}

	public void setMode() {
		this.mode = this.platformConfigMap.get("mode");
	}

	public String getAppend() {
		return append;
	}

	public void setAppend() {
		this.append = this.platformConfigMap.get("append");
	}

	public String getCheckLoadCssSelector() {
		return checkLoadCssSelector;
	}

	public void setCheckLoadCssSelector() {
		this.checkLoadCssSelector = this.platformConfigMap.get("checkLoadCssSelector");
	}

	public String getProductUrlReg() {
		return productUrlReg;
	}

	public void setProductUrlReg() {
		this.productUrlReg = this.platformConfigMap.get("productUrlReg");
	}

	public String getNextPageCssSelector() {
		return nextPageCssSelector;
	}

	public void setNextPageCssSelector() {
		this.nextPageCssSelector = this.platformConfigMap.get("nextPageCssSelector");
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix() {
		this.prefix = this.platformConfigMap.get("prefix");
	}

	public String getIsSameUrl() {
		return isSameUrl;
	}

	public void setIsSameUrl() {
		this.isSameUrl = this.platformConfigMap.get("isSameUrl");
	}
	
}
