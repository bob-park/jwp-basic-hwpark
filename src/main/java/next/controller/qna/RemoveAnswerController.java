package next.controller.qna;

import core.mvc.AbstractController;
import core.mvc.view.ModelAndView;
import next.dao.AnswerDao;
import next.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang3.math.NumberUtils.toLong;

public class RemoveAnswerController extends AbstractController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final AnswerDao answerDao;

  public RemoveAnswerController(AnswerDao answerDao) {
    this.answerDao = answerDao;
  }

  @Override
  public ModelAndView execute(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    var answerId = toLong(request.getParameter("answerId"));

    logger.debug("answerId : {}", answerId);

    answerDao.deleteById(answerId);

    response.setContentType("application/json;charset=UTF-8");

    return jsonView().addObject("result", Result.ok());
  }
}
