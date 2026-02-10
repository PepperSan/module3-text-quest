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

    // ====== Константы шагов ======
    private static final String STEP_START = "start";
    private static final String STEP_HOME = "home";
    private static final String STEP_MALL = "mall";
    private static final String STEP_FOOD = "food";
    private static final String STEP_ELECTRONICS = "electronics";

    // ====== Константы choice ======
    private static final String CHOICE_START = "start";          // выйти на старт / начать заново
    private static final String CHOICE_DOTA = "dota";
    private static final String CHOICE_SLEEP = "sleep";
    private static final String CHOICE_SHOP = "shop";            // домой -> тц
    private static final String CHOICE_MALL_FOOD = "mall_food";
    private static final String CHOICE_MALL_ELECTRONICS = "mall_electronics";
    private static final String CHOICE_BACK_HOME = "back_home";
    private static final String CHOICE_BACK_MALL = "back_mall";

    private static final String CHOICE_BUY = "buy";
    private static final String CHOICE_UNDO_LAST = "undo_last";
    private static final String CHOICE_CLEAR_CART = "clear_cart";

    // ====== Session keys ======
    private static final String S_STEP = "step";
    private static final String S_MONEY = "money";
    private static final String S_CALORIES = "calories";
    private static final String S_CART = "cart";

    private static final String S_LAST_ACTION = "lastAction"; // антиспам-ключ последнего действия

    private static final String S_LAST_ITEM_LINE = "lastItemLine";
    private static final String S_LAST_PRICE = "lastPrice";
    private static final String S_LAST_CAL = "lastCal";

    // ====== Правила игры ======
    private static final int START_MONEY = 1000;
    private static final int MAX_CALORIES = 120;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        // Инициализация сессии при первом заходе
        if (session.getAttribute(S_STEP) == null) {
            session.setAttribute(S_STEP, STEP_START);
            session.setAttribute(S_MONEY, START_MONEY);
            session.setAttribute(S_CALORIES, 0);
            session.setAttribute(S_CART, "");

            session.setAttribute(S_LAST_ACTION, null);

            resetLastPurchase(session);
        }


        String step = getString(session, S_STEP, STEP_START);
        if (STEP_START.equals(step)) {
            req.getRequestDispatcher("/WEB-INF/jsp/start.jsp").forward(req, resp);
        } else {
            forwardView(req, resp, session, "", null, null);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        // Если по какой-то причине сессия не инициализирована
        if (session.getAttribute(S_STEP) == null) {
            session.setAttribute(S_STEP, STEP_START);
            session.setAttribute(S_MONEY, START_MONEY);
            session.setAttribute(S_CALORIES, 0);
            session.setAttribute(S_CART, "");
            session.setAttribute(S_LAST_ACTION, null);
            resetLastPurchase(session);
        }

        String step = getString(session, S_STEP, STEP_HOME); // по умолчанию игра
        int money = getInt(session, S_MONEY, START_MONEY);
        int calories = getInt(session, S_CALORIES, 0);
        String cart = getString(session, S_CART, "");

        String choice = req.getParameter("choice");
        String itemId = req.getParameter("itemId");

        //  Нормализуем "выйти на старт"
        if ("to_start".equals(choice)) {
            choice = CHOICE_START;
        }

        // 1) Антиспам по действию (ключ зависит от buy:itemId)
//        String actionKey = buildActionKey(choice, itemId);

        // системные действия не блокируем (чтобы не бесило)
//        boolean isSystem = CHOICE_UNDO_LAST.equals(choice)
//                || CHOICE_CLEAR_CART.equals(choice)
//                || CHOICE_START.equals(choice);

//        String lastAction = (String) session.getAttribute(S_LAST_ACTION);
//        if (!isSystem && actionKey != null && actionKey.equals(lastAction)) {
//            forwardView(req, resp, session, "⛔ Ты только что это сделал. Выбери другое действие.",
//                    choice, itemId);
//            return;
//        }
//        session.setAttribute(S_LAST_ACTION, actionKey);
//
        String text = "";

        // 2) Логика действий
        if (choice == null) {
            text = "❓ Пустой выбор. Нажми кнопку.";
        }

        // --- START / RESTART ---
        else if (CHOICE_START.equals(choice)) {
            // Сброс "раунда": деньги/калории/корзина/шаг
            money = START_MONEY;
            calories = 0;
            cart = "";
            step = STEP_HOME;

            // сброс undo + антиспам
            resetLastPurchase(session);
            session.setAttribute(S_LAST_ACTION, null);

            text = "🔄 Новый раунд! Ты дома. Что дальше?";
        }

        // --- HOME ---
        else if (CHOICE_DOTA.equals(choice)) {
            calories += 50;
            step = STEP_HOME;
            text = "🎮 Ты залип в Dota. +50 калорий.";
        }
        else if (CHOICE_SLEEP.equals(choice)) {
            calories += 20;
            step = STEP_HOME;
            text = "😴 Ты поспал. +20 калорий.";
        }
        else if (CHOICE_SHOP.equals(choice)) {
            step = STEP_MALL;
            text = "🛍️ Ты в торговом центре. Куда идём?";
        }

        // --- MALL ---
        else if (CHOICE_MALL_FOOD.equals(choice)) {
            step = STEP_FOOD;
            text = "🍔 Фудкорт. Что берём?";
        }
        else if (CHOICE_MALL_ELECTRONICS.equals(choice)) {
            step = STEP_ELECTRONICS;
            text = "🖥️ Магазин электроники. Выбирай покупку!";
        }
        else if (CHOICE_BACK_HOME.equals(choice)) {
            step = STEP_HOME;
            text = "🏠 Ты вернулся домой. Что дальше?";
        }

        // --- BUY ---
        else if (CHOICE_BUY.equals(choice)) {
            Item item = findItem(itemId);

            if (item == null) {
                text = "❓ Неизвестный товар.";
            } else if (money < item.price) {
                text = "⛔ Не хватает денег на " + item.name + " (" + item.price + ").";
            } else if (item.calories > 0 && calories >= MAX_CALORIES) {
                text = "🍽️ Ты уже наелся. Больше не лезет.";
            } else {
                // списать деньги + добавить калории
                money -= item.price;
                calories += item.calories;
                if (calories > MAX_CALORIES) calories = MAX_CALORIES;

                // записать в корзину строкой (для отображения)
                String line = "• " + item.name + " (-" + item.price + ")\n";
                cart = (cart == null ? "" : cart) + line;

                // сохранить последнее для undo
                session.setAttribute(S_LAST_ITEM_LINE, line);
                session.setAttribute(S_LAST_PRICE, item.price);
                session.setAttribute(S_LAST_CAL, item.calories);

                text = "✅ Купил: " + item.name + " (-" + item.price + ").";
            }
        }

        // --- UNDO LAST ---
        else if (CHOICE_UNDO_LAST.equals(choice)) {
            String lastLine = (String) session.getAttribute(S_LAST_ITEM_LINE);
            int lastPrice = getInt(session, S_LAST_PRICE, 0);
            int lastCal = getInt(session, S_LAST_CAL, 0);

            if (lastLine == null || lastLine.isBlank()) {
                text = "🧺 Нечего отменять.";
            } else {
                // удаляем последнюю строку из конца (если совпадает)
                if (cart != null && cart.endsWith(lastLine)) {
                    cart = cart.substring(0, cart.length() - lastLine.length());
                } else {
                    // запасной вариант — просто чистим корзину, если строка не совпала
                    cart = (cart == null ? "" : cart);
                }

                money += lastPrice;
                calories -= lastCal;
                if (calories < 0) calories = 0;

                resetLastPurchase(session);
                text = "↩️ Последняя покупка отменена.";
            }
        }

        // --- CLEAR CART ---
        else if (CHOICE_CLEAR_CART.equals(choice)) {
            cart = "";
            resetLastPurchase(session);
            text = "🧹 Корзина очищена (покупки уже оплачены, деньги не возвращаются).";
        }

        // --- BACK MALL (из магазинов) ---
        else if (CHOICE_BACK_MALL.equals(choice)) {
            step = STEP_MALL;
            text = "🛍️ Ты снова в ТЦ. Куда дальше?";
        }

        else {
            text = "❓ Неизвестное действие: " + choice;
        }

        // 3) Нормализация границ
        if (money < 0) money = 0;
        if (calories < 0) calories = 0;
        if (calories >= 120) {
            calories = 120;

            if ("dota".equals(choice)) {
                text = "🎮 Хватит играть. Ты уже на пределе.";
            } else if ("sleep".equals(choice)) {
                text = "😴 Ты уже выспался. Пора вставать.";
            } else {
                text = "😵 Ты уже на пределе. Калории уперлись в 120.";
            }
        }



        // 4) Сохранить в session
        session.setAttribute(S_STEP, step);
        session.setAttribute(S_MONEY, money);
        session.setAttribute(S_CALORIES, calories);
        session.setAttribute(S_CART, cart);

        // 5) Отдать в JSP
        forwardView(req, resp, session, text, choice, itemId);
    }

    // ====== Helpers ======

    private void forwardView(HttpServletRequest req,
                             HttpServletResponse resp,
                             HttpSession session,
                             String text,
                             String choice,
                             String itemId) throws ServletException, IOException {

        String step = getString(session, S_STEP, STEP_HOME);
        int money = getInt(session, S_MONEY, START_MONEY);
        int calories = getInt(session, S_CALORIES, 0);
        String cart = getString(session, S_CART, "");

        req.setAttribute("step", step);
        req.setAttribute("money", money);
        req.setAttribute("calories", calories);
        req.setAttribute("text", text);
        req.setAttribute("cart", cart);


        req.setAttribute("debug", "DEBUG -> choice=" + choice + ", itemId=" + itemId);

        req.getRequestDispatcher("/WEB-INF/jsp/view.jsp").forward(req, resp);
    }

    private static String buildActionKey(String choice, String itemId) {
        if (choice == null) return null;
        if (CHOICE_BUY.equals(choice)) {
            return "buy:" + itemId;
        }
        return choice;
    }

    private static int getInt(HttpSession session, String key, int def) {
        Object v = session.getAttribute(key);
        return (v instanceof Integer) ? (Integer) v : def;
    }

    private static String getString(HttpSession session, String key, String def) {
        Object v = session.getAttribute(key);
        return (v instanceof String) ? (String) v : def;
    }

    private static void resetLastPurchase(HttpSession session) {
        session.setAttribute(S_LAST_ITEM_LINE, null);
        session.setAttribute(S_LAST_PRICE, 0);
        session.setAttribute(S_LAST_CAL, 0);
    }

    // ====== Items ======

    private static class Item {
        String id;
        String name;
        int price;
        int calories;

        Item(String id, String name, int price, int calories) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.calories = calories;
        }
    }

    // общий список (и для электроники, и для еды)
    private static final Item[] SHOP_ITEMS = {
            new Item("cable", "USB-C кабель", 120, 0),
            new Item("mouse", "Игровая мышь", 350, 0),
            new Item("ram", "ОЗУ 16GB", 600, 0),
            new Item("pizza", "Пицца (фудкорт)", 80, 25),
            new Item("cola", "Кола 0.5", 20, 10),
    };

    private static Item findItem(String id) {
        if (id == null) return null;
        for (Item it : SHOP_ITEMS) {
            if (it.id.equals(id)) return it;
        }
        return null;
    }
}
