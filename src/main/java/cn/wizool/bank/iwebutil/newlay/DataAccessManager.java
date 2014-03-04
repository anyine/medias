package cn.wizool.bank.iwebutil.newlay;

import java.util.HashMap;
import java.util.Map;

public class DataAccessManager {

	private Map<String, DataAccessSupport> dataAccessObjects = new HashMap<String, DataAccessSupport>();

	public Map<String, DataAccessSupport> getDataAccessObjects() {
		return dataAccessObjects;
	}

	public void setDataAccessObjects(
			Map<String, DataAccessSupport> dataAccessObjects) {
		this.dataAccessObjects = dataAccessObjects;
	}

	public DataAccessSupport getDataAccessObject(String name) {
		return this.dataAccessObjects.get(name);
	}

}
