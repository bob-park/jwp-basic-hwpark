package core.mvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestMapping {

  private final Controller forward = new ForwardController();

  private final Map<String, Controller> mappings = Collections.synchronizedMap(new HashMap<>());

  public RequestMapping add(String uri, Controller controller) {
    mappings.put(uri, controller);
    return this;
  }

  public Controller get(String uri) {
    return mappings.get(uri);
  }

  public Controller getForward() {
    return forward;
  }
}
