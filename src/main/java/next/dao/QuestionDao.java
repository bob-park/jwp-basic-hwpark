package next.dao;

import core.jdbc.JdbcTemplate;
import core.jdbc.KeyHolder;
import core.jdbc.RowMapper;
import next.model.Question;

import java.sql.Timestamp;
import java.util.List;

public class QuestionDao {

  private final JdbcTemplate template = JdbcTemplate.getInstance();

  private static final RowMapper<Question> QUESTION_ROW_MAPPER =
      rs ->
          new Question(
              rs.getLong("questionId"),
              rs.getString("writer"),
              rs.getString("title"),
              rs.getString("contents"),
              rs.getTimestamp("createdDate"),
              rs.getInt("countOfAnswer"));

  public List<Question> findAll() {
    String sql =
        "SELECT questionId, writer, title, contents, createdDate, countOfAnswer FROM QUESTIONS "
            + "order by questionId desc";

    return template.query(sql, QUESTION_ROW_MAPPER);
  }

  public Question findById(long questionId) {
    String sql =
        "SELECT questionId, writer, title, contents, createdDate, countOfAnswer FROM QUESTIONS "
            + "WHERE questionId = ?";

    return template.queryForObject(sql, new Object[] {questionId}, QUESTION_ROW_MAPPER);
  }

  public Question insert(Question question) {
    var sql =
        "INSERT INTO QUESTIONS (writer, title, contents, createdDate, countOfAnswer) VALUES (?, ?, ?, ?, ?)";

    var keyHolder = new KeyHolder();

    template.update(
        sql,
        keyHolder,
        question.getWriter(),
        question.getTitle(),
        question.getContents(),
        new Timestamp(question.getTimeFromCreateDate()),
        0);

    return findById(keyHolder.getId());
  }

  public Question updateIncrementAnswerCount(Long questionId) {
    var sql = "UPDATE QUESTIONS set countOfAnswer = countOfAnswer + 1 WHERE questionId = ?";

    template.update(sql, questionId);

    return findById(questionId);
  }

  public Question updateDecrementAnswerCount(Long questionId) {
    var sql = "UPDATE QUESTIONS set countOfAnswer = countOfAnswer - 1 WHERE questionId = ?";

    template.update(sql, questionId);

    return findById(questionId);
  }

  public Question update(Question question) {
    var sql = "UPDATE QUESTIONS set " + "title = ? " + ", contents = ? " + "where questionId = ?";

    template.update(sql, question.getTitle(), question.getContents(), question.getQuestionId());

    return findById(question.getQuestionId());
  }

  public boolean checkDelete(long questionId, String writer) {
    var sql =
        "select count(questionId) as count from questions where questionId = ? and writer= ?  and countOfAnswer = (select count(answerId) from answers where questionId = ? and writer = ? )";

    long count =
        template.queryForObject(
            sql,
            new Object[] {questionId, writer, questionId, writer},
            (rs -> rs.getLong("count")));

    return count > 0;
  }

  public void delete(long questionId) {
    var sql = "DELETE FROM QUESTIONS where questionId = ?";

    template.update(sql, questionId);
  }
}
