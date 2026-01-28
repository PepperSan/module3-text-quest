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

<form method="get" action="${pageContext.request.contextPath}/module3_text_quest">
    <button type="submit">Выйти на старт</button>
</form>


<form method="post" action="${pageContext.request.contextPath}/module3_text_quest">
    <button type="submit" name="choice" value="dota">Поиграть в Dota</button>
</form>


</body>
</html>
