package core.di.bean;

public interface BeanDefinitionRegistry {
  void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition);
}
