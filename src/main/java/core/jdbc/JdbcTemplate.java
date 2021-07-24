package core.jdbc;

import core.jdbc.exception.DataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

public class JdbcTemplate {

  private final DataSource dataSource;

  public JdbcTemplate(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void update(String sql, Object... params) {

    update(sql, null, params);
  }

  public void update(String sql, KeyHolder keyHolder, Object... params) {
    try (var conn = getConnection();
        var statement = conn.prepareStatement(sql)) {

      var index = 1;

      for (Object param : params) {
        statement.setObject(index++, param);
      }

      statement.executeUpdate();

      if (isNotEmpty(keyHolder)) {
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
          keyHolder.setId(rs.getLong(1));
        }
        rs.close();
      }

    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  public <T> List<T> query(String sql, RowMapper<T> mapper) {
    return query(sql, null, mapper);
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
      throw new DataAccessException(e);
    }

    return result;
  }

  public <T> T queryForObject(String sql, RowMapper<T> mapper) {
    return queryForObject(sql, null, mapper);
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
      throw new DataAccessException(e);
    }

    return null;
  }

  private Connection getConnection() throws SQLException {
    return this.dataSource.getConnection();
  }
}
