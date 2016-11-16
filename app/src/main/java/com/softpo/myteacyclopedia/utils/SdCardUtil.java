package com.softpo.myteacyclopedia.utils;

import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SdCardUtil {
	
	
	public static boolean isMounted(){
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)){
			return true;
		}
		return false;
	}
	
	public static void saveFile(byte[] data,String root, String fileName){
//		if(!isMounted()){
//			return false;
//		}

	//	File file = Environment.getExternalStoragePublicDirectory(type);
		//保存数据的文件
		File file = new File(root,fileName);
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file);
			fos.write(data,0,data.length);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}


	//取数据
	public static byte[] pickFromSdCard(String fileName) {
		FileInputStream fis = null;
		ByteArrayOutputStream baos=null;
		try {
			fis = new FileInputStream(fileName);
			baos = new ByteArrayOutputStream();

			int len=0;
			byte[] buf=new byte[1024*8];
			while ((len=fis.read(buf))!=-1){
				baos.write(buf,0,len);
			}

			return baos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
