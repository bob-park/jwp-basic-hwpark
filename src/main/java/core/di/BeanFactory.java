package core.di;

import com.google.common.collect.Maps;
import core.annotation.Controller;
import core.di.inject.Injector;
import core.di.inject.impl.ConstructorInjector;
import core.di.inject.impl.FieldInjector;
import core.di.inject.impl.SetterInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final Set<Class<?>> preInstantiateBeans;
  private final Map<Class<?>, Object> beans = Maps.newHashMap();
  private final List<Injector> injectors;

  public BeanFactory(Set<Class<?>> preInstantiateBeans) {
    this.preInstantiateBeans = preInstantiateBeans;

    this.injectors =
        Arrays.asList(
            new FieldInjector(this), new SetterInjector(this), new ConstructorInjector(this));
  }

  @SuppressWarnings("unchecked")
  public <T> T getBean(Class<T> requiredType) {
    return (T) beans.get(requiredType);
  }

  public void setBean(Class<?> clazz, Object bean) {
    beans.put(clazz, bean);
  }

  public Set<Class<?>> getPreInstantiateBeans() {
    return preInstantiateBeans;
  }

  public boolean contains(Class<?> concreteClazz) {
    return preInstantiateBeans.contains(concreteClazz);
  }

  public void initialize() {
    for (Class<?> clazz : preInstantiateBeans) {
      if (beans.get(clazz) == null) {
        logger.debug("instantiate Class : {}", clazz);

        inject(clazz);
      }
    }
  }

  private void inject(Class<?> clazz) {
    for (Injector injector : injectors) {
      injector.inject(clazz);
    }
  }

  public Map<Class<?>, Object> getControllers() {
    Map<Class<?>, Object> controllers = Maps.newHashMap();

    for (Class<?> clazz : preInstantiateBeans) {
      Annotation annotation = clazz.getAnnotation(Controller.class);

      if (annotation != null) {
        controllers.put(clazz, beans.get(clazz));
      }
    }

    return controllers;
  }
}
