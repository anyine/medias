package cn.wizool.bank.iwebutil.newlay;

import java.util.HashMap;
import java.util.Map;

public class ServiceFactory {

	private Map<String, ServiceManager> serviceManagers = new HashMap<String, ServiceManager>();

	public void setServiceManagers(
			Map<String, ServiceManager> serviceManagers) {
		this.serviceManagers = serviceManagers;
	}

	public Map<String, ServiceManager> getServiceManagers() {
		return serviceManagers;
	}

	public ServiceManager getServiceManager(String name) {
		return serviceManagers.get(name);
	}

}
