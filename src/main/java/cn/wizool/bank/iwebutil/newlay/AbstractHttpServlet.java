package cn.wizool.bank.iwebutil.newlay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.springframework.aop.framework.Advised;
import org.springframework.web.context.ContextLoaderListener;

public abstract class AbstractHttpServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static AtomicLong number = new AtomicLong();
	private static Properties properties = new Properties();
	private static Map<String, Integer> map = new ConcurrentHashMap<String, Integer>();

	public ThreadLocal<HttpServletRequest> threadLocalRequest = new ThreadLocal<HttpServletRequest>();

	public ThreadLocal<HttpServletResponse> threadLocalResponse = new ThreadLocal<HttpServletResponse>();

	private static ServiceFactory serviceFactory;

	public AbstractHttpServlet() {
		super();
	}

	protected Object getService(String mng, String svc) {
		ServiceFactory serviceFactory2 = this.getServiceFactory();
		ServiceManager serviceManager = serviceFactory2.getServiceManager(mng);
		Map<String, Advised> serviceProxyBeans = serviceManager
				.getServiceProxyBeans();
		return serviceProxyBeans.get(svc);
	}

	public ServiceFactory getServiceFactory() {
		if (serviceFactory == null) {
			serviceFactory = (ServiceFactory) ContextLoaderListener
					.getCurrentWebApplicationContext()
					.getBean("ServiceFactory");
		}
		return serviceFactory;
	}

	public boolean process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String method = request.getParameter("method");
		if (!method.equals("dump")) {
			synchronized (map) {
				if (method.equals("getUUIDByIp")) {
					System.out.println(request.getRemoteAddr());
					return true;
				}
				String name = this.getClass().getName() + "." + method;
				Integer count = map.get(name);
				if (count != null) {
					map.put(name, count + 1);
				} else {
					map.put(name, 1);
				}
			}
		} else {
			synchronized (map) {
				this.getResponse().getWriter()
						.write(number.longValue() + "\r\n");
				for (String key : map.keySet()) {
					this.getResponse().getWriter()
							.write(key + ":" + map.get(key) + "\r\n");
				}
			}
			return true;
		}

		if (method != null) {
			Method m = null;
			try {
				number.incrementAndGet();
				m = this.getClass().getDeclaredMethod(method);
				if (m != null) {
					m.invoke(this);
				}
				return true;
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				System.out.println("error: " + this.getClass().getName() + ":"
						+ m.getName());
				// e.printStackTrace();
			} finally {
				number.decrementAndGet();
			}
		}
		System.out.println(method + ": call error!");
		return false;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.threadLocalRequest.set(request);
		this.threadLocalResponse.set(response);
		this.process(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.threadLocalRequest.set(request);
		this.threadLocalResponse.set(response);
		this.process(request, response);
	}

	protected HttpServletRequest getRequest() {
		return this.threadLocalRequest.get();
	}

	protected HttpServletResponse getResponse() {
		return this.threadLocalResponse.get();
	}

	public String getProperty(String name, String deflt) {
		return properties.getProperty(name, deflt);
	}

	public String getProperty(String name) {
		return properties.getProperty(name);
	}

	public Object setProperty(String name, String value) {
		return properties.setProperty(name, value);
	}

	public static String getCookieValue(HttpServletRequest request,
			String cookieName, String defaultValue) {
		return getCookieValue(request.getCookies(), cookieName, defaultValue);
	}

	public static String getCookieValue(HttpServletRequest request,
			String cookieName) {
		return getCookieValue(request.getCookies(), cookieName, null);
	}

	public static String getCookieValue(Cookie[] cookies, String cookieName,
			String defaultValue) {
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookieName.equals(cookie.getName()))
				return (cookie.getValue());
		}
		return (defaultValue);
	}

	public static String getCookieValue(Cookie[] cookies, String cookieName) {
		return getCookieValue(cookies, cookieName, null);
	}

	/*
	 * 黄绪伟
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Method getSetMethod(Class objectClass, String fieldName) {
		try {
			Class[] parameterTypes = new Class[1];
			java.lang.reflect.Field field = objectClass
					.getDeclaredField(fieldName);
			parameterTypes[0] = field.getType();
			StringBuffer sb = new StringBuffer("set");
			sb.append(fieldName.substring(0, 1).toUpperCase());
			sb.append(fieldName.substring(1));
			Method method = objectClass
					.getMethod(sb.toString(), parameterTypes);
			return method;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void invokeSet(Object o, String fieldName, Object value) {
		Method method = getSetMethod(o.getClass(), fieldName);
		try {
			method.invoke(o, new Object[] { value });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void invokeSetSimpleType(Object o, String fieldName,
			String fieldValue) {
		try {
			Field field = o.getClass().getDeclaredField(fieldName);
			String fieldTypeName = field.getType().getName();
			if (fieldValue != null) {
				if (fieldTypeName.equals("java.lang.String")) {
					invokeSet(o, fieldName, fieldValue);
				} else if (fieldValue != null && fieldValue.length() > 0) {
					fieldValue = fieldValue.replace(",", "");
					if (fieldTypeName.equals("int")
							|| fieldTypeName.equals("java.lang.Integer"))
						invokeSet(o, fieldName, Integer.parseInt(fieldValue));
					if (fieldTypeName.equals("java.lang.Long")
							|| fieldTypeName.equals("long"))
						invokeSet(o, fieldName, Long.parseLong(fieldValue));
					if (fieldTypeName.equals("java.lang.Float")
							|| fieldTypeName.equals("float"))
						invokeSet(o, fieldName, Float.parseFloat(fieldValue));
					if (fieldTypeName.equals("java.lang.Double")
							|| fieldTypeName.equals("double"))
						invokeSet(o, fieldName, Double.parseDouble(fieldValue));
				}
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param request
	 * @param o
	 * @return
	 * 
	 *         对象中字段是int、java.lang.Integer、java.lang.Long、java.lang.Float、java.
	 *         lang.Double和java.lang.String类型的 会自动从request中取值并给对象o赋值
	 */
	public static Object setObject(HttpServletRequest request, Object o) {
		try {
			Field[] fields = o.getClass().getDeclaredFields();
			for (Field field : fields) {
				String fieldName = field.getName();
				String fieldValue = request.getParameter(fieldName);
				if (fieldValue != null) {
					invokeSetSimpleType(o, fieldName, fieldValue);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}

	public static Object setObject(HttpServletRequest request, Object o,
			String... fieldNames) {
		try {
			for (String fieldName : fieldNames) {
				String fieldValue = request.getParameter(fieldName);
				invokeSetSimpleType(o, fieldName, fieldValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}

	/*
	 * 拆分字符串，返回数组，如果为空返回null
	 */
	public static String[] join(String ids) {
		try {
			String[] idArray = ids.split(",");
			return idArray;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 接收一个Double类型的数，格式化成 000,000.00 的格式 i 是保留小数点后的位数
	 */
	public static String formatNumber(double d, int i) {
		try {
			String str = String.format("%1$,f", d);
			String result = str.substring(0, str.length() - 6 + i);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String formatNumberDefault(double d) {
		return formatNumber(d, 2);
	}

	public String getFileName(Part file, String charset) {
		int i = 0;
		String fileHeader = file.getHeader("content-disposition");
		byte[] fileNames = fileHeader.getBytes();
		for (i = 0; i < fileNames.length; i++) {
			if (fileNames[i] == '=') {
				break;
			}
		}
		byte[] f = new byte[fileNames.length - i - 3];
		System.arraycopy(fileNames, i + 2, f, 0, f.length);
		try {
			return new String(f, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getFilePartValue(HttpServletRequest request, String name) {
		try {
			Part part = request.getPart(name);
			if (part != null) {
				FilePart filepart = new FilePart(part);
				return filepart.getPartValue();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getFilePartValue(HttpServletRequest request, String name,
			String charset) {
		try {
			Part part = request.getPart(name);
			FilePart filepart = new FilePart(charset, part);
			return filepart.getPartValue();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getFileEndName(String fileName) {
		int i = fileName.lastIndexOf(".");
		String endName = "";
		endName = fileName.substring(i + 1);
		return endName;
	}
}
