import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;

import cn.itcast.commons.CommonUtils;
import shopping.user.dao.UserDao;
import shopping.user.domain.User;


public class UserDaoTest {
	static UserDao userDao = new UserDao();
	

	@BeforeEach
	public void setUp() throws Exception {
	}

	@Test
	public void testAjaxValidateLoginname() {
		boolean count = false;
		try {
			count = userDao.ajaxValidateLoginname("123");
			System.out.println(count);
			Assert.assertEquals(true,count);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			fail("Not yet implemented");
		}
		
	}

	@Test
	public void testAjaxValidateEmail() {
		boolean count=false;
		try {
			count = userDao.ajaxValidateEmail("1602263154@qq.com");
			System.out.println(count);
			Assert.assertEquals(true,count);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			fail("Not yet implemented");
		}
	}

	@Test
	public void testAdd() {
		try {
			User user=new User();
			user.setUid(CommonUtils.uuid());
			user.setLoginname("haha");
			user.setLoginpass("hhhh");
			user.setEmail("1602678267@qq.com");
			user.setStatus(false);
			user.setActivationCode(CommonUtils.uuid()+CommonUtils.uuid());
			userDao.add(user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			fail("Not yet implemented");
		}
	}

	@Test
	public void testUpdateLoginPass() {
		try {
			userDao.updateLoginPass("5E36CC34EC2B4ED5B888D022F8252223", "123");
			System.out.println("UserDaoTest.class");
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			fail("Not yet implemented");
		}
	}

	@AfterEach
	public void tearDown() throws Exception {
	}
}
