package cn.wizool.bank.iwebutil;

import java.io.File;

public class FormDataField {
	private ContentDisposition contentDisposition;
	private ContentType contentType;
	private int begin;
	private int end;
	private File file;
	private String value;

	public File getFile() {
		return file;
	}

	public ContentDisposition getContentDisposition() {
		return contentDisposition;
	}

	public void setContentDisposition(ContentDisposition contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public void parseField(String field) {
		if (field.startsWith("--"))
			return;
		int p = field.indexOf(':');
		if (p != -1) {
			String key = field.substring(0, p).trim();
			String value = field.substring(p + 2).trim();
			this.setField(key.toLowerCase(), value);
		}
	}

	private void setField(String key, String value) {
		if (key.equals("content-type")) {
			this.setContentType(new ContentType(value));
		} else if (key.equals("content-disposition")) {
			this.setContentDisposition(new ContentDisposition(value));
		}
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setValue(String str) {
		this.value = str;
	}

	public String getValue() {
		return this.value;
	}

	public boolean isFile() {
		return this.getContentDisposition().isFile();
	}

}
