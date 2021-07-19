package core.di.factory;

import com.google.common.collect.Sets;
import core.annotation.Controller;
import core.annotation.Repository;
import core.annotation.Service;
import core.di.BeanFactory;
import core.di.factory.example.MyQnaService;
import core.di.factory.example.QnaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BeanFactoryTest {
  private Reflections reflections;
  private BeanFactory beanFactory;

  @BeforeEach
  @SuppressWarnings("unchecked")
  void setup() {
    reflections = new Reflections("core.di.factory.example");
    Set<Class<?>> preInstantiateClazz =
        getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
    beanFactory = new BeanFactory(preInstantiateClazz);
    beanFactory.initialize();
  }

  @Test
  void di() throws Exception {
    QnaController qnaController = beanFactory.getBean(QnaController.class);

    assertThat(qnaController).isNotNull();
    assertThat(qnaController.getQnaService()).isNotNull();

    MyQnaService qnaService = qnaController.getQnaService();

    assertThat(qnaService.getUserRepository()).isNotNull();
    assertThat(qnaService.getQuestionRepository()).isNotNull();
  }

  @SuppressWarnings("unchecked")
  Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
    Set<Class<?>> beans = Sets.newHashSet();
    for (Class<? extends Annotation> annotation : annotations) {
      beans.addAll(reflections.getTypesAnnotatedWith(annotation));
    }
    return beans;
  }
}
