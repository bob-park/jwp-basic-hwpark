package core.mvc;

import core.mvc.view.ModelAndView;
import core.nmvc.HandlerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter implements HandlerAdapter {

  @Override
  public boolean support(Object handler) {
    return handler instanceof Controller;
  }

  @Override
  public ModelAndView handle(
      HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    return ((Controller) handler).execute(request, response);
  }
}
