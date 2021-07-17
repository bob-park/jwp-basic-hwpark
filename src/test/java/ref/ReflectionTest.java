package ref;

import next.model.User;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReflectionTest {

  @Test
  void createUser() throws Exception {
    Class<User> clazz = User.class;

    User user = null;

    for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {

      if (constructor.getParameters().length == 4) {
        user = (User) constructor.newInstance("test", "test", "test", "test@test.com");

        System.out.println(user);
        break;
      }
    }

    assertThat(user).isNotNull();
  }

  @Test
  void accessStudent() throws Exception {

    Student student = new Student();

    Class<Student> clazz = Student.class;

    Field nameFiled = clazz.getDeclaredField("name");
    Field ageField = clazz.getDeclaredField("age");

    nameFiled.setAccessible(true);
    ageField.setAccessible(true);

    nameFiled.set(student, "test");
    ageField.set(student, 1);

    assertAll(
        () -> assertThat(student.getName()).isEqualTo("test"),
        () -> assertThat(student.getAge()).isEqualTo(1));
  }
}
