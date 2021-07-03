package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import next.model.Question;

import java.util.List;

public class QuestionDao {

  private final JdbcTemplate template;

  private static final RowMapper<Question> QUESTION_ROW_MAPPER =
      rs ->
          new Question(
              rs.getLong("questionId"),
              rs.getString("writer"),
              rs.getString("title"),
              rs.getString("contents"),
              rs.getTimestamp("createdDate"),
              rs.getInt("countOfAnswer"));

  public QuestionDao() {
    this.template = new JdbcTemplate();
  }

  public List<Question> findAll() {
    String sql =
        "SELECT questionId, writer, title, createdDate, countOfAnswer FROM QUESTIONS "
            + "order by questionId desc";

    return template.query(sql, QUESTION_ROW_MAPPER);
  }

  public Question findById(long questionId) {
    String sql =
        "SELECT questionId, writer, title, contents, createdDate, countOfAnswer FROM QUESTIONS "
            + "WHERE questionId = ?";

    return template.queryForObject(sql, new Object[] {questionId}, QUESTION_ROW_MAPPER);
  }
}
