package cn.wizool.bank.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import cn.wizool.bank.common.PlatFormHttpServlet;
import cn.wizool.bank.iwebutil.DateUtil;
import cn.wizool.bank.model.Document;

public class ExportExcelServlet extends PlatFormHttpServlet {
	private static final long serialVersionUID = -8560484799690498477L;

	/**
	 * 导出文件列表
	 */
	public void testExportFiles() {
		HttpServletResponse res = this.getResponse();
		try {
			// 获取输出流
			OutputStream os = res.getOutputStream();
			// 清空输出流
			res.reset();
			// 设定输出文件头
			res.setHeader("Content-disposition",
					"attachment; filename=files.xls");
			// 定义输出类型
			res.setContentType("application/msexcel");

			// 建立Excel文件
			WritableWorkbook wbook = Workbook.createWorkbook(os);
			// 工作表名称
			WritableSheet sheet1 = wbook.createSheet("文件列表", 0);

			// 设置Excel字体
			// WritableFont font = new WritableFont();

			WritableCellFormat title = new WritableCellFormat();
			String[] titleArr = { "ID", "IND", "长度", "名称", "后缀", "类型", "修改时间",
					"修改联社ID" };
			// 设置Excel表头
			for (int i = 0; i < titleArr.length; i++) {
				Label t = new Label(i, 0, titleArr[i], title);
				sheet1.addCell(t);
			}

			// 从数据库获取数据列表
			List<Document> list = getDocumentService().getListAll();
			for (int j = 0; j < list.size(); j++) {
				sheet1.addCell(new Label(0, j + 1, list.get(j).getId()));
				sheet1.addCell(new Label(1, j + 1, String.valueOf(list.get(j)
						.getIndex())));
				sheet1.addCell(new Label(2, j + 1, String.valueOf(list.get(j)
						.getLength())));
				sheet1.addCell(new Label(3, j + 1, list.get(j).getName()));
				sheet1.addCell(new Label(4, j + 1, list.get(j).getSurfix()));
				sheet1.addCell(new Label(5, j + 1, list.get(j).getType()));
				sheet1.addCell(new Label(6, j + 1, DateUtil.format(list.get(j)
						.getUploadDate())));
//				sheet1.addCell(new Label(7, j + 1, list.get(j)
//						.getUploadDepartment().getId()));
			}

			// 写入文件
			wbook.write();
			wbook.close();
			// 关闭流
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 导出机构列表
	 * 
	 * 原来的架构
	 */
	public void testExportDepartment() {
		HttpServletResponse res = this.getResponse();
		try {
			// 获取输出流
			OutputStream os = res.getOutputStream();
			// 清空输出流
			res.reset();
			// 设定输出文件头
			res.setHeader("Content-disposition",
					"attachment; filename=depts.xls");
			// 定义输出类型
			res.setContentType("application/msexcel");

			// 建立Excel文件
			WritableWorkbook wbook = Workbook.createWorkbook(os);
			// 工作表名称
			WritableSheet sheet1 = wbook.createSheet("机构列表", 0);

			// 设置Excel字体
			// WritableFont font = new WritableFont();

			WritableCellFormat title = new WritableCellFormat();
			String[] titleArr = { "联社名称", "网点名称", "设备名称", "设备IP", "类型" };
			// 设置Excel表头
			for (int i = 0; i < titleArr.length; i++) {
				Label t = new Label(i, 0, titleArr[i], title);
				sheet1.addCell(t);
			}

			// 从数据库获取数据列表
			// NamedConditions cond = new NamedConditions("getListByType");
			// List<Department> list = getDepartmentService().transSelectByCond(
			// cond);
			// for (int j = 0; j < list.size(); j++) {
			// sheet1.addCell(new Label(0, j + 1, list.get(j).getParent()
			// .getParent().getName()));
			// sheet1.addCell(new Label(1, j + 1, list.get(j).getParent()
			// .getName()));
			// sheet1.addCell(new Label(2, j + 1, list.get(j).getName()));
			// sheet1.addCell(new Label(3, j + 1, list.get(j).getIp()));
			// sheet1.addCell(new Label(4, j + 1, list.get(j).getType()));
			// }

			// 写入文件
			wbook.write();
			wbook.close();
			// 关闭流
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
