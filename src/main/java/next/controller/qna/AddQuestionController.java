package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.view.ModelAndView;
import next.dao.QuestionDao;
import next.model.Question;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddQuestionController extends AbstractController {

  private final QuestionDao questionDao;

  public AddQuestionController(QuestionDao questionDao) {
    this.questionDao = questionDao;
  }

  @Override
  public ModelAndView execute(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    questionDao.insert(
        new Question(
            request.getParameter("writer"),
            request.getParameter("title"),
            request.getParameter("contents")));

    return jspView("/");
  }
}
