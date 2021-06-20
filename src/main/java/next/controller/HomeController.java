<<<<<<< HEAD:src/main/java/next/controller/HomeController.java
package next.controller;
=======
package next.web.user;
>>>>>>> master:src/main/java/next/controller/user/CreateUserServlet.java

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

<<<<<<< HEAD:src/main/java/next/controller/HomeController.java
import core.db.DataBase;

@WebServlet("")
public class HomeController extends HttpServlet {
=======
import next.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.db.DataBase;

@WebServlet("/user/create")
public class CreateUserServlet extends HttpServlet {
>>>>>>> master:src/main/java/next/controller/user/CreateUserServlet.java
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(CreateUserServlet.class);

    @Override
<<<<<<< HEAD:src/main/java/next/controller/HomeController.java
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("users", DataBase.findAll());
        RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
        rd.forward(req, resp);
=======
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                req.getParameter("email"));
        log.debug("user : {}", user);
        DataBase.addUser(user);
        resp.sendRedirect("/user/list");
>>>>>>> master:src/main/java/next/controller/user/CreateUserServlet.java
    }
}
