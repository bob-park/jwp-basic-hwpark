package next.configuration;

import core.annotation.Bean;
import core.annotation.ComponentScan;
import core.annotation.Configuration;
import core.jdbc.JdbcTemplate;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan({"core", "next"})
public class MyConfiguration {

  @Bean
  public DataSource getDataSource() {
    BasicDataSource dataSource = new BasicDataSource();

    dataSource.setDriverClassName("org.h2.Driver");
    dataSource.setUrl("jdbc:h2:~/jwp-basic;AUTO_SERVER=TRUE");
    dataSource.setUsername("sa");
    dataSource.setPassword("");

    return dataSource;
  }

  @Bean
  public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }
}
