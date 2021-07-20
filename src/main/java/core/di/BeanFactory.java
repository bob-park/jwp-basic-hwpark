package core.di;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import core.di.bean.BeanDefinition;
import core.di.bean.BeanDefinitionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory implements BeanDefinitionRegistry {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final Map<Class<?>, Object> beans = Maps.newHashMap();
  private final Map<Class<?>, BeanDefinition> beanDefinitions = Maps.newHashMap();

  public Set<Class<?>> getBeanClasses() {
    return beanDefinitions.keySet();
  }

  @SuppressWarnings("unchecked")
  public <T> T getBean(Class<T> clazz) {
    Object bean = beans.get(clazz);

    if (bean != null) {
      return (T) bean;
    }

    Class<?> concreteClass = findConcreteClass(clazz);

    BeanDefinition beanDefinition = beanDefinitions.get(concreteClass);

    bean = inject(beanDefinition);

    beans.put(concreteClass, bean);

    return (T) bean;
  }

  public boolean contains(Class<?> clazz) {
    return beanDefinitions.containsKey(clazz);
  }

  @Override
  public void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
    logger.debug("register bean : {}", clazz);

    beanDefinitions.put(clazz, beanDefinition);
  }

  public void initialize() {
    for (Class<?> clazz : beanDefinitions.keySet()) {
      getBean(clazz);
    }
  }

  private Class<?> findConcreteClass(Class<?> clazz) {
    Set<Class<?>> beanClasses = getBeanClasses();

    Class<?> concreteClazz = BeanFactoryUtils.findConcreteClass(clazz, beanClasses);

    if (!beanClasses.contains(concreteClazz)) {
      throw new IllegalStateException(clazz + " 는 bean 이 아니다.");
    }

    return concreteClazz;
  }

  private Object inject(BeanDefinition beanDefinition) {

    switch (beanDefinition.getResolvedInjectMode()) {
      case NO:
        return BeanUtils.instantiateClass(beanDefinition.getBeanClazz());

      case FIELD:
        return injectField(beanDefinition);

      default:
        return injectConstructor(beanDefinition);
    }
  }

  private Object injectConstructor(BeanDefinition beanDefinition) {
    Constructor<?> constructor = beanDefinition.getInjectConstructor();

    List<Object> args = Lists.newArrayList();

    for (Class<?> clazz : constructor.getParameterTypes()) {

      args.add(getBean(clazz));
    }

    return BeanUtils.instantiateClass(constructor, args.toArray());
  }

  private Object injectField(BeanDefinition beanDefinition) {

    Object bean = BeanUtils.instantiateClass(beanDefinition.getBeanClazz());

    Set<Field> injectedFields = beanDefinition.getInjectFields();

    for (Field field : injectedFields) {
      injectField(bean, field);
    }

    return bean;
  }

  private void injectField(Object bean, Field field) {
    logger.debug("Inject Bean : {}, Field : {}", bean, field);

    try {
      field.setAccessible(true);
      field.set(bean, getBean(field.getType()));
    } catch (IllegalAccessException | IllegalArgumentException e) {
      logger.error("Error inject field - {}", e.getMessage(), e);
    }
  }
}
