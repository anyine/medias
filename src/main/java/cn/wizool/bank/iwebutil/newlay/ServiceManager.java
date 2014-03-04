package cn.wizool.bank.iwebutil.newlay;

import java.util.HashMap;
import java.util.Map;

import org.springframework.aop.framework.Advised;

public class ServiceManager {

	private DataAccessFactory dataAccessFactory;
	private Map<String, Advised> serviceProxyBeans = new HashMap<String, Advised>();

	public DataAccessFactory getDataAccessFactory() {
		return dataAccessFactory;
	}

	public void setDataAccessFactory(DataAccessFactory daoFactory) {
		this.dataAccessFactory = daoFactory;
	}

	public void setServiceProxyBeans(Map<String, Advised> serviceProxyBeans) {
		this.serviceProxyBeans = serviceProxyBeans;
	}

	public Map<String, Advised> getServiceProxyBeans() {
		return serviceProxyBeans;
	}

	public Advised getService(String name) {
		return this.serviceProxyBeans.get(name);
	}
}
