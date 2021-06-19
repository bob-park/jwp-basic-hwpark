package next.web.user;

import core.db.DataBase;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@WebServlet("/user/login")
public class LoginUserServlet extends HttpServlet {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    var user = DataBase.findUserById(req.getParameter("userId"));

    if (isNotEmpty(user) && StringUtils.equals(user.getPassword(), req.getParameter("password"))) {

      req.getSession().setAttribute("user", user);
      resp.sendRedirect("/");

      return;
    }

    resp.sendRedirect("/user/login_failed.html");
  }
}
