package cn.wizool.bank.iwebutil.newlay.query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NamedConditions implements QueryConditions {

	private Map<String, String[]> parameters = new HashMap<String, String[]>();

	public NamedConditions(Map<String, String[]> parameters) {
		this.parameters = parameters;
	}

	public NamedConditions() {
	}

	public NamedConditions(String type) {
		this.setType(type);
	}

	public void setParameters(Map<String, String[]> parameters) {
		this.parameters = parameters;
	}

	public Map<String, String[]> getParameters() {
		return parameters;
	}

	public int size() {
		return parameters.size();
	}

	public String[] get(Object key) {
		return parameters.get(key);
	}

	public String[] put(String key, String[] value) {
		return parameters.put(key, value);
	}

	public String[] put(String key, String value) {
		return parameters.put(key, new String[] { value });
	}

	public Set<String> keySet() {
		return parameters.keySet();
	}

	public Collection<String[]> values() {
		return parameters.values();
	}

	public void setType(String type) {
		this.put("pt_type", new String[] { type });
	}

	public String getType() {
		return this.get("pt_type")[0];
	}

	// ////////////////////////////////////////////////////
	public String getString(String key) {
		return this.get("s" + key)[0];
	}

	public void putString(String key, String value) {
		this.put("s" + key, new String[] { value });
	}

	public String[] getStringArray(String key) {
		return this.get("sa" + key);
	}

	public void putStringArray(String key, String[] value) {
		this.put("sa" + key, value);
	}

	// ////////////////////////////////////////////////////
	public Integer getInteger(String key) {
		return Integer.parseInt(this.get("i" + key)[0]);
	}

	public void putInteger(String key, Integer value) {
		this.put("i" + key, new String[] { value.toString() });
	}

	public Integer[] getIntegerArray(String key) {
		return toIntegers(this.get("ia" + key));
	}

	public void putIntegerArray(String key, Integer[] value) {
		this.put("ia" + key, toStrings(value));
	}

	public static Integer[] toIntegers(String[] strs) {
		Integer[] ints = new Integer[strs.length];
		for (int i = 0; i < strs.length; i++) {
			ints[i] = Integer.parseInt(strs[i]);
		}
		return ints;
	}

	public static String[] toStrings(Integer[] ints) {
		String[] strs = new String[ints.length];
		for (int i = 0; i < strs.length; i++) {
			strs[i] = ints[i].toString();
		}
		return strs;
	}

	// /////////////////////////////////////////////////////
	public Boolean getBoolean(String key) {
		return Boolean.parseBoolean(this.get("b" + key)[0]);
	}

	public void putBoolean(String key, Boolean value) {
		this.put("b" + key, new String[] { value.toString() });
	}

	// /////////////////////////////////////////////////////
	public Double getDouble(String key) {
		return Double.parseDouble(this.get("d" + key)[0]);
	}

	public void putDouble(String key, Double value) {
		this.put("d" + key, new String[] { value.toString() });
	}

	public Double[] getDoubleArray(String key) {
		return toDoubles(this.get("da" + key));
	}

	public void putDoubleArray(String key, Double[] value) {
		this.put("da" + key, toStrings(value));
	}

	public static Double[] toDoubles(String[] strs) {
		Double[] ints = new Double[strs.length];
		for (int i = 0; i < strs.length; i++) {
			ints[i] = Double.parseDouble(strs[i]);
		}
		return ints;
	}

	public static String[] toStrings(Double[] ints) {
		String[] strs = new String[ints.length];
		for (int i = 0; i < strs.length; i++) {
			strs[i] = ints[i].toString();
		}
		return strs;
	}

	// ////////////////////////////////////////////////
	public Date getDate(String key) {
		return toDate(this.get("t" + key)[0]);
	}

	public void putDate(String key, Date value) {
		this.put("t" + key, new String[] { toString(value) });
	}

	public Date[] getDateArray(String key) {
		return toDates(this.get("ta" + key));
	}

	public void putDateArray(String key, Date[] value) {
		this.put("ta" + key, toStrings(value));
	}

	public static Date[] toDates(String[] strs) {
		Date[] ints = new Date[strs.length];
		for (int i = 0; i < strs.length; i++) {
			ints[i] = toDate(strs[i]);
		}
		return ints;
	}

	public static String[] toStrings(Date[] ints) {
		String[] strs = new String[ints.length];
		for (int i = 0; i < strs.length; i++) {
			strs[i] = toString(ints[i]);
		}
		return strs;
	}

	public static String toString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}

	public static Date toDate(String str) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return formatter.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object toObject(String param) {
		return convert(parameters.get(param), param);
	}

	private Object convert(String[] parameter, String type) {
		if (type.charAt(1) == 'a') {
			List<Object> list = new ArrayList<Object>();
			if (parameter != null) {
				for (String p : parameter) {
					list.add(convert(p, type.replaceFirst("a", "")));
				}
			} else {
				if (type.startsWith("i")) {
					list.add(new Integer(0));
				} else if (type.startsWith("d")) {
					list.add(new Integer(0));
				} else if (type.startsWith("b")) {
					list.add(new Boolean(false));
				} else if (type.startsWith("t")) {
					list.add(new Date());
				} else if (type.startsWith("s")) {
					list.add("");
				}
			}
			return list;
		}
		if (parameter == null) {
			if (type.startsWith("i")) {
				return new Integer(0);
			} else if (type.startsWith("d")) {
				return new Double(0.0);
			} else if (type.startsWith("b")) {
				return new Boolean(false);
			} else if (type.startsWith("t")) {
				return new Date();
			} else if (type.startsWith("s")) {
				return "";
			}
		}
		return convert(parameter[0], type);
	}

	private Object convert(String parameter, String type) {
		if (type.startsWith("i")) {
			if (parameter.equals(""))
				return new Integer(0);
			return Integer.parseInt(parameter);
		} else if (type.startsWith("d")) {
			if (parameter.equals(""))
				return new Double(0.0);
			return Double.parseDouble(parameter);
		} else if (type.startsWith("b")) {
			if (parameter.equals(""))
				return new Boolean(false);
			return Boolean.parseBoolean(parameter);
		} else if (type.startsWith("t")) {
			try {
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parse(parameter);
			} catch (ParseException e) {
				return new Date();
			}
		} else if (type.equals("s")) {
			if (parameter.equals(""))
				return "";
			return parameter;
		}
		return parameter;
	}

	public boolean exists(String param) {

		if (!this.parameters.containsKey(param))
			return false;

		if (this.parameters.get(param) == null) {
			return false;
		}

		if (this.parameters.get(param).length == 0) {
			if (param.charAt(1) != 'a') {
				return false;
			} else {
				return true;
			}
		}

		if (param.charAt(1) != 'a') {
			String first = this.parameters.get(param)[0];
			if ((first == null || first.trim().equals(""))) {
				return false;
			} else {
				return true;
			}
		}

		return true;
	}
}
