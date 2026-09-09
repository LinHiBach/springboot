<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sách Category</title>
</head>
<body>
    <h2>Danh sách danh mục</h2>
    
    <c:if test="${not empty message}">
    <p style="color:green">
        <c:out value="${message}" />
    </p>
</c:if>

<c:url var="addUrl" value="/admin/categories/add" />

<p>
    <a href="${addUrl}">+ Thêm danh mục</a>
</p>

    <table border="1" cellpadding="10" cellspacing="0">
        <thead>
            <tr>
                <th>ID</th>
                <th>Tên danh mục</th>
                <th>Đường dẫn ảnh</th>
                <th>Trạng thái</th>
                <th>Thao tác</th>
            </tr>
        </thead>

        <tbody>
            <c:forEach items="${categories}" var="category">
                <tr>
                    <td>
                        <c:out value="${category.categoryId}" />
                    </td>
                    <td>
                        <c:out value="${category.categoryname}" />
                    </td>
                    <td>
                        <c:out value="${category.images}" />
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${category.status == 1}">
                                Hoạt động
                            </c:when>
                            <c:otherwise>
                                Không hoạt động
                            </c:otherwise>
                        </c:choose>
                    </td>
					<td>
					    <c:url var="editUrl"
					           value="/admin/categories/edit/${category.categoryId}" />
					
					    <c:url var="deleteUrl"
					           value="/admin/categories/delete/${category.categoryId}" />
					
					    <a href="${editUrl}">Sửa</a>
					
					    <form action="${deleteUrl}"
					          method="post"
					          style="display:inline"
					          onsubmit="return confirm('Bạn có chắc muốn xóa danh mục này?');">
					
					        <button type="submit">Xóa</button>
					    </form>
					</td>                
		</tr>
            </c:forEach>

            <c:if test="${empty categories}">
                <tr>
                    <td colspan="5">Chưa có danh mục nào.</td>
                    
                    
                </tr>
            </c:if>
        </tbody>
    </table>
</body>
</html>