package shopping.cart.service;

import java.sql.SQLException;
import java.util.List;

import shopping.cart.dao.CartItemDao;
import shopping.cart.domain.CartItem;
import cn.itcast.commons.CommonUtils;

public class CartItemService {

	private CartItemDao cartItemDao = new CartItemDao();
	
	//添加购物车条目
	//cartItem为表单参数，_cartItem为数据库获取
	public void add(CartItem cartItem){
		try{
			CartItem _cartItem = cartItemDao.findByPidAndUid(
					cartItem.getProduct().getPid(), cartItem.getUser().getUid());
			if(_cartItem == null){
				cartItem.setCartItemId(CommonUtils.uuid());
				cartItemDao.addCartItem(cartItem);
			}else{
				int quantity = _cartItem.getQuantity()+cartItem.getQuantity();
				cartItemDao.updateQuantity(_cartItem.getCartItemId(), quantity);
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	//查询当前用户的购物车
	public List<CartItem> myCart(String uid){
		try {
			return cartItemDao.findByUser(uid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//批量删除
	public void batchDelete(String cartItemIds){
		try {
			cartItemDao.batchDelete(cartItemIds);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//修改购物车条目的数量
	public CartItem updateQuantity(String cartItemId,int quantity){
		try {
			cartItemDao.updateQuantity(cartItemId, quantity);
			return cartItemDao.findByCartItemId(cartItemId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	//生成订单：根据cartItemIds查找所有cartItem
	public List<CartItem> loadCartItems(String cartItemIds){
		try {
			return cartItemDao.findByCartItemIds(cartItemIds);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
