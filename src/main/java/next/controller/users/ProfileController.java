package next.controller.users;

import core.db.DataBase;
import next.controller.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProfileController implements Controller {

  @Override
  public String execute(HttpServletRequest request, HttpServletResponse response) {
    String userId = request.getParameter("userId");
    var user = DataBase.findUserById(userId);

    if (user == null) {
      throw new NullPointerException("사용자를 찾을 수 없습니다.");
    }

    request.setAttribute("user", user);
    return "/users/profile";
  }
}
