package cn.wizool.bank.service;

import java.util.List;

import cn.wizool.bank.iwebutil.newlay.ICommonServiceSupport;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.QueryObjectCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Branch;
import cn.wizool.bank.model.User;

public interface BranchService extends ICommonServiceSupport {

	public abstract void transInit(String uid, User u);

	public abstract void transCreateBranch(String uid, Branch b);

	public abstract void transUpdateBranch(String uid, String id, String name);

	public abstract void transDeleteBranch(String uid, String id);

	public abstract void transGetBranchById(String uid, String id,
			QueryObjectCallback<Branch> callback);

	public abstract Branch getBranch(NamedConditions cond);

	public abstract Branch getObjectById(String id);

	public abstract void transSelect(String uid, int start, int limit,
			NamedConditions cond, QueryListCallback<Branch> callback);

	public abstract int getCount();

	public abstract List<Branch> getListByCond(NamedConditions cond);

	public abstract String[] getIdsByCurrentId(String id);

}
