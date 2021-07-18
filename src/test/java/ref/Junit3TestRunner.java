package ref;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Junit3TestRunner {

  @Test
  void run() throws Exception {
    Class<Junit3Test> clazz = Junit3Test.class;

    Object instance = clazz.getDeclaredConstructor().newInstance();

    for (Method method : clazz.getDeclaredMethods()) {

      if (StringUtils.startsWith(method.getName(), "test")) {
        method.invoke(instance);
      }
    }
  }
}
