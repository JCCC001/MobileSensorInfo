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
 * �������
 * @author TristanHuang
 * 2017-6-28  ����4:49:57
 */
public class Toolbox {
	
	/**
	 * @return ����sd����Ŀ¼��·��
	 */
	public static String getSDPath(){
		  File sdDir = null;
		  boolean sdCardExist = Environment.getExternalStorageState()
		  .equals(android.os.Environment.MEDIA_MOUNTED); //�ж�sd���Ƿ����
		  if (sdCardExist)
		  {
		  sdDir = Environment.getExternalStorageDirectory();//��ȡ��Ŀ¼
		  }
		  return sdDir.toString();
	}
	
	/**
	 * @return ���ؽ�Ҫ�½���txt�ļ���
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
