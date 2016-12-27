package com.businessAdmin.web;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.dom4j.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Hello world!
 * 
 */
public class App {
	
	/**判断一个网页元素是否存在
	 * @param driver
	 * @param locator
	 * @return
	 */
	public static boolean isElementExsit(WebDriver driver, By locator) {
		boolean flag = false;
		try {
			WebElement element=driver.findElement(locator);
			flag=null!=element;
		} catch (NoSuchElementException e) {
			System.out.println("Element:" + locator.toString()
					+ " is not exsit!");
		}
		return flag;
	}
	public static void main(String[] args) {
		int timeout = 10;
		System.out.println("Hello World!");
		String url = "https://www.sgs.gov.cn/notice";
		System.setProperty("webdriver.chrome.driver", "libs/chromedriver.exe");
		WebDriver driver = null;
		driver = new ChromeDriver();
		String curWindowHandle = driver.getWindowHandle();
		driver.manage().window().maximize();

		driver.get(url);
		final WebDriver tmpDriver = driver.switchTo().window(curWindowHandle);
		// driver.get("http://gsxt.zjaic.gov.cn/appbasicinfo/doViewAppBasicInfo.do?corpid=13B40AF723C246F78BAC7D7E84A3A255DFE5E0D4C1DD312559B55E8E9A5F08B8");
		 
		 //driver.switchTo().frame("con_nav_ifm_1");
		 
		// System.out.println(driver.getPageSource());
		// 等待页面加载完毕，超时时间设为 timeout秒
		/*
		try {
			(new WebDriverWait(driver, timeout))
					.until(new ExpectedCondition<Boolean>() {
						public Boolean apply(WebDriver d) {

							String sellCount = tmpDriver
									.findElement(
											By.cssSelector("li.tm-ind-sellCount span.tm-count"))
									.getText();
							String reviewCount = tmpDriver
									.findElement(
											By.cssSelector("li.tm-ind-reviewCount span.tm-count"))
									.getText();

							return !(sellCount.equals("") || sellCount
									.equals("-"))
									&& !(reviewCount.equals("") || reviewCount
											.equals("-"));

						}
					});
		} catch (TimeoutException e) {

		}
		*/
		//获取检索内容的文本框
		 WebElement companyName = driver.findElement(By.cssSelector("div.portal div.middle form#formInfo div.inp-wrap input#keyword.inp"));

	      //传入检索文本内容
		 companyName.clear();
		 companyName.sendKeys("食品公司");
		 //获取“搜索”按钮，并且模拟单击事件
		 WebElement searchButton = driver.findElement(By.cssSelector("div.portal div.middle form#formInfo div.inp-btn a img"));  
		 searchButton.click();
		// while(true)
		// {
		 //输入验证码
		 /*Scanner sc = new Scanner(System.in);
		 String code=sc.nextLine();
		 
		 //获取验证码输入文本框
		
		 WebElement verifyCode = driver.findElement(By.cssSelector("input#cpt-input"));
		 verifyCode.clear();
		 verifyCode.sendKeys(code);
		
		 //获取验证码的“搜索”按钮，并且模拟单击事件
		 WebElement verifyButton = driver.findElement(By.cssSelector("html body.popup div div#captcha-popup div.btn-bar a.btn-02"));  
		 verifyButton.click();
		 System.out.println(driver.getPageSource());
		 */
		 //if(!isElementExsit(driver,By.id("verifyCode")))
		// break;
		 
		// }
		 try{			 
			 Thread.sleep(10000);
		 System.out.println(driver.getPageSource());
		 }
		 catch(Exception e){
			 System.out.println(e.toString());
		 }
		 List<WebElement> companyLinks=driver.findElements(By.cssSelector("div.main div.search div.list-info div.list-item div.link a"));
		 if(companyLinks!=null)
			 companyLinks.get(0).click();
		 //driver.switchTo().window(nameOrHandle);
		 
			
		 
		 
		driver.quit();
	}
	
	public static void iframe(){
		String url="http://gsxt.zjaic.gov.cn/appbasicinfo/doViewAppBasicInfo.do?corpid=AE86F2AD751AB427952E58F9F8898F444CB03D9499C41A0EAE268347C6B884C4";
		
	}
}
