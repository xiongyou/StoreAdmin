package com.store.producturl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;


import com.store.config.StoreConfigAttr;

public class ProcessIn {
	public static StoreConfigAttr storeConfigAttr = new StoreConfigAttr("webstore.xml");
	public static Logger logger = Logger.getLogger("com.foo");
	
	public static void run(int start, int max,int timeout, Writer writer, Writer err) throws IOException, Exception {
		
		
		if (start > max) {
			throw new Exception();
		}
		final int length = (max - start + 1);
		System.setProperty("webdriver.chrome.driver", "libs/chromedriver.exe");
		String fileName=storeConfigAttr.getXpathText("webstoreConfig/storeUrlFile/text()");
		String encoding=storeConfigAttr.getXpathText("webstoreConfig/fileEncoding/text()");
		File file = new File("conf/"+fileName);
		int lineHandled = 0;
		BufferedReader reader = null;
		int lineNum = 0;
		int allProductCount=0;
		
		//初始化，不显示图片
		
		WebDriverObj driverObj = new WebDriverObj();
		WebDriver driver=null;
		
		
		while (lineHandled < length) {
			String line;
			try {		
				driver=  driverObj.getAWebDriver();
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
				
				lineNum = 0;
				while (true) {
					lineNum++;
					line = reader.readLine();
					if (lineNum < start) {
						continue;
					}

					if (lineNum > max) {
						logger.debug("处理完成，本次一共找到 "+allProductCount+" 件商品");
						break;
					}

					// 到达文件末尾
					if (line == null) {
						logger.debug("处理完成，本次一共找到 "+allProductCount+" 件商品");
						break;
					}

					
					lineHandled++;
					
					final String[] tmps = line.split("\t");
					logger.debug("当前行 " + lineNum+"："+line);
					if (tmps.length != 3) {
						err.write(lineNum + "\t" + line + "\t数据格式错误");
						err.write("\r\n");
						err.flush();
						continue;
					}					
					
					//如果不是配置的网站，则提示出错，然后继续

					String storeUrl=tmps[0];
					if(storeUrl.lastIndexOf("/")==storeUrl.length()-1)
						storeUrl=storeUrl.substring(0, storeUrl.length()-1);
					String website=tmps[1];
					
					storeConfigAttr.set(website);
					List<String> urlList = driverObj.getProductUrl(storeUrl, website, timeout,driver);					
					if(urlList==null||urlList.size()==0){
						err.write(lineNum+"\t"+line+"\t未找到产品链接");
						err.write("\r\n");
						err.flush();
						continue;
					}
					String productUrl="";
					for (String str : urlList) {
						productUrl=storeConfigAttr.getPrefix() + str;
						writer.write(lineNum + "\t"+line+"\t"+productUrl);						
						writer.write("\r\n");
						writer.flush();						
					}					
					int storeCount=urlList.size();
					logger.debug("此店铺找到 "+storeCount+" 件商品");
					
					allProductCount+=storeCount;
				}
				if(line==null)
					logger.debug("处理完成，本次一共找到 "+allProductCount+" 件商品");
					break;
			} catch (StaleElementReferenceException e) {
				if (reader != null) {
					reader.close();
				}
				driver.quit();
				start = lineNum--;
				lineHandled--;
				continue;
			}catch(Exception e){
				
				System.out.println(e.toString());
				if (reader != null) {
					reader.close();
				}		
				driver.quit();
				start = lineNum--;
				lineHandled--;
				continue;
			}
			
			
		}
		driver.quit();
	}

	
	public static void save(int start, int length, int timeout) throws Exception {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.chrome.driver", "libs/chromedriver.exe");

		Date date=new Date();
		String path="output/"+ new SimpleDateFormat("yyyy/MM/dd").format(date);
		
		File f=new File(path);
		if(!f.exists())
			f.mkdirs();
		File file = new File(path +"/"+ start + ".txt");
		File err = new File(path+"/" + start + "_err.txt");
		if (!file.exists()) {
			file.createNewFile();
		} else {
			System.out.println("文件已存在或创建文件失败");
			return;
		}

		if (!err.exists()) {
			err.createNewFile();
		} else {
			System.out.println("文件已存在或创建文件失败");
			return;
		}

		OutputStreamWriter oswErr = new OutputStreamWriter(
				new FileOutputStream(err), "utf-8");
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(
				file), "utf-8");
		BufferedWriter writer = new BufferedWriter(osw);
		BufferedWriter writerErr = new BufferedWriter(oswErr);
		run(start, start + length - 1, timeout, writer,
				writerErr);
		writer.close();
		}

	public static void main(String args[]) {
		logger.setLevel(Level.DEBUG);
		try {
			System.setProperty("webdriver.chrome.driver", "libs/chromedriver.exe");			
			int startLine=Integer.parseInt(args[0]);
			int count=Integer.parseInt(args[1]);
			int timeout = Integer.parseInt(args[2]);
			
			save(startLine,count,timeout);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug(e.toString());
		}
		// 1.打开所有产品页面

		// 2.获取所有产品页面内容

		// 3.提取产品URL
	}
}
