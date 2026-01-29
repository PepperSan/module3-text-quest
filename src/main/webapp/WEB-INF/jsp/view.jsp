<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quest</title>
</head>
<body>

<h2>Шаг: ${step}</h2>
<p>${text}</p>

<p>💰 Деньги: <b>${money}</b> | 🔥 Калории: <b>${calories}</b></p>

<!-- кнопка "Выйти на старт" -->
<form method="get" action="${pageContext.request.contextPath}/module3_text_quest">
    <button type="submit">Выйти на старт</button>
</form>

<!-- выбор: "Поиграть в Dota" (POST, сохраняем money/calories/step) -->
<form method="post" action="${pageContext.request.contextPath}/module3_text_quest">
    <input type="hidden" name="step" value="${step}">
    <input type="hidden" name="money" value="${money}">
    <input type="hidden" name="calories" value="${calories}">
    <button type="submit" name="choice" value="dota">Поиграть в Dota</button>
</form>



</body>
</html>
