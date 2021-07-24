package next.dao.impl;

import core.annotation.Inject;
import core.annotation.Repository;
import core.jdbc.JdbcTemplate;
import core.jdbc.KeyHolder;
import core.jdbc.RowMapper;
import next.dao.AnswerDao;
import next.model.Answer;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class JdbcAnswerDao implements AnswerDao {

  private final JdbcTemplate template;

  @Inject
  public JdbcAnswerDao(JdbcTemplate template) {
    this.template = template;
  }

  private static final RowMapper<Answer> MAPPER =
      rs ->
          new Answer(
              rs.getLong("answerId"),
              rs.getString("writer"),
              rs.getString("contents"),
              rs.getTimestamp("createdDate"),
              rs.getLong("questionId"));

  @Override
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

  @Override
  public Answer findById(long answerId) {

    var sql =
        "SELECT answerId, writer, contents, createdDate, questionId FROM ANSWERS WHERE answerId = ?";

    return template.queryForObject(sql, new Object[] {answerId}, MAPPER);
  }

  @Override
  public List<Answer> findAllByQuestionId(long questionId) {
    var sql =
        "SELECT answerId, writer, contents, createdDate, questionId FROM ANSWERS WHERE questionId = ? "
            + "order by answerId desc";

    return template.query(sql, new Object[] {questionId}, MAPPER);
  }

  @Override
  public void deleteById(long answerId) {
    var sql = "DELETE FROM answers WHERE answerId = ?";

    template.update(sql, answerId);
  }
}
