package core.di;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Sets;

import core.annotation.Bean;
import core.annotation.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.reflections.ReflectionUtils.*;

public class BeanFactoryUtils {

  private static final Logger logger = LoggerFactory.getLogger(BeanFactoryUtils.class);

  private BeanFactoryUtils() {}

  /**
   * 인자로 전달하는 클래스의 생성자 중 @Inject 애노테이션이 설정되어 있는 생성자를 반환 @Inject 애노테이션이 설정되어 있는 생성자는 클래스당 하나로 가정한다.
   *
   * @param clazz
   * @return
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public static Constructor<?> getInjectedConstructor(Class<?> clazz) {
    Set<Constructor> injectedConstructors = getAllConstructors(clazz, withAnnotation(Inject.class));
    if (injectedConstructors.isEmpty()) {
      return null;
    }
    return injectedConstructors.iterator().next();
  }

  @SuppressWarnings("unchecked")
  public static Set<Field> getInjectedFields(Class<?> clazz) {
    return getAllFields(clazz, withAnnotation(Inject.class));
  }

  @SuppressWarnings("unchecked")
  public static Set<Method> getInjectedMethod(Class<?> clazz) {
    return getAllMethods(clazz, withAnnotation(Inject.class));
  }

  @SuppressWarnings("unchecked")
  public static Set<Method> getBeanMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
    return getAllMethods(clazz, withAnnotation(annotation));
  }

  public static Optional<Object> invokeMethod(Method method, Object bean, Object[] args) {
    try {
      return Optional.ofNullable(method.invoke(bean, args));
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      logger.error(e.getMessage());
      return Optional.empty();
    }
  }

  /**
   * 인자로 전달되는 클래스의 구현 클래스. 만약 인자로 전달되는 Class가 인터페이스가 아니면 전달되는 인자가 구현 클래스, 인터페이스인 경우 BeanFactory가
   * 관리하는 모든 클래스 중에 인터페이스를 구현하는 클래스를 찾아 반환
   *
   * @param injectedClazz
   * @param preInstantiateBeans
   * @return
   */
  public static Class<?> findConcreteClass(
      Class<?> injectedClazz, Set<Class<?>> preInstantiateBeans) {
    if (!injectedClazz.isInterface()) {
      return injectedClazz;
    }

    for (Class<?> clazz : preInstantiateBeans) {
      Set<Class<?>> interfaces = Sets.newHashSet(clazz.getInterfaces());
      if (interfaces.contains(injectedClazz)) {
        return clazz;
      }
    }

    throw new IllegalStateException(injectedClazz + " 인터페이스를 구현하는 Bean이 존재하지 않는다.");
  }
}
