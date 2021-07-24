package core.di.bean;

import core.annotation.Bean;
import core.di.BeanFactoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Set;

public class AnnotatedBeanDefinitionReader {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final BeanDefinitionRegistry beanDefinitionRegistry;

  public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
    this.beanDefinitionRegistry = beanDefinitionRegistry;
  }

  public void register(Class<?>... annotationClasses) {
    for (Class<?> annotationClass : annotationClasses) {
      registerBean(annotationClass);
    }
  }

  private void registerBean(Class<?> annotationClass) {
    beanDefinitionRegistry.registerBeanDefinition(
        annotationClass, new BeanDefinition(annotationClass));

    Set<Method> beanMethods = BeanFactoryUtils.getBeanMethods(annotationClass, Bean.class);

    for (Method beanMethod : beanMethods) {
      logger.debug("@Bean method : {}", beanMethod);

      AnnotatedBeanDefinition annotatedBeanDefinition =
          new AnnotatedBeanDefinition(annotationClass, beanMethod);
      beanDefinitionRegistry.registerBeanDefinition(
          beanMethod.getReturnType(), annotatedBeanDefinition);
    }
  }
}
