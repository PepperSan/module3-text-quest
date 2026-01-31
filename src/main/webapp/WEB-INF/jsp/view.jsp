<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quest</title>
</head>
<body>

<h2>Шаг: ${step}</h2>
<pre>${text}</pre>


<p>💰 Деньги: <b>${money}</b> | 🔥 Калории: <b>${calories}</b></p>

<form method="get" action="${pageContext.request.contextPath}/">
    <button type="submit">Выйти на старт</button>
</form>



<%
    String step = (String) request.getAttribute("step");
    boolean isEnd = "gameOver".equals(step) || "walkHome".equals(step) || "restHome".equals(step);

    String cart = (String) session.getAttribute("cart");
    if (cart == null) cart = "";
%>

<% if ("mall".equals(step)) { %>
<form method="post" action="${pageContext.request.contextPath}/module3_text_quest">
    <input type="hidden" name="choice" value="mall_food">
    <button type="submit">🍕 Фудкорт</button>
</form>

<form method="post" action="${pageContext.request.contextPath}/module3_text_quest">
    <input type="hidden" name="choice" value="mall_electronics">
    <button type="submit">🖥 Электроника</button>
</form>
<% } %>

<% if ("food".equals(step)) { %>
<h3>Фудкорт</h3>

<form method="post" action="${pageContext.request.contextPath}/module3_text_quest">
    <input type="hidden" name="choice" value="buy_pizza">
    <button type="submit">Купить пиццу (80) +25 кал</button>
</form>

<form method="post" action="${pageContext.request.contextPath}/module3_text_quest">
    <input type="hidden" name="choice" value="buy_cola">
    <button type="submit">Купить колу (20) +10 кал</button>
</form>

<form method="post" action="${pageContext.request.contextPath}/module3_text_quest">
    <input type="hidden" name="choice" value="back_mall">
    <button type="submit">⬅ Назад в ТЦ</button>
</form>
<% } %>


<% if ("shop".equals(step)) { %>
<h3>Товары</h3>

<form method="post" action="${pageContext.request.contextPath}/module3_text_quest">
    <input type="hidden" name="choice" value="buy">
    <input type="hidden" name="itemId" value="cable">
    <button type="submit">Купить USB-C кабель (120)</button>
</form>

<form method="post" action="${pageContext.request.contextPath}/module3_text_quest">
    <input type="hidden" name="choice" value="buy">
    <input type="hidden" name="itemId" value="mouse">
    <button type="submit">Купить игровую мышь (350)</button>
</form>

<form method="post" action="${pageContext.request.contextPath}/module3_text_quest">
    <input type="hidden" name="choice" value="buy">
    <input type="hidden" name="itemId" value="ram">
    <button type="submit">Купить ОЗУ 16GB (600)</button>
</form>

<form method="post" action="${pageContext.request.contextPath}/module3_text_quest">
    <input type="hidden" name="choice" value="buy">
    <input type="hidden" name="itemId" value="pizza">
    <button type="submit">Купить пиццу (80) +25 кал</button>
</form>

<form method="post" action="${pageContext.request.contextPath}/module3_text_quest">
    <input type="hidden" name="choice" value="buy">
    <input type="hidden" name="itemId" value="cola">
    <button type="submit">Купить колу (20) +10 кал</button>
</form>

<h3>Корзина</h3>
<pre><%= cart %></pre>

<form method="post" action="${pageContext.request.contextPath}/module3_text_quest">
    <input type="hidden" name="choice" value="backHome">
    <button type="submit">⬅️ Назад домой</button>
</form>
<% } %>


<% if (!isEnd) { %>
<form method="post" action="${pageContext.request.contextPath}/">
    <input type="hidden" name="choice" value="dota">
    <button type="submit">Поиграть в Dota</button>
</form>

<form method="post" action="${pageContext.request.contextPath}/">
    <input type="hidden" name="choice" value="sleep">
    <button type="submit">Поспать</button>
</form>

<form method="post" action="${pageContext.request.contextPath}/">
    <input type="hidden" name="choice" value="shop">
    <button type="submit">Пойти в торговый центр</button>
</form>

<% } %>

</body>
</html>
