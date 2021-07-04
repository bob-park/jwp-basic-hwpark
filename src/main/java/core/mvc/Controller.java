package core.mvc;

import core.mvc.view.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Controller {

  View execute(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
