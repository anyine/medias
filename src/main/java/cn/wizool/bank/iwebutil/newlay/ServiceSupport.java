package cn.wizool.bank.iwebutil.newlay;

public class ServiceSupport {

	private DataAccessFactory dataAccessFactory;
	private ServiceFactory serviceFactory;

	public void setDataAccessFactory(DataAccessFactory daoFactory) {
		this.dataAccessFactory = daoFactory;
	}

	public DataAccessFactory getDataAccessFactory() {
		return dataAccessFactory;
	}

	protected DataAccessSupport getDataAccessObject(String mng, String dao) {
		return this.getDataAccessFactory().getDataAccessManager(mng)
				.getDataAccessObject(dao);
	}

	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	public ServiceFactory getServiceFactory() {
		return serviceFactory;
	}

}
