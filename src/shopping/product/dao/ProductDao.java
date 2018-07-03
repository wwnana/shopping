package shopping.product.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import shopping.category.domain.Category;
import shopping.pager.Expression;
import shopping.pager.PageBean;
import shopping.pager.PageConstants;
import shopping.product.domain.Product;
import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class ProductDao {
	private QueryRunner qr = new TxQueryRunner();
	
	//按pid查询
	public Product findByPid(String pid) throws SQLException{
		String sql = "select * from product p ,category c where p.cid=c.cid and p.pid=?";
		Map<String,Object> map = qr.query(sql, new MapHandler(),pid);
		Product product = CommonUtils.toBean(map, Product.class);
		Category category = CommonUtils.toBean(map, Category.class);
		product.setCategory(category);
		
		if(map.get("paid")!=null){
			Category parent = new Category();
			parent.setCid((String)map.get("paid"));
			category.setParent(parent);
		}
		
		return product;
	}
	//按分类查找
	public PageBean<Product> findByCategory(String cid,int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("cid","=",cid));
		return findByCriteria(exprList,pc);
	}
	//按产品名模糊查找
	public PageBean<Product> findByPname(String pname,int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("pname","like","%"+pname+"%"));
		return findByCriteria(exprList,pc);
	}
	//按生产地模糊查找
	public PageBean<Product> findByPlace(String place,int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("place","like","%"+place+"%"));
		return findByCriteria(exprList,pc);
	}
	//按多条件组合模糊查找
	public PageBean<Product> findByCombination(Product criteria,int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("pname","like","%"+criteria.getPname()+"%"));
		exprList.add(new Expression("place","like","%"+criteria.getPlace()+"%"));
		return findByCriteria(exprList,pc);
	}	
	//通用的查询方法
	private PageBean<Product> findByCriteria(List<Expression> exprList,int pc) throws SQLException{
		//pc:当前页码 
		//得到ps:每页记录数
		int ps = PageConstants.PRODUCT_PAGE_SIZE;
		
		//通过exprList来生成where子句
		StringBuilder whereSql = new StringBuilder(" where 1=1");
		List<Object> params = new ArrayList<Object>();
		for(Expression expr : exprList){
			whereSql.append(" and ").append(expr.getName())
			    .append(" ").append(expr.getOperator()).append(" ");
			if(!expr.getOperator().equals("is null")){
				whereSql.append("?");
				params.add(expr.getValue());
			}
		}
		//得到tr:总记录
		String sql = "select count(*) from product"+whereSql;
		Number number = (Number)qr.query(sql, new ScalarHandler(),params.toArray());
		int tr = number.intValue();
		
		//得到beanList,即当前页记录
		sql ="select * from product"+ whereSql +" limit ?,?";
		params.add((pc-1)*ps);//当前页首行记录的下标
		params.add(ps);//每页记录数
		List<Product> beanList = qr.query(sql, new BeanListHandler<Product>(Product.class),params.toArray());
		
		//创建PageBean,设置参数
		PageBean<Product> pb = new PageBean<Product>();
		
		pb.setBeanList(beanList);
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		
		
		return  pb;
	}
	
	//根据二级分类id查询产品个数
	public int findProductCountByCategory(String cid) throws SQLException{
		String sql = "select count(*) from product where cid=?";
		Number number = (Number)qr.query(sql, new ScalarHandler(),cid);
		return number == null ? 0 : number.intValue();
	}
	//添加产品
	public void add(Product product) throws SQLException{
		String sql = "insert into product(pid,pname,price,image_b,image_w,cid,place) "
				+ "values(?,?,?,?,?,?,?)";
		Object[] params = {product.getPid(),product.getPname(),product.getPrice(),product.getImage_b(),product.getImage_w()
				,product.getCategory().getCid(),product.getPlace()};
		qr.update(sql,params);
		
	}
	//编辑产品
	public void edit(Product product) throws SQLException{
		String sql = "update product set pname=?,price=?,cid=?,place=? where pid=?";
		Object[] params = {product.getPname(),product.getPrice(),product.getCategory().getCid(),product.getPlace(),product.getPid()};
		qr.update(sql,params);
	}
	//删除产品
	public void delete(String pid) throws SQLException{
		String sql = "delete from product where pid=?";
		qr.update(sql,pid);
	}
}
