package com.peppersan.textquest.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.IOException;

@WebServlet("/")
public class StartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/WEB-INF/jsp/start.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String choice = req.getParameter("choice");

        if ("yes".equals(choice)) {
            req.setAttribute("result", "Ты принял вызов НЛО.");
        } else {
            req.setAttribute("result", "Ты отказался от вызова.");
        }

        req.getRequestDispatcher("/WEB-INF/jsp/result.jsp")
                .forward(req, resp);
    }

}

