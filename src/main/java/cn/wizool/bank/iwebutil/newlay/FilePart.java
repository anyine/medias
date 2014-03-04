package cn.wizool.bank.iwebutil.newlay;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.servlet.http.Part;

/**
 * 获取part中value值
 * 
 * @author 黄绪伟
 * 
 */
public class FilePart {
	public FilePart() {
		super();
	}

	public FilePart(Part part) {
		this.part = part;
	}

	public FilePart(String charset, Part part) {
		this.charset = charset;
		this.part = part;
	}

	private String charset = "UTF-8";

	private Part part;

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	/**
	 * 需设置part，charset默认是utf-8
	 * 
	 * @return
	 */
	public String getPartValue() {
		try {
			Part part = this.getPart();
			String charset = this.getCharset();
			if (part != null && this.getCharset() != null) {
				String str = "";
				BufferedInputStream bis = new BufferedInputStream(
						part.getInputStream());
				InputStreamReader inputStreamReader = new InputStreamReader(
						bis, charset);
				BufferedReader input = new BufferedReader(inputStreamReader);
				String tempString = "";

				while ((tempString = input.readLine()) != null) {
					str += tempString;
				}
				input.close();
				return str;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
