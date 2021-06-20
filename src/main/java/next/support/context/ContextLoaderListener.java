package next.support.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import core.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;

@WebListener
public class ContextLoaderListener implements ServletContextListener {
  private static final Logger logger = LoggerFactory.getLogger(ContextLoaderListener.class);

  private static final String DB_DRIVER = "org.h2.Driver";
  private static final String DB_URL = "jdbc:h2:~/jwp-basic;AUTO_SERVER=TRUE";
  private static final String DB_USERNAME = "sa";
  private static final String DB_PW = "";

  //    @Override
  //    public void contextInitialized(ServletContextEvent sce) {
  //        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
  //        populator.addScript(new ClassPathResource("jwp.sql"));
  //        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
  //
  //        logger.info("Completed Load ServletContext!");
  //    }
  //
  //    @Override
  //    public void contextDestroyed(ServletContextEvent sce) {
  //    }

  public static DataSource getDataSource() {
    BasicDataSource ds = new BasicDataSource();
    ds.setDriverClassName(DB_DRIVER);
    ds.setUrl(DB_URL);
    ds.setUsername(DB_USERNAME);
    ds.setPassword(DB_PW);
    return ds;
  }

  public static Connection getConnection() {
    try {
      return getDataSource().getConnection();
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }
}
