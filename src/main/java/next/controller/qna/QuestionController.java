package next.controller.qna;

import core.annotation.Controller;
import core.annotation.Inject;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import core.mvc.view.ModelAndView;
import core.nmvc.AbstractNewController;
import next.controller.UserSessionUtils;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.exception.CannotDeleteException;
import next.model.Question;
import next.model.User;
import next.service.qna.QuestionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class QuestionController extends AbstractNewController {

  private final QuestionService questionService;

  private final QuestionDao questionDao;
  private final AnswerDao answerDao;

  @Inject
  public QuestionController(
      QuestionService questionService, QuestionDao questionDao, AnswerDao answerDao) {
    this.questionService = questionService;
    this.questionDao = questionDao;
    this.answerDao = answerDao;
  }

  @RequestMapping("/qna/show")
  public ModelAndView show(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    var questionId = Long.parseLong(request.getParameter("questionId"));

    request.setAttribute("question", questionDao.findById(questionId));
    request.setAttribute("answers", answerDao.findAllByQuestionId(questionId));

    return jspView("/qna/show.jsp");
  }

  @RequestMapping("/qna/form")
  public ModelAndView createForm(HttpServletRequest req, HttpServletResponse resp)
      throws Exception {
    if (!UserSessionUtils.isLogined(req.getSession())) {
      return jspView("redirect:/users/loginForm");
    }
    return jspView("/qna/form.jsp");
  }

  @RequestMapping(value = "/qna/create", method = RequestMethod.POST)
  public ModelAndView create(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    if (!UserSessionUtils.isLogined(request.getSession())) {
      return jspView("redirect:/users/loginForm");
    }
    User user = UserSessionUtils.getUserFromSession(request.getSession());
    Question question =
        new Question(
            user.getUserId(), request.getParameter("title"), request.getParameter("contents"));
    questionDao.insert(question);
    return jspView("redirect:/");
  }

  @RequestMapping("/qna/updateForm")
  public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp)
      throws Exception {
    if (!UserSessionUtils.isLogined(req.getSession())) {
      return jspView("redirect:/users/loginForm");
    }

    long questionId = Long.parseLong(req.getParameter("questionId"));
    Question question = questionDao.findById(questionId);
    if (!question.isSameWriter(UserSessionUtils.getUserFromSession(req.getSession()))) {
      throw new IllegalStateException("다른 사용자가 쓴 글을 수정할 수 없습니다.");
    }
    return jspView("/qna/update.jsp").addObject("question", question);
  }

  @RequestMapping(value = "/qna/update", method = RequestMethod.POST)
  public ModelAndView update(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    if (!UserSessionUtils.isLogined(req.getSession())) {
      return jspView("redirect:/users/loginForm");
    }

    long questionId = Long.parseLong(req.getParameter("questionId"));
    Question question = questionDao.findById(questionId);
    if (!question.isSameWriter(UserSessionUtils.getUserFromSession(req.getSession()))) {
      throw new IllegalStateException("다른 사용자가 쓴 글을 수정할 수 없습니다.");
    }

    Question newQuestion =
        new Question(question.getWriter(), req.getParameter("title"), req.getParameter("contents"));
    question.update(newQuestion);
    questionDao.update(question);
    return jspView("redirect:/");
  }

  @RequestMapping(value = "/qna/delete", method = RequestMethod.POST)
  public ModelAndView delete(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    if (!UserSessionUtils.isLogined(req.getSession())) {
      return jspView("redirect:/users/loginForm");
    }

    long questionId = Long.parseLong(req.getParameter("questionId"));
    try {
      questionService.removeQuestion(
          questionId, UserSessionUtils.getUserFromSession(req.getSession()));
      return jspView("redirect:/");
    } catch (CannotDeleteException e) {
      return jspView("show.jsp")
          .addObject("question", questionDao.findById(questionId))
          .addObject("answers", answerDao.findAllByQuestionId(questionId))
          .addObject("errorMessage", e.getMessage());
    }
  }
}
