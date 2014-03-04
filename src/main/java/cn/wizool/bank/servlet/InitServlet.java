package cn.wizool.bank.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.wizool.bank.common.MD5;
import cn.wizool.bank.common.PlatFormHttpServlet;
import cn.wizool.bank.iwebutil.JSON;
import cn.wizool.bank.model.Branch;
import cn.wizool.bank.model.Config;
import cn.wizool.bank.model.User;

public class InitServlet extends PlatFormHttpServlet {
	private static final long serialVersionUID = -4122411151734175347L;

	@Override
	public boolean process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		return super.process(request, response);
	}

	public void getAppStatus() {
		JSON.sendSuccess(getResponse());
	}

	public void systemRes() {
		Config c1 = getConfigService().selectById("maxCount");
		if (c1 == null) {
			c1 = new Config();
			c1.setName("maxCount");
			c1.setValue("20");
			getConfigService().transCreate(null, c1);
		}
		Config c2 = getConfigService().selectById("waitTime");
		if (c2 == null) {
			c2 = new Config();
			c2.setName("waitTime");
			c2.setValue("1");
			getConfigService().transCreate(null, c2);
		}

		int count = getBranchService().getCount();
		if (count == 0) {
			Branch b = new Branch();
			b.setId("root");
			b.setName("山东省农村信用社");
			getBranchService().transCreateBranch(null, b);
			User u = new User();
			u.setId(UUID.randomUUID().toString());
			u.setName("root");
			u.setPassword(MD5.Sha1("root"));
			u.setParent(b);
			getUserService().transCreate(null, u);
			JSON.sendSuccess(getResponse());
		} else if (count > 0) {
			JSON.sendErrorMessage(getResponse(), "");
		}
	}

	public void register() {
		HttpServletRequest request = getRequest();
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		User u = new User();
		u.setId(UUID.randomUUID().toString());
		u.setName(name);
		u.setPassword(MD5.Sha1(password));
		try {
			getBranchService().transInit(null, u);
			setCurrentUser(u);
			Cookie cookie = new Cookie("userName", URLEncoder.encode(name,
					"UTF-8"));
			cookie.setPath("/");
			getResponse().addCookie(cookie);
			JSON.sendSuccess(getResponse());
		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "注册失败！");
			e.printStackTrace();
		}
	}
}
