package core.web.servlet;

import core.mvc.Controller;
import core.mvc.RequestMapping;
import next.controller.users.*;
import next.dao.UserDao;
import next.service.user.UserService;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

  private static final String DEFAULT_SUFFIX = "jsp";
  private static final String DEFAULT_EXECUTE_SEPARATOR = ":";
  private static final String DEFAULT_REDIRECT_PREFIX = "redirect";

  private final RequestMapping mapping = new RequestMapping();

  public DispatcherServlet() {

    var userDao = new UserDao();
    var userService = new UserService(userDao);

    mapping
        .add("/users/list", new ListUserController(userService))
        .add("/users/create", new CreateUserController())
        .add("/users/login", new LoginController(userService))
        .add("/users/logout", new LogoutController())
        .add("/users/profile", new ProfileController())
        .add("/users/update", new UpdateUserController(userService));
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String uri = req.getRequestURI();

    var controller = findController(uri);

    try {

      String viewName = controller.execute(req, resp);

      move(viewName, req, resp);

    } catch (Exception e) {
      throw new ServletException(e.getMessage());
    }
  }

  private Controller findController(String uri) {

    var controller = mapping.get(uri);

    if (isEmpty(controller)) {
      controller = mapping.getForward();
    }

    return controller;
  }

  private void move(String viewName, HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    boolean isRedirect = viewName.startsWith(DEFAULT_REDIRECT_PREFIX);

    if (isRedirect) {
      redirect(viewName.split(DEFAULT_EXECUTE_SEPARATOR)[1], request, response);
      return;
    }

    forward(viewName, request, response);
  }

  private void redirect(String redirect, HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    response.sendRedirect(redirect);
  }

  private void forward(String forward, HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    var rd = request.getRequestDispatcher(addSuffix(forward));
    rd.forward(request, response);
  }

  private String addSuffix(String uri) {

    String result = uri;

    String[] tokens = uri.split("[.]");

    if (tokens.length < 2 && !StringUtils.equals("/", uri)) {
      result = uri + "." + DEFAULT_SUFFIX;
    }

    return result;
  }
}
