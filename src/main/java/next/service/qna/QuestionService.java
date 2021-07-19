package next.service.qna;

import next.controller.UserSessionUtils;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
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

  public void removeQuestion(long questionId, String userId) {

    var question = questionDao.findById(questionId);

    if (isEmpty(question)) {
      throw new IllegalStateException("no exist question.");
    }

    if (!StringUtils.equals(userId, question.getWriter())) {
      throw new IllegalStateException("No same question user.");
    }


    if (!questionDao.checkDelete(question.getQuestionId(), question.getWriter())) {
      throw new IllegalStateException("can not remove question.");
    }

    questionDao.delete(question.getQuestionId());

    logger.debug("delete questionId : {}", question.getQuestionId());
  }
}
