package core.nmvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class AnnotationHandlerMappingTest {
  private AnnotationHandlerMapping handlerMapping;

//  @BeforeEach
//  void setup() {
//    handlerMapping = new AnnotationHandlerMapping("core.nmvc");
//    handlerMapping.initialize();
//  }
//
//  @Test
//  void getHandler() throws Exception {
//    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/findUserId");
//    MockHttpServletResponse response = new MockHttpServletResponse();
//    HandlerExecution execution = handlerMapping.getHandler(request);
//    execution.handle(request, response);
//  }
}
