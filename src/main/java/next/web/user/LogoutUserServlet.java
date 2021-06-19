package next.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user/logout")
public class LogoutUserServlet extends HttpServlet {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    var session = req.getSession();

    session.removeAttribute("user");

    resp.sendRedirect("/user/login.jsp");
  }
}
