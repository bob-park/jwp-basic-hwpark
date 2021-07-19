package next.model;

import next.exception.CannotDeleteException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class QuestionTest {

  @DisplayName("질문 삭제 - 글쓴이 다름")
  @ParameterizedTest
  @MethodSource("provideCanDelete")
  void canDeleteWithDifferentWriter(String userId, String writer) {

    User user = newUser(userId);
    Question question = newQuestion(writer);

    assertThatExceptionOfType(CannotDeleteException.class)
        .isThrownBy(() -> question.canDelete(user, new ArrayList<>()));
  }

  private User newUser(String userId) {
    return new User(userId, userId, userId, userId + "@test.com");
  }

  private Question newQuestion(String writer) {
    return new Question(1L, writer, "test", "test", new Date(), 1);
  }

  private static Stream<Arguments> provideCanDelete() {
    return Stream.of(Arguments.of("test1", "test2"));
  }
}
