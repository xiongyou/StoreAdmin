package com.store.producturl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverObj {
	public WebDriver getAWebDriver() {
		WebDriver driver = null;
		// 初始化，不显示图片
		Map<String, Object> contentSettings = new HashMap<String, Object>();
		contentSettings.put("images", 2);
		Map<String, Object> preferences = new HashMap<String, Object>();
		preferences.put("profile.default_content_setting_values", contentSettings);
		DesiredCapabilities caps = DesiredCapabilities.chrome();
		caps.setCapability("chrome.prefs", preferences);

		driver = new ChromeDriver(caps);
		driver.manage().window().maximize();
		return driver;
	}

	public List<String> getProductUrl(String storeUrl, String website, int timeout, WebDriver driver) throws Exception {
		final String cssSelector = ProcessIn.storeConfigAttr.getCheckLoadCssSelector();
		String curWindowHandle = driver.getWindowHandle();
		List<String> urlList = new ArrayList<String>();
		String allProductPageUrl = "";
		// 处理所有产品获取方式
		if (ProcessIn.storeConfigAttr.getMode().equals("0")) {
			allProductPageUrl = storeUrl + ProcessIn.storeConfigAttr.getAppend(); // 判断店铺链接后面是否有/
			driver.get(allProductPageUrl);
		} else {
			driver.get(storeUrl);
		}
		//int lastContentHash=-1;
		while (true) {
			final WebDriver tmpDriver = driver.switchTo().window(curWindowHandle);
			String tmpUrl = driver.getCurrentUrl();
			try {
				(new WebDriverWait(driver, timeout)).until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						String cssContent = tmpDriver.findElement(By.cssSelector(cssSelector)).getText();
						System.out.println("第 " + cssContent + " 页");
						boolean b = cssContent.equals("") || cssContent.equals("-");

						return !b;
					}
				});
			} catch (TimeoutException e) {
				System.out.println("店铺不存在 或访问超时!");
				return urlList;
			}

			String content = driver.getPageSource().replaceAll("&lt;", "<").replaceAll("&gt;", ">")
					.replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
			
			System.out.println(content);
			// 匹配产品URL
			urlList.addAll(this.resultOfRegex(content, ProcessIn.storeConfigAttr.getProductUrlReg()));

			// 下一页跳转
			WebElement nextPage = driver
					.findElement(By.cssSelector(ProcessIn.storeConfigAttr.getNextPageCssSelector()));
			nextPage.click();
			String curUrl = driver.getCurrentUrl();
			// System.out.println(curUrl);
			// System.out.println(tmpUrl);
			if (tmpUrl.equals(curUrl)) {
				break;
			}
			/*if (ProcessIn.storeConfigAttr.getIsSameUrl().equals("0")) {
				if (tmpUrl.equals(curUrl)) {
					break;
				}
			}
			else{
				if (tmpUrl.equals(curUrl)) {
					int curContentHash=content.hashCode();
					if(lastContentHash==curContentHash){
						break;
					}
					else{
						lastContentHash=curContentHash;
					}
				}
			}*/
			
		}

		// for(String str:urlList){
		// System.out.println(str);
		// }

		return urlList;
	}

	/**
	 * 根据正则表达式处理原始字符串
	 * 
	 * @param oldContent
	 * @param regex
	 * @return 配置的列表
	 * @throws Exception
	 */
	public List<String> resultOfRegex(String oldContent, String regex) throws Exception {
		Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
		Matcher ma = pa.matcher(oldContent);
		List<String> resultList = new ArrayList<String>();
		// StringBuffer sb = new StringBuffer();
		while (ma.find()) {
			resultList.add(ma.group().trim());
		}
		return resultList;
	}
}
