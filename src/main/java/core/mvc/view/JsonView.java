package core.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

  private final ObjectMapper om;

  public JsonView() {
    this.om = new ObjectMapper();
  }

  @Override
  public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
      throws Exception {

    response.setContentType("application/json;charset=UTF-8");

    var out = response.getWriter();

    out.print(om.writeValueAsString(model));
  }

  //  private Map<String, Object> createModel(HttpServletRequest request) {
  //    Enumeration<String> names = request.getAttributeNames();
  //
  //    Map<String, Object> model = new HashMap<>();
  //
  //    while (names.hasMoreElements()) {
  //      String name = names.nextElement();
  //
  //      model.put(name, request.getAttribute(name));
  //    }
  //
  //    return model;
  //  }
}
