package shopping.cart.domain;

import java.math.BigDecimal;
import shopping.product.domain.Product;
import shopping.user.domain.User;

public class CartItem {

	private String cartItemId;
	private int quantity;
	private Product product;
	private User user;
	
	//添加小计方法
	public double getSubtotal(){
		//使用BigDecimal不会有误差，要求必须使用String类型构造器
		BigDecimal b1 = new BigDecimal(product.getPrice()+"");//转化成字符串
		BigDecimal b2 = new BigDecimal(quantity+"");
		BigDecimal b3 = b1.multiply(b2);
		return b3.doubleValue();
	}
	
	public String getCartItemId() {
		return cartItemId;
	}
	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
