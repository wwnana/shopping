package shopping.product.service;

import java.sql.SQLException;

import shopping.pager.PageBean;
import shopping.product.dao.ProductDao;
import shopping.product.domain.Product;


public class ProductService {
	private ProductDao productDao = new ProductDao();
	//按pid加载产品
	public Product load(String pid){
		try {
			return productDao.findByPid(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//按分类查找
	public PageBean<Product> findByCategory(String cid,int pc){
		try {
			return productDao.findByCategory(cid, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//按产品名模糊查找
	public PageBean<Product> findByPname(String pname,int pc){
		try {
			return productDao.findByPname(pname, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//按生产地模糊查找
	public PageBean<Product> findByPlace(String place,int pc){
		try {
			return productDao.findByPlace(place, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}	
	//按多条件组合模糊查找
	public PageBean<Product> findByCombination(Product criteria,int pc){
		try {
			return productDao.findByCombination(criteria, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}	
	//根据二级分类id查询产品个数
	public int findProductCountByCategory(String cid){
		try {
			return productDao.findProductCountByCategory(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//添加产品
	public void add(Product Product){
		try {
			productDao.add(Product);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//编辑产品
	public void edit(Product Product){
		try {
			productDao.edit(Product);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//删除产品
	public void delete(String pid){
		try {
			productDao.delete(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
