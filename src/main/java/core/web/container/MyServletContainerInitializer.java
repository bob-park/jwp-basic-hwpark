package core.web.container;

import core.web.initializer.WebApplicationInitializer;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@HandlesTypes(WebApplicationInitializer.class)
public class MyServletContainerInitializer implements ServletContainerInitializer {

  @Override
  public void onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext servletContext)
      throws ServletException {
    List<WebApplicationInitializer> initializers = new LinkedList<>();

    if (webAppInitializerClasses != null) {
      for (Class<?> waiClass : webAppInitializerClasses) {
        try {
          initializers.add(
              (WebApplicationInitializer) waiClass.getDeclaredConstructor().newInstance());
        } catch (Throwable e) {
          throw new ServletException("Failed to instantiate WebApplicationInitializer class", e);
        }
      }
    }

    if (initializers.isEmpty()) {
      servletContext.log("No Spring WebApplicationInitializer types detected on classpath.");
    }

    for (WebApplicationInitializer initializer : initializers) {
      initializer.onStartup(servletContext);
    }
  }
}
