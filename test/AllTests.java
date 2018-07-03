import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CategoryDaoTest.class, ProductDaoTest.class, UserDaoTest.class })
public class AllTests {

}
