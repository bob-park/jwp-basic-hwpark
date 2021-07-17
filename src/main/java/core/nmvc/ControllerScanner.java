package core.nmvc;

import com.google.common.collect.Maps;
import core.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final Reflections reflections;

  public ControllerScanner(Object... basePackage) {
    this.reflections = new Reflections(basePackage);
  }

  public Map<Class<?>, Object> getControllers() {
    Set<Class<?>> preInitialedControllers = reflections.getTypesAnnotatedWith(Controller.class);
    return instantiateControllers(preInitialedControllers);
  }

  private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> preInitialedControllers) {
    Map<Class<?>, Object> controllers = Maps.newHashMap();

    try {
      for (Class<?> clazz : preInitialedControllers) {
        controllers.put(clazz, clazz.getDeclaredConstructor().newInstance());
      }
    } catch (InstantiationException
        | IllegalAccessException
        | NoSuchMethodException
        | InvocationTargetException e) {

      logger.error(e.getMessage(), e);
    }

    return controllers;
  }
}
