package core.di.factory.example;

import core.annotation.Inject;
import core.annotation.Service;

//@Service
public class MyQnaService {
  private final UserRepository userRepository;
  private final QuestionRepository questionRepository;

  @Inject
  public MyQnaService(UserRepository userRepository, QuestionRepository questionRepository) {
    this.userRepository = userRepository;
    this.questionRepository = questionRepository;
  }

  public UserRepository getUserRepository() {
    return userRepository;
  }

  public QuestionRepository getQuestionRepository() {
    return questionRepository;
  }
}
