package next.controller.users;

import core.db.DataBase;
import next.controller.Controller;
import next.controller.UserSessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListUserController implements Controller {

  @Override
  public String execute(HttpServletRequest request, HttpServletResponse response) {
    if (!UserSessionUtils.isLogined(request.getSession())) {
      return "redirect:/users/login";
    }

    request.setAttribute("users", DataBase.findAll());
    return "/users/list.jsp";
  }
}
