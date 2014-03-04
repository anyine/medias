package cn.wizool.bank.iwebutil;

import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.cfg.NamingStrategy;

public class DatabaseNameGenerator extends ImprovedNamingStrategy implements
		NamingStrategy {

	private static final long serialVersionUID = 1L;

	private String prefix;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public String columnName(String columnName) {

		return addUnderscores(columnName).toLowerCase();

	}

	@Override
	public String tableName(String tableName) {

		return this.getPrefix() + addUnderscores(tableName).toLowerCase();

	}
}
