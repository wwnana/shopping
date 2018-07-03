package shopping.category.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shopping.category.domain.Category;
import shopping.category.service.CategoryService;
import cn.itcast.servlet.BaseServlet;

public class CategoryServlet extends BaseServlet{

	private CategoryService categoryService = new CategoryService();
	//查找所有分类
	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<Category> parents = categoryService.findAll();
		req.setAttribute("parents", parents);
		
		return "f:/jsps/left.jsp";
	}

	
}
