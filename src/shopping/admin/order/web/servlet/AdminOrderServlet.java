package shopping.admin.order.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shopping.order.domain.Order;
import shopping.order.service.OrderService;
import shopping.pager.PageBean;
import shopping.user.domain.User;
import cn.itcast.servlet.BaseServlet;


@WebServlet("/admin/AdminOrderServlet")
public class AdminOrderServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private OrderService orderService = new OrderService();
	//获取当前页码
   	private int getPc(HttpServletRequest request){
   		int pc = 1;
   		String param = request.getParameter("pc");
   		if(param != null && !param.trim().isEmpty()){
   			pc = Integer.parseInt(param);
   		}
   		return pc;
   	}
   	//获取url作为分页导航中的超链接
   	private String getUrl(HttpServletRequest request){
   		//eg:http://localhost:8080/shopping/OrderServlet?method=findByUid&cid=xxx&pc=xx
   		//result:/shopping/OrderServlet+?+method=findByUid&cid=xxx&pc=xx
   		String url = request.getRequestURL()+"?"+request.getQueryString();
   		int index = url.lastIndexOf("&pc=");
   		if(index != -1){
   			url = url.substring(0,index);//截取掉pc参数
   		}
   		return url;
   	}
   	//查询所有订单
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int pc = getPc(request);
		String url = getUrl(request);
		PageBean<Order> pb = orderService.findAll(pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/adminjsps/admin/order/list.jsp";
	}

	//根据状态查询订单
	public String findByStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int pc = getPc(request);
		String url = getUrl(request);
		int status = Integer.parseInt(request.getParameter("status"));
		PageBean<Order> pb = orderService.findByStatus(status, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/adminjsps/admin/order/list.jsp";
	}	
	//加载订单
	public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String oid = request.getParameter("oid");
		String btn = request.getParameter("btn");
		Order order = orderService.load(oid);
		request.setAttribute("order", order);
		request.setAttribute("btn", btn);//btn说明了用户点击那个链接访问本方法
		return "f:/adminjsps/admin/order/desc.jsp";
	}
	//取消订单
	public String cancel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String oid = request.getParameter("oid");
		int status = orderService.findStatus(oid);
		if(status != 1){
			request.setAttribute("code", "error");
			request.setAttribute("msg", "状态不对，不能取消！");
			return "f:/adminjsps/admin/msg.jsp";
		}
		request.setAttribute("code", "success");
		request.setAttribute("msg", "您已取消订单！");
		orderService.updateStatus(oid, 5);
		return "f:/adminjsps/admin/msg.jsp";
	}
	//发货
	public String deliver(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String oid = request.getParameter("oid");
		int status = orderService.findStatus(oid);
		if(status != 2){
			request.setAttribute("code", "error");
			request.setAttribute("msg", "状态不对，不能发货！");
			return "f:/adminjsps/admin/msg.jsp";
		}
		request.setAttribute("code", "success");
		request.setAttribute("msg", "您的订单已发货！");
		orderService.updateStatus(oid, 3);
		return "f:/adminjsps/admin/msg.jsp";
	}
}
