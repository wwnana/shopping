package shopping.admin.service;

import java.sql.SQLException;

import shopping.admin.dao.AdminDao;
import shopping.admin.domain.Admin;

public class AdminService {
	private AdminDao adminDao = new AdminDao();
	
	//登录后台
	public Admin login(Admin admin){
		try {
			return adminDao.find(admin.getAdminname(),admin.getAdminpwd());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
