<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8"/>
    <title>Text Quest</title>
</head>
<body>

<h1>Шаг: ${step}</h1>

<c:if test="${not empty text}">
    <p style="font-size: 18px;">${text}</p>
</c:if>

<p style="font-size: 20px;">
    💰 Деньги: <b>${money}</b> |
    🔥 Калории: <b>${calories}</b>
</p>

<!-- Кнопка "Выйти на старт" / начать заново -->
<form method="post" style="margin: 8px 0;">
    <input type="hidden" name="choice" value="start"/>
    <button type="submit">Выйти на старт</button>
</form>

<hr/>

<!-- ===== HOME ===== -->
<c:if test="${step == 'home'}">
    <form method="post" style="margin: 6px 0;">
        <input type="hidden" name="choice" value="dota"/>
        <button type="submit">Поиграть в Dota</button>
    </form>

    <form method="post" style="margin: 6px 0;">
        <input type="hidden" name="choice" value="sleep"/>
        <button type="submit">Поспать</button>
    </form>

    <form method="post" style="margin: 6px 0;">
        <input type="hidden" name="choice" value="shop"/>
        <button type="submit">Пойти в торговый центр</button>
    </form>
</c:if>

<!-- ===== MALL ===== -->
<c:if test="${step == 'mall'}">
    <h2>Торговый центр</h2>

    <form method="post" style="margin: 6px 0;">
        <input type="hidden" name="choice" value="mall_food"/>
        <button type="submit">🍔 Фудкорт</button>
    </form>

    <form method="post" style="margin: 6px 0;">
        <input type="hidden" name="choice" value="mall_electronics"/>
        <button type="submit">🖥️ Электроника</button>
    </form>

    <form method="post" style="margin: 6px 0;">
        <input type="hidden" name="choice" value="back_home"/>
        <button type="submit">🏠 Назад домой</button>
    </form>
</c:if>

<!-- ===== FOOD ===== -->
<c:if test="${step == 'food'}">
    <h2>Фудкорт</h2>

    <!-- Покупки: ВАЖНО -> choice=buy + itemId=... -->
    <form method="post" style="margin: 6px 0;">
        <input type="hidden" name="choice" value="buy"/>
        <input type="hidden" name="itemId" value="pizza"/>
        <button type="submit">Купить пиццу (80) +25 кал</button>
    </form>

    <form method="post" style="margin: 6px 0;">
        <input type="hidden" name="choice" value="buy"/>
        <input type="hidden" name="itemId" value="cola"/>
        <button type="submit">Купить колу (20) +10 кал</button>
    </form>

    <form method="post" style="margin: 6px 0;">
        <input type="hidden" name="choice" value="undo_last"/>
        <button type="submit">↩️ Отменить последнюю покупку</button>
    </form>

    <form method="post" style="margin: 6px 0;">
        <input type="hidden" name="choice" value="clear_cart"/>
        <button type="submit">Очистить корзину</button>
    </form>

    <hr/>

    <h3>Корзина</h3>
    <pre style="font-size: 16px; background: #f3f3f3; padding: 8px;"><c:out value="${cart}"/></pre>

    <form method="post" style="margin: 6px 0;">
        <input type="hidden" name="choice" value="back_mall"/>
        <button type="submit">↩️ Назад в ТЦ</button>
    </form>

    <form method="post" style="margin: 6px 0;">
        <input type="hidden" name="choice" value="back_home"/>
        <button type="submit">🏠 Назад домой</button>
    </form>
</c:if>

<!-- ===== ELECTRONICS ===== -->
<c:if test="${step == 'electronics'}">
    <h2>Товары (Электроника)</h2>

    <!-- Покупки: ВАЖНО -> choice=buy + itemId=... -->
    <form method="post" style="margin: 6px 0;">
        <input type="hidden" name="choice" value="buy"/>
        <input type="hidden" name="itemId" value="cable"/>
        <button type="submit">Купить USB-C кабель (120)</button>
    </form>

    <form method="post" style="margin: 6px 0;">
        <input type="hidden" name="choice" value="buy"/>
        <input type="hidden" name="itemId" value="mouse"/>
        <button type="submit">Купить игровую мышь (350)</button>
    </form>

    <form method="post" style="margin: 6px 0;">
        <input type="hidden" name="choice" value="buy"/>
        <input type="hidden" name="itemId" value="ram"/>
        <button type="submit">Купить ОЗУ 16GB (600)</button>
    </form>

    <form method="post" style="margin: 6px 0;">
        <input type="hidden" name="choice" value="undo_last"/>
        <button type="submit">↩️ Отменить последнюю покупку</button>
    </form>

    <form method="post" style="margin: 6px 0;">
        <input type="hidden" name="choice" value="clear_cart"/>
        <button type="submit">Очистить корзину</button>
    </form>

    <hr/>

    <h3>Корзина</h3>
    <pre style="font-size: 16px; background: #f3f3f3; padding: 8px;"><c:out value="${cart}"/></pre>

    <form method="post" style="margin: 6px 0;">
        <input type="hidden" name="choice" value="back_mall"/>
        <button type="submit">↩️ Назад в ТЦ</button>
    </form>

    <form method="post" style="margin: 6px 0;">
        <input type="hidden" name="choice" value="back_home"/>
        <button type="submit">🏠 Назад домой</button>
    </form>
</c:if>

</body>
</html>
