package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.KeyHolder;
import core.jdbc.RowMapper;
import next.model.Answer;

import java.sql.Timestamp;
import java.util.List;

public class AnswerDao {

  private final JdbcTemplate template;

  private static final RowMapper<Answer> MAPPER =
      rs ->
          new Answer(
              rs.getLong("answerId"),
              rs.getString("writer"),
              rs.getString("contents"),
              rs.getTimestamp("createdDate"),
              rs.getLong("questionId"));

  public AnswerDao() {
    this.template = new JdbcTemplate();
  }

  public Answer insert(Answer answer) {
    var sql = "INSERT INTO ANSWERS (writer, contents, createdDate, questionId) VALUES (?, ?, ?, ?)";

    var keyHolder = new KeyHolder();

    template.update(
        sql,
        keyHolder,
        answer.getWriter(),
        answer.getContents(),
        new Timestamp(answer.getTimeFromCreateDate()),
        answer.getQuestionId());

    return findById(keyHolder.getId());
  }

  public Answer findById(long answerId) {

    var sql =
        "SELECT answerId, writer, contents, createdDate, questionId FROM ANSWERS WHERE answerId = ?";

    return template.queryForObject(sql, new Object[] {answerId}, MAPPER);
  }

  public List<Answer> findAllByQuestionId(long questionId) {
    var sql =
        "SELECT answerId, writer, contents, createdDate, questionId FROM ANSWERS WHERE questionId = ? "
            + "order by answerId desc";

    return template.query(sql, new Object[] {questionId}, MAPPER);
  }

  public void deleteById(long answerId) {
    var sql = "DELETE FROM answers WHERE answerId = ?";

    template.update(sql, answerId);
  }
}
