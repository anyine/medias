package cn.wizool.bank.iwebutil.newlay.query;

import java.util.HashMap;
import java.util.Map;

public class ParameterString {
	Map<String, String> templates = new HashMap<String, String>();
	Map<String, String> parameters = new HashMap<String, String>();

	public String getString(String name) {
		String tmp = this.templates.get(name);

		if (tmp == null)
			return null;

		for (String key : parameters.keySet()) {
			tmp = tmp.replace("<" + key + ">", parameters.get(key));
		}

		return tmp;
	}

	public void setTemplate(String name, String template) {
		this.templates.put(name, template);
	}

	public void setParameter(String name, String value) {
		this.parameters.put(name, value);
	}

}
