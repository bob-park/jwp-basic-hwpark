package next.dao;

import next.model.Question;

import java.util.List;

public interface QuestionDao {

  List<Question> findAll();

  Question findById(long questionId);

  Question insert(Question question);

  Question updateIncrementAnswerCount(Long questionId);

  Question updateDecrementAnswerCount(Long questionId);

  Question update(Question question);

  boolean checkDelete(long questionId, String writer);

  void delete(long questionId);
}
