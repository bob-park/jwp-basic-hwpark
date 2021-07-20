package core.di.bean;

import com.google.common.collect.Sets;
import core.di.BeanFactoryUtils;
import core.di.inject.type.InjectType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

public class BeanDefinition {

  private final Class<?> beanClazz;
  private final Constructor<?> injectConstructor;
  private final Set<Field> injectFields;

  public BeanDefinition(Class<?> clazz) {
    this.beanClazz = clazz;

    this.injectConstructor = getInjectConstructor(clazz);
    this.injectFields = getInjectedFields(clazz, injectConstructor);
  }

  private Constructor<?> getInjectConstructor(Class<?> clazz) {
    return BeanFactoryUtils.getInjectedConstructor(clazz);
  }

  private Set<Field> getInjectedFields(Class<?> clazz, Constructor<?> constructor) {

    Set<Field> injectedFields = Sets.newHashSet();

    if (constructor != null) {
      return injectedFields;
    }

    Set<Class<?>> injectedProperties = getInjectedPropertiesType(clazz);

    Field[] fields = clazz.getFields();

    for (Field field : fields) {
      if (injectedProperties.contains(field.getType())) {
        injectedFields.add(field);
      }
    }

    return injectedFields;
  }

  private Set<Class<?>> getInjectedPropertiesType(Class<?> clazz) {

    Set<Class<?>> properties = Sets.newHashSet();

    Set<Method> injectedMethod = BeanFactoryUtils.getInjectedMethod(clazz);

    for (Method method : injectedMethod) {
      Class<?>[] parameterTypes = method.getParameterTypes();

      if (parameterTypes.length != 1) {
        throw new IllegalStateException("DI Method Parameter 가 하나이여야 합니다.");
      }

      injectedMethod.add(method);
    }

    return properties;
  }

  public Class<?> getBeanClazz() {
    return beanClazz;
  }

  public Constructor<?> getInjectConstructor() {
    return injectConstructor;
  }

  public Set<Field> getInjectFields() {
    return injectFields;
  }

  public InjectType getResolvedInjectMode() {
    if (injectConstructor != null) {
      return InjectType.CONSTRUCTOR;
    }

    if (!injectFields.isEmpty()) {
      return InjectType.FIELD;
    }

    return InjectType.NO;
  }
}
