package cn.wizool.bank.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import cn.wizool.bank.common.PlatFormServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Task;
import cn.wizool.bank.model.TaskLog;
import cn.wizool.bank.service.TaskLogService;

public class TaskLogServiceImpl extends PlatFormServiceSupport implements
		TaskLogService {

	@Override
	public void transSelect(String uid, int start, int limit,
			NamedConditions c, QueryListCallback<TaskLog> callback) {
		List<TaskLog> list = getTaskLogDao().select(start, limit, c);
		callback.callback(getTaskLogDao().count(c), list);
	}

	@Override
	public void transCreate(String uid, String tid, String id, String type) {
		// if (type.equals("STARTTEMPTASK")) {
		// Media m = new Media();
		// m.setId(tid);
		// m.setName("临时任务");
		// m.setDisplay(true);
		// m.setPubishDate(new Date());
		// m.setPublisher(getDepartmentDao().select(uid));
		// List<Department> list = new ArrayList<Department>();
		// list.add(getDepartmentDao().select(id));
		// m.setDepartments(list);
		// getMediaDao().create(m);
		// }

		String str = "";
		TaskLog tl = new TaskLog();
		// Department d = getDepartmentDao().select(id);
		Task t = getTaskDao().select(tid);
		tl.setId(UUID.randomUUID().toString());
		tl.setTask(t);
		tl.setDepartment(getMachineDao().select(id));
		if (type.equals("STARTTASK")) {
			str = "开始任务";
		} else if (type.equals("ENDTASK")) {
			str = "结束任务";
		} else if (type.equals("REMOVETASK")) {
			str = "删除任务";
		}
		// else if (type.equals("STARTTEMPTASK")) {
		// str = "开启临时任务";
		// } else if (type.equals("ENDTEMPTASK")) {
		// str = "结束临时任务";
		// }
		tl.setOperateType(str);
		tl.setBirth(new Date());
		getTaskLogDao().create(tl);
	}

	@Override
	public void transDeleteSelected(String uid, String[] ids) {
		getTaskLogDao().delete(ids);
	}

	@Override
	public void transDeleteAll(String uid, NamedConditions cond) {
		List<TaskLog> list = getTaskLogDao().select(0, Integer.MAX_VALUE, cond);
		for (TaskLog t : list) {
			if (t != null) {
				getTaskLogDao().delete(t);
			}
		}
	}

	@Override
	public int selectCountByCond(NamedConditions c) {
		return getTaskLogDao().count(c);
	}

}
