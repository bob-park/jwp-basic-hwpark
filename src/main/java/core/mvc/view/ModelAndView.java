package core.mvc.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

  private final View view;
  private final Map<String, Object> model = new HashMap<>();

  public ModelAndView(View view) {
    this.view = view;
  }

  public ModelAndView addObject(String name, String value) {
    model.put(name, value);
    return this;
  }

  public View getView() {
    return view;
  }

  public Map<String, Object> getModel() {
    return Collections.unmodifiableMap(model);
  }
}
