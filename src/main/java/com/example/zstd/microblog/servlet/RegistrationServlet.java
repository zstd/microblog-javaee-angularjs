package com.example.zstd.microblog.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

@WebServlet(urlPatterns = {"/register"})
public class RegistrationServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(RegistrationServlet.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("doGet");
        try (PrintWriter out = response.getWriter()){
            out.println("Hello World ! Want to register!");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
