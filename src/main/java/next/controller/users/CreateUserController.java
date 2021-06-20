package next.controller.users;

import core.db.DataBase;
import next.controller.Controller;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateUserController implements Controller {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public String execute(HttpServletRequest request, HttpServletResponse response) {

    User user =
        new User(
            request.getParameter("userId"),
            request.getParameter("password"),
            request.getParameter("name"),
            request.getParameter("email"));
    logger.debug("User : {}", user);

    DataBase.addUser(user);

    return "redirect:/";
  }
}
