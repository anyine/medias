package cn.wizool.bank.service.impl;

import java.util.List;

import cn.wizool.bank.common.PlatFormServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.QueryObjectCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Branch;
import cn.wizool.bank.model.User;
import cn.wizool.bank.service.BranchService;

public class BranchServiceImpl extends PlatFormServiceSupport implements
		BranchService {

	@Override
	public void transInit(String uid, User u) {
		Branch branch = new Branch();
		branch.setId("root");
		branch.setName("枣庄农信办事处");
		getBranchDao().create(branch);

		u.setParent(branch);
		getUserDao().create(u);
	}

	@Override
	public void transCreateBranch(String uid, Branch b) {
		getBranchDao().create(b);
	}

	@Override
	public void transUpdateBranch(String uid, String id, String name) {
		Branch b = getBranchDao().select(id);
		b.setName(name);
		getBranchDao().update(b);
	}

	@Override
	public void transDeleteBranch(String uid, String id) {
		Branch b = getBranchDao().select(id);
		if (b.getParent() != null) {
			b.getParent().getChild().remove(b);
		}
		getBranchDao().delete(b);
	}

	@Override
	public void transGetBranchById(String uid, String id,
			QueryObjectCallback<Branch> callback) {
		callback.callback(getBranchDao().select(id));
	}

	@Override
	public Branch getObjectById(String id) {
		return getBranchDao().select(id);
	}

	@Override
	public Branch getBranch(NamedConditions cond) {
		return getBranchDao().select(cond);
	}

	@Override
	public void transSelect(String uid, int start, int limit,
			NamedConditions cond, QueryListCallback<Branch> callback) {
		callback.callback(getBranchDao().count(cond),
				getBranchDao().select(start, limit, cond));
	}

	@Override
	public int getCount() {
		return getBranchDao().count();
	}

	@Override
	public List<Branch> getListByCond(NamedConditions cond) {
		return getBranchDao().select(0, Integer.MAX_VALUE, cond);
	}

	@Override
	public String[] getIdsByCurrentId(String id) {
		String str = getIdsStr(id) + id;
		return str.split(";");
	}

	private String getIdsStr(String id) {
		String str = "";
		NamedConditions cond = new NamedConditions("getChildIdArrByBranchId");
		cond.putString("ParentId", id);
		List<Branch> list = getListByCond(cond);
		for (Branch b : list) {
			str += b.getId() + ";";
			str += getIdsStr(b.getId());
		}
		return str;
	}
}
