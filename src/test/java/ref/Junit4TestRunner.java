package ref;

import org.junit.jupiter.api.Test;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Method;

class Junit4TestRunner {

  @SuppressWarnings("unchecked")
  @Test
  void run() throws Exception {
    Class<Junit4Test> clazz = Junit4Test.class;

    for (Method method :
        ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(MyTest.class))) {
      method.invoke(clazz.getDeclaredConstructor().newInstance());
    }
  }
}
