package next.service.qna;

import next.controller.UserSessionUtils;
import next.dao.QuestionDao;
import next.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

public class QuestionService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final UserDao userDao;
  private final QuestionDao questionDao;

  public QuestionService(UserDao userDao, QuestionDao questionDao) {
    this.userDao = userDao;
    this.questionDao = questionDao;
  }

  public void removeQuestion(HttpSession session, long questionId) {

    var question = questionDao.findById(questionId);

    if (isEmpty(question)) {
      throw new IllegalStateException("no exist question.");
    }

    if (!UserSessionUtils.isSameUser(session, userDao.findByUserId(question.getWriter()))) {
      throw new IllegalStateException("No same question user.");
    }

    if (!questionDao.checkDelete(question.getQuestionId(), question.getWriter())) {
      throw new IllegalStateException("can not remove question.");
    }

    questionDao.delete(question.getQuestionId());

    logger.debug("delete questionId : {}", question.getQuestionId());
  }
}
