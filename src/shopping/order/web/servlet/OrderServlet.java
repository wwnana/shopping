package shopping.order.web.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shopping.cart.domain.CartItem;
import shopping.cart.service.CartItemService;
import shopping.order.domain.Order;
import shopping.order.domain.OrderItem;
import shopping.order.service.OrderService;
import shopping.pager.PageBean;
import shopping.user.domain.User;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

@WebServlet("/OrderServlet")
public class OrderServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    private OrderService orderService = new OrderService();
    private CartItemService cartItemService = new CartItemService();
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
   		//eg:http://localhost:8080/goods/OrderServlet?method=findByUid&cid=xxx&pc=xx
   		//result:/goods/OrderServlet+?+method=findByUid&cid=xxx&pc=xx
   		String url = request.getRequestURL()+"?"+request.getQueryString();
   		int index = url.lastIndexOf("&pc=");
   		if(index != -1){
   			url = url.substring(0,index);//截取掉pc参数
   		}
   		return url;
   	}
   	//我的订单
	public String myOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int pc = getPc(request);
		String url = getUrl(request);
		User user = (User)request.getSession().getAttribute("sessionUser");
		PageBean<Order> pb = orderService.myOrders(user.getUid(), pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/jsps/order/list.jsp";
	}
	//生成订单
	public String createOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取所有购物车条目的id，查询之
		String cartItemIds = request.getParameter("cartItemIds");
		List<CartItem> cartItemList = cartItemService.loadCartItems(cartItemIds);
		//创建order
		Order order = new Order();
		order.setOid(CommonUtils.uuid());
		order.setOrdertime(String.format("%tF %<tT", new Date()));
		order.setStatus(1);
		order.setAddress(request.getParameter("address"));
		User owner = (User)request.getSession().getAttribute("sessionUser");
		order.setOwner(owner);
		
		BigDecimal total = new BigDecimal("0");
		for(CartItem cartItem : cartItemList){
			total = total.add(new BigDecimal(cartItem.getSubtotal()+""));
		}
		order.setTotal(total.doubleValue());
		//创建List<OrderItem>,一个CartItem对应一个OrderItem
		List<OrderItem> orderItemList = new ArrayList();
		for(CartItem cartItem : cartItemList){
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderItemId(CommonUtils.uuid());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setSubtotal(cartItem.getSubtotal());
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setOrder(order);
			orderItemList.add(orderItem);
		}
		order.setOrderItemList(orderItemList);
		
		orderService.createOrders(order);
		//删除购物车条目
		cartItemService.batchDelete(cartItemIds);
		
		request.setAttribute("order", order);
		
		return "f:/jsps/order/ordersucc.jsp";
	}
	//加载订单
	public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String oid = request.getParameter("oid");
		String btn = request.getParameter("btn");
		Order order = orderService.load(oid);
		request.setAttribute("order", order);
		request.setAttribute("btn", btn);//btn说明了用户点击那个链接访问本方法
		return "f:/jsps/order/desc.jsp";
	}
	//取消订单
	public String cancel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String oid = request.getParameter("oid");
		int status = orderService.findStatus(oid);
		if(status != 1){
			request.setAttribute("code", "error");
			request.setAttribute("msg", "状态不对，不能取消！");
			return "f:/jsps/msg.jsp";
		}
		request.setAttribute("code", "success");
		request.setAttribute("msg", "您已取消订单！");
		orderService.updateStatus(oid, 5);
		return "f:/jsps/msg.jsp";
	}
	//确认收货
	public String confirm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String oid = request.getParameter("oid");
		int status = orderService.findStatus(oid);
		if(status != 3){
			request.setAttribute("code", "error");
			request.setAttribute("msg", "状态不对，不能确认收货！");
			return "f:/jsps/msg.jsp";
		}
		request.setAttribute("code", "success");
		request.setAttribute("msg", "您已确认收货！");
		orderService.updateStatus(oid, 4);
		return "f:/jsps/msg.jsp";
	}
	
	//加载订单
	public String paymentPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("oid", request.getParameter("oid"));
		return payment(request,response);
	}
	
	//支付方法
	public String payment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String oid = request.getParameter("oid");
		int status = orderService.findStatus(oid);
		if(status != 1){
			request.setAttribute("code", "error");
			request.setAttribute("msg", "状态不对，不能支付！");
			return "f:/jsps/msg.jsp";
		}
		request.setAttribute("code", "success");
		request.setAttribute("msg","您已支付成功！");
		orderService.updateStatus(oid, 2);
		return "f:/jsps/msg.jsp";
	}
}
