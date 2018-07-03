import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;

import cn.itcast.commons.CommonUtils;
import shopping.category.dao.CategoryDao;
import shopping.category.domain.Category;

public class CategoryDaoTest {
	static CategoryDao categoryDao = new CategoryDao();
	
	@BeforeEach
	public void setUp() throws Exception {
	}

	@AfterEach
	public void tearDown() throws Exception {
	}

	@Test
	public void testAdd() {
		try {
			Category category = new Category();
			category.setCid(CommonUtils.uuid());
			category.setCname("测试名");
			category.setParent(null);
			category.setDesc("测试描述");
			categoryDao.add(category);
		}catch(SQLException e) {
			fail("Not yet implemented");
		}
	}

	@Test
	public void testLoad() {
		try {
			Category category=new Category();
			category=categoryDao.load("F53EA3749140454FADB8A53812DC8E88");
			System.out.println(category.getCname());
		}catch(SQLException e) {
			fail("Not yet implemented");
		}
	}

	@Test
	public void testDeleteCategory() {
		try {
			categoryDao.deleteCategory("CommonUtils.uuid()");
			System.out.println("CategoryDaoTest.class");
		}catch(SQLException e) {
			fail("Not yet implemented");
		}
	}

}
