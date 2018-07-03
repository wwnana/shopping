package shopping.user.web.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import shopping.user.domain.User;
import shopping.user.service.UserService;
import shopping.user.service.exception.UserException;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

/**
 * Servlet implementation class UserServlet
 */
//@WebServlet("/UserServlet")
public class UserServlet extends BaseServlet {

	private UserService userService = new UserService();
	
	//退出功能
	public String quit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().invalidate();
		return "r:/jsps/user/login.jsp";
	}
		
	//修改密码功能
	public String updateLoginpass(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//保存页面表单数据
		User formUser = CommonUtils.toBean(request.getParameterMap(), User.class);
		//获取当前用户的uid,即获取当前用户在session中数据
		User user = (User) request.getSession().getAttribute("sessionUser");
		/*if(user == null){
			request.setAttribute("msg", "请先登录！");
			request.setAttribute("code", "error");
			return "f:/jsps/msg.jsp";
		}*/
		try {
			userService.updateLoginpass(user.getUid(), formUser.getLoginpass(), formUser.getNewpass());
			request.setAttribute("msg", "修改密码成功！");
			request.setAttribute("code", "success");
			return "f:/jsps/msg.jsp";
		} catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("user", formUser);//为了回显
			return "f:/jsps/user/pwd.jsp";
		}
	}
		
	//注册功能
	public String register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
			
		//封装表单数据到User对象中
		User formUser = CommonUtils.toBean(request.getParameterMap(), User.class);//将请求的Map类型参数转换为Bean类型，保存到User对象中
		//进行表单校验，如果校验失败，保存错误信息，转到register.jsp
		Map<String,String> errors = validateRegister(formUser,request.getSession());
		if(errors.size()>0){
			request.setAttribute("form", formUser);
			request.setAttribute("errors", errors);
			return "f:/jsps/user/register.jsp";
		}
		//使用service完成业务
		userService.register(formUser);
		//保存成功信息，转发到msg.jsp
		request.setAttribute("code", "success");//将成功信息保存到request中
		request.setAttribute("msg", "注册成功！请立即去邮箱激活！");
		return "f:/jsps/msg.jsp";//返回值表示转发(forward)或重定向(redirect)的目标页面
	}
	private Map<String,String> validateRegister(User formUser,HttpSession session){
		Map<String,String> errors = new HashMap<String,String>();
		//校验用户名
		String loginname = formUser.getLoginname();
		if(loginname == null || loginname.trim().isEmpty()){//如果用户名为空或者忽略空格后为空
			errors.put("loginname", "用户名不能为空！");
		}else if(loginname.length()<3 || loginname.length()>20){
			errors.put("loginname", "用户名长度必须在3~20之间！");
		}else if(!userService.ajaxValidateLoginname(loginname)){
			errors.put("loginname", "用户名已被注册！");
		}
		//校验密码
		String loginpass = formUser.getLoginpass();
		if(loginpass == null || loginpass.trim().isEmpty()){
			errors.put("loginpass", "密码不能为空！");
		}else if(loginpass.length()<3 || loginpass.length()>20){
			errors.put("loginpass", "密码长度必须在3~20之间！");
		}
		//校验确认密码
		String reloginpass = formUser.getReloginpass();
		if(reloginpass == null || reloginpass.trim().isEmpty()){
			errors.put("reloginpass", "确认密码不能为空！");
		}else if(!reloginpass.equals(formUser.getLoginpass())){
			errors.put("reloginpass", "两次密码不一致！");
		}
		//校验Email
		String email = formUser.getEmail();
		if(email == null || email.trim().isEmpty()){
			errors.put("email", "Email不能为空！");
		}else if(!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")){
			errors.put("email", "Email输入错误！");
		}else if(!userService.ajaxValidateEmail(email)){
			errors.put("email", "Email已被注册！");
		}
		//校验验证码
		String verifyCode = formUser.getVerifyCode();
		String vCode = (String)session.getAttribute("vCode");
		if(verifyCode == null || verifyCode.trim().isEmpty()){
			errors.put("verifyCode", "验证码不能为空！");
		}else if(!verifyCode.equalsIgnoreCase(vCode)){
			errors.put("verifyCode", "验证码输入错误！");
		}
		return errors;
	}
    //注册用户名校验
	public String ajaxValidateLoginname(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String loginname = request.getParameter("loginname");//从页面获取用户名参数
		boolean b = userService.ajaxValidateLoginname(loginname);
		response.getWriter().print(b);//传回页面
		return null;
	}
	//注册Email校验
	public String ajaxValidateEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String email = request.getParameter("email");
		boolean b = userService.ajaxValidateEmail(email);
		response.getWriter().print(b);
		return null;
	}
	//注册验证码校验
	public String ajaxValidateVerifyCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String verifyCode = request.getParameter("verifyCode");//获取页面输入框中的验证码
		String vCode = (String) request.getSession().getAttribute("vCode");//获取图片中的验证码
		boolean b = verifyCode.equalsIgnoreCase(vCode);//两者在忽略大小写的情况下进行比较
		response.getWriter().print(b);
		return null;
	}
	//用户点击邮箱中的链接时，激活功能
	public String activation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//获取激活码
		String code = (String) request.getParameter("activationCode");
		try {
			userService.activation(code);
			request.setAttribute("code", "success");
			request.setAttribute("msg", "恭喜！激活成功！请登录！");
		} catch (UserException e) {
			request.setAttribute("code", "error");
			request.setAttribute("msg", e.getMessage());//获取异常中信息
		}
		return "f:/jsps/msg.jsp";
	}
	//登录功能
	public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		//1.封装表单数据到User
		User formUser = CommonUtils.toBean(request.getParameterMap(), User.class);
		//2.校验登录名和密码
		Map<String,String> errors = validateLogin(formUser,request.getSession());
		if(errors.size()>0){
			request.setAttribute("user", formUser);
			request.setAttribute("errors", errors);
			return "f:/jsps/user/login.jsp";
		}
		//3.判断用户是否存在，若不存在或存在，但状态为false，保存错误信息，转发到login.jsp;否则，登录成功，将user保存到session中，并设置cookie
		User user = userService.login(formUser); 
		if(user == null){
			request.setAttribute("msg", "用户名或密码错误！");
			request.setAttribute("user", formUser);
			return "f:/jsps/user/login.jsp";
		}else if(!user.isStatus()){
			request.setAttribute("msg", "该用户未激活！");
			request.setAttribute("user", formUser);
			return "f:/jsps/user/login.jsp";
		}else{
			//登录成功，保存用户到session
			request.getSession().setAttribute("sessionUser", user);
			//获取用户名保存到cookie中
			String loginname = user.getLoginname();
			loginname = URLEncoder.encode(loginname, "utf-8");
			Cookie cookie = new Cookie("loginname",loginname);
			cookie.setMaxAge(1*60*60*24*10);//保存10天
			response.addCookie(cookie);
			return "r:/index.jsp";
		}
	}
	//登录功能校验
	private Map<String,String> validateLogin(User formUser,HttpSession session){
		Map<String,String> errors = new HashMap<String,String>();
		//校验验证码
		String verifyCode = formUser.getVerifyCode();
	    String vCode = (String)session.getAttribute("vCode");
		if(verifyCode == null || verifyCode.trim().isEmpty()){
			errors.put("verifyCode", "验证码不能为空！");
		}else if(!verifyCode.equalsIgnoreCase(vCode)){
			errors.put("verifyCode", "验证码输入错误！");
		}
		return errors;
	}
}
