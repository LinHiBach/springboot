<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trang chủ Admin</title>
</head>
<body>
    <h2>Chào mừng đến trang quản trị</h2>
    <p>Chọn chức năng cần quản lý:</p>

    <ul>
        <li><a href="${pageContext.request.contextPath}/admin/categories">CRUD và tìm kiếm Category</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/users">CRUD và tìm kiếm User</a></li>
    </ul>
</body>
</html>
