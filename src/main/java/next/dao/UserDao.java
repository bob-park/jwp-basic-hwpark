package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import next.model.User;

import java.util.List;

public class UserDao {

  private final JdbcTemplate template;

  private static final RowMapper<User> MAPPER =
      rs ->
          new User(
              rs.getString("userId"),
              rs.getString("password"),
              rs.getString("name"),
              rs.getString("email"));

  public UserDao() {
    this.template = new JdbcTemplate();
  }

  public void insert(User user) {
    var sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

    template.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
  }

  public void update(User user) {

    var sql = "UPDATE USERS SET password=?, name=?, email=? WHERE userId=?";

    template.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
  }

  public List<User> findAll() {

    var sql = "SELECT userId, password, name, email FROM USERS";

    return template.query(sql, null, MAPPER);
  }

  public User findByUserId(String userId) {

    String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

    return template.queryForObject(sql, new Object[] {userId}, MAPPER);
  }
}
