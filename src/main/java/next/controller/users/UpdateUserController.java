package next.controller.users;

import core.db.DataBase;
import next.controller.Controller;
import next.controller.UserSessionUtils;
import next.model.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateUserController implements Controller {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public String execute(HttpServletRequest request, HttpServletResponse response) {

    if (StringUtils.equalsIgnoreCase("get", request.getMethod())) {
      String userId = request.getParameter("userId");
      User user = DataBase.findUserById(userId);
      if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
        throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
      }
      request.setAttribute("user", user);
      return "/users/update";
    }

    var user = DataBase.findUserById(request.getParameter("userId"));
    if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
      throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
    }

    User updateUser =
        new User(
            request.getParameter("userId"),
            request.getParameter("password"),
            request.getParameter("name"),
            request.getParameter("email"));
    logger.debug("Update User : {}", updateUser);
    user.update(updateUser);

    return "redirect:/";
  }
}
