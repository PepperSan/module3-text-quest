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

        int money = parseIntOrDefault(req.getParameter("money"), 1000);
        int calories = parseIntOrDefault(req.getParameter("calories"), 0);

        String choice = req.getParameter("choice");

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
            if (money < 100) {
                req.setAttribute("text", "Денег на донат нет. Идёшь спать/работать 😄");
            } else {
                money -= 100;
                calories += 50;
                req.setAttribute("text", "Ты залип в Dota всю ночь. -100 денег, +50 калорий.");
            }
        }

        if ("sleep".equals(choice)) {
            calories -= 20;
            text = "Ты выспался. -20 калорий (организм восстановился).";
        }

        if ("shop".equals(choice)) {
            money -= 300;
            calories += 30;
            text = "Ты сходил в торговый центр. -300 денег, +30 калорий (фудкорт).";
        }


        req.setAttribute("step", "home");
        req.setAttribute("money", money);
        req.setAttribute("calories", calories);
        req.setAttribute("text", text);

        req.getRequestDispatcher("/WEB-INF/jsp/view.jsp").forward(req, resp);


    }

    private int parseIntOrDefault(String value, int def) {
        if (value == null || value.isBlank()) return def;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return def;
        }
    }

}

