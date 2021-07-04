package next.controller.users;

import core.mvc.Controller;
import core.mvc.view.JspView;
import core.mvc.view.View;
import next.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProfileController implements Controller {

  private final UserService userService;

  public ProfileController(UserService userService) {
    this.userService = userService;
  }

  @Override
  public View execute(HttpServletRequest request, HttpServletResponse response) {
    String userId = request.getParameter("userId");
    var user = userService.findUser(userId);

    if (user == null) {
      throw new NullPointerException("사용자를 찾을 수 없습니다.");
    }

    request.setAttribute("user", user);
    return new JspView("/users/profile");
  }
}
