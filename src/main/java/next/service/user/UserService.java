package next.service.user;

import next.dao.UserDao;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final UserDao userDao;

  public UserService(UserDao userDao) {
    this.userDao = userDao;
  }

  public List<User> findAll() {
    return userDao.findAll();
  }

  public User findUser(String userId) {
    return userDao.findByUserId(userId);
  }

  public void createUser(User user) {
    userDao.insert(user);
  }

  public void updateUser(User user) {
    userDao.update(user);
  }
}
