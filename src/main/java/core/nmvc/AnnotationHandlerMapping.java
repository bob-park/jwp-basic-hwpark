package core.nmvc;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import core.annotation.Controller;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import core.di.BeanFactory;
import core.nmvc.context.ApplicationContext;
import core.nmvc.scan.ClasspathBeanDefinitionScanner;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final Object[] basePackage;

  private final Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

  public AnnotationHandlerMapping(Object... basePackage) {
    this.basePackage = basePackage;
  }

  public void initialize() {

    ApplicationContext context = new ApplicationContext(basePackage);

    Map<Class<?>, Object> controllers = getControllers(context);

    Set<Method> methods = getRequestMappingMethods(controllers.keySet());

    for (Method method : methods) {
      RequestMapping rm = method.getAnnotation(RequestMapping.class);

      logger.debug("register handlerExecution : url is {}, method is {}", rm.value(), method);

      handlerExecutions.put(
          createHandlerKey(rm),
          new HandlerExecution(context.getBean(method.getDeclaringClass()), method));
    }
  }

  public HandlerExecution getHandler(HttpServletRequest request) {
    String requestUri = request.getRequestURI();

    RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());

    logger.debug("requestUri : {}, requestMethod : {}", requestUri, rm);

    return handlerExecutions.get(new HandlerKey(requestUri, rm));
  }

  @SuppressWarnings("unchecked")
  private Set<Method> getRequestMappingMethods(Set<Class<?>> controllers) {
    Set<Method> requestMappingMethods = Sets.newHashSet();

    for (Class<?> clazz : controllers) {
      requestMappingMethods.addAll(
          ReflectionUtils.getAllMethods(
              clazz, ReflectionUtils.withAnnotation(RequestMapping.class)));
    }

    return requestMappingMethods;
  }

  private HandlerKey createHandlerKey(RequestMapping rm) {
    return new HandlerKey(rm.value(), rm.method());
  }

  private Map<Class<?>, Object> getControllers(ApplicationContext context) {

    Map<Class<?>, Object> controllers = Maps.newHashMap();

    for (Class<?> clazz : context.getBeanClasses()) {

      Annotation annotation = clazz.getAnnotation(Controller.class);

      if (annotation != null) {
        controllers.put(clazz, context.getBean(clazz));
      }
    }

    return controllers;
  }
}
