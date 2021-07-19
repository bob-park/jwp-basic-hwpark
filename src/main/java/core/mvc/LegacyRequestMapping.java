package core.mvc;

import core.nmvc.HandlerMapping;
import next.controller.HomeController;
import next.controller.qna.*;
import next.dao.impl.JdbcAnswerDao;
import next.dao.impl.JdbcQuestionDao;
import next.dao.impl.JdbcUserDao;
import next.service.qna.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LegacyRequestMapping implements HandlerMapping {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final Controller forward = new ForwardController();

  private final Map<String, Controller> mappings = Collections.synchronizedMap(new HashMap<>());

  public LegacyRequestMapping add(String uri, Controller controller) {
    mappings.put(uri, controller);

    logger.info("request mappings [{}]", uri);

    return this;
  }

  public Controller get(String uri) {
    return mappings.get(uri);
  }

  public Controller getForward() {
    return forward;
  }

  public void initMapping() {
    /*
     * dao
     */
    var userDao = new JdbcUserDao();
    var questionDao = new JdbcQuestionDao();
    var answerDao = new JdbcAnswerDao();

    /*
     * service
     */
    var questionService = new QuestionService(userDao, questionDao);

    // home
    mappings.put("/", new HomeController(questionDao));

    // qna
    mappings.put("/qna/form", new AddQuestionController(questionDao));
    mappings.put("/qna/show", new ShowController(questionDao, answerDao));
    mappings.put("/qna/updateForm", new UpdateQuestionController(userDao, questionDao));
    mappings.put("/qna/remove", new RemoveQuestionController(questionService));

    /*
     * api
     */
    // qna
    mappings.put("/api/qna/list", new QnaListController(questionDao));
    mappings.put("/api/qna/addAnswer", new AddAnswerController(answerDao, questionDao));
    mappings.put("/api/qna/removeAnswer", new RemoveAnswerController(answerDao, questionDao));
    mappings.put("/api/qna/remove", new ApiRemoveQuestionController(questionService));
  }

  @Override
  public Controller getHandler(HttpServletRequest request) {
    return get(request.getRequestURI());
  }
}
