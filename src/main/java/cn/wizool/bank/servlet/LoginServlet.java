package cn.wizool.bank.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.wizool.bank.common.MD5;
import cn.wizool.bank.common.PlatFormHttpServlet;
import cn.wizool.bank.iwebutil.JSON;
import cn.wizool.bank.iwebutil.newlay.QueryListCallback;
import cn.wizool.bank.iwebutil.newlay.QueryObjectCallback;
import cn.wizool.bank.iwebutil.newlay.query.NamedConditions;
import cn.wizool.bank.model.User;

public class LoginServlet extends PlatFormHttpServlet {
	private static final long serialVersionUID = -9007436179227292776L;

	@Override
	public boolean process(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		return super.process(request, response);
	}

	public void login() {
		HttpServletRequest request = getRequest();
		final String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		NamedConditions cond = new NamedConditions("login");
		cond.putString("Name", userName);
		cond.putString("Password", MD5.Sha1(password));
		try {
			getUserService().transSelectByCond(null, 0, Integer.MAX_VALUE, cond,
					new QueryListCallback<User>() {
						@Override
						public void callback(int total, List<User> objs) {
							if (objs.size() == 1) {
								setCurrentUser(objs.get(0));
								try {
									Cookie cookie;
									cookie = new Cookie("userName", URLEncoder
											.encode(userName, "UTF-8"));
									cookie.setPath("/");
									getResponse().addCookie(cookie);
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
								JSON.sendSuccess(getResponse());
							} else {
								JSON.sendErrorMessage(getResponse(),
										"用户名或密码错误!");
							}
						}
					});
		} catch (Exception e) {
			JSON.sendErrorMessage(getResponse(), "登录失败！");
			e.printStackTrace();
		}
	}

	public void modifyPassword() {
		HttpServletRequest request = getRequest();
		final String oldPassword = request.getParameter("oldPassword");
		final String newPassword = request.getParameter("newPassword");
		String id = getCurrentUser().getId();
		getUserService().transSelectById(null, id,
				new QueryObjectCallback<User>() {
					@Override
					public void callback(User objs) {
						if (objs.getPassword().equals(MD5.Sha1(oldPassword))) {
							objs.setPassword(MD5.Sha1(newPassword));
							JSON.sendSuccess(getResponse());
						} else {
							JSON.sendErrorMessage(getResponse(), "原密码输入有误!");
						}
					}
				});
	}
}
