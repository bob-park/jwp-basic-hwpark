package core.mvc;

import core.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Controller {

  ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
