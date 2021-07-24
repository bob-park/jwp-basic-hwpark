package next.dao.impl;

import core.annotation.Inject;
import core.annotation.Repository;
import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import next.dao.UserDao;
import next.model.User;

import java.util.List;

@Repository
public class JdbcUserDao implements UserDao {

  private final JdbcTemplate template;

  @Inject
  public JdbcUserDao(JdbcTemplate template) {
    this.template = template;
  }

  private static final RowMapper<User> MAPPER =
      rs ->
          new User(
              rs.getString("userId"),
              rs.getString("password"),
              rs.getString("name"),
              rs.getString("email"));

  @Override
  public void insert(User user) {
    var sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

    template.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
  }

  @Override
  public void update(User user) {

    var sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";

    template.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
  }

  @Override
  public List<User> findAll() {

    var sql = "SELECT userId, password, name, email FROM USERS";

    return template.query(sql, null, MAPPER);
  }

  @Override
  public User findByUserId(String userId) {

    String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

    return template.queryForObject(sql, new Object[] {userId}, MAPPER);
  }
}
