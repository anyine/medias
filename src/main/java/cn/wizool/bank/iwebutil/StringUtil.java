package cn.wizool.bank.iwebutil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 字符串 公共类
 * 
 * @author wangyan
 * 
 */
public abstract class StringUtil {
	public static String[] sessionType = { "listMedia", "listNotice",
			"listTrain" };
	public static final int KB = 1024;
	public static final int MB = KB * 1024;
	public static final int GB = MB * 1024;
	public static final int TB = GB * 1024;
	public static final int MINUTE = 60;
	public static final int HOUR = 3600;

	/**
	 * 判断该字符串是否为空
	 * 
	 * @param str
	 * @return true 不为空，否则 false
	 */
	public static boolean notEmpty(String str) {
		boolean falg = true;
		if (str == null || str.equals("")) {
			falg = false;
		}
		return falg;
	}

	/**
	 * 根据文件长度判断单位
	 * 
	 * @param length
	 * @return 返回带有单位的文件长度
	 */
	public static String returnLength(int length, boolean blank) {
		String b = blank ? " " : "";
		if (((length / KB) > 0) && ((length / KB) < 1024)) {// KB
			return length / KB + b + "K";
		} else if (((length / MB) > 0) && ((length / MB) < 1024)) {// MB
			return length / MB + b + "M";
		} else if (((length / GB) > 0) && ((length / GB) < 1024)) {// GB
			return length / GB + b + "G";
		} else if (((length / TB) > 0) && ((length / TB) < 1024)) {// TB
			return length / TB + b + "T";
		} else {// BYTE
			return length + b + "B";
		}
	}

	public static String returnLength(int length) {
		return returnLength(length, true);
	}

	/**
	 * 根据文件后缀 判断文件类型
	 * 
	 * @param string
	 * @return 范围字符串文件类型
	 */
	public static String getDocType(String surfix) {
		String str = surfix.trim().toLowerCase();
		if (str.equals("xls") || str.equals("xlsx")) {
			return "Excel表格";
		} else if (str.equals("doc") || str.equals("docx")) {
			return "Word文档";
		} else if (str.equals("png") || str.equals("jpg") || str.equals("bmp")
				|| str.equals("jpeg") || str.equals("gif")) {
			return "图片";
		} else if (str.equals("flv") || str.equals("avi") || str.equals("mp4")
				|| str.equals("rmvb") || str.equals("3gp")
				|| str.equals("mpeg") || str.equals("mpg") || str.equals("wmv")
				|| str.equals("flv") || str.equals("mg4") || str.equals("mov")
				|| str.equals("mts") || str.equals("dvd") || str.equals("vob")
				|| str.equals("rm") || str.equals("vcd") || str.equals("svcd")
				|| str.equals("ogm") || str.equals("f4v") || str.equals("mkv")
				|| str.equals("asf")) {
			return "视频";
		} else if (str.equals("pdf")) {
			return "PDF文档";
		} else if (str.equals("ppt") || str.equals("pptx")) {
			return "幻灯片";
		} else if (str.equals("gd")) {
			return "GD文档";
		} else {
			return "其他";
		}
	}

	public static String getAge(long age) {
		if (age < MINUTE) {
			return age + "秒";
		} else {
			if (age < HOUR) {
				if (age % MINUTE == 0) {
					return age / MINUTE + "分";
				} else {
					return age / MINUTE + "分" + age % MINUTE + "秒";
				}
			} else {
				if (age % HOUR == 0) {
					return age / HOUR + "小时";
				} else {
					if (age % HOUR % MINUTE == 0) {
						return age / HOUR + "小时" + age % HOUR / MINUTE + "分";
					} else {
						String s = age / 3600 + "小时";
						if (age % HOUR < MINUTE) {
							return s + age % HOUR % MINUTE + "秒";
						} else {
							return age / HOUR + "小时" + age % HOUR / MINUTE + "分"
									+ age % HOUR % MINUTE + "秒";
						}
					}
				}
			}
		}
	}

	/**
	 * 判断两个String数组的有效数值有否一致，无需考虑顺序
	 * 
	 * @param A
	 * @param B
	 * @return true 为相同，否则 false
	 */
	public static boolean compareStringArr(String[] A, String[] B) {
		boolean bool = false;
		boolean isNull = isEqualNull(A) && isEqualNull(B);
		if (isNull) {
			return true;
		} else {
			if (isEqualNull(A) == false && isEqualNull(B) == false) {
				List lstA = removeSame(removeBlank(A));
				List lstB = removeSame(removeBlank(B));
				if (lstA.size() == lstB.size()) {
					// System.out.println("a = " + lstA + " ; b  = " + lstB);
					bool = outOfOrder(lstA, lstB);
				} else {
					// System.out.println("two col length not equal");
				}
			} else {
				return false;
			}
		}
		return bool;
	}

	/**
	 * Compare the col ...
	 * 
	 * @param str
	 * @return boolean isNull
	 */
	private static boolean isEqualNull(String[] str) {
		boolean isNull = false;
		String[] str2 = new String[str.length];
		int count = 0;
		if (str.length > 1) {
			for (int i = 0; i < str.length; i++) {
				if (str[i] != null && !"".equals(str[i].trim())) {
					str2[count++] = str[i];
				}
			}
			if (count > 0) {
				isNull = false;
			} else {
				isNull = true;
			}
		} else if (str == null || str.length == 0) {
			isNull = true;
		} else if (str.length == 1
				&& (str[0] == null || "".equals(str[0].trim()))) {
			isNull = true;
		}
		return isNull;
	}

	/**
	 * Remove the blank.
	 * 
	 * @return List havaBlank.
	 */
	private static List removeBlank(String[] str) {
		List haveBlank = new ArrayList();
		for (int i = 0; i < str.length; i++) {
			if (str[i] != null && !"".equals(str[i].trim())) {
				haveBlank.add(str[i].trim());
			}
		}
		return haveBlank;
	}

	/**
	 * Remove the same.
	 * 
	 * @return List same
	 */
	private static List removeSame(List lst) {
		List same = new ArrayList();
		Set set = new HashSet();
		Iterator it = lst.iterator();
		while (it.hasNext()) {
			set.add(it.next());
		}
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			same.add(iterator.next());
		}
		return same;
	}

	/**
	 * Out of order.
	 * 
	 * @param lst1
	 * @param lst2
	 * @return boolean out
	 */
	private static boolean outOfOrder(List lst1, List lst2) {
		boolean out = true;
		if (lst1.size() == lst2.size()) {
			for (int i = 0; i < lst1.size(); i++) {
				if (!lst2.contains(lst1.get(i))) {
					out = false;
				}
			}
		}
		return out;
	}

}
