package core.nmvc;

import core.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerExecutionHandlerAdapter implements HandlerAdapter {

  @Override
  public boolean support(Object handler) {
    return handler instanceof HandlerExecution;
  }

  @Override
  public ModelAndView handle(
      HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    return ((HandlerExecution) handler).handle(request, response);
  }
}
