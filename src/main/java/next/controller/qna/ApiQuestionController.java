package next.controller.qna;

import core.annotation.Controller;
import core.annotation.Inject;
import core.annotation.RequestMapping;
import core.annotation.RequestMethod;
import core.jdbc.exception.DataAccessException;
import core.mvc.view.ModelAndView;
import core.nmvc.AbstractNewController;
import next.controller.UserSessionUtils;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.exception.CannotDeleteException;
import next.model.Answer;
import next.model.Result;
import next.model.User;
import next.service.qna.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ApiQuestionController extends AbstractNewController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final QuestionService questionService;
  private final QuestionDao questionDao;
  private final AnswerDao answerDao;

  @Inject
  public ApiQuestionController(
      QuestionService questionService, QuestionDao questionDao, AnswerDao answerDao) {
    this.questionService = questionService;
    this.questionDao = questionDao;
    this.answerDao = answerDao;
  }

  @RequestMapping(value = "/api/qna/deleteQuestion", method = RequestMethod.POST)
  public ModelAndView deleteQuestion(HttpServletRequest req, HttpServletResponse resp)
      throws Exception {
    if (!UserSessionUtils.isLogined(req.getSession())) {
      return jsonView().addObject("result", Result.fail("Login is required"));
    }

    long questionId = Long.parseLong(req.getParameter("questionId"));
    try {
      questionService.removeQuestion(
          questionId, UserSessionUtils.getUserFromSession(req.getSession()));
      return jsonView().addObject("result", Result.ok());
    } catch (CannotDeleteException e) {
      return jsonView().addObject("result", Result.fail(e.getMessage()));
    }
  }

  @RequestMapping("/api/qna/list")
  public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    return jsonView().addObject("questions", questionDao.findAll());
  }

  @RequestMapping(value = "/api/qna/addAnswer", method = RequestMethod.POST)
  public ModelAndView addAnswer(HttpServletRequest req, HttpServletResponse response)
      throws Exception {
    if (!UserSessionUtils.isLogined(req.getSession())) {
      return jsonView().addObject("result", Result.fail("Login is required"));
    }

    User user = UserSessionUtils.getUserFromSession(req.getSession());
    Answer answer =
        new Answer(
            user.getUserId(),
            req.getParameter("contents"),
            Long.parseLong(req.getParameter("questionId")));
    logger.debug("answer : {}", answer);

    Answer savedAnswer = answerDao.insert(answer);
    questionDao.updateIncrementAnswerCount(savedAnswer.getQuestionId());

    return jsonView().addObject("answer", savedAnswer).addObject("result", Result.ok());
  }

  @RequestMapping(value = "/api/qna/removeAnswer", method = RequestMethod.POST)
  public ModelAndView deleteAnswer(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    Long answerId = Long.parseLong(request.getParameter("answerId"));

    ModelAndView mav = jsonView();
    try {
      answerDao.deleteById(answerId);
      mav.addObject("result", Result.ok());
    } catch (DataAccessException e) {
      mav.addObject("result", Result.fail(e.getMessage()));
    }
    return mav;
  }
}
