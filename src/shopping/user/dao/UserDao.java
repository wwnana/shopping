package shopping.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import shopping.user.domain.User;
import cn.itcast.jdbc.TxQueryRunner;

public class UserDao {
	private TxQueryRunner qr = new TxQueryRunner();
	
	//用户名校验
	public boolean ajaxValidateLoginname(String loginname) throws SQLException{
		String sql = "select count(1) from user where loginname=?";
		Number num = (Number)qr.query(sql, new ScalarHandler(),loginname);//返回单行单列的结果
		return num.intValue()==0;
	}
	
	//Email校验
	public boolean ajaxValidateEmail(String email) throws SQLException{
		String sql = "select count(1) from user where email=?";
		Number num = (Number)qr.query(sql, new ScalarHandler(),email);
		return num.intValue()==0;
	}
	//提交注册表单,实现注册用户添加到数据库
	public void add(User user) throws SQLException{
		String sql = "insert into user value(?,?,?,?,?,?)";
		Object[] params = {user.getUid(),user.getLoginname(),user.getLoginpass(),user.getEmail(),
				user.isStatus(),user.getActivationCode()};
		qr.update(sql, params);
	}
	//激活功能，通过激活码查找用户
	public User findByCode(String code){
		String sql = "select * from user where activationCode=?";
		try {
			return qr.query(sql, new BeanHandler<User>(User.class),code);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	//激活功能，修改用户状态
	public void updateStatus(String uid,boolean status) throws SQLException{
		String sql = "update user set status=? where uid=?";
		qr.update(sql,status,uid);
	}
	//登录功能，通过登录名和登录密码查找用户
	public User findByLoginnameAndLoginpass(String loginname,String loginpass) throws SQLException{
		String sql = "select * from user where loginname=? and loginpass=?";
		return qr.query(sql, new BeanHandler<User>(User.class),loginname,loginpass);//一行多列
	}
	//修改密码功能：旧密码校验
	public boolean findByUidAndLoginpass(String uid,String loginpass) throws SQLException{
		String sql = "select count(*) from user where uid=? and loginpass=?";
		Number number = (Number)qr.query(sql, new ScalarHandler(),uid,loginpass);
		return number.intValue()>0;
	}
	//修改密码功能：修改密码
	public void updateLoginPass(String uid,String loginpass) throws SQLException{
		String sql = "update user set loginpass=? where uid=?";
		qr.update(sql,loginpass,uid);
	}

}
