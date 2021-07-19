package next.service.qna;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.exception.CannotDeleteException;
import next.model.Question;
import next.model.User;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

  @Mock private QuestionDao questionDao;

  @Mock private AnswerDao answerDao;

  private QuestionService questionService;

  @BeforeEach
  void setup() {
    questionService = new QuestionService(questionDao, answerDao);
  }

  @DisplayName("질문 삭제 - 없는 질문")
  @ParameterizedTest
  @MethodSource("provideQuestion")
  void removeQuestionWithNoExist(long questionId, String userId) {
    when(questionDao.findById(questionId)).thenReturn(null);

    assertThatExceptionOfType(CannotDeleteException.class)
        .isThrownBy(() -> questionService.removeQuestion(questionId, newUser(userId)));
  }

  @DisplayName("질문 삭제 - 같은 사용자 답편 없음")
  @ParameterizedTest
  @MethodSource("provideQuestion")
  void removeQuestionWithSameUserNoAnswer(long questionId, String userId) {

    Question question = newQuestion(1, userId);

    when(questionDao.findById(questionId)).thenReturn(question);
    when(answerDao.findAllByQuestionId(questionId)).thenReturn(Lists.newArrayList());

    questionService.removeQuestion(questionId, newUser(userId));
  }

  private User newUser(String userId) {
    return new User(userId, userId, userId, userId + "@test.com");
  }

  private Question newQuestion(long questionId, String userId) {
    return new Question(questionId, userId, "test", "test", new Date(), 1);
  }

  private static Stream<Arguments> provideQuestion() {
    return Stream.of(Arguments.of(1, "test1"));
  }
}
