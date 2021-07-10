package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.view.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.QuestionDao;
import next.model.Question;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

public class AddQuestionController extends AbstractController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final QuestionDao questionDao;

  public AddQuestionController(QuestionDao questionDao) {
    this.questionDao = questionDao;
  }

  @Override
  public ModelAndView execute(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    HttpSession session = request.getSession();

    if (StringUtils.equalsIgnoreCase("GET", request.getMethod())) {

      if (UserSessionUtils.isLogined(session)) {
        return jspView("/qna/form.jsp");
      }

      return jspView("/users/login.jsp");
    }

    var user = UserSessionUtils.getUserFromSession(session);

    if (isEmpty(user)) {
      throw new IllegalStateException("Not logged in.");
    }

    var newQuestion =
        questionDao.insert(
            new Question(
                user.getUserId(), request.getParameter("title"), request.getParameter("contents")));

    logger.debug("question : {}", newQuestion);

    return jspView("/");
  }
}
