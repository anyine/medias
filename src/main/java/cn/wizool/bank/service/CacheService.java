package cn.wizool.bank.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

import cn.wizool.bank.cache.MachineCache;
import cn.wizool.bank.iwebutil.newlay.ICommonServiceSupport;

public interface CacheService extends ICommonServiceSupport {

	public void boot(String id, String version);

	public void active(String id);

	public void downfile(String id, String fid, long size);

	public void removeFile(String id, String fid);

	public void removeTask(String id, String tid);

	public void writeDescription(String id, String tid, OutputStream os) throws IOException;

	public void loadAllMachines();

	public List<MachineCache> queryMachine(Properties cond);
	
	public void modifyTask(String tid);

	public void taskStatus(String id, String tid, String status);
}
