package next.controller.qna;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.mvc.Controller;
import next.dao.AnswerDao;
import next.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang3.math.NumberUtils.toLong;

public class RemoveAnswerController implements Controller {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final AnswerDao answerDao;

  public RemoveAnswerController(AnswerDao answerDao) {
    this.answerDao = answerDao;
  }

  @Override
  public String execute(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    var om = new ObjectMapper();

    var answerId = toLong(request.getParameter("answerId"));

    logger.debug("answerId : {}", answerId);

    answerDao.deleteById(answerId);

    response.setContentType("application/json;charset=UTF-8");

    var out = response.getWriter();
    out.print(om.writeValueAsString(Result.ok()));

    return null;
  }
}
