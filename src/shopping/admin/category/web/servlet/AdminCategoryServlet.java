package shopping.admin.category.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import shopping.product.service.ProductService;

import shopping.category.domain.Category;
import shopping.category.service.CategoryService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;


@WebServlet("/admin/AdminCategoryServlet")
public class AdminCategoryServlet extends BaseServlet {
	
	private CategoryService categoryService = new CategoryService();
	private ProductService productService = new ProductService();
	
	//查询分类
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("parents", categoryService.findAll());
		return "f:/adminjsps/admin/category/list.jsp";
	}
	//添加父分类
	public String addParent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Category parent = CommonUtils.toBean(request.getParameterMap(), Category.class);
		parent.setCid(CommonUtils.uuid());
		categoryService.add(parent);
		return findAll(request,response);
	}
	//添加子分类做准备，即显示一级下拉框
	public String addChildPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String paid = request.getParameter("paid");//点击当前的父分类id
		List<Category> parents = categoryService.findParents();
		
		request.setAttribute("paid", paid);
		request.setAttribute("parents", parents);
		
		return "f:/adminjsps/admin/category/add2.jsp";
		
	}
	//添加子分类
	public String addChild(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Category child = CommonUtils.toBean(request.getParameterMap(), Category.class);
		child.setCid(CommonUtils.uuid());
		Category parent = new Category();
		String paid = request.getParameter("paid");
		parent.setCid(paid);
		child.setParent(parent);
		categoryService.add(child);
		return findAll(request,response);
	}
	//修改父分类做准备，即显示文本框信息
	public String editParentPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cid = request.getParameter("cid");//点击当前的父分类id
		Category parent = categoryService.load(cid);
		
		request.setAttribute("parent", parent);
		
		return "f:/adminjsps/admin/category/edit.jsp";
		
	}
	//修改父分类
	public String editParent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Category parent = CommonUtils.toBean(request.getParameterMap(), Category.class);
		categoryService.edit(parent);
		return findAll(request,response);
	}
	//修改子分类做准备，即显示一级下拉框与信息
	public String editChildPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cid = request.getParameter("cid");//点击当前的子分类id
		Category child = categoryService.load(cid);
		
		request.setAttribute("child", child);
		request.setAttribute("parents", categoryService.findParents());
		
		return "f:/adminjsps/admin/category/edit2.jsp";
		
	}
	//修改子分类
	public String editChild(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Category child = CommonUtils.toBean(request.getParameterMap(), Category.class);
		String paid = request.getParameter("paid");
		Category parent = new Category();
		parent.setCid(paid);
		child.setParent(parent);
		categoryService.edit(child);
		return findAll(request,response);
	}	
	
	//删除父分类
	public String deleteParent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cid = request.getParameter("cid");
		int num = categoryService.findChildrenCountByParent(cid);
		if(num != 0){
			request.setAttribute("msg", "该一级分类下还有二级分类，不能删除！");
			return "f:/adminjsps/msg.jsp";
		}
		categoryService.deleteCategory(cid);
		return findAll(request,response);
	}
	//删除子分类
	public String deleteChild(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cid = request.getParameter("cid");
		int num = productService.findProductCountByCategory(cid);
		if(num != 0){
			request.setAttribute("msg", "该分类下还有产品，不能删除！");
			return "f:/adminjsps/msg.jsp";
		}
		categoryService.deleteCategory(cid);
		return findAll(request,response);
	}
}
