package next;

import java.io.File;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServerLauncher {
  private static final Logger logger = LoggerFactory.getLogger(WebServerLauncher.class);

  public static void main(String[] args) throws Exception {
    String webappDirLocation = "webapp/";
    Tomcat tomcat = new Tomcat();
    tomcat.setPort(8080);

    // ! tomcat 9 embedded 인 경우 getConnector() 를 호출하여 기본 connector 를 설정할 수 있음.
    tomcat.getConnector();

    tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
    logger.info(
        "configuring app with basedir: {}", new File("./" + webappDirLocation).getAbsolutePath());

    tomcat.start();
    tomcat.getServer().await();
  }
}
