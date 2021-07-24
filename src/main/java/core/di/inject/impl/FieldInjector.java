package core.di.inject.impl;

import core.di.BeanFactory;
import core.di.BeanFactoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Set;

public class FieldInjector extends AbstractInjector {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  public FieldInjector(BeanFactory beanFactory) {
    super(beanFactory);
  }

  @Override
  Set<?> getInjectBeans(Class<?> clazz) {
    return BeanFactoryUtils.getInjectedFields(clazz);
  }

  @Override
  Class<?> getBeanClass(Object injectedBean) {
    Field field = (Field) injectedBean;
    return field.getType();
  }

  @Override
  void inject(Object injectedBean, Object bean, BeanFactory beanFactory) {

    Field field = (Field) injectedBean;

    try {
      field.setAccessible(true);
      field.set(beanFactory.getBean(field.getDeclaringClass()), bean);

    } catch (IllegalAccessException | IllegalArgumentException e) {
      logger.error("Error inject field - {}", e.getMessage(), e);
    }
  }
}
