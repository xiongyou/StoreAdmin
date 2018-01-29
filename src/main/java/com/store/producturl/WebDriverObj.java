package com.store.producturl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.store.config.Input;

public class WebDriverObj {
	public static Logger logger = Logger.getLogger(WebDriverObj.class.getSimpleName());

	public WebDriver getAWebDriver() {
		System.setProperty("webdriver.chrome.driver", "libs/chromedriver.exe");

		ChromeOptions options = new ChromeOptions();
		List<String> chromeArgs = new ArrayList<String>();

		chromeArgs.add(
				"user-agent=Mozilla/5.0 (compatible; Baiduspider-render/2.0; +http://www.baidu.com/search/spider.html)");
		options.addArguments(chromeArgs);
		WebDriver driver = new ChromeDriver(options);

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
		 int lastContentHash=-1; //当下一页链接相同、不会变化时，用内容文本进行判断是否为同一个页面

		String userName = ProcessIn.storeConfigAttr.getXpathText("webstoreConfig/login/@username");
		String password = ProcessIn.storeConfigAttr.getXpathText("webstoreConfig/login/@password");

		while (true) {
			Thread.sleep(200);
			String tmpurl = driver.getCurrentUrl();

			int loginTime = 0;
			while (tmpurl.contains("login") && loginTime < 5) {
				try {
					System.out.println("需要登录!");
					if ("Tmall".equals(website)) {
						driver.switchTo().frame("J_loginIframe");
					}

					// 切换到输入用户名、密码登录
					try {
						WebElement switchBtn = driver.findElement(By.cssSelector("a.J_Quick2Static"));
						switchBtn.click();
					}
					// 输入用户名密码
					finally {

						WebElement usernameEle = driver.findElement(By.cssSelector("#TPL_username_1"));
						usernameEle.clear();
						usernameEle.sendKeys(userName);
						Thread.sleep(2000);

						WebElement pwdEle = driver.findElement(By.cssSelector("#TPL_password_1"));
						pwdEle.clear();
						pwdEle.sendKeys(password + "1");

						Thread.sleep(2000);

						// 判断是否有滑块

						// 点击登录
						driver.findElement(By.cssSelector("button#J_SubmitStatic")).click();
						Thread.sleep(2000);
					}
					// pageList.add("需要登录");
					// return pageList;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					loginTime++;
					if (loginTime >= 5) {
						System.out.println("登录出错超过5次，请检查用户名、密码，然后输入回车!");

						Input.sc.nextLine();
					}
					driver.switchTo().defaultContent();
					tmpurl = driver.getCurrentUrl();
				}
			}

			// 天猫验证
			if (tmpurl.contains("sec.taobao")) {
				System.out.println("需要验证，然后输入回车!");

				Input.sc.nextLine();
			}

			boolean bLogin = false;

			if ("TaoBao".equals(website) || "Tmall".equals(website)) {
				while (!bLogin) {
					try {
						// 注意，此处登录与验证码的弹窗是同一个窗体对象
						driver.findElement(By.cssSelector("div.sufei-dialog-content"));
						// 输入用户名密码
						driver.switchTo().frame("sufei-dialog-content");

						// 先判断是否为验证码弹窗
						try {
							WebElement identCodeEle = driver.findElement(By.cssSelector("div#J_CodeContainer"));
							System.out.println("需要输入验证码！");
							Input.sc.nextLine();
						} catch (Exception e) {

							WebElement usernameEle = driver.findElement(By.cssSelector("#TPL_username_1"));
							usernameEle.clear();
							usernameEle.sendKeys(userName);
							Thread.sleep(2000);

							WebElement pwdEle = driver.findElement(By.cssSelector("#TPL_password_1"));
							pwdEle.clear();
							pwdEle.sendKeys(password);
							Thread.sleep(2000);
							// 点击登录

							driver.findElement(By.cssSelector("button#J_SubmitStatic")).click();

							// 此时 没跳出frame，如果这时定位default content中的元素也会报错
							// dr.findElement(By.id("id1"));//error
							/** 跳出frame,进入default content;重新定位id="id1"的div */
							driver.switchTo().defaultContent();
						}

					}
					// 没有弹窗，不需要登录
					catch (Exception e) {
						bLogin = true;
						e.printStackTrace();
						try {
							Thread.sleep(500);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						driver.navigate().refresh();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}

			String tmpUrl = driver.getCurrentUrl();
			final WebDriver tmpDriver = driver.switchTo().window(curWindowHandle);

			// 京东懒加载
			if ("JingDong".equals(website)) {
				driver.findElement(By.cssSelector("body")).sendKeys(Keys.END);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
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

			// System.out.println(content);
			// 匹配产品URL
			urlList.addAll(this.resultOfRegex(content, ProcessIn.storeConfigAttr.getProductUrlReg()));

			// 下一页跳转
			try{
			WebElement nextPage = driver
					.findElement(By.cssSelector(ProcessIn.storeConfigAttr.getNextPageCssSelector()));
			nextPage.click();
			}
			catch(Exception e){
				System.out.println("下一页无法点击");
				break;
			}
			// 切换下一页时暂停1秒
			logger.debug("点击下一页");
			System.out.println("点击下一页：" + System.currentTimeMillis());
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			logger.debug("已切换下一页");
			System.out.println("已切换下一页：" + System.currentTimeMillis());
			String curUrl = driver.getCurrentUrl();
			System.out.println(curUrl);
			System.out.println(tmpUrl);
			
			/*
			if (tmpUrl.equals(curUrl)) {
				break;
			}*/

			if (ProcessIn.storeConfigAttr.getIsSameUrl().equals("0")) {
				if (tmpUrl.equals(curUrl)) {
					break;
				}
			} else {
				if (tmpUrl.equals(curUrl)) {
					int curContentHash = content.hashCode();
					if (lastContentHash == curContentHash) {
						break;
					} else {
						lastContentHash = curContentHash;
					}
				}
			}

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
