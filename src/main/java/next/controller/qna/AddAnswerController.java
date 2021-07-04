package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.view.ModelAndView;
import next.dao.AnswerDao;
import next.model.Answer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang3.math.NumberUtils.toLong;

public class AddAnswerController extends AbstractController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final AnswerDao answerDao;

  public AddAnswerController(AnswerDao answerDao) {
    this.answerDao = answerDao;
  }

  @Override
  public ModelAndView execute(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    if (StringUtils.equalsIgnoreCase("POST", request.getMethod())) {

      var answer =
          new Answer(
              request.getParameter("writer"),
              request.getParameter("contents"),
              toLong(request.getParameter("questionId")));

      logger.debug("answer : {}", answer);

      var savedAnswer = answerDao.insert(answer);

      return jsonView().addObject("answer", savedAnswer);
    }

    throw new IllegalStateException("Not allowed method.");
  }
}
