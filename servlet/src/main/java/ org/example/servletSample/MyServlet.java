package com.suntr.servletSample;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "MyServlet", urlPatterns = {"/my"})
public class MyServlet implements Servlet {

    private transient ServletConfig servletConfig;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.servletConfig = config;
    }

    @Override
    public ServletConfig getServletConfig() {
        return this.servletConfig;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        String servletInfo = getServletInfo();
        res.setContentType("text/html");
        PrintWriter writer = res.getWriter();
        writer.append("<html><head><title>").append(servletInfo).append("</title></head><body>")
                .append(servletInfo).append("</body></html>");
        writer.flush();
    }

    @Override
    public String getServletInfo() {
        return "Servlet Info: no info";
    }

    @Override
    public void destroy() {

    }
}
