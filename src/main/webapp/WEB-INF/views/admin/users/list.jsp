<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý người dùng</title>
</head>
<body>
    <c:url var="listUrl" value="/admin/users"/>

    <div class="page-heading">
        <div>
            <h2>Quản lý người dùng</h2>
            <p class="muted">Danh sách tài khoản quản trị và người dùng trong hệ thống.</p>
        </div>
        <a class="btn" href="${listUrl}/add">+ Thêm người dùng</a>
    </div>

    <c:if test="${not empty message}">
        <div class="notice" role="status">
            <c:out value="${message}"/>
        </div>
    </c:if>

    <form action="${listUrl}" method="get"
          style="display:flex; gap:12px; flex-wrap:wrap; align-items:center; margin:20px 0;">
        <label for="keyword" style="margin-bottom:0;">Tìm kiếm:</label>
        <input type="search" id="keyword" name="keyword"
               value="${fn:escapeXml(keyword)}"
               placeholder="Nhập tên đăng nhập, họ tên hoặc email..."
               style="padding:10px 14px; border:1px solid #ccc; border-radius:8px; min-width:280px;">
        <button type="submit" class="btn">Tìm kiếm</button>
        <a href="${listUrl}" class="btn" style="background:#f1f5f9; color:#334155;">Xem tất cả</a>
    </form>

    <section class="card" aria-label="Danh sách người dùng">
        <div class="card-header">
            Danh sách tài khoản
            <span class="muted"> · ${fn:length(users)} người dùng</span>
        </div>

        <div class="table-scroll">
            <table>
                <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Tên đăng nhập</th>
                        <th scope="col">Họ và tên</th>
                        <th scope="col">Email</th>
                        <th scope="col">Vai trò</th>
                        <th scope="col">Trạng thái</th>
                        <th scope="col">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${users}" var="user">
                        <tr>
                            <td class="muted">#<c:out value="${user.userId}"/></td>
                            <td><strong><c:out value="${user.username}"/></strong></td>
                            <td><c:out value="${user.fullName}"/></td>
                            <td><c:out value="${user.email}"/></td>
                            <td>
                                <span class="badge" style="${user.role == 'ADMIN' ? 'background:#fef3c7; color:#b45309;' : 'background:#e0e7ff; color:#3730a3;'}">
                                    <c:out value="${user.role}"/>
                                </span>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${user.status == 1}">
                                        <span class="badge badge-active">● Hoạt động</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-inactive">○ Đã khóa / Chờ OTP</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div class="actions">
                                    <a class="btn btn-edit" href="${listUrl}/edit/${user.userId}">Sửa</a>
                                    <form action="${listUrl}/delete/${user.userId}" method="post"
                                          onsubmit="return confirm('Bạn có chắc muốn xóa người dùng này?');">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                        <button type="submit" class="btn-delete">Xóa</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty users}">
                        <tr>
                            <td colspan="7" class="empty-state">
                                <strong>
                                    <c:choose>
                                        <c:when test="${not empty keyword}">Không tìm thấy người dùng phù hợp với từ khóa.</c:when>
                                        <c:otherwise>Chưa có người dùng nào trong hệ thống.</c:otherwise>
                                    </c:choose>
                                </strong>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </section>
</body>
</html>
