package cn.wizool.bank.common;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Properties;

public class MD5 {

	public static String byteArrayToHex(byte[] b) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
				'b', 'c', 'd', 'e', 'f' };
		char[] ob = new char[2];
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			ob[0] = Digit[(b[i] >>> 4) & 0X0F];
			ob[1] = Digit[b[i] & 0X0F];
			s.append(new String(ob));
		}
		return s.toString();
	}

	public static String Sha1(String str) {
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			return byteArrayToHex(sha.digest(str.getBytes()));
		} catch (Exception e) {
			return null;
		}
	}

	public static String Md5(String str) {
		try {
			MessageDigest sha = MessageDigest.getInstance("MD5");
			return byteArrayToHex(sha.digest(str.getBytes()));
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void write(Properties prop, OutputStream os, String encoding) throws Exception {
		StringBuffer sb = new StringBuffer();
		MessageDigest md = MessageDigest.getInstance("MD5");
		for (Map.Entry<Object, Object> key : prop.entrySet()) {
			String name = key.getKey().toString();
			String value = key.getValue().toString();
			String line = name + "=" + value + "\r\n";
			md.update(line.getBytes(encoding));
			sb.append(line);
		}
		String line = "#"+MD5.byteArrayToHex(md.digest())+"\r\n";
		os.write(line.getBytes(encoding));
		os.write(sb.toString().getBytes(encoding));
	}
	
	public static void main(String[] args) throws Exception {
		Properties prop = new Properties();
		prop.put("size", "12345");
		prop.put("md5", "md5");
		OutputStream os = new FileOutputStream("/home/zhangbo/prop.txt");
		write(prop, os, "gbk");
		os.close();
	}
}
