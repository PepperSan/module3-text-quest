<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Text Quest</title>
</head>
<body>

<h1>Айтишник-шопоголик</h1>
<p>
    Ты решил обновить гаджеты, но как всегда — есть соблазн:
    Dota, сон… или всё-таки торговый центр.
</p>

<form method="post" action="${pageContext.request.contextPath}/">
    <button type="submit" name="choice" value="start">Начать игру</button>
</form>

</body>
</html>

