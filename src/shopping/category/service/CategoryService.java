package shopping.category.service;

import java.sql.SQLException;
import java.util.List;

import shopping.category.dao.CategoryDao;
import shopping.category.domain.Category;



public class CategoryService {

	private CategoryDao categoryDao = new CategoryDao();
	//查找所有分类
	public List<Category> findAll(){
		try {
			return categoryDao.findAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//添加分类
	public void add(Category category){
		try {
			categoryDao.add(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//查询所有一级分类
	public List<Category> findParents(){
		try {
			return categoryDao.findParents();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//加载category
	public Category load(String cid){
		try {
			return categoryDao.load(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//修改分类
	public void edit(Category category){
		try {
			categoryDao.edit(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//根据pid查询子分类个数
	public int findChildrenCountByParent(String pid){
		try {
			return categoryDao.findChildrenCountByParent(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}		
	}
	//删除分类
	public void deleteCategory(String cid){
		try {
			categoryDao.deleteCategory(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//查询指定父分类下的子分类
	public List<Category> findChildren(String pid){
		try {
			return categoryDao.findByParent(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}	
}
