package core.di;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import core.annotation.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final Set<Class<?>> preInstantiateBeans;

  private final Map<Class<?>, Object> beans = Maps.newHashMap();

  public BeanFactory(Set<Class<?>> preInstantiateBeans) {
    this.preInstantiateBeans = preInstantiateBeans;
  }

  @SuppressWarnings("unchecked")
  public <T> T getBean(Class<T> requiredType) {
    return (T) beans.get(requiredType);
  }

  public void initialize() {
    for (Class<?> clazz : preInstantiateBeans) {
      if (beans.get(clazz) == null) {
        instantiateClass(clazz);
      }
    }
  }

  public Object instantiateClass(Class<?> clazz) {
    Object bean = beans.get(clazz);

    if (bean != null) {
      return bean;
    }

    Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);

    if (injectedConstructor == null) {
      bean = BeanUtils.instantiateClass(clazz);

      beans.put(clazz, bean);

      return bean;
    }

    logger.debug("Constructor : {}", injectedConstructor);
    bean = injectedConstructor(injectedConstructor);
    beans.put(clazz, bean);

    return bean;
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

  private Object injectedConstructor(Constructor<?> constructor) {
    Class<?>[] pTypes = constructor.getParameterTypes();

    List<Object> args = Lists.newArrayList();

    for (Class<?> clazz : pTypes) {
      Class<?> concreteClazz = BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans);

      if (!preInstantiateBeans.contains(concreteClazz)) {
        throw new IllegalStateException(clazz + "는 bean 이 아니다.");
      }

      Object bean = beans.get(concreteClazz);

      if (bean == null) {
        bean = instantiateClass(concreteClazz);
      }

      args.add(bean);
    }

    return BeanUtils.instantiateClass(constructor, args.toArray());
  }
}
