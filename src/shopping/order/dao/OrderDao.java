package shopping.order.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import shopping.product.domain.Product;
import shopping.order.domain.Order;
import shopping.order.domain.OrderItem;
import shopping.pager.Expression;
import shopping.pager.PageBean;
import shopping.pager.PageConstants;
import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class OrderDao {

	private QueryRunner qr = new TxQueryRunner();
	
	//加载订单
	public Order findByOid(String oid) throws SQLException{
		String sql = "select * from t_order where oid=?";
		Order order = qr.query(sql, new BeanHandler<Order>(Order.class),oid);
		loadOrderItem(order);
		return order;
	}
	
	//生成订单：插入订单
	public void add(Order order) throws SQLException{
		//插入订单
		String sql = "insert into t_order values(?,?,?,?,?,?)";
		Object[] params = {order.getOid(),order.getOrdertime(),order.getTotal(),
				order.getStatus(),order.getAddress(),order.getOwner().getUid()};
		qr.update(sql,params);
		//循环遍历每个订单，生成其对应的订单条目
		sql = "insert into orderitem values(?,?,?,?,?,?,?,?)";
		int len = order.getOrderItemList().size();
		Object[][] objs = new Object[len][];
		for(int i=0;i<len;i++){
			OrderItem orderItem = order.getOrderItemList().get(i);
			objs[i] = new Object[]{orderItem.getOrderItemId(),orderItem.getQuantity(),
					orderItem.getSubtotal(),orderItem.getProduct().getPid(),orderItem.getProduct().getPname(),
					orderItem.getProduct().getPrice(),orderItem.getProduct().getImage_b(),order.getOid()
			   };
		}
		qr.batch(sql, objs);
		
	}
	
	
	//根据当前用户查询我的订单
	public PageBean<Order> findByUid(String uid,int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("uid","=",uid));
		return findByCriteria(exprList,pc);
	}
	//根据状态查询订单
	public PageBean<Order> findByStatus(int status,int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("status","=",status+""));
		return findByCriteria(exprList,pc);
	}	
	//后台：查询所有订单
	public PageBean<Order> findAll(int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		return findByCriteria(exprList,pc);
	}	
	
	private PageBean<Order> findByCriteria(List<Expression> exprList,int pc) throws SQLException{
		//得到ps
		int ps = PageConstants.ORDER_PAGE_SIZE;//每页记录数
		//通过exprList来生成where子句
		StringBuilder whereSql = new StringBuilder(" where 1=1");
		List<Object> params = new ArrayList<Object>();//对应sql中的？
		for(Expression expr : exprList){
			whereSql.append(" and ").append(expr.getName()).append(" ")
				.append(expr.getOperator()).append(" ");
			if(!expr.getOperator().equals("is null")){
				whereSql.append("?");
				params.add(expr.getValue());
			}
		}
		
		//得到总记录数tr
		String sql = "select count(*) from t_order" + whereSql;
		Number number = (Number)qr.query(sql, new ScalarHandler(),params.toArray());
		int tr = number.intValue();
		
		//得到beanList，即当前页记录
		sql = "select * from t_order"+whereSql+" order by ordertime desc limit ?,?";
		params.add((pc-1)*ps);//当前页首行记录的下标
		params.add(ps);//一共查询几行，就是每页记录数
		
		List<Order> beanList = qr.query(sql,new BeanListHandler<Order>(Order.class),params.toArray());
	
		//获取每个订单的订单条目
		for(Order order : beanList){
			loadOrderItem(order);
		}
		//创建PageBean，设置参数
		PageBean<Order> pb = new PageBean<Order>();
		
		pb.setBeanList(beanList);
		pb.setPs(ps);
		pb.setTr(tr);
		pb.setPc(pc);
		
		return pb;
	}

	//为每个订单加载其订单条目
	private void loadOrderItem(Order order) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "select * from orderitem where oid=?";
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(),order.getOid());
		List<OrderItem> orderItemList = toOrderItemList(mapList);
		order.setOrderItemList(orderItemList);
		
	}

	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		// TODO Auto-generated method stub
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for(Map<String,Object> map : mapList){
			OrderItem orderItem = toOrderItem(map);
			orderItemList.add(orderItem);
		}
		return orderItemList;
	}

	private OrderItem toOrderItem(Map<String, Object> map) {
		// TODO Auto-generated method stub
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		Product product = CommonUtils.toBean(map, Product.class);
		orderItem.setProduct(product);
		
		return orderItem;
	}
	
	//查找状态
	public int findStatus(String oid) throws SQLException{
		String sql = "select status from t_order where oid=?";
		Number number = (Number)qr.query(sql, new ScalarHandler(),oid);
		return number.intValue();
	}
	//修改状态
	public void updateStatus(String oid,int status) throws SQLException{
		String sql ="update t_order set status=? where oid=?";
		qr.update(sql,status,oid);
	}
}
