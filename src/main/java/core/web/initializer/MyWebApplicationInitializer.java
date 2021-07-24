package core.web.initializer;

import core.nmvc.AnnotationHandlerMapping;
import core.web.servlet.DispatcherServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class MyWebApplicationInitializer implements WebApplicationInitializer {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {

    AnnotationHandlerMapping ahm = new AnnotationHandlerMapping("next");
    ahm.initialize();

    ServletRegistration.Dynamic dispatcher =
        servletContext.addServlet("dispatcher", new DispatcherServlet(ahm));

    dispatcher.setLoadOnStartup(1);
    dispatcher.addMapping("/");

    logger.info("Start MyWebApplication Initializer.");
  }
}
