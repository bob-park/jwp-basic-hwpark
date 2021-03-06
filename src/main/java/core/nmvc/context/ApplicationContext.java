package core.nmvc.context;

import java.util.Set;

public interface ApplicationContext {
  <T> T getBean(Class<T> clazz);

  Set<Class<?>> getBeanClasses();
}
