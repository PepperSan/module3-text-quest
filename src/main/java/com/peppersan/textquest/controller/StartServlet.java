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
        req.getRequestDispatcher("/WEB-INF/jsp/start.jsp").forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String choice = req.getParameter("choice");

//        if (!"start".equals(choice)) {
//            resp.sendRedirect(req.getContextPath() + "/module3_text_quest");
//            return;
//        }

// стартовые значения (пока каждый раз одни и те же)
        req.setAttribute("step", "home");
        req.setAttribute("money", 1000);
        req.setAttribute("calories", 0);

// дефолтный текст
        String text =
                "Что сделаешь прямо сейчас?\n" +
                        "1) Поиграть в Dota\n" +
                        "2) Поспать\n" +
                        "3) Пойти в торговый центр";

// первый реальный выбор
        if ("dota".equals(choice)) {
            text = "Ты залип в Dota всю ночь. Минус продуктивность.";
        }

        if ("sleep".equals(choice)) {
            text = "Ты хорошо выспался и восстановил силы.";
        }

        if ("shop".equals(choice)) {
            text = "Ты пошёл в торговый центр и потратил деньги.";
        }

        req.setAttribute("text", text);
        req.getRequestDispatcher("/WEB-INF/jsp/view.jsp").forward(req, resp);

    }


}

