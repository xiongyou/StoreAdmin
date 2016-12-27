package com.store.general.url;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FirstLayer {
	public void getStoreUrls(String filename) throws IOException {
		File file = new File(filename);
		BufferedReader reader = null;
		while (true) {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			String[] tmps = line.split("\t");
			if (tmps.length != 3) {
				System.out.println(line + "\t数据格式错误");
				continue;
			}
			String storeUrl = tmps[0];
			if (storeUrl.lastIndexOf("/") == storeUrl.length() - 1)
				storeUrl = storeUrl.substring(0, storeUrl.length() - 1);
			String website = tmps[1];
		}
	}

}
