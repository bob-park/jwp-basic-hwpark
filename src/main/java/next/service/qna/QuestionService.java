package next.service.qna;

import next.controller.UserSessionUtils;
import next.dao.impl.JdbcQuestionDao;
import next.dao.impl.JdbcUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

public class QuestionService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final JdbcUserDao jdbcUserDao;
  private final JdbcQuestionDao jdbcQuestionDao;

  public QuestionService(JdbcUserDao jdbcUserDao, JdbcQuestionDao jdbcQuestionDao) {
    this.jdbcUserDao = jdbcUserDao;
    this.jdbcQuestionDao = jdbcQuestionDao;
  }

  public void removeQuestion(HttpSession session, long questionId) {

    var question = jdbcQuestionDao.findById(questionId);

    if (isEmpty(question)) {
      throw new IllegalStateException("no exist question.");
    }

    if (!UserSessionUtils.isSameUser(session, jdbcUserDao.findByUserId(question.getWriter()))) {
      throw new IllegalStateException("No same question user.");
    }

    if (!jdbcQuestionDao.checkDelete(question.getQuestionId(), question.getWriter())) {
      throw new IllegalStateException("can not remove question.");
    }

    jdbcQuestionDao.delete(question.getQuestionId());

    logger.debug("delete questionId : {}", question.getQuestionId());
  }
}
