package next.controller.users;

import core.db.DataBase;
import core.mvc.Controller;
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
  public String execute(HttpServletRequest request, HttpServletResponse response) {

    if (StringUtils.equalsIgnoreCase("get", request.getMethod())) {
      return "/users/login";
    }

    String userId = request.getParameter("userId");
    String password = request.getParameter("password");
    try {
      var user = userService.findUser(userId);

      if (isEmpty(user)) {
        request.setAttribute("loginFailed", true);
        return "redirect:/users/login";
      }

      if (user.matchPassword(password)) {
        HttpSession session = request.getSession();
        session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
        return "redirect:/";
      } else {
        request.setAttribute("loginFailed", true);
        return "redirect:/users/login";
      }
    } catch (Exception e) {
      throw new IllegalStateException(e.getMessage());
    }
  }
}
