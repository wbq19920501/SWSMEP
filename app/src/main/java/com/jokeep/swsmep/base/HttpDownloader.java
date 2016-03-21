package com.jokeep.swsmep.base;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/**
 *
 * @Project: Android_Download
 * @Desciption:
 * @Author: LinYiSong
 * @Date: 2011-3-25~2011-3-25
 */
public class HttpDownloader {

	private URL url = null;

	/**
	 * 根据URL下载文件,前提是这个文件当中的内容是文本,函数的返回值就是文本当中的内容
	 * 1.创建一个URL对象
	 * 2.通过URL对象,创建一个HttpURLConnection对象
	 * 3.得到InputStream
	 * 4.从InputStream当中读取数据
	 * @param urlStr
	 * @return
	 */
	public String download(String urlStr){
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		try {
			url = new URL(urlStr);
			//根据URL取得与资源提供的服务器的连接
			HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
			//将连接流管道成BufferedReader
			buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			//利用BufferedReader逐行读取文本信息,并添加到StringBuffer中
			while( (line = buffer.readLine()) != null){
				sb.append(line);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				buffer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//将读取的文本信息以String的形式输出
		return sb.toString();
	}

	/**
	 *
	 * @param urlStr
	 * @param path
	 * @param fileName
	 * @return
	 * 		-1:文件下载出错
	 * 		 0:文件下载成功
	 * 		 1:文件已经存在
	 */
	public int downFile(String urlStr, String path, String fileName){
		System.out.println("downFile");
		InputStream inputStream = null;
		try {
			FileUtils fileUtils = new FileUtils();
			//判断文件是否存在
			if(fileUtils.isFileExist(path + fileName)){
				System.out.println("exits");
				return 1;
			} else {
				//得到io流
				inputStream = getInputStreamFromURL(urlStr);
				//从input流中将文件写入SD卡中
				System.out.println("inpustStream:"+inputStream);
				File resultFile = fileUtils.write2SDFromInput(path, fileName, inputStream);
				if(resultFile == null){
					return -1;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		finally{
			try {
				if(inputStream!=null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * 根据URL得到输入流
	 * @param urlStr
	 * @return
	 */
	public InputStream getInputStreamFromURL(String urlStr) {
		HttpURLConnection urlConn = null;
		InputStream inputStream = null;
		try {
			url = new URL(urlStr);
			urlConn = (HttpURLConnection)url.openConnection();
			inputStream = urlConn.getInputStream();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return inputStream;
	}
}
