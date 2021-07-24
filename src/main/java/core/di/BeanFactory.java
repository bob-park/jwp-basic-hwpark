package core.di;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import core.annotation.PostConstruct;
import core.di.bean.AnnotatedBeanDefinition;
import core.di.bean.BeanDefinition;
import core.di.bean.BeanDefinitionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    BeanDefinition beanDefinition = beanDefinitions.get(clazz);

    if (beanDefinition instanceof AnnotatedBeanDefinition) {
      Optional<Object> optionalBean = createAnnotatedBean(beanDefinition);
      optionalBean.ifPresent(b -> beans.put(clazz, b));

      if (optionalBean.isPresent()) {
        bean = optionalBean.get();
      }

      initialize(bean, clazz);

      return (T) bean;
    }

    Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, getBeanClasses());

    beanDefinition = beanDefinitions.get(concreteClass);

    bean = inject(beanDefinition);

    beans.put(concreteClass, bean);

    initialize(bean, concreteClass);

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

  private void initialize(Object bean, Class<?> beanClass) {
    Set<Method> initializeMethods = BeanFactoryUtils.getBeanMethods(beanClass, PostConstruct.class);
    if (initializeMethods.isEmpty()) {
      return;
    }
    for (Method initializeMethod : initializeMethods) {
      logger.debug("@PostConstruct Initialize Method : {}", initializeMethod);
      BeanFactoryUtils.invokeMethod(
          initializeMethod, bean, populateArguments(initializeMethod.getParameterTypes()));
    }
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

  private Optional<Object> createAnnotatedBean(BeanDefinition beanDefinition) {

    AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) beanDefinition;

    Method method = annotatedBeanDefinition.getMethod();

    Object[] args = populateArguments(method.getParameterTypes());
    return BeanFactoryUtils.invokeMethod(method, getBean(method.getDeclaringClass()), args);
  }

  private Object[] populateArguments(Class<?>[] paramTypes) {
    List<Object> args = Lists.newArrayList();
    for (Class<?> param : paramTypes) {
      Object bean = getBean(param);
      if (bean == null) {
        throw new NullPointerException(param + "에 해당하는 Bean이 존재하지 않습니다.");
      }
      args.add(getBean(param));
    }
    return args.toArray();
  }
}
