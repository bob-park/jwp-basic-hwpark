package core.di.bean;

public interface BeanDefinitionReader {

    void loadBeanDefinitions(Class<?>... annotationClasses);

}
