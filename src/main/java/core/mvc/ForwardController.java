package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForwardController implements Controller {

  @Override
  public String execute(HttpServletRequest request, HttpServletResponse response) {

    return request.getRequestURI();
  }
}
