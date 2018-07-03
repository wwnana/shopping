package shopping.cart.dao;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import shopping.product.domain.Product;
import shopping.cart.domain.CartItem;
import shopping.user.domain.User;
import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class CartItemDao {
	
	private QueryRunner qr = new TxQueryRunner();
	
	//将单个map类型的购物车条目转换成cartItem类型
	private CartItem toCartItem(Map<String,Object> map){
		if(map == null || map.size() == 0 ) return null;
		CartItem cartItem = CommonUtils.toBean(map, CartItem.class);
		Product product = CommonUtils.toBean(map, Product.class);
		User user = CommonUtils.toBean(map, User.class);
		cartItem.setProduct(product);
		cartItem.setUser(user);
		return cartItem;
	}
	
	//将多个map类型的购物车条目转换成多个cartItem类型
	private List<CartItem> toCartItemList(List<Map<String,Object>> mapList){
		List<CartItem> cartItemList = new ArrayList(); 
		for(Map<String,Object> map : mapList){
			CartItem cartItem = toCartItem(map);
			cartItemList.add(cartItem);	
		}
		return cartItemList;
	}
	
	//根据当前用户的uid，查询购物车条目
	public List<CartItem> findByUser(String uid) throws SQLException{
		String sql = "select * from cartitem c,product p where c.pid = p.pid and uid=?";
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(),uid);
		return toCartItemList(mapList);
	}
	
	//根据cartItemId，查询购物车条目
	public CartItem findByCartItemId(String cartItemId) throws SQLException{
		String sql = "select * from cartitem c,product p where c.pid = p.pid and cartItemId=?";
		Map<String,Object> map = qr.query(sql, new MapHandler(),cartItemId);
		return toCartItem(map);
	}
		
	//根据uid和pid查询当前用户是否存在对应条目
	public CartItem findByPidAndUid(String pid,String uid) throws SQLException{
		String sql = "select * from cartitem where pid=? and uid=?";
		Map<String,Object> map = qr.query(sql, new MapHandler(),pid,uid);
		CartItem cartItem = toCartItem(map);
		return cartItem;
	}
	
	//修改条目数量
	public void updateQuantity(String cartItemId,int quantity) throws SQLException{
		String sql = "update cartitem set quantity=? where cartItemId=?";
		qr.update(sql,quantity,cartItemId);
	}
	
	//添加条目
	public void addCartItem(CartItem cartItem) throws SQLException{
		String sql = "insert into cartitem(cartItemId,quantity,pid,uid) values(?,?,?,?)";
		Object[] params = {cartItem.getCartItemId(),cartItem.getQuantity(),
				cartItem.getProduct().getPid(),cartItem.getUser().getUid()};
		qr.update(sql,params);
	}
	
	//生成批量删除的where子句
	public String toWhereSql(int len){
		StringBuilder sb = new StringBuilder("cartItemId in(");
		for(int i=0;i<len;i++){
			sb.append("?");
			if(i<len-1){
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
		
	}
	//批量删除
	public void batchDelete(String cartItemIds) throws SQLException{
		Object[] cartItemIdArray = cartItemIds.split(",");
		String sql ="delete from cartitem where "+ toWhereSql(cartItemIdArray.length);
		qr.update(sql,cartItemIdArray);
	}
	
	//生成订单：根据cartItemIds查找所有cartItem
	public List<CartItem> findByCartItemIds(String cartItemIds) throws SQLException{
		Object[] cartItemIdArray = cartItemIds.split(",");
		String sql ="select * from cartitem c,product p where c.pid=p.pid and "+ toWhereSql(cartItemIdArray.length);
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(),cartItemIdArray);
		return toCartItemList(mapList);
	}
}
