package shopping.admin.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shopping.admin.domain.Admin;
import shopping.admin.service.AdminService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;


@WebServlet("/AdminServlet")
public class AdminServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
    private AdminService adminService = new AdminService();
    
    //登录后台
  	public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  		Admin form = CommonUtils.toBean(request.getParameterMap(), Admin.class);
  		Admin admin = adminService.login(form);
  		if(admin == null){
  			request.setAttribute("msg", "用户名或者密码错误!");
  			return "f:/adminjsps/login.jsp";
  		}
  		request.getSession().setAttribute("admin", admin);
  		return "r:/adminjsps/admin/index.jsp";
  	}

}
