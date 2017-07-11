/**
 * 
 */
package com.tristan.sensorinfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import android.os.Environment;

/**
 * 工具箱包
 * @author TristanHuang
 * 2017-6-28  下午4:49:57
 */
public class Toolbox {
	
	/**
	 * @return 返回sd卡根目录的路径
	 */
	public static String getSDPath(){
		  File sdDir = null;
		  boolean sdCardExist = Environment.getExternalStorageState()
		  .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
		  if (sdCardExist)
		  {
		  sdDir = Environment.getExternalStorageDirectory();//获取跟目录
		  }
		  return sdDir.toString();
	}
	
	/**
	 * @return 返回将要新建的txt文件名
	 */
	public static String getFileNameString() {
		Calendar cal = Calendar.getInstance();
		String filenameString = Toolbox.getSDPath().toString()+File.separator+"logfiles"
				+File.separator+"logfile_"+cal.get(Calendar.YEAR)+"_"+(cal.get(Calendar.MONTH)+1)+"_"
				+cal.get(Calendar.DATE)+"_"+cal.get(Calendar.HOUR)+"_"+cal.get(Calendar.MINUTE)+"_"
				+cal.get(Calendar.SECOND)+".txt";
        return filenameString;
		
	}
	
	public static void writeSensorData(String file, String conent) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true)));
			out.write(conent);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
