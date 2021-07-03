package next.controller.qna;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.mvc.Controller;
import next.dao.AnswerDao;
import next.model.Answer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang3.math.NumberUtils.toLong;

public class AddAnswerController implements Controller {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final AnswerDao answerDao;

  public AddAnswerController(AnswerDao answerDao) {
    this.answerDao = answerDao;
  }

  @Override
  public String execute(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    var om = new ObjectMapper();

    if (StringUtils.equalsIgnoreCase("POST", request.getMethod())) {

      var answer =
          new Answer(
              request.getParameter("writer"),
              request.getParameter("contents"),
              toLong(request.getParameter("questionId")));

      logger.debug("answer : {}", answer);

      var savedAnswer = answerDao.insert(answer);

      response.setContentType("application/json;charset=UTF-8");

      var out = response.getWriter();
      out.print(om.writeValueAsString(savedAnswer));

      return null;
    }

    throw new IllegalStateException("Not allowed method.");
  }
}
