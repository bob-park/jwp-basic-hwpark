package next.service.user;

import next.dao.impl.JdbcUserDao;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final JdbcUserDao jdbcUserDao;

  public UserService(JdbcUserDao jdbcUserDao) {
    this.jdbcUserDao = jdbcUserDao;
  }

  public List<User> findAll() {
    return jdbcUserDao.findAll();
  }

  public User findUser(String userId) {
    return jdbcUserDao.findByUserId(userId);
  }

  public void createUser(User user) {
    jdbcUserDao.insert(user);
  }

  public void updateUser(User user) {
    jdbcUserDao.update(user);
  }
}
