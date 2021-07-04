package next.controller;

import core.mvc.AbstractController;
import core.mvc.view.ModelAndView;
import next.dao.QuestionDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HomeController extends AbstractController {

  private final QuestionDao questionDao;

  public HomeController(QuestionDao questionDao) {
    this.questionDao = questionDao;
  }

  @Override
  public ModelAndView execute(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    return jspView("/home.jsp").addObject("questions", questionDao.findAll());
  }
}
