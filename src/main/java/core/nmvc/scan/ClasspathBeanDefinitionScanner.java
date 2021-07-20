package core.nmvc.scan;

import com.google.common.collect.Sets;
import core.annotation.Controller;
import core.annotation.Repository;
import core.annotation.Service;
import core.di.bean.BeanDefinition;
import core.di.bean.BeanDefinitionRegistry;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ClasspathBeanDefinitionScanner {

  private final BeanDefinitionRegistry beanDefinitionRegistry;

  public ClasspathBeanDefinitionScanner(BeanDefinitionRegistry beanDefinitionRegistry) {
    this.beanDefinitionRegistry = beanDefinitionRegistry;
  }

  @SuppressWarnings("unchecked")
  public void doScan(Object... basePackage) {

    Reflections reflections = new Reflections(basePackage);

    Set<Class<?>> beanClasses =
        getTypeAnnotatedWith(reflections, Controller.class, Service.class, Repository.class);

    for (Class<?> clazz : beanClasses) {
      beanDefinitionRegistry.registerBeanDefinition(clazz, new BeanDefinition(clazz));
    }
  }

  @SuppressWarnings("unchecked")
  public Set<Class<?>> getTypeAnnotatedWith(
      Reflections reflections, Class<? extends Annotation>... annotations) {
    Set<Class<?>> preInstantiatedBeans = Sets.newHashSet();

    for (Class<? extends Annotation> annotation : annotations) {
      preInstantiatedBeans.addAll(reflections.getTypesAnnotatedWith(annotation));
    }

    return preInstantiatedBeans;
  }
}
