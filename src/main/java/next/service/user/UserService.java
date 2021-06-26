package next.service.user;

import next.dao.UserDao;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class UserService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final UserDao userDao;

  public UserService(UserDao userDao) {
    this.userDao = userDao;
  }

  public List<User> findAll() throws SQLException {
    return userDao.findAll();
  }
}
