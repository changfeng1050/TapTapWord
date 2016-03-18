package com.example.changfeng.taptapword.util;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	
	public static final int LEN = 32;

	public static String getFileMd5String(String filepath) throws Exception{
		MessageDigest md = MessageDigest.getInstance("MD5");
		
		FileInputStream fis = new FileInputStream(filepath);
		
		byte[] dateBytes = new byte[1024];
		
		int numRead = 0;
		
		while ((numRead = fis.read(dateBytes)) != -1) {
			md.update(dateBytes, 0, numRead);
		};
		fis.close();
		byte[] mdBytes = md.digest();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mdBytes.length; i++) {
			sb.append(Integer.toString((mdBytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	public static String getMd5(String content)  throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("MD5");

        md.update(content.getBytes());
        byte[] mdBytes = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdBytes.length; i++) {
            sb.append(Integer.toString((mdBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();

    }

}
