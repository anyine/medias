package cn.wizool.bank.iwebutil;

import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.SchedulerException;
import org.springframework.web.context.ContextLoaderListener;

import cn.wizool.bank.common.TimerScheduler;
import cn.wizool.bank.iwebutil.newlay.ServiceFactory;
import cn.wizool.bank.model.Media;
import cn.wizool.bank.model.Train;
import cn.wizool.bank.service.CacheService;
import cn.wizool.bank.service.MachineTasksService;
import cn.wizool.bank.service.MediaService;
import cn.wizool.bank.service.TrainService;
import cn.wizool.bank.servlet.InterfaceServlet;

public class ServletCreateListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		final ServletContext sc = sce.getServletContext();

		String[] packages = sc.getInitParameter("servletPackages").split(":");
		for (String p : packages) {
			PackageSearcher cs = new PackageSearcher();
			cs.setPackageName(p.replace('.', '/'));
			cs.setListener(new PackageSearcherListener() {
				@Override
				public void found(String path, boolean isDir, String resource) {
					if (resource.endsWith("Servlet.class")
							&& !resource.contains("$")) {
						String map = "/"
								+ resource.replaceAll("Servlet\\.class$",
										"Servlet") + "/*";
						String cls = resource.replaceAll("Servlet\\.class$",
								"Servlet").replaceAll("/", ".");
						sc.addServlet(UUID.randomUUID().toString(), cls)
								.addMapping(map);
					}
				}
			});
			cs.search();
		}

		ServiceFactory sf = (ServiceFactory) ContextLoaderListener
				.getCurrentWebApplicationContext().getBean("ServiceFactory");
		((CacheService) sf.getServiceManager("platform").getService(
				"CacheService")).loadAllMachines();
		getMedias(sf);
		getTrains(sf);
	}

	private void getTrains(ServiceFactory sf) {
		TrainService ts = (TrainService) sf.getServiceManager("platform")
				.getService("TrainService");
		MachineTasksService mts = (MachineTasksService) sf.getServiceManager(
				"platform").getService("MachineTasksService");

		List<Train> list = ts.getSelectByCond();
		for (Train t : list) {
			String taskId = "S:" + t.getId();
			String[] machines = mts.getMachinesByTaskId(t.getId());
			TimerScheduler.getDefault().schedule(taskId, machines,
					t.getStartDate());
		}
	}

	private void getMedias(ServiceFactory sf) {
		MediaService ms = (MediaService) sf.getServiceManager("platform")
				.getService("MediaService");
		MachineTasksService mts = (MachineTasksService) sf.getServiceManager(
				"platform").getService("MachineTasksService");
		List<Media> list = ms.getSelectByCond();
		for (Media m : list) {
			String taskId = "S:" + m.getId();
			String[] machines = mts.getMachinesByTaskId(m.getId());
			try {
				InterfaceServlet.getScheduler().schedule(taskId, machines,
						m.getDispatch());
			} catch (SchedulerException e) {
				// e.printStackTrace();
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
