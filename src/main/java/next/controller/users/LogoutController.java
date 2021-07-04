package next.controller.users;

import core.mvc.AbstractController;
import core.mvc.view.ModelAndView;
import next.controller.UserSessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutController extends AbstractController {

  @Override
  public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) {

    HttpSession session = request.getSession();
    session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);

    return jspView("redirect:/");
  }
}
