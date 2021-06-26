package next.controller.users;

import core.mvc.Controller;
import next.controller.UserSessionUtils;
import next.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ListUserController implements Controller {

  private final UserService userService;

  public ListUserController(UserService userService) {
    this.userService = userService;
  }

  @Override
  public String execute(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    if (!UserSessionUtils.isLogined(request.getSession())) {
      return "redirect:/users/login";
    }

    request.setAttribute("users", userService.findAll());

    return "/users/list.jsp";
  }
}
