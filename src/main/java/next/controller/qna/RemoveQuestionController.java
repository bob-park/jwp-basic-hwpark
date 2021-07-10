package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.view.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.QuestionDao;
import next.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.math.NumberUtils.toLong;

public class RemoveQuestionController extends AbstractController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final UserDao userDao;
  private final QuestionDao questionDao;

  public RemoveQuestionController(UserDao userDao, QuestionDao questionDao) {
    this.userDao = userDao;
    this.questionDao = questionDao;
  }

  @Override
  public ModelAndView execute(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    var question = questionDao.findById(toLong(request.getParameter("questionId")));

    if (isEmpty(question)) {
      throw new IllegalStateException("no exist question.");
    }

    if (!UserSessionUtils.isSameUser(
        request.getSession(), userDao.findByUserId(question.getWriter()))) {
      throw new IllegalStateException("No same question user.");
    }

    if (!questionDao.checkDelete(question.getQuestionId(), question.getWriter())) {
      throw new IllegalStateException("can not remove question.");
    }

    questionDao.delete(question.getQuestionId());

    logger.debug("delete questionId : {}", question.getQuestionId());

    return jspView("redirect:/");
  }
}
