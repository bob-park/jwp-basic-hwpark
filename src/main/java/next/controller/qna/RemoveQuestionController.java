package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.view.ModelAndView;
import next.controller.UserSessionUtils;
import next.model.User;
import next.service.qna.QuestionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.math.NumberUtils.toLong;

public class RemoveQuestionController extends AbstractController {

  private final QuestionService questionService;

  public RemoveQuestionController(QuestionService questionService) {
    this.questionService = questionService;
  }

  @Override
  public ModelAndView execute(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    User user = UserSessionUtils.getUserFromSession(request.getSession());

    if (isEmpty(user)) {
      throw new IllegalStateException("Not logged in.");
    }

    questionService.removeQuestion(toLong(request.getParameter("questionId")), user);

    return jspView("redirect:/");
  }
}
