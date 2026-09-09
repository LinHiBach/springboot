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
        <h1>QUẢN TRỊ HỆ THỐNG</h1>

        <nav>
            <a href="${pageContext.request.contextPath}/admin">
                Trang quản trị
            </a>
            |
            <a href="${pageContext.request.contextPath}/">
                Trang người dùng
            </a>
        </nav>

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