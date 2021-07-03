package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.Controller;
import next.dao.AnswerDao;
import next.dao.QuestionDao;

public class ShowController implements Controller {

  private final QuestionDao questionDao;
  private final AnswerDao answerDao;

  public ShowController(QuestionDao questionDao, AnswerDao answerDao) {
    this.questionDao = questionDao;
    this.answerDao = answerDao;
  }

  @Override
  public String execute(HttpServletRequest req, HttpServletResponse resp) {
    var questionId = Long.parseLong(req.getParameter("questionId"));

    req.setAttribute("question", questionDao.findById(questionId));
    req.setAttribute("answers", answerDao.findAllByQuestionId(questionId));

    return "/qna/show.jsp";
  }
}
