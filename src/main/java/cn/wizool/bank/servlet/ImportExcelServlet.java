package cn.wizool.bank.servlet;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jxl.Sheet;
import jxl.Workbook;
import cn.wizool.bank.common.MD5;
import cn.wizool.bank.common.PlatFormHttpServlet;
import cn.wizool.bank.iwebutil.newlay.QueryObjectCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.Branch;
import cn.wizool.bank.model.Document;
import cn.wizool.bank.model.Machine;
import cn.wizool.bank.model.User;

public class ImportExcelServlet extends PlatFormHttpServlet {
	private static final long serialVersionUID = 7818227723269607762L;

	/**
	 * 导入机构列表
	 */
	public void testImportMachine() {
		try {
			InputStream is = new FileInputStream("depts.xls");
			Workbook wbook = Workbook.getWorkbook(is);
			Sheet st = wbook.getSheet(0);
			int row = st.getRows();

			for (int i = 1; i < row; i++) {
				if (!(st.getCell(0, i).getContents().equals(""))) {
					String deptStr = st.getCell(0, i).getContents().trim();
					String locationStr = st.getCell(1, i).getContents().trim();
					String nameStr = st.getCell(2, i).getContents().trim();
					String ipStr = st.getCell(3, i).getContents().trim();
					String typeStr = st.getCell(4, i).getContents().trim();
					String linkman = st.getCell(5, i).getContents().trim();
					String phone = st.getCell(6, i).getContents().trim();
					String mobilephone = st.getCell(7, i).getContents().trim();

//					System.out.println(deptStr + "    " + locationStr + "    "
//							+ nameStr + "    " + ipStr + "    " + typeStr
//							+ "   " + linkman + "   " + phone + "   "
//							+ mobilephone);

					Map<String, String> map = new HashMap<String, String>();
					map.put("dept", deptStr);
					map.put("location", locationStr);
					map.put("name", nameStr);
					map.put("ip", ipStr);
					map.put("type", typeStr);
					map.put("linkman", linkman);
					map.put("phone", phone);
					map.put("mobilephone", mobilephone);

					Branch root = createRoot("枣庄农信办事处");// 创建办事处
					Branch dept = createDept(root, map);// 创建联社
					Branch location = createLocation(dept, map);// 创建网点
					createMachine(location, map);// 创建机器
				}
			}

			wbook.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建办事处
	 * 
	 * @param map
	 * @return
	 */
	private Branch createRoot(String rootName) {
		Branch root = getBranchService().getObjectById("root");
		List<Branch> list = getChilds(root);
		Branch branch = null;
		if (list.size() > 0) {
			for (Branch b : list) {
				if (b.getName().equals(rootName)) {
					branch = b;
					break;
				}
			}
		}
		if (branch == null) {
			branch = new Branch();
			branch.setId(UUID.randomUUID().toString());
			branch.setName(rootName);
			branch.setParent(root);
			getBranchService().transCreateBranch(null, branch);
			User u = new User();
			u.setId(UUID.randomUUID().toString());
			u.setName("system");
			u.setPassword(MD5.Sha1("admin"));
			u.setParent(branch);
			getUserService().transCreate(null, u);
			addChild(root, branch);
		}
		return branch;
	}

	/**
	 * 创建联社
	 * 
	 * @param map
	 * @return
	 */
	private Branch createDept(Branch root, Map<String, String> map) {
		List<Branch> list = getChilds(root);
		Branch branch = null;
		if (list.size() > 0) {
			for (Branch b : list) {
				if (b.getName().equals(map.get("dept"))) {
					branch = b;
					break;
				}
			}
		}
		if (branch == null) {
			branch = new Branch();
			branch.setId(UUID.randomUUID().toString());
			branch.setName(map.get("dept"));
			branch.setParent(root);
			getBranchService().transCreateBranch(null, branch);
			addChild(root, branch);
		}
		return branch;
	}

	/**
	 * 创建网点
	 * 
	 * @param parent
	 * @param map
	 * @return
	 */
	private Branch createLocation(Branch dept, Map<String, String> map) {
		List<Branch> list = getChilds(dept);
		Branch branch = null;
		if (list.size() > 0) {
			for (Branch b : list) {
				if (b.getName().equals(map.get("location"))) {
					branch = b;
					break;
				}
			}
		}
		if (branch == null) {
			branch = new Branch();
			branch.setId(UUID.randomUUID().toString());
			branch.setName(map.get("location"));
			branch.setParent(dept);
			getBranchService().transCreateBranch(null, branch);
			addChild(dept, branch);
		}
		return branch;
	}

	/**
	 * 创建机器
	 * 
	 * @param parent
	 * @param map
	 */
	private void createMachine(Branch location, Map<String, String> map) {
		NamedConditions cond = new NamedConditions("getMachine");
		cond.putString("BranchId", location.getId());
		cond.putString("Name", map.get("name"));
		cond.putString("Ip", map.get("ip"));
		Machine m = getMachineService().getMachine(cond);
		if (m == null) {
			m = new Machine();
			m.setId(UUID.randomUUID().toString());
			m.setName(map.get("name"));
			m.setIp(map.get("ip"));
			m.setType(map.get("type"));
			m.setLinkman(map.get("linkman"));
			m.setPhone(map.get("phone"));
			m.setMobilephone(map.get("mobilephone"));
			m.setParent(location);
			getMachineService().transCreateMachine(null, m);
		}
	}

	/**
	 * 获取所有孩子
	 * 
	 * @param parent
	 * @return
	 */
	private List<Branch> getChilds(Branch parent) {
		final List<Branch> list = new ArrayList<Branch>();
		getBranchService().transGetBranchById(null, parent.getId(),
				new QueryObjectCallback<Branch>() {
					@Override
					public void callback(Branch objs) {
						if (objs.getChild().size() > 0) {
							for (Branch b : objs.getChild()) {
								list.add(b);
							}
						}
					}
				});
		return list;
	}

	/**
	 * 添加孩子
	 * 
	 * @param parent
	 * @param child
	 */
	private void addChild(Branch parent, Branch child) {
		final Branch b = child;
		getBranchService().transGetBranchById(null, parent.getId(),
				new QueryObjectCallback<Branch>() {
					@Override
					public void callback(Branch objs) {
						objs.getChild().add(b);
					}
				});
	}

	/**
	 * 导入文件列表
	 */
	public void testImportFiles() {
		try {
			InputStream is = new FileInputStream("files.xls");
			Workbook wbook = Workbook.getWorkbook(is);
			Sheet st = wbook.getSheet(0);
			int row = st.getRows();
			for (int i = 1; i < row; i++) {
				if (!(st.getCell(0, i).getContents().equals(""))) {
					Document doc = new Document();
					doc.setId(st.getCell(0, i).getContents());
					doc.setIndex(Integer.parseInt(st.getCell(1, i)
							.getContents()));
					doc.setLength(Long
							.parseLong(st.getCell(2, i).getContents()));
					doc.setName(st.getCell(3, i).getContents());
					doc.setSurfix(st.getCell(4, i).getContents());
					doc.setType(st.getCell(5, i).getContents());

					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					doc.setUploadDate(sdf.parse(st.getCell(6, i).getContents()));

//					doc.setUploadDepartment(getBranchService().getObjectById(
//							st.getCell(7, i).getContents()));
					// doc.setUploadDepartment(getDepartmentService()
					// .selectObject(st.getCell(7, i).getContents()));
					this.getDocumentService().transCreate(null, doc);
				}
			}

			wbook.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
