package cn.wizool.bank.servlet;

import java.util.List;
import java.util.Map;

import cn.wizool.bank.common.PlatFormHttpServlet;
import cn.wizool.bank.iwebutil.JSON;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Config;
import cn.wizool.bank.model.Machine;

public class ConfigServlet extends PlatFormHttpServlet {
	private static final long serialVersionUID = -7367643454886678814L;

	/**
	 * 添加利率牌
	 */
	public void create() {
		Map<String, String[]> map = getRequest().getParameterMap();
		try {
			if (getConfigService().getListCount() < 3) {
				for (String name : map.keySet()) {
					if (!(name.equals("method"))) {
						Config c = new Config();
						c.setName(name);
						c.setValue(map.get(name)[0]);
						getConfigService().transCreate(null, c);
						// 执行命令
						putTask();
					}
				}
			} else {
				for (String name : map.keySet()) {
					Config c = getConfigService().selectById(name);
					if (c != null) {
						c.setValue(map.get(name)[0]);
						getConfigService().transUpdate(null, c);
						// 执行命令
						putTask();
					}
				}
			}

			JSON.sendSuccess(getResponse(), true);
		} catch (Exception e) {
			JSON.sendSuccess(getResponse(), "添加利率牌失败！");
			e.printStackTrace();
		}
	}

	private void putTask() {
		String dept = "";
		NamedConditions cond = new NamedConditions("getPc");
		cond.putString("Type", "广告");
		List<Machine> list = getMachineService().transSelectByCond(cond);
		for (Machine d : list) {
			dept += d.getId() + " ";
		}
		String[] depts = dept.split(" ");
		for (String s : depts) {
			InterfaceServlet.putOneTask(s, "S:getConfig");
		}
	}
	/**
	 * 获取利率牌信息
	 */
	public void getList() {
		int count = getConfigService().getListCount();
		final JSON json = new JSON(getResponse());
		json.beginObject();

		json.setAttribute("count", count);
		json.beginAttribute("list");
		NamedConditions c = new NamedConditions("getAll");
		getConfigService().transSelectAll(null, c,
				new QueryListCallback<Config>() {
					@Override
					public void callback(int total, List<Config> objs) {
						json.beginObject();
						if (total > 0) {
							for (Config c : objs) {
								json.setAttribute(c.getName(), c.getValue());
							}
						}
						json.endObject();
					}
				});
		json.endAttribute();
		json.endObject();
		json.end();
	}

	/**
	 * 添加广告信息
	 */
	public void createRoll() {
		String massage = getRequest().getParameter("message");
		try {
			Config c = getConfigService().selectById("message");
			if (c == null) {
				c = new Config();
				c.setName("message");
				c.setValue(massage);
				getConfigService().transCreate(null, c);
			} else {
				c.setValue(massage);
				getConfigService().transUpdate(null, c);
			}

			JSON.sendSuccess(getResponse(), true);
		} catch (Exception e) {
			JSON.sendSuccess(getResponse(), "添加广告信息失败！");
			e.printStackTrace();
		}

	}

	/**
	 * 获取广告信息
	 */
	public void getRollMassage() {
		final JSON json = new JSON(getResponse());
		json.beginObject();

		NamedConditions c = new NamedConditions("getAll");
		getConfigService().transSelectAll(null, c,
				new QueryListCallback<Config>() {
					@Override
					public void callback(int total, List<Config> objs) {
						if (total > 0) {
							for (Config c : objs) {
								if (c.getName().equals("message")) {
									json.setAttribute("message", c.getValue());
								}
							}
						}
					}
				});
		json.endObject();
		json.end();
	}

	/**
	 * 获取config类的条数
	 */
	public void getCount() {
		int count = getConfigService().getListCount();
		JSON json = new JSON(getResponse());
		json.beginObject();
		json.setAttribute("count", count);
		json.endObject();
		json.end();
	}

}
