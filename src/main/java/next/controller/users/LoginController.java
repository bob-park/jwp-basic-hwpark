package next.controller.users;

import core.mvc.Controller;
import core.mvc.view.JspView;
import core.mvc.view.View;
import next.controller.UserSessionUtils;
import next.service.user.UserService;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

public class LoginController implements Controller {

  private final UserService userService;

  public LoginController(UserService userService) {
    this.userService = userService;
  }

  @Override
  public View execute(HttpServletRequest request, HttpServletResponse response) {

    if (StringUtils.equalsIgnoreCase("get", request.getMethod())) {
      return new JspView("/users/login");
    }

    String userId = request.getParameter("userId");
    String password = request.getParameter("password");

    var user = userService.findUser(userId);

    if (isEmpty(user)) {
      request.setAttribute("loginFailed", true);
      return new JspView("redirect:/users/login");
    }

    if (user.matchPassword(password)) {
      HttpSession session = request.getSession();
      session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
      return new JspView("redirect:/");
    } else {
      request.setAttribute("loginFailed", true);
      return new JspView("redirect:/users/login");
    }
  }
}
