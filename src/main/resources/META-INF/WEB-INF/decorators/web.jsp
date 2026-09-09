<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><sitemesh:write property="title" /></title>
    <sitemesh:write property="head" />
</head>
<body>
    <header>
        <h2>Website quản lý</h2>
        <hr>
    </header>

    <main>
        <sitemesh:write property="body" />
    </main>

    <footer>
        <hr>
        <p>Bài thực hành Spring Boot – JSP/JSTL</p>
    </footer>
</body>
</html>