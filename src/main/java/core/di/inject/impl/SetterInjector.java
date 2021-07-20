package core.di.inject.impl;

import core.di.BeanFactory;
import core.di.BeanFactoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class SetterInjector extends AbstractInjector {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  public SetterInjector(BeanFactory beanFactory) {
    super(beanFactory);
  }

  @Override
  Set<?> getInjectBeans(Class<?> clazz) {
    return BeanFactoryUtils.getInjectedMethod(clazz);
  }

  @Override
  Class<?> getBeanClass(Object injectedBean) {

    Method method = (Method) injectedBean;

    logger.debug("invoke method : {}", method);

    Class<?>[] parameterTypes = method.getParameterTypes();

    if (parameterTypes.length != 1) {
      throw new IllegalStateException("DI Method Parameter 가 하나이여야 합니다.");
    }

    return parameterTypes[0];
  }

  @Override
  void inject(Object injectedBean, Object bean, BeanFactory beanFactory) {

    Method method = (Method) injectedBean;

    try {

      method.invoke(beanFactory.getBean(method.getDeclaringClass()), bean);

    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      logger.error("Error inject method - {}", e.getMessage(), e);
    }
  }
}
