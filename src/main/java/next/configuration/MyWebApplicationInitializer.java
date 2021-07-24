package next.configuration;

import core.nmvc.AnnotationHandlerMapping;
import core.nmvc.context.AnnotationConfigApplicationContext;
import core.nmvc.context.ApplicationContext;
import core.web.initializer.WebApplicationInitializer;
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

    ApplicationContext ac = new AnnotationConfigApplicationContext(MyConfiguration.class);

    AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping(ac);

    handlerMapping.initialize();

    ServletRegistration.Dynamic dispatcher =
        servletContext.addServlet("dispatcher", new DispatcherServlet(handlerMapping));

    dispatcher.setLoadOnStartup(1);
    dispatcher.addMapping("/");

    logger.info("Start MyWebApplication Initializer.");
  }
}
