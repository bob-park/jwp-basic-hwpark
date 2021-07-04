package core.mvc;

import core.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForwardController extends AbstractController {

  @Override
  public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) {

    return jspView(request.getRequestURI());
  }
}
