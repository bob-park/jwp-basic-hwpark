package core.web.servlet;

import core.mvc.Controller;
import core.mvc.LegacyRequestMapping;
import next.controller.HomeController;
import next.controller.qna.*;
import next.controller.users.*;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.dao.UserDao;
import next.service.qna.QuestionService;
import next.service.user.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

//@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

  private final LegacyRequestMapping mapping = new LegacyRequestMapping();


  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {

    String uri = req.getRequestURI();

    var controller = findController(uri);

    try {

      var mav = controller.execute(req, resp);
      var view = mav.getView();

      view.render(mav.getModel(), req, resp);

    } catch (Exception e) {
      throw new ServletException(e.getMessage());
    }
  }

  private Controller findController(String uri) {

    var controller = mapping.get(uri);

    if (isEmpty(controller)) {
      controller = mapping.getForward();
    }

    return controller;
  }
}
