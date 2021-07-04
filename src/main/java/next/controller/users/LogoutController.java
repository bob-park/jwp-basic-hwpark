package next.controller.users;

import core.mvc.Controller;
import core.mvc.view.JspView;
import core.mvc.view.View;
import next.controller.UserSessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutController implements Controller {

  @Override
  public View execute(HttpServletRequest request, HttpServletResponse response) {

    HttpSession session = request.getSession();
    session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);

    return new JspView("redirect:/");
  }
}
