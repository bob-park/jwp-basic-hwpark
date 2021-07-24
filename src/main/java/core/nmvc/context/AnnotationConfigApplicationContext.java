package core.nmvc.context;

import com.google.common.collect.Lists;
import core.annotation.ComponentScan;
import core.di.BeanFactory;
import core.di.bean.AnnotatedBeanDefinitionReader;
import core.nmvc.scan.ClasspathBeanDefinitionScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class AnnotationConfigApplicationContext implements ApplicationContext {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final BeanFactory beanFactory;

  public AnnotationConfigApplicationContext(Class<?>... annotationClasses) {
    Object[] basePackages = findBasePackages(annotationClasses);

    this.beanFactory = new BeanFactory();

    AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beanFactory);

    reader.register(annotationClasses);

    if (basePackages.length > 0) {
      ClasspathBeanDefinitionScanner scanner = new ClasspathBeanDefinitionScanner(beanFactory);

      scanner.doScan(basePackages);
    }

    beanFactory.initialize();
  }

  @Override
  public <T> T getBean(Class<T> clazz) {
    return beanFactory.getBean(clazz);
  }

  @Override
  public Set<Class<?>> getBeanClasses() {
    return beanFactory.getBeanClasses();
  }

  private Object[] findBasePackages(Class<?>[] annotationClasses) {
    List<Object> basePackages = Lists.newArrayList();

    for (Class<?> annotationClass : annotationClasses) {
      ComponentScan componentScan = annotationClass.getAnnotation(ComponentScan.class);

      if (componentScan == null) {
        continue;
      }

      for (String basePackage : componentScan.value()) {
        logger.debug("Component scan base package : {}", basePackage);
      }

      basePackages.addAll(Arrays.asList(componentScan.value()));
    }

    return basePackages.toArray();
  }
}
