package next.controller.users;

import core.db.DataBase;
import core.mvc.Controller;
import next.model.User;
import next.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateUserController implements Controller {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final UserService userService;

  public CreateUserController(UserService userService) {
    this.userService = userService;
  }

  @Override
  public String execute(HttpServletRequest request, HttpServletResponse response) {

    User user =
        new User(
            request.getParameter("userId"),
            request.getParameter("password"),
            request.getParameter("name"),
            request.getParameter("email"));
    logger.debug("User : {}", user);

    userService.createUser(user);

    return "redirect:/";
  }
}
