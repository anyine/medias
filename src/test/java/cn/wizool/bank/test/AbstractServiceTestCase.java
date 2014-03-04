package cn.wizool.bank.test;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.wizool.bank.iwebutil.newlay.DataAccessFactory;
import cn.wizool.bank.iwebutil.newlay.ServiceFactory;

public abstract class AbstractServiceTestCase extends TestCase {

	protected ApplicationContext context;
	protected ServiceFactory sf;
	protected DataAccessFactory daf;

	@Override
	protected void setUp() throws Exception {
		context = new ClassPathXmlApplicationContext("/ApplicationContext.xml");
		sf = (ServiceFactory) context.getBean("ServiceFactory");
		daf = (DataAccessFactory) context.getBean("DataAccessFactory");
		super.setUp();
	}

}
