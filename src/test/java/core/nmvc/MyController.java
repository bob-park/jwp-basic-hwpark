package core.nmvc;

import core.annotation.Controller;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import core.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MyController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @RequestMapping(value = "/users/findUserId")
  public ModelAndView findUserId(HttpServletRequest request, HttpServletResponse response) {
    logger.debug("findUserId");
    return null;
  }

  @RequestMapping(value = "/users", method = RequestMethod.POST)
  public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
    logger.debug("save");
    return null;
  }
}
