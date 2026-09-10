<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm danh mục mới</title>
</head>
<body>
    <div class="page-heading">
        <div>
            <h2>Thêm danh mục mới</h2>
            <p class="muted">Tạo mới phân loại sản phẩm trong hệ thống.</p>
        </div>
        <a class="btn" style="background:#f1f5f9; color:#334155;" href="${pageContext.request.contextPath}/admin/categories">← Quay lại danh sách</a>
    </div>

    <c:url var="saveUrl" value="/admin/categories/save" />

    <form:form action="${saveUrl}"
               method="post"
               modelAttribute="category">

        <form:errors path="*" element="div" cssClass="notice" cssStyle="background:#fef2f2; border-color:#fecaca; color:#dc2626;" />

        <div style="margin-bottom:18px;">
            <label for="categoryname">Tên danh mục <span style="color:#dc2626;">*</span></label>
            <form:input path="categoryname"
                        id="categoryname"
                        maxlength="255"
                        placeholder="Nhập tên danh mục..." />
            <form:errors path="categoryname" cssStyle="color:#dc2626; font-size:12px; margin-top:4px; display:block;" />
        </div>

        <div style="margin-bottom:18px;">
            <label for="images">Đường dẫn ảnh</label>
            <form:input path="images"
                        id="images"
                        maxlength="255"
                        placeholder="https://example.com/image.jpg hoặc image.png" />
            <form:errors path="images" cssStyle="color:#dc2626; font-size:12px; margin-top:4px; display:block;" />
        </div>

        <div style="margin-bottom:24px;">
            <label for="status">Trạng thái</label>
            <form:select path="status" id="status">
                <form:option value="1" label="● Hoạt động" />
                <form:option value="0" label="○ Không hoạt động" />
            </form:select>
            <form:errors path="status" cssStyle="color:#dc2626; font-size:12px; margin-top:4px; display:block;" />
        </div>

        <div style="display:flex; gap:12px; align-items:center;">
            <button type="submit" class="btn">Lưu danh mục</button>
            <a href="${pageContext.request.contextPath}/admin/categories" class="btn" style="background:#f1f5f9; color:#334155;">Hủy bỏ</a>
        </div>

    </form:form>
</body>
</html>