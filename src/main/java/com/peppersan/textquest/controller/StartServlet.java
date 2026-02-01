package com.peppersan.textquest.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import java.io.IOException;

@WebServlet("/")
public class StartServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        session.setAttribute("money", 1000);
        session.setAttribute("calories", 0);
        session.setAttribute("step", "start");
        session.setAttribute("lastChoice", null);


        req.getRequestDispatcher("/WEB-INF/jsp/start.jsp").forward(req, resp);
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();



        Integer moneyObj = (Integer) session.getAttribute("money");
        Integer caloriesObj = (Integer) session.getAttribute("calories");
        String step = (String) session.getAttribute("step");
        String cart = (String) session.getAttribute("cart");
        String choice = req.getParameter("choice");
        String lastChoice = (String) session.getAttribute("lastChoice");
        String text = "Выбирай: Dota / Сон / ТЦ";

        if (cart == null) {
            cart = "";
        }
        int money = (moneyObj == null) ? 1000 : moneyObj;
        int calories = (caloriesObj == null) ? 0 : caloriesObj;
        if (step == null) step = "home";

        if (choice != null && choice.equals(lastChoice)) {
            text = "⏳ Ты только что это сделал. Выбери другое действие.";

        } else {

            session.setAttribute("lastChoice", choice);
        }



        // старт игры
        if ("start".equals(choice)) {
            money = 1000;
            calories = 0;
            step = "home";
            text = "Что сделаешь прямо сейчас?";
        }

        // действия
        else if ("dota".equals(choice)) {
            calories += 50;
            text = "Ты залип в Dota всю ночь. +50 калорий.";
            step = "home";

        }


        if ("sleep".equals(choice)) {
            if ("sleep".equals(lastChoice)) {
                text = "Ты уже спал. Если ещё поспишь — проспишь жизнь 😴";
            } else if (calories >= 100) {
                text = "Ты и так бодр. Лучше займись делом.";
            } else {
                calories += 20;
                text = "Ты выспался. +20 калорий.";
            }
        }

        if ("shop".equals(choice)) {
            step = "mall";
            text = "🏬 Ты в торговом центре. Куда идём?";
        }


        // предупреждение про такси (когда меньше 100, но не 0)
        if (money > 0 && money < 100) {
            text += "\n⚠️ Осталось меньше 100. Оставь на автобус/такси, иначе пойдёшь пешком.";
        }


        if ("shop".equals(choice)) {
            step = "mall";
            text = "Ты в торговом центре. Куда идём?";
        }


        if ("mall_food".equals(choice)) {
            step = "food";
            text = "🍕 Фудкорт пахнет победой и фастфудом. Что берём?";
        }

        if ("mall_electronics".equals(choice)) {
            step = "electronics";
            text = "🖥️ Магазин электроники. Выбирай покупку!";
        }


        if ("buy_pizza".equals(choice)) {
            if (money >= 80) {
                money -= 80;
                calories += 25;
                cart += "Пицца (фудкорт) (-80)\n";
                text = "✅ Купил пиццу (-80) и зарядился (+25 кал).";
                step = "food";
            } else {
                text = "❌ На пиццу не хватает денег.";
                step = "food";
            }
        }

        if ("buy_cola".equals(choice)) {
            if (money >= 20) {
                money -= 20;
                calories += 10;
                cart += "Кола 0.5 (-20)\n";
                text = "✅ Купил колу (-20) (+10 кал).";
                step = "food";
            } else {
                text = "❌ На колу не хватает денег.";
                step = "food";
            }
        }


        String currentStep = step;

        if ("buy".equals(choice)) {
            String itemId = req.getParameter("itemId");
            Item item = findItem(itemId);

            if (item == null) {
                text = "❓ Неизвестный товар.";
                step = "shop";
            } else if (money < item.price) {
                text = "⛔ Не хватило денег на: " + item.name + " (" + item.price + ").";
                step = "shop";
            } else {
                money -= item.price;
                calories += item.calories;


                cart = (String) session.getAttribute("cart");
                if (cart == null) cart = "";
                cart = cart + "• " + item.name + " (-" + item.price + ")\n";
                session.setAttribute("cart", cart);

                text = "✅ Купил: " + item.name + " (-" + item.price + ").";
                step = currentStep;
            }


        }

        if (money <= 0) {
            money = 0;

            if (!"home".equals(step) && !"restHome".equals(step)) {
                text = "🚶 Денег на транспорт нет — идёшь домой пешком. -30 калорий.";
                calories -= 30;
                step = "walkHome";
            }
        }
        if (calories < 0) calories = 0;


        if (calories >= 120) {
            calories = 120;
            text = "😵 Перегруз. Слишком много калорий — ты валишься на диван.";
            step = "restHome";
        }


        if ("backHome".equals(choice)) {
            step = "home";
            text = "🏠 Ты дома. Что делаешь дальше?";
        }


        if ("back_mall".equals(choice)) {
            step = "mall";
            text = "🏬 Ты снова в ТЦ. Куда дальше?";
        }

        // сохраняем в session
        session.setAttribute("lastChoice", choice);
        session.setAttribute("money", money);
        session.setAttribute("calories", calories);
        session.setAttribute("step", step);
        session.setAttribute("cart", cart);


        // отдаем в JSP
        req.setAttribute("step", step);
        req.setAttribute("money", money);
        req.setAttribute("calories", calories);
        req.setAttribute("text", text);

        req.getRequestDispatcher("/WEB-INF/jsp/view.jsp").forward(req, resp);
    }

    private static class Item {
        String id;
        String name;
        int price;
        int calories;
        Item(String id, String name, int price, int calories) {
            this.id = id; this.name = name; this.price = price; this.calories = calories;
        }
    }

    private static final Item[] SHOP_ITEMS = {
            new Item("cable", "USB-C кабель", 120, 0),
            new Item("mouse", "Игровая мышь", 350, 0),
            new Item("ram", "ОЗУ 16GB", 600, 0),
            new Item("pizza", "Пицца (фудкорт)", 80, 25),
            new Item("cola", "Кола 0.5", 20, 10),
    };

    private Item findItem(String id) {
        for (Item it : SHOP_ITEMS) if (it.id.equals(id)) return it;
        return null;
    }



}



