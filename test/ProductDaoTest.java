import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;

import shopping.category.dao.CategoryDao;
import shopping.category.domain.Category;
import shopping.product.dao.ProductDao;
import shopping.product.domain.Product;

public class ProductDaoTest {
	static ProductDao productDao = new ProductDao();

	@BeforeEach
	public void setUp() throws Exception {
	}

	@AfterEach
	public void tearDown() throws Exception {
	}

	@Test
	public void testFindProductCountByCategory() {
		int count=0;
		try {
			count=productDao.findProductCountByCategory("EB56D4BA71C74CC898A0B1881D903B3B");
			System.out.println(count);
			Assert.assertEquals(3,count);
		}catch(SQLException e) {
			fail("Not yet implemented");
		}
	}

	@Test
	public void testEdit() {
		try {
			Product product = new Product();
			Category category = new Category();
			category.setCid("EB56D4BA71C74CC898A0B1881D903B3B");
			product.setPid("14167A94F46D4A29BEB7EC601B9C9F85");
			product.setPname("藏帽子（藏式遮阳帽子）");
			product.setPrice(130);
			product.setPlace("西藏");
			product.setCategory(category);
			productDao.edit(product);
		}catch(SQLException e) {
			fail("Not yet implemented");
		}
	}

	@Test
	public void testDelete() {
		try {
			productDao.delete("F5DD1A363D99410FAD0098EFF04497CB");
			System.out.println("ProductDaoTest.class");
		}catch(SQLException e) {
			fail("Not yet implemented");
		}
	}

}
