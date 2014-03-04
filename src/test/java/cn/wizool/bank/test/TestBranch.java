package cn.wizool.bank.test;

import cn.wizool.bank.dao.BranchDao;
import cn.wizool.bank.dao.MachineDao;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Branch;
import cn.wizool.bank.model.Machine;
import cn.wizool.bank.service.BranchService;

// abstract
public abstract class TestBranch extends AbstractServiceTestCase {

	BranchDao bd;
	MachineDao md;
	BranchService bs;
	String str;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		bs = (BranchService) this.sf.getServiceManager("platform").getService(
				"BranchService");
		bd = (BranchDao) this.daf.getDataAccessManager("platform")
				.getDataAccessObject("BranchDao");
		md = (MachineDao) this.daf.getDataAccessManager("platform")
				.getDataAccessObject("MachineDao");
	}

	public void testGetAllMachines() {
		NamedConditions cond = new NamedConditions("getAllMachines");
		for (Machine m : md.select(0, Integer.MAX_VALUE, cond)) {
			System.out.println(m.getParent().getParent().getName() + "  "
					+ m.getParent().getName() + "  " + m.getType());
		}
	}

	public void testGetChildArr() {
		String parentId = "13c84789-c9b4-432b-9656-8e30a70ee73a";
		String[] strArr = bs.getIdsByCurrentId(parentId);
		// System.out.println(strArr.length);
		// for (String s : strArr) {
		// System.out.println(s);
		// }
	}

	public void testBranchCount() {
		String id = "c5448ca2-af9d-41a0-9072-bb637332bd3f";
		Machine m = md.select(id);
		String s = getParentName(m);
		String str = "null—>枣庄农信办事处—>市中联社—>市郊营业室";
		// System.out.println(s);
		// System.out.println(str.substring(str.indexOf(">") + 1));
	}

	private String getParentName(Machine m) {
		return getName(m.getParent());
	}

	private String getName(Branch parent) {
		if (parent != null) {
			str = getName(parent.getParent()) + "—>" + parent.getName();
		}
		return str;
	}
}
