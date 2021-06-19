package next.dao;

import next.model.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserDaoTest {

  @Test
  void crud() throws Exception {
    User expected = new User("userId", "password", "name", "javajigi@email.com");
    UserDao userDao = new UserDao();
    userDao.insert(expected);

    User actual = userDao.findByUserId(expected.getUserId());

    assertThat(expected).isEqualTo(actual);
  }
}
