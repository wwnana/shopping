package shopping.product.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shopping.category.domain.Category;
import shopping.pager.PageBean;
import shopping.product.domain.Product;
import shopping.product.service.ProductService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

@WebServlet("/ProductServlet")
public class ProductServlet extends BaseServlet{
	private ProductService productService = new ProductService();


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
		//eg:http://localhost:8080/shopping/ProductServlet?method=findByCategory&cid=xxx&pc=xx
		//result:/shopping/ProductServlet+?+method=findByCategory&cid=xxx&pc=xx
		String url = request.getRequestURL()+"?"+request.getQueryString();
		int index = url.lastIndexOf("&pc=");
		if(index != -1){
			url = url.substring(0,index);//截取掉pc参数
		}
		return url;
	}
	//加载产品
	public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pid = request.getParameter("pid");
		Product product = productService.load(pid);
		Category category=product.getCategory();
		Category parent=category.getParent();
		request.setAttribute("product", product);
		request.setAttribute("parent", parent);
		return "f:/jsps/product/desc.jsp";
	}
	//按分类查找
	public String findByCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int pc = getPc(request);
		String url = getUrl(request);
		String cid = request.getParameter("cid");
		PageBean<Product> pb = productService.findByCategory(cid, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/jsps/product/list.jsp";
	}
	//按产品名模糊查找
	public String findByPname(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int pc = getPc(request);
		String url = getUrl(request);
		String pname = request.getParameter("pname");
		PageBean<Product> pb = productService.findByPname(pname, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/jsps/product/list.jsp";
	}
	//按生产地模糊查找
	public String findByPlace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int pc = getPc(request);
		String url = getUrl(request);
		String place = request.getParameter("place");
		PageBean<Product> pb = productService.findByPlace(place, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/jsps/product/list.jsp";
	}
	//多条件组合查询
	public String findByCombination(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int pc = getPc(request);
		String url = getUrl(request);
		Product criteria = CommonUtils.toBean(request.getParameterMap(), Product.class);
		PageBean<Product> pb = productService.findByCombination(criteria, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/jsps/product/list.jsp";
	}
}
