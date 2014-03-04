package cn.wizool.bank.common;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PropertiesOutputStream {

	private MessageDigest md = null;
	private OutputStream outputStream;
	private String charset;

	public PropertiesOutputStream(OutputStream os, String charset) {
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		this.outputStream = os;
		this.charset = charset;
	}

	public void setProperty(String name, String value) throws UnsupportedEncodingException,
			IOException {
		byte[] line = (name + "=" + value + "\r\n").getBytes(this.charset);
		md.update(line);
		this.outputStream.write(line);
	}

	public void close() throws IOException {
		byte[] line = ("#"+MD5.byteArrayToHex(md.digest())).getBytes();
		this.outputStream.write(line);
		this.outputStream.flush();
	}

	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		OutputStream os = new FileOutputStream("a.txt");
		
		PropertiesOutputStream pos  = new PropertiesOutputStream(os, "GBK");
		pos.setProperty("name", "123456");
		pos.setProperty("hello", "fasdffsdafasd");
		
		pos.close();
	}
}
