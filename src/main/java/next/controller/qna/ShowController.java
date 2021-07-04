package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.view.ModelAndView;
import next.dao.AnswerDao;
import next.dao.QuestionDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowController extends AbstractController {

  private final QuestionDao questionDao;
  private final AnswerDao answerDao;

  public ShowController(QuestionDao questionDao, AnswerDao answerDao) {
    this.questionDao = questionDao;
    this.answerDao = answerDao;
  }

  @Override
  public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) {
    var questionId = Long.parseLong(req.getParameter("questionId"));

    req.setAttribute("question", questionDao.findById(questionId));
    req.setAttribute("answers", answerDao.findAllByQuestionId(questionId));

    return jspView("/qna/show.jsp");
  }
}
