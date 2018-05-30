package com.suntr.servletSample;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "MyGenericServlet" , urlPatterns = "/myg", initParams = {})
public class MyGenericServlet extends GenericServlet {

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {

        String id = req.getParameter("id");
        PrintWriter writer = res.getWriter();
        writer.print("Hello:" + id);
    }
}
