package next.support.context;

import core.annotation.Inject;
import core.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.servlet.ServletContextEvent;
import javax.sql.DataSource;

public class DBInitializer {
  private static final Logger logger = LoggerFactory.getLogger(DBInitializer.class);

  private final DataSource dataSource;

  @Inject
  public DBInitializer(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @PostConstruct
  public void contextInitialized(ServletContextEvent sce) {
    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.addScript(new ClassPathResource("jwp.sql"));
    DatabasePopulatorUtils.execute(populator, dataSource);

    logger.info("Completed Load ServletContext!");
  }
}
