package shopping.category.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import shopping.category.domain.Category;
import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class CategoryDao {
	private QueryRunner qr = new TxQueryRunner();
	
	//将map类型转换成category类型
	private Category toCategory(Map<String,Object> map){
		//map{cid:xx,cname:xx,paid:xx,desc:xx,orderBy:xx}
		//category{cid:xx,cname:xx,parent:xx,desc:xx}parent是一个对象
		Category category = CommonUtils.toBean(map, Category.class);
		String paid = (String) map.get("paid");
		if(paid != null){
			Category parent = new Category();//创建一个对象
			parent.setCid(paid);
			category.setParent(parent);
		}
		return category;
	}
	//将maplist类型转换为category类型
	private List<Category> toCategoryList(List<Map<String,Object>> mapList){
		List<Category> categoryList = new ArrayList<Category>();
		for(Map<String,Object> map : mapList ){
			Category c = toCategory(map);
			categoryList.add(c);
			
		}
		return categoryList;
	}
	
	public List<Category> findAll() throws SQLException{
		String sql = "select * from category where paid is null";//查询所有一级分类
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler());
		
		List<Category> parents = toCategoryList(mapList);
		//循环遍历每一个父类，为其加载子类
		for(Category parent : parents){
			List<Category> children = findByParent(parent.getCid());
			parent.setChildren(children);
		}
		return parents;
	}
	//通过父类查询子类
	public List<Category> findByParent(String paid) throws SQLException{
		String sql = "select * from category where paid=?";
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(),paid);
		return toCategoryList(mapList);
	}
	//添加分类
	public void add(Category category) throws SQLException{
		String sql = "insert into category(cid,cname,paid,`desc`) values(?,?,?,?)";
		//判断一级分类还是二级分类
		String paid=null;
		if(category.getParent() != null){
			paid = category.getParent().getCid();
		}
		Object[] params = {category.getCid(),category.getCname(),paid,category.getDesc()};
		qr.update(sql,params);
	}
	
	//查找所有一级分类
	public List<Category> findParents() throws SQLException{
		String sql = "select * from category where paid is null";//查询所有一级分类
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler());
		
		return toCategoryList(mapList);
	}	
	
	//通过cid加载Category
	public Category load(String cid) throws SQLException{
		String sql = "select * from category where cid=?";
		Map<String,Object> map = qr.query(sql, new MapHandler(),cid);
		return toCategory(map);
	}
	
	//修改category
	public void edit(Category category) throws SQLException{
		String sql ="update category set cname=?,paid=?,`desc`=? where cid=?";
		String paid = null;
		if(category.getParent() != null){
			paid = category.getParent().getCid();
		}
		Object[] params = {category.getCname(),paid,category.getDesc(),category.getCid()};
 		qr.update(sql,params);
	}
	
	//根据父分类id查询子分类个数
	public int findChildrenCountByParent(String paid) throws SQLException{
		String sql = "select count(*) from category where paid=?";
		Number cnt = (Number)qr.query(sql, new ScalarHandler(),paid);
		return cnt == null ? 0 : cnt.intValue();
	}
	
	//删除分类
	public void deleteCategory(String cid) throws SQLException{
		String sql = "delete from category where cid=?";
		qr.update(sql,cid);
	}
}
