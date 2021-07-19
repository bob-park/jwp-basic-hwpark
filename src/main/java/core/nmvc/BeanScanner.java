package core.nmvc;

import com.google.common.collect.Sets;
import core.annotation.Controller;
import core.annotation.Repository;
import core.annotation.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

public class BeanScanner {

  private final Reflections reflections;

  public BeanScanner(Object... basePackage) {
    this.reflections = new Reflections(basePackage);
  }

  @SuppressWarnings("unchecked")
  public Set<Class<?>> scan() {
    return getTypeAnnotationWith(Controller.class, Service.class, Repository.class);
  }

  @SuppressWarnings("unchecked")
  public Set<Class<?>> getTypeAnnotationWith(Class<? extends Annotation>... annotations) {
    Set<Class<?>> preInstantiatedBeans = Sets.newHashSet();

    for (Class<? extends Annotation> annotation : annotations) {
      preInstantiatedBeans.addAll(reflections.getTypesAnnotatedWith(annotation));
    }

    return preInstantiatedBeans;
  }
}
