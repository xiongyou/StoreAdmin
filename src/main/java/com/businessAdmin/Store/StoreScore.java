package com.businessAdmin.Store;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import org.dom4j.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StoreScore {
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
		String url = "https://item.taobao.com/item.htm?id=25846756871";
		System.setProperty("webdriver.chrome.driver", "libs/chromedriver.exe");
		WebDriver driver = null;
		driver = new ChromeDriver();
		String curWindowHandle = driver.getWindowHandle();
		driver.manage().window().maximize();

		driver.get(url);
		final WebDriver tmpDriver = driver.switchTo().window(curWindowHandle);
		
		// 等待页面加载完毕，超时时间设为 timeout秒
		
		try {
			(new WebDriverWait(driver, timeout))
					.until(new ExpectedCondition<Boolean>() {
						public Boolean apply(WebDriver d) {
							if("Tmall".equals("Tmall"))
							{
							String storeScoreLink = tmpDriver
									.findElement(
											By.cssSelector("#J-From"))
									.getText();
							System.out.println(storeScoreLink);

							return !(storeScoreLink.equals("") || storeScoreLink
									.equals("-")
									);
							}
							else								
								{
								String storeScoreLink = tmpDriver
										.findElement(
												By.cssSelector("#header-content > div.shop-summary.J_TShopSummary > p > span.line-left.J_TShopLeft > span.shop-rank > a"))
										.getText();
								

								return !(storeScoreLink.equals("") || storeScoreLink
										.equals("-")
										);
								}
							

						}
					});
		} catch (TimeoutException e) {

		}
		
		//单击评分链接
		 WebElement storeScoreLink = driver.findElement(By.cssSelector("#J-From"));

		 storeScoreLink.click();
		 
		 
		 //得到当前窗口的句柄 
		 String currentWindow = driver.getWindowHandle();	
		//得到所有窗口的句柄
		 Set<String> handles = driver.getWindowHandles();
		 Iterator<String> it = handles.iterator();
		 while(it.hasNext()){
			 if(currentWindow == it.next())
				 continue;
			 WebDriver window = driver.switchTo().window(it.next()); 
			 //获得新窗口，新的webDriver实例。
			 System.out.println("title,url = "+window.getTitle()+","+window.getCurrentUrl());
			 }
		
		 try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 System.out.println(driver.getPageSource());
		 
		 
			
		 
		 
		driver.quit();
	}

}
