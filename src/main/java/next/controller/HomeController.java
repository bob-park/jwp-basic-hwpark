package next.controller;

import core.annotation.Controller;
import core.annotation.Inject;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import core.mvc.view.ModelAndView;
import core.nmvc.AbstractNewController;
import next.dao.QuestionDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class HomeController extends AbstractNewController {

  private final QuestionDao questionDao;

  @Inject
  public HomeController(QuestionDao questionDao) {
    this.questionDao = questionDao;
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView execute(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    return jspView("/home.jsp").addObject("questions", questionDao.findAll());
  }
}
