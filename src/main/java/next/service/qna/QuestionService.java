package next.service.qna;

import core.annotation.Inject;
import core.annotation.Service;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.exception.CannotDeleteException;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Service
public class QuestionService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final QuestionDao questionDao;
  private final AnswerDao answerDao;

  @Inject
  public QuestionService(QuestionDao questionDao, AnswerDao answerDao) {
    this.answerDao = answerDao;
    this.questionDao = questionDao;
  }

  public void removeQuestion(long questionId, User user) {

    var question = questionDao.findById(questionId);

    if (isEmpty(question)) {
      throw new CannotDeleteException("no exist question.");
    }

    if (question.canDelete(user, answerDao.findAllByQuestionId(questionId))) {
      questionDao.delete(question.getQuestionId());
    }

    logger.debug("delete questionId : {}", question.getQuestionId());
  }
}
