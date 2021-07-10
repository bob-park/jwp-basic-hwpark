package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.view.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.QuestionDao;
import next.dao.UserDao;
import next.model.Question;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.math.NumberUtils.toLong;

public class UpdateQuestionController extends AbstractController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final UserDao userDao;
  private final QuestionDao questionDao;

  public UpdateQuestionController(UserDao userDao, QuestionDao questionDao) {
    this.userDao = userDao;
    this.questionDao = questionDao;
  }

  @Override
  public ModelAndView execute(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    var session = request.getSession();

    var question = questionDao.findById(toLong(request.getParameter("questionId")));

    if (isEmpty(question)) {
      throw new IllegalStateException("no exist question.");
    }

    if (!UserSessionUtils.isSameUser(session, userDao.findByUserId(question.getWriter()))) {
      throw new IllegalStateException("not same question user.");
    }

    if (StringUtils.equalsIgnoreCase("GET", request.getMethod())) {
      return jspView("/qna/updateForm.jsp").addObject("question", question);
    }

    var newQuestion =
        questionDao.update(
            new Question(
                question.getQuestionId(),
                question.getWriter(),
                request.getParameter("title"),
                request.getParameter("contents"),
                question.getCreatedDate(),
                question.getCountOfComment()));

    logger.debug("update question : {}", newQuestion);

    return jspView("/");
  }
}
