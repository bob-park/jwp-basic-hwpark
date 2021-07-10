package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.view.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.QuestionDao;
import next.dao.UserDao;
import next.service.qna.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    questionService.removeQuestion(
        request.getSession(), toLong(request.getParameter("questionId")));

    return jspView("redirect:/");
  }
}
