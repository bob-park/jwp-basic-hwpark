package core.nmvc.context;

import core.di.BeanFactory;
import core.nmvc.scan.ClasspathBeanDefinitionScanner;

import java.util.Set;

public class ApplicationContext {
  private final BeanFactory beanFactory;

  public ApplicationContext(Object... basePackage) {
    this.beanFactory = new BeanFactory();

    ClasspathBeanDefinitionScanner scanner = new ClasspathBeanDefinitionScanner(beanFactory);

    scanner.doScan(basePackage);

    beanFactory.initialize();
  }

  public <T> T getBean(Class<T> clazz) {
    return beanFactory.getBean(clazz);
  }

  public Set<Class<?>> getBeanClasses() {
    return beanFactory.getBeanClasses();
  }
}
