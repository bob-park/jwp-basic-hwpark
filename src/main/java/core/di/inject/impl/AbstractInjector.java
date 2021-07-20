package core.di.inject.impl;

import com.google.common.collect.Lists;
import core.di.BeanFactory;
import core.di.BeanFactoryUtils;
import core.di.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Set;

public abstract class AbstractInjector implements Injector {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final BeanFactory beanFactory;

  protected AbstractInjector(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  @Override
  public void inject(Class<?> clazz) {

    instantiateClass(clazz);

    Set<?> injectedBeans = getInjectBeans(clazz);

    for (Object injectBean : injectedBeans) {
      Class<?> beanClass = getBeanClass(injectBean);

      inject(injectBean, instantiateClass(beanClass), beanFactory);
    }
  }

  abstract Set<?> getInjectBeans(Class<?> clazz);

  abstract Class<?> getBeanClass(Object injectedBean);

  abstract void inject(Object injectedBean, Object bean, BeanFactory beanFactory);

  private Object instantiateClass(Class<?> clazz) {
    Object bean = beanFactory.getBean(clazz);

    if (bean != null) {
      return bean;
    }

    Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);

    if (injectedConstructor == null) {
      bean = BeanUtils.instantiateClass(clazz);

      beanFactory.setBean(clazz, bean);

      return bean;
    }

    logger.debug("Constructor : {}", injectedConstructor);
    bean = instantiateConstructor(injectedConstructor);
    beanFactory.setBean(clazz, bean);

    return bean;
  }

  private Object instantiateConstructor(Constructor<?> constructor) {
    Class<?>[] pTypes = constructor.getParameterTypes();

    List<Object> args = Lists.newArrayList();

    for (Class<?> clazz : pTypes) {
      Class<?> concreteClazz =
          BeanFactoryUtils.findConcreteClass(clazz, beanFactory.getPreInstantiateBeans());

      if (!beanFactory.contains(concreteClazz)) {
        throw new IllegalStateException(clazz + "는 bean 이 아니다.");
      }

      Object bean = beanFactory.getBean(concreteClazz);

      if (bean == null) {
        bean = instantiateClass(concreteClazz);
      }

      args.add(bean);
    }

    return BeanUtils.instantiateClass(constructor, args.toArray());
  }
}
