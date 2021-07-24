package core.di.bean;

import java.lang.reflect.Method;

public class AnnotatedBeanDefinition extends BeanDefinition {

  private final Method method;

  public AnnotatedBeanDefinition(Class<?> clazz, Method method) {
    super(clazz);
    this.method = method;
  }

  public Method getMethod() {
    return method;
  }
}
