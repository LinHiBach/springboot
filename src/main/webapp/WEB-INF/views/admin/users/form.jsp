<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title><c:out value="${empty userId ? 'Thêm người dùng mới' : 'Cập nhật thông tin người dùng'}" /></title>
</head>
<body>
    <div class="page-heading">
        <div>
            <h2><c:out value="${empty userId ? 'Thêm người dùng mới' : 'Cập nhật người dùng'}" /></h2>
            <p class="muted">
                <c:choose>
                    <c:when test="${empty userId}">Tạo tài khoản mới và phân quyền trong hệ thống.</c:when>
                    <c:otherwise>Chỉnh sửa thông tin tài khoản người dùng ID #<c:out value="${userId}" />.</c:otherwise>
                </c:choose>
            </p>
        </div>
        <a class="btn" style="background:#f1f5f9; color:#334155;" href="${pageContext.request.contextPath}/admin/users">← Quay lại danh sách</a>
    </div>

    <!-- Phân biệt action Add vs Edit bằng ${empty userId} -->
    <c:choose>
        <c:when test="${empty userId}">
            <c:url var="formAction" value="/admin/users/save" />
        </c:when>
        <c:otherwise>
            <c:url var="formAction" value="/admin/users/update/${userId}" />
        </c:otherwise>
    </c:choose>

    <form:form method="post" action="${formAction}" modelAttribute="user">
        <!-- Thông báo lỗi tổng quan nếu có -->
        <form:errors path="*" element="div" cssClass="notice" cssStyle="background:#fef2f2; border-color:#fecaca; color:#dc2626;" />

        <!-- 1. Username -->
        <div style="margin-bottom:18px;">
            <label for="username">Tên đăng nhập <span style="color:#dc2626;">*</span></label>
            <form:input path="username" id="username" maxlength="50" placeholder="Ví dụ: nguyenvana" />
            <form:errors path="username" cssStyle="color:#dc2626; font-size:12px; margin-top:4px; display:block;" />
        </div>

        <!-- 2. Full Name -->
        <div style="margin-bottom:18px;">
            <label for="fullName">Họ và tên <span style="color:#dc2626;">*</span></label>
            <form:input path="fullName" id="fullName" maxlength="100" placeholder="Ví dụ: Nguyễn Văn A" />
            <form:errors path="fullName" cssStyle="color:#dc2626; font-size:12px; margin-top:4px; display:block;" />
        </div>

        <!-- 3. Email -->
        <div style="margin-bottom:18px;">
            <label for="email">Địa chỉ Email <span style="color:#dc2626;">*</span></label>
            <form:input path="email" id="email" type="email" maxlength="255" placeholder="name@example.com" />
            <form:errors path="email" cssStyle="color:#dc2626; font-size:12px; margin-top:4px; display:block;" />
        </div>

        <!-- 4. Password -->
        <div style="margin-bottom:18px;">
            <label for="password">
                Mật khẩu
                <c:if test="${empty userId}">
                    <span style="color:#dc2626;">*</span>
                </c:if>
            </label>
            <form:password path="password" id="password" showPassword="false" autocomplete="new-password" placeholder="••••••••" />
            <small class="muted" style="display:block; margin-top:6px;">
                <c:choose>
                    <c:when test="${empty userId}">
                        Mật khẩu cần tối thiểu 8 ký tự và tối đa 72 byte.
                    </c:when>
                    <c:otherwise>
                        Để trống mật khẩu nếu không muốn thay đổi.
                    </c:otherwise>
                </c:choose>
            </small>
            <form:errors path="password" cssStyle="color:#dc2626; font-size:12px; margin-top:4px; display:block;" />
        </div>

        <!-- 5. Role & Status -->
        <div style="display:grid; grid-template-columns:1fr 1fr; gap:16px; margin-bottom:24px;">
            <div>
                <label for="role">Vai trò</label>
                <form:select path="role" id="role">
                    <form:option value="USER" label="USER (Người dùng)" />
                    <form:option value="ADMIN" label="ADMIN (Quản trị viên)" />
                </form:select>
                <form:errors path="role" cssStyle="color:#dc2626; font-size:12px; margin-top:4px; display:block;" />
            </div>

            <div>
                <label for="status">Trạng thái</label>
                <form:select path="status" id="status">
                    <form:option value="1" label="● Hoạt động" />
                    <form:option value="0" label="○ Đã khóa / Tạm dừng" />
                </form:select>
                <form:errors path="status" cssStyle="color:#dc2626; font-size:12px; margin-top:4px; display:block;" />
            </div>
        </div>

        <!-- Nút thao tác -->
        <div style="display:flex; gap:12px; align-items:center;">
            <button type="submit" class="btn">
                <c:out value="${empty userId ? 'Tạo người dùng' : 'Lưu thay đổi'}" />
            </button>
            <a href="${pageContext.request.contextPath}/admin/users" class="btn" style="background:#f1f5f9; color:#334155;">Hủy bỏ</a>
        </div>
    </form:form>
</body>
</html>
