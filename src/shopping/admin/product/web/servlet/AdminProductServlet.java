package shopping.admin.product.web.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import shopping.category.domain.Category;
import shopping.category.service.CategoryService;
import shopping.pager.PageBean;
import shopping.product.domain.Product;
import shopping.product.service.ProductService;
import cn.itcast.servlet.BaseServlet;

@WebServlet("/admin/AdminProductServlet")
public class AdminProductServlet extends BaseServlet{
	private static final long serialVersionUID = 1L;
	private CategoryService categoryService = new CategoryService();
	private ProductService productService = new ProductService();
	
	//查找所有分类
	public String findCategoryAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Category> parents = categoryService.findAll();
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/product/left.jsp";
	}
	
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
	//加载图书
	public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pid = request.getParameter("pid");
		Product product = productService.load(pid);
		request.setAttribute("product", product);
		
		request.setAttribute("parents", categoryService.findParents());
		
		//获取当前图书所属一级分类的所有二级分类
		String paid = product.getCategory().getParent().getCid();
		request.setAttribute("children", categoryService.findChildren(paid));
		
		return "f:/adminjsps/admin/product/desc.jsp";
	}
	//按分类查找
	public String findByCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int pc = getPc(request);
		String url = getUrl(request);
		String cid = request.getParameter("cid");
		PageBean<Product> pb = productService.findByCategory(cid, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/adminjsps/admin/product/list.jsp";
	}
	//按产品名模糊查找
	public String findByPname(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int pc = getPc(request);
		String url = getUrl(request);
		String pname = request.getParameter("pname");
		PageBean<Product> pb = productService.findByPname(pname, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/adminjsps/admin/product/list.jsp";
	}
	//按生产地模糊查找
	public String findByPress(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int pc = getPc(request);
		String url = getUrl(request);
		String place = request.getParameter("place");
		PageBean<Product> pb = productService.findByPlace(place, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/adminjsps/admin/product/list.jsp";
	}
	//多条件组合查询
	public String findByCombination(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int pc = getPc(request);
		String url = getUrl(request);
		Product criteria = CommonUtils.toBean(request.getParameterMap(), Product.class);
		PageBean<Product> pb = productService.findByCombination(criteria, pc);
		pb.setUrl(url);
		request.setAttribute("pb", pb);
		return "f:/adminjsps/admin/product/list.jsp";
	}

	//添加产品：显示所有一级分类
	public String addPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Category> parents = categoryService.findParents();
		request.setAttribute("parents", parents);
		return "f:/adminjsps/admin/product/add.jsp";
	}
	//异步请求显示一级分类下的二级分类
	public String ajaxFindChildren(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String paid = request.getParameter("paid");
		List<Category> children = categoryService.findChildren(paid);
		String json = toJson(children);
		response.getWriter().print(json);
		return null;
	}

	//{"cid":"xxx","cname":"xxxx"}
	private String toJson(Category category) {
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"cid\"").append(":").append("\"").append(category.getCid()).append("\"");
	    sb.append(",");
		sb.append("\"cname\"").append(":").append("\"").append(category.getCname()).append("\"");
		sb.append("}");
		return sb.toString();
	}
	//[{"cid":"xxx","cname":"xxxx"},{"cid":"xxx","cname":"xxxx"}]
	private String toJson(List<Category> categoryList) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder("[");
		for(int i=0;i<categoryList.size();i++){
			sb.append(toJson(categoryList.get(i)));
			if(i<categoryList.size()-1){
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	//编辑产品
	public String edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map map = request.getParameterMap();
		Product product = CommonUtils.toBean(map, Product.class);
		Category category = CommonUtils.toBean(map, Category.class);
		product.setCategory(category);
		productService.edit(product);
		request.setAttribute("msg", "修改产品成功！");
		
		return "f:/adminjsps/msg.jsp";
	}
	
	//删除产品
	public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pid = request.getParameter("pid");
		
		//删除大小图图片
		Product product = productService.load(pid);
		
		String savepath = this.getServletContext().getRealPath("/");//获取绝对路径
		//System.out.println("savepath="+savepath);
		new File(savepath,product.getImage_b()).delete();//删除小图
		new File(savepath,product.getImage_w()).delete();//删除大图
		
		productService.delete(pid);
		request.setAttribute("msg", "删除产品成功！");
		
		return "f:/adminjsps/msg.jsp";
	}

}
