package next.controller.users;

import core.db.DataBase;
import next.controller.Controller;
import next.controller.UserSessionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

public class LoginController implements Controller {

  @Override
  public String execute(HttpServletRequest request, HttpServletResponse response) {

    if (StringUtils.equalsIgnoreCase("get", request.getMethod())) {
      return "/users/login";
    }

    String userId = request.getParameter("userId");
    String password = request.getParameter("password");
    var user = DataBase.findUserById(userId);

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
  }
}