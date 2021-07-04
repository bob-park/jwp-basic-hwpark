package next.controller.users;

import core.mvc.AbstractController;
import core.mvc.view.ModelAndView;
import next.controller.UserSessionUtils;
import next.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ListUserController extends AbstractController {

  private final UserService userService;

  public ListUserController(UserService userService) {
    this.userService = userService;
  }

  @Override
  public ModelAndView execute(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    if (!UserSessionUtils.isLogined(request.getSession())) {
      return jspView("redirect:/users/login");
    }

    request.setAttribute("users", userService.findAll());

    return jspView("/users/list.jsp");
  }
}
