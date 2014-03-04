package cn.wizool.bank.iwebutil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

public class FormDataContent {

	private byte[] buffer = null;
	private int cur = 0;
	private String bundary = null;
	private List<FormDataField> fields = new ArrayList<FormDataField>();
	private HashMap<String, ArrayList<String>> parameters = new HashMap<String, ArrayList<String>>();
	private static String path = "c:";
	private static int MAX_BUFFER_SIZE = 20 * 1024 * 1024;

	public List<FormDataField> getFields() {
		return fields;
	}

	public void setFields(List<FormDataField> fields) {
		this.fields = fields;
	}

	public FormDataContent(byte[] buffer, String bundary) {
		this.buffer = buffer;
		this.cur = 0;
		this.bundary = bundary;
	}

	public FormDataContent(HttpServletRequest request) {
		try {
			InputStream in = request.getInputStream();
			long len = request.getContentLength();
			if (len < MAX_BUFFER_SIZE) {
				this.buffer = new byte[(int) len];
				long lft = len;
				while (lft > 0) {
					int tmp = in
							.read(this.buffer, (int) (len - lft), (int) lft);
					lft = lft - tmp;
				}
			} else {
				in.skip(len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.cur = 0;
		this.bundary = new ContentType(request.getContentType()).getBoundary();
	}

	public boolean parse() {
		FormDataField fdf = null;

		try {
			if (buffer == null) {
				return false;
			}

			while ((fdf = this.parseNextField()) != null) {
				this.fields.add(fdf);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return true;
	}

	private FormDataField parseNextField() throws UnsupportedEncodingException {
		FormDataField fdf = new FormDataField();
		String field = "";
		boolean firstField = true;
		int MAX_LINE_LEN = 1024;
		byte line[] = new byte[MAX_LINE_LEN];
		int pos = 0;
		boolean ret = false;
		while (true) {
			if (cur == buffer.length) {
				break;
			}

			line[pos++] = buffer[cur++];

			if (pos == MAX_LINE_LEN) {
				break;
			}

			if (pos > 1 && line[pos - 2] == '\r' && line[pos - 1] == '\n') {
				String l = new String(line, 0, pos - 2, "UTF-8").trim();
				if (pos == 2) {
					fdf.parseField(field);
					ret = true;
					break;
				} else {
					if (firstField) {
						firstField = false;
						fdf.parseField(l);
					} else {
						if (line[0] == '\t' || line[0] == ' ') {
							field += l;
						} else {
							fdf.parseField(field);
							field = l;
						}
					}
				}
				pos = 0;
			}
		}

		if (!ret) {
			return null;
		}

		fdf.setBegin(cur);

		byte[] bs = ("--" + this.bundary).getBytes();
		// find end
		while (cur < buffer.length) {
			int i = 0;
			for (i = 0; i < bs.length; i++) {
				if (buffer[cur + i] != bs[i])
					break;
			}
			if (i == bs.length)
				break;
			cur++;
		}

		if (cur == buffer.length) {
			return null;
		}

		fdf.setEnd(cur - 2);

		if (fdf.getContentDisposition().isFile()) {
			try {
				File file = new File(path + "/" + UUID.randomUUID().toString());
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(buffer, fdf.getBegin(), fdf.getEnd() - fdf.getBegin());
				fos.close();
				fdf.setFile(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				String str;
				str = new String(buffer, fdf.getBegin(), fdf.getEnd()
						- fdf.getBegin(), "utf-8");
				fdf.setValue(str);
				String n = fdf.getContentDisposition().getName();
				String v = fdf.getValue();
				this.setParameter(n, v);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		return fdf;
	}

	public static FormDataContent parse(HttpServletRequest request) {
		FormDataContent content = new FormDataContent(request);
		content.parse();
		return content;
	}

	public String getParameter(String key) {
		ArrayList<String> values = this.parameters.get(key);
		if (values == null) {
			return null;
		}
		if (values.size() == 0) {
			return null;
		} else {
			return values.get(0);
		}
	}

	public void setParameter(String name, String value) {
		ArrayList<String> parameter = parameters.get(name);
		if (parameter == null) {
			parameter = new ArrayList<String>();
			parameters.put(name, parameter);
		}
		parameter.add(value);
	}

	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(this.parameters.keySet());
	}

	public String[] getParameterValues(String name) {
		ArrayList<String> list = this.parameters.get(name);
		return list.toArray(new String[list.size()]);
	}

	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> map = new HashMap<String, String[]>();

		for (Entry<String, ArrayList<String>> entry : this.parameters
				.entrySet()) {
			String key = entry.getKey();
			map.put(key, this.getParameterValues(key));
		}

		return map;
	}
}
