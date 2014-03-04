package cn.wizool.bank.iwebutil.newlay;

public class Test {

	public static String toCount(String hql) {
		return hql.replaceFirst("^ *(select +(.*) )?from +",
		"select count($2) from ").replace("count()", "count(*)");
	}

	public static String toUpdate(String hql) {
		return hql.replaceFirst("^ *(select +(distinct)?(.*) )?from +",
				"update $3 from ");
	}

	public static String toDelete(String hql) {
		return hql.replaceFirst("^ *(select +(distinct)?(.*) )?from +",
				"delete from ");
	}

	public static void main(String[] args) {
		System.out.println(toDelete("from fjsdkfj sdkjfk fsdjk"));
		System.out.println(toDelete("  from   fjsdkfj   sdkjfk fsdjk"));
		System.out
				.println(toDelete("  select  distinct  a  from fjsdkfj sdkjfk fsdjk"));
		System.out.println(toDelete(" select  * from fjsdkfj sdkjfk fsdjk"));
		System.out.println(toDelete(" select * from fjsdkfj sdkjfk fsdjk"));
	}
}
