package next.controller.users;

import core.mvc.Controller;
import core.mvc.view.JspView;
import core.mvc.view.View;
import next.controller.UserSessionUtils;
import next.model.User;
import next.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateUserController implements Controller {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final UserService userService;

  public UpdateUserController(UserService userService) {
    this.userService = userService;
  }

  @Override
  public View execute(HttpServletRequest request, HttpServletResponse response) {

    var user = userService.findUser(request.getParameter("userId"));

    if (StringUtils.equalsIgnoreCase("get", request.getMethod())) {
      if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
        throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
      }
      request.setAttribute("user", user);
      return new JspView("/users/update");
    }

    if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
      throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
    }

    var updateUser =
        new User(
            request.getParameter("userId"),
            request.getParameter("password"),
            request.getParameter("name"),
            request.getParameter("email"));
    logger.debug("Update User : {}", updateUser);

    user.update(updateUser);

    userService.updateUser(user);

    return new JspView("redirect:/");
  }
}
