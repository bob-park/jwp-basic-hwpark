package core.mvc;

import core.mvc.view.JspView;
import core.mvc.view.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForwardController implements Controller {

  @Override
  public View execute(HttpServletRequest request, HttpServletResponse response) {

    return new JspView(request.getRequestURI());
  }
}
