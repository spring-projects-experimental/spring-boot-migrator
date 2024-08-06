package com.examples.jee.web;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "The Servlet", urlPatterns = {"/foo", "/bar"})
public class TheServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

    }
}