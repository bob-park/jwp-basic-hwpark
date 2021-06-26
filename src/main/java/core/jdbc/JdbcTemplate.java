package core.jdbc;

import core.jdbc.exception.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

  public void update(String sql, Object... params) {

    try (var conn = ConnectionManager.getConnection();
        var statement = conn.prepareStatement(sql)) {

      var index = 1;

      for (Object param : params) {
        statement.setObject(index++, param);
      }

      statement.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException(e.getMessage());
    }
  }

  public <T> List<T> query(String sql, Object[] params, RowMapper<T> mapper) {

    List<T> result = new ArrayList<>();

    try (var conn = ConnectionManager.getConnection();
        var statement = conn.prepareStatement(sql)) {

      if (params != null) {
        var index = 1;

        for (Object param : params) {
          statement.setObject(index++, param);
        }
      }

      ResultSet rs = statement.executeQuery();

      while (rs.next()) {
        result.add(mapper.mapRow(rs));
      }
    } catch (SQLException e) {
      throw new DataAccessException(e.getMessage());
    }

    return result;
  }

  public <T> T queryForObject(String sql, Object[] params, RowMapper<T> mapper) {
    try (var conn = ConnectionManager.getConnection();
        var statement = conn.prepareStatement(sql)) {

      if (params != null) {
        var index = 1;

        for (Object param : params) {
          statement.setObject(index++, param);
        }
      }

      ResultSet rs = statement.executeQuery();

      if (rs.next()) {
        return mapper.mapRow(rs);
      }
    } catch (SQLException e) {
      throw new DataAccessException(e.getMessage());
    }

    return null;
  }
}
