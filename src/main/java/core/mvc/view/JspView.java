package core.mvc.view;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

public class JspView implements View {

  private static final String DEFAULT_SUFFIX = "jsp";
  private static final String DEFAULT_EXECUTE_SEPARATOR = ":";
  private static final String DEFAULT_REDIRECT_PREFIX = "redirect";

  private final String viewName;

  public JspView(String viewName) {

    checkArgument(isNotEmpty(viewName), "viewName must be provided.");

    this.viewName = viewName;
  }

  @Override
  public void render(HttpServletRequest request, HttpServletResponse response) throws Exception {

    move(viewName, request, response);
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
