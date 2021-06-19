package next.web.user;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.db.DataBase;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@WebServlet("/user/list")
public class ListUserServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    var session = req.getSession();

    if (isEmpty(session.getAttribute("user"))) {
      resp.sendRedirect("/user/login.jsp");
      return;
    }

    req.setAttribute("users", DataBase.findAll());
    RequestDispatcher rd = req.getRequestDispatcher("/user/list.jsp");
    rd.forward(req, resp);
  }
}
