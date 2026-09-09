<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm danh mục</title>
</head>
<body>
    <h2>Thêm danh mục mới</h2>

    <c:url var="saveUrl" value="/admin/categories/save" />
    <c:url var="listUrl" value="/admin/categories" />

    <form:form action="${saveUrl}"
               method="post"
               modelAttribute="category">

        <p>
            <label for="categoryname">Tên danh mục:</label>
            <br>

            <form:input path="categoryname"
                        id="categoryname"
                        maxlength="255" />

            <br>
            <form:errors path="categoryname" cssStyle="color:red" />
        </p>

        <p>
            <label for="images">Đường dẫn ảnh:</label>
            <br>

            <form:input path="images"
                        id="images"
                        maxlength="255" />

            <br>
            <form:errors path="images" cssStyle="color:red" />
        </p>

        <p>
            <label for="status">Trạng thái:</label>
            <br>

            <form:select path="status" id="status">
                <form:option value="1" label="Hoạt động" />
                <form:option value="0" label="Không hoạt động" />
            </form:select>

            <br>
            <form:errors path="status" cssStyle="color:red" />
        </p>

        <button type="submit">Lưu danh mục</button>
        <a href="${listUrl}">Quay lại danh sách</a>

    </form:form>
</body>
</html>