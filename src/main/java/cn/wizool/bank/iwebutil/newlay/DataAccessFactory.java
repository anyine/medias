package cn.wizool.bank.iwebutil.newlay;

import java.util.HashMap;
import java.util.Map;

public class DataAccessFactory {

	private Map<String, DataAccessManager> dataAccessManagers = new HashMap<String, DataAccessManager>();

	public DataAccessManager getDataAccessManager(String name) {
		return this.dataAccessManagers.get(name);
	}

	public void setDataAccessManagers(
			Map<String, DataAccessManager> dataAccessManagers) {
		this.dataAccessManagers = dataAccessManagers;
	}

	public Map<String, DataAccessManager> getDataAccessManagers() {
		return dataAccessManagers;
	}

}
