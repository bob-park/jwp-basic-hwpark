package next.controller.users;

import core.mvc.AbstractController;
import core.mvc.view.ModelAndView;
import next.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProfileController extends AbstractController {

  private final UserService userService;

  public ProfileController(UserService userService) {
    this.userService = userService;
  }

  @Override
  public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) {
    String userId = request.getParameter("userId");
    var user = userService.findUser(userId);

    if (user == null) {
      throw new NullPointerException("사용자를 찾을 수 없습니다.");
    }

    request.setAttribute("user", user);
    return jspView("/users/profile");
  }
}
