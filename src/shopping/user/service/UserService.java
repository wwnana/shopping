package shopping.user.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;

import shopping.user.dao.UserDao;
import shopping.user.domain.User;
import shopping.user.service.exception.UserException;
import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;

public class UserService {

	private UserDao userDao = new UserDao();
	
	//修改密码功能
	public void updateLoginpass(String uid,String oldPass,String newPass) throws UserException{
		try {
			boolean bool = userDao.findByUidAndLoginpass(uid, oldPass);
			if(!bool){
				throw new UserException("原密码错误！");
			}
			userDao.updateLoginPass(uid, newPass);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	//登录功能
	public User login(User user) throws SQLException{
		return userDao.findByLoginnameAndLoginpass(user.getLoginname(), user.getLoginpass());
		
	}
	//激活功能
	public void activation(String code) throws UserException{
		//1.查询激活码是否有效2.查询是否已激活3.修改状态
		User user = userDao.findByCode(code);
		if(user == null)
			throw new UserException("此激活码无效！");
		if(user.isStatus())
			throw new UserException("该用户已经激活！请勿二次操作！");
		try {
			userDao.updateStatus(user.getUid(), true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
			
	}
	//用户名注册校验
	public boolean ajaxValidateLoginname(String loginname){
		try {
			return userDao.ajaxValidateLoginname(loginname);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
	}
	//Email注册校验
	public boolean ajaxValidateEmail(String email){
		try {
			return userDao.ajaxValidateEmail(email);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		
	}	
	//注册提交到数据库
	public void register(User user){
		//1.数据补齐
		user.setUid(CommonUtils.uuid());//生成随机32位数
		user.setActivationCode(CommonUtils.uuid()+CommonUtils.uuid());
		user.setStatus(false);//是否已注册状态为否
		//2.向数据库插入
		try {
			userDao.add(user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		//3.发邮件
		Properties prop = new Properties();
		try {
			//加载模板文件
			prop.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		//创建session对象，登录邮件服务器
		String host = prop.getProperty("host");//服务器名
		String name = prop.getProperty("username");//登录名
		String pass = prop.getProperty("password");//登录密码
		Session session = MailUtils.createSession(host, name, pass);
		//创建mail对象
		String from = prop.getProperty("from");//发件人
		String to = user.getEmail();//收件人
		String subject = prop.getProperty("subject");//主题
		String content = MessageFormat.format(prop.getProperty("content"),user.getActivationCode());
		//用已保存在数据库中的激活码替换模板文件中的占位符{0}
		Mail mail = new Mail(from,to,subject,content);
		
		try {
			MailUtils.send(session, mail);//发送邮件
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
}
