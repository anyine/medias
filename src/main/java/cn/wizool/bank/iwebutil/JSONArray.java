package cn.wizool.bank.iwebutil;

import javax.servlet.http.HttpServletResponse;

public class JSONArray {
	private JSON json;

	private boolean success = true;
	private String message = "出现错误！";
	private int total = 0;

	private String name;

	public JSONArray(HttpServletResponse response) {
		this(response, false);
	}

	public JSONArray(HttpServletResponse response, boolean html) {
		json = new JSON(response, true, html);
		json.beginObject();
		json.beginAttribute("root");
		json.beginArray();
	}

	public void setAttribute(String name, int value) {
		fire(name);
		json.setAttribute(name, value);
	}

	public void setAttribute(String name, float value) {
		fire(name);
		json.setAttribute(name, value);
	}

	public void setAttribute(String name, double value) {
		fire(name);
		json.setAttribute(name, value);
	}

	public void setAttribute(String name, boolean value) {
		fire(name);
		json.setAttribute(name, value);
	}

	public void setAttribute(String name, String value) {
		fire(name);
		json.setAttribute(name, value);
	}

	private void fire(String name) {
		if (this.name == null) {
			this.name = name;
			json.beginObject();
		} else if (this.name.equals(name)) {
			json.endObject();
			json.beginObject();
		}
	}

	public void flush() {
		if (this.name != null)
			json.endObject();
		json.endArray();
		json.endAttribute();

		json.setAttribute("success", success);
		json.setAttribute("message", message);
		json.setAttribute("total", total);

		json.endObject();
		json.end();
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
