package core.web.servlet;

import next.controller.Controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestMapping {

  private final Map<String, Controller> MAPPINGS = Collections.synchronizedMap(new HashMap<>());

  public RequestMapping add(String uri, Controller controller) {
    MAPPINGS.put(uri, controller);
    return this;
  }

  public Controller get(String uri) {
    return MAPPINGS.get(uri);
  }
}
