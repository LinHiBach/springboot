<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý danh mục</title>
</head>
<body>
    <c:url var="addUrl" value="/admin/categories/add" />

    <div class="page-heading">
        <div>
            <h2>Quản lý danh mục</h2>
            <p class="muted">
                Theo dõi và cập nhật các danh mục của bạn.
            </p>
        </div>

        <a class="btn" href="${addUrl}">
            + Thêm danh mục
        </a>
    </div>

    <c:if test="${not empty message}">
        <div class="notice" role="status">
            <c:out value="${message}" />
        </div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="notice" style="background:#fef2f2; border-color:#fecaca; color:#dc2626;" role="alert">
            <c:out value="${error}" />
        </div>
    </c:if>

    <c:url var="searchUrl" value="/admin/categories" />

    <form action="${searchUrl}"
          method="get"
          style="display:flex; gap:12px; flex-wrap:wrap;
                 align-items:center; margin:20px 0;">

        <label for="keyword">Tên danh mục:</label>

        <input type="search"
               id="keyword"
               name="keyword"
               value="${fn:escapeXml(keyword)}"
               placeholder="Nhập tên cần tìm..."
               style="padding:10px; border:1px solid #ccc;
                      border-radius:6px;">

        <button type="submit" class="btn">
            Tìm kiếm
        </button>

        <a href="${searchUrl}" class="btn" style="background:#f1f5f9; color:#334155;">
            Xem tất cả
        </a>
    </form>

    <section class="card" aria-label="Danh sách danh mục">
        <div class="card-header">
            Danh sách danh mục
            <span class="muted">
                · ${fn:length(categories)} mục
            </span>
        </div>

        <div class="table-scroll">
            <table>
                <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Tên danh mục</th>
                        <th scope="col">Đường dẫn ảnh</th>
                        <th scope="col">Trạng thái</th>
                        <th scope="col">Thao tác</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach items="${categories}" var="category">
                        <tr>
                            <td class="muted">
                                #<c:out value="${category.categoryId}" />
                            </td>

                            <td class="category-name">
                                <c:out value="${category.categoryname}" />
                            </td>

                            <td>
                                <c:choose>
                                    <c:when test="${not empty category.images}">
                                        <span class="image-path">
                                            <c:out value="${category.images}" />
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="muted">Chưa có ảnh</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td>
                                <c:choose>
                                    <c:when test="${category.status == 1}">
                                        <span class="badge badge-active">
                                            ● Hoạt động
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-inactive">
                                            ○ Không hoạt động
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td>
                                <c:url var="editUrl"
                                       value="/admin/categories/edit/${category.categoryId}" />

                                <c:url var="deleteUrl"
                                       value="/admin/categories/delete/${category.categoryId}" />

                                <div class="actions">
                                    <a class="btn btn-edit" href="${editUrl}">
                                        Sửa
                                    </a>

                                    <form action="${deleteUrl}"
                                          method="post"
                                          onsubmit="return confirm('Bạn có chắc muốn xóa danh mục này?');">

                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                        <button class="btn-delete" type="submit">
                                            Xóa
                                        </button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty categories}">
                        <tr>
                            <td colspan="5" class="empty-state">
                                <p>
                                    <strong>
                                        <c:choose>
                                            <c:when test="${not empty keyword}">
                                                Không tìm thấy danh mục phù hợp.
                                            </c:when>
                                            <c:otherwise>
                                                Chưa có danh mục nào.
                                            </c:otherwise>
                                        </c:choose>
                                    </strong>
                                </p>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </section>
</body>
</html>
