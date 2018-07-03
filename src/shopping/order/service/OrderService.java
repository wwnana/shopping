package shopping.order.service;

import java.sql.SQLException;

import cn.itcast.jdbc.JdbcUtils;
import shopping.order.dao.OrderDao;
import shopping.order.domain.Order;
import shopping.pager.PageBean;

public class OrderService {
	private OrderDao orderDao = new OrderDao();
	
	//我的订单
	public PageBean<Order> myOrders(String uid,int pc){
		try{
			JdbcUtils.beginTransaction();
			PageBean<Order> pb = orderDao.findByUid(uid, pc);
			JdbcUtils.commitTransaction();
			return pb;
		}catch(SQLException e){
			try{
				JdbcUtils.rollbackTransaction();
			}catch(SQLException e1){}
			throw new RuntimeException(e);
		}
	}
	
	//根据状态查询订单
	public PageBean<Order> findByStatus(int status,int pc){
		try{
			JdbcUtils.beginTransaction();
			PageBean<Order> pb = orderDao.findByStatus(status, pc);
			JdbcUtils.commitTransaction();
			return pb;
		}catch(SQLException e){
			try{
				JdbcUtils.rollbackTransaction();
			}catch(SQLException e1){}
			throw new RuntimeException(e);
		}
	}	
	//后台：查询所有订单
	public PageBean<Order> findAll(int pc){
		try{
			JdbcUtils.beginTransaction();
			PageBean<Order> pb = orderDao.findAll(pc);
			JdbcUtils.commitTransaction();
			return pb;
		}catch(SQLException e){
			try{
				JdbcUtils.rollbackTransaction();
			}catch(SQLException e1){}
			throw new RuntimeException(e);
		}
	}
	
	//生成订单
	public void createOrders(Order order){
		try{
			JdbcUtils.beginTransaction();
			orderDao.add(order);
			JdbcUtils.commitTransaction();
		}catch(SQLException e){
			try{
				JdbcUtils.rollbackTransaction();
			}catch(SQLException e1){}
			throw new RuntimeException(e);
		}
	}
	
	//加载订单
	public Order load(String oid){
		try{
			JdbcUtils.beginTransaction();
			Order order = orderDao.findByOid(oid);
			JdbcUtils.commitTransaction();
			return order;
		}catch(SQLException e){
			try{
				JdbcUtils.rollbackTransaction();
			}catch(SQLException e1){}
			throw new RuntimeException(e);
		}
	}
	//查找订单状态
	public int findStatus(String oid){
		try {
			return orderDao.findStatus(oid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//修改订单状态
	public void updateStatus(String oid,int status){
		try {
			orderDao.updateStatus(oid, status);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
