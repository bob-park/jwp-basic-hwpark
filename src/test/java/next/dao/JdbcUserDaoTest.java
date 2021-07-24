package next.dao;

import core.jdbc.JdbcTemplate;
import next.dao.impl.JdbcUserDao;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcUserDaoTest {

  private JdbcTemplate jdbcTemplate;

  @BeforeEach
  void setup() {
    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.addScript(new ClassPathResource("jwp.sql"));
//    DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());

//    jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
  }

  @Test
  void crud() throws Exception {
    User expected = new User("userId", "password", "name", "javajigi@email.com");

    JdbcUserDao jdbcUserDao = new JdbcUserDao(jdbcTemplate);
    jdbcUserDao.insert(expected);
    User actual = jdbcUserDao.findByUserId(expected.getUserId());

    assertThat(actual).isEqualTo(expected);

    expected.update(new User("userId", "password2", "name2", "sanjigi@email.com"));
    jdbcUserDao.update(expected);
    actual = jdbcUserDao.findByUserId(expected.getUserId());
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void findAll() throws Exception {
    JdbcUserDao jdbcUserDao = new JdbcUserDao(jdbcTemplate);
    List<User> users = jdbcUserDao.findAll();
    assertThat(users.size()).isEqualTo(1);
  }
}
