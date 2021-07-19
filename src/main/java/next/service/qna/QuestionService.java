package next.service.qna;

import next.controller.UserSessionUtils;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.exception.CannotDeleteException;
import next.model.User;
import org.h2.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

public class QuestionService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final QuestionDao questionDao;
  private final AnswerDao answerDao;

  public QuestionService(QuestionDao questionDao, AnswerDao answerDao) {
    this.answerDao = answerDao;
    this.questionDao = questionDao;
  }

  public void removeQuestion(long questionId, User user) {

    var question = questionDao.findById(questionId);

    if (isEmpty(question)) {
      throw new CannotDeleteException("no exist question.");
    }

    if (!StringUtils.equals(user.getUserId(), question.getWriter())) {
      throw new CannotDeleteException("No same question user.");
    }

    if (!questionDao.checkDelete(question.getQuestionId(), question.getWriter())) {
      throw new CannotDeleteException("can not remove question.");
    }

    questionDao.delete(question.getQuestionId());

    logger.debug("delete questionId : {}", question.getQuestionId());
  }
}
