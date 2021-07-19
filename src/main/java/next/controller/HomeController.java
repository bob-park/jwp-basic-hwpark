package next.controller;

import core.mvc.AbstractController;
import core.mvc.view.ModelAndView;
import next.dao.impl.JdbcQuestionDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HomeController extends AbstractController {

  private final JdbcQuestionDao jdbcQuestionDao;

  public HomeController(JdbcQuestionDao jdbcQuestionDao) {
    this.jdbcQuestionDao = jdbcQuestionDao;
  }

  @Override
  public ModelAndView execute(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    return jspView("/home.jsp").addObject("questions", jdbcQuestionDao.findAll());
  }
}
