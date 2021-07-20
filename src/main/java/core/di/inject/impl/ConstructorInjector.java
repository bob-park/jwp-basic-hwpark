package core.di.inject.impl;

import com.google.common.collect.Sets;
import core.di.BeanFactory;

import java.util.Set;

public class ConstructorInjector extends AbstractInjector {

    public ConstructorInjector(BeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    Set<?> getInjectBeans(Class<?> clazz) {
    return Sets.newHashSet();
    }

    @Override
    Class<?> getBeanClass(Object injectedBean) {
        return null;
    }

    @Override
    void inject(Object injectedBean, Object bean, BeanFactory beanFactory) {

    }
}
