package cn.wizool.bank.iwebutil;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Stack;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletResponse;

public class JSON {

	private HttpServletResponse response;

	private class Status {
		public int num;// 当前数组长度或者对象中属性个数
		public String typ;// 类型：O：对象，A：数组， S：初始状态，P：属性

		public Status(String typ) {
			this.num = 0;
			this.typ = typ;
		}
	}

	Stack<Status> stack = new Stack<Status>();

	private PrintStream stream = null;

	public JSON(OutputStream s) {
		try {
			this.stream = new PrintStream(s, false, "UTF-8");
			stack.push(new Status("S"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public JSON(HttpServletResponse response) {
		this(response, true, false);
	}

	public JSON(HttpServletResponse response, boolean gzip) {
		this(response, gzip, false);
	}

	public JSON(HttpServletResponse response, boolean gzip, boolean html) {
		this.response = response;
		try {
			if (html) {
				response.setContentType("text/html; charset=utf-8");
			} else {
				response.setContentType("application/json; charset=utf-8");
			}
			OutputStream out = response.getOutputStream();
			if (gzip) {
				response.setHeader("Content-Encoding", "gzip");
				out = new GZIPOutputStream(out);
			}
			this.stream = new PrintStream(out, true, "utf-8");
			stack.push(new Status("S"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void beginObject() {
		if (stack.lastElement().num == 0) {
			this.stream.print("{");
		} else {
			this.stream.print(",{");
		}
		stack.lastElement().num++;
		stack.push(new Status("O"));
	}

	public void endObject() {
		this.stream.print("}");
		stack.pop();
		// this.stream.flush();
	}

	public void beginArray() {
		if (stack.lastElement().num == 0) {
			this.stream.print("[");
		} else {
			this.stream.print(",[");
		}
		stack.lastElement().num++;
		stack.push(new Status("A"));
	}

	public void addItem(int item) {
		addItemValue(String.valueOf(item));
	}

	public void addItem(float item) {
		addItemValue(String.valueOf(item));
	}

	public void addItem(double item) {
		addItemValue(String.valueOf(item));
	}

	public void addItem(boolean item) {
		addItemValue(String.valueOf(item));
	}

	public void addItem(String item) {
		item = item == null ? "" : item;
		addItemValue("'" + process(item) + "'");
	}

	private static String process(String str) {
		str = str.replaceAll("\\\\", "\\\\\\\\");
		str = str.replaceAll("\\t", "\\\\t");
		str = str.replaceAll("\\r", "\\\\r");
		str = str.replaceAll("\\n", "\\\\n");
		str = str.replaceAll("'", "\\\\'");
		return str;
	}

	public void addItemValue(String item) {
		if (stack.lastElement().typ.equals("A")
				|| stack.lastElement().typ.equals("P")) {
			if (stack.lastElement().num == 0) {
				this.stream.print(item);
			} else {
				this.stream.print("," + item);
			}
			stack.lastElement().num++;
		}
	}

	public void endArray() {
		this.stream.print("]");
		stack.pop();
		// this.stream.flush();
	}

	public void setAttribute(String name, int value) {
		setAttributeValue(name, String.valueOf(value));
	}

	public void setAttribute(String name, float value) {
		setAttributeValue(name, String.valueOf(value));
	}

	public void setAttribute(String name, double value) {
		setAttributeValue(name, String.valueOf(value));
	}

	public void setAttribute(String name, boolean value) {
		setAttributeValue(name, String.valueOf(value));
	}

	public void setAttribute(String name, String value) {
		value = value == null ? "" : value;
		setAttributeValue(name, "'" + process(value) + "'");
	}

	protected void setAttributeValue(String name, String value) {
		if (stack.lastElement().typ.equals("O")) {
			if (stack.lastElement().num == 0) {
				this.stream.print(name + ":" + value);
			} else {
				this.stream.print(",'" + name + "':" + value);
			}
		}
		stack.lastElement().num++;
	}

	public void beginAttribute(String name) {
		if (stack.lastElement().typ.equals("O")) {
			if (stack.lastElement().num == 0) {
				this.stream.print("'"+name + "':");
			} else {
				this.stream.print(",'" + name + "':");
			}
			stack.lastElement().num++;
			stack.push(new Status("P"));
		}
	}

	public void endAttribute() {
		stack.pop();
		stack.lastElement().num++;
		// this.stream.flush();
	}

	public void end() {
		stream.close();
		if (this.response != null) {
			try {
				this.response.flushBuffer();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}

	public static void sendErrorMessage(HttpServletResponse response,
			String message, boolean html) {
		JSON json = new JSON(response, true, html);
		json.beginObject();
		json.setAttribute("success", false);
		if (message != null && message.length() > 0)
			json.setAttribute("message", message);
		json.endObject();
		json.end();
	}

	public static void sendErrorMessage(HttpServletResponse response,
			String message) {
		sendErrorMessage(response, message, false);
	}

	public static void sendSuccess(HttpServletResponse response, boolean html) {
		JSON json = new JSON(response, true, html);
		json.beginObject();
		json.setAttribute("success", true);
		json.endObject();
		json.end();
	}

	public static void sendSuccess(HttpServletResponse response, String message) {
		JSON json = new JSON(response, true, true);
		json.beginObject();
		json.setAttribute("success", true);
		json.setAttribute("message", message);
		json.endObject();
		json.end();
	}

	public static void sendSuccess(HttpServletResponse response) {
		sendSuccess(response, false);
	}

}
