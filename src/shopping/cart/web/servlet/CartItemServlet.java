package shopping.cart.web.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import shopping.product.domain.Product;

import shopping.cart.domain.CartItem;
import shopping.cart.service.CartItemService;
import shopping.user.domain.User;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

@WebServlet("/CartItemServlet")
public class CartItemServlet extends BaseServlet {
	
	private CartItemService cartItemService = new CartItemService();
	
	//添加购物车条目
	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//从页面可以获取pid和quantity
		Map map = request.getParameterMap();
		CartItem cartItem = CommonUtils.toBean(map, CartItem.class);//quantity
		Product product = CommonUtils.toBean(map, Product.class);//pid
		User user = (User) request.getSession().getAttribute("sessionUser");//uid
		if(user == null){
			request.setAttribute("code", "error");
			request.setAttribute("msg", "请先登录！");
			request.getRequestDispatcher("/jsps/msg.jsp").forward(request, response);
		}
		else {
			cartItem.setProduct(product);
			cartItem.setUser(user);
			cartItemService.add(cartItem);
			
			return myCart(request,response);
		}
		return null;
	}
	
	//查询我的购物车
	public String myCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		User user = (User)request.getSession().getAttribute("sessionUser");
		String uid = user.getUid();
		List<CartItem> cartItemList = cartItemService.myCart(uid);
		request.setAttribute("cartItemList", cartItemList);
		return "f:/jsps/cart/list.jsp";
	}

	//批量删除
	public String batchDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cartItemIds = request.getParameter("cartItemIds");
		cartItemService.batchDelete(cartItemIds);
		return myCart(request,response);
	}
	
	//修改购物车条目
	public String updateQuantity(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cartItemId = request.getParameter("cartItemId");
		int quantity = Integer.parseInt(request.getParameter("quantity"));
		CartItem cartItem = cartItemService.updateQuantity(cartItemId, quantity);
		//以json格式返回quantity和subtotal
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"quantity\"").append(":").append(cartItem.getQuantity());
		sb.append(",");
		sb.append("\"subtotal\"").append(":").append(cartItem.getSubtotal());
		sb.append("}");
		response.getWriter().print(sb);

		return null;
	}
	
	//生成订单：根据cartItemIds查找所有cartItem
	public String loadCartItems(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cartItemIds = request.getParameter("cartItemIds");
		double total = Double.parseDouble(request.getParameter("total"));
		List<CartItem> cartItemList = cartItemService.loadCartItems(cartItemIds);
		request.setAttribute("cartItemList", cartItemList);
		request.setAttribute("total", total);
		request.setAttribute("cartItemIds", cartItemIds);
		return "f:/jsps/cart/showitem.jsp";
	}
}
