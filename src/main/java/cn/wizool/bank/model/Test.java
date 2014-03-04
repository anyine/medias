package cn.wizool.bank.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.wizool.bank.iwebutil.DateUtil;
import cn.wizool.bank.iwebutil.StringUtil;
import cn.wizool.bank.iwebutil.DateUtil.DateFormatType;

public class Test {
	public static Hashtable<String, String[]> files = new Hashtable<String, String[]>();

	public static void main(String[] args) {
		System.out.println(StringUtil.getDocType("VOB"));
		System.out.println(StringUtil.sessionType[0]);
		String[] a = { "c", "b" };
		String[] b = { "b", "c" };
		boolean flag = compareStringArr(a, b);
		System.out.println(flag);
		files.put("11111", a);
		files.put("22222", b);
		System.out.println(files.keySet().size());
		for (String s : files.keySet()) {
			System.out.println(s);
			System.out.println(files.get(s));
		}
	}

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
					System.out.println("a = " + lstA + " ; b  = " + lstB);
					bool = outOfOrder(lstA, lstB);
				} else {
					System.out.println("two col length not equal");
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
	public static boolean isEqualNull(String[] str) {
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
	public static List removeBlank(String[] str) {
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
	public static List removeSame(List lst) {
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
	public static boolean outOfOrder(List lst1, List lst2) {
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

	private static String getScore(long i) {
		if (i < 60) {
			return i + "秒";
		} else {
			if (i < 3600) {
				if (i % 60 == 0) {
					return i / 60 + "分";
				} else {
					return i / 60 + "分" + i % 60 + "秒";
				}
			} else {
				if (i % 3600 == 0) {
					return i / 3600 + "时";
				} else {
					if (i % 3600 % 60 == 0) {
						return i / 3600 + "时" + i % 3600 / 60 + "分";
					} else {
						String s = i / 3600 + "时";
						if (i % 3600 < 60) {
							return s + i % 3600 % 60 + "秒";
						} else {
							return i / 3600 + "时" + i % 3600 / 60 + "分" + i
									% 3600 % 60 + "秒";
						}
					}
				}
			}
		}
	}

}
