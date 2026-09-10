<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title><sitemesh:write property="title" /></title>

    <style>
        :root {
            --primary: #4f46e5;
            --text: #172033;
            --muted: #64748b;
            --border: #e2e8f0;
        }

        * { box-sizing: border-box; }

        body {
            margin: 0;
            font-family: "Segoe UI", Arial, sans-serif;
            color: var(--text);
            background: #f4f6fb;
        }

        a { color: var(--primary); }

        .sidebar {
            position: fixed;
            inset: 0 auto 0 0;
            width: 240px;
            padding: 30px 20px;
            background: #111b31;
            color: white;
        }

        .brand {
            font-size: 25px;
            font-weight: 800;
            letter-spacing: -1px;
            margin-bottom: 6px;
        }

        .brand span { color: #a5b4fc; }

        .sidebar-caption {
            color: #a8b4cb;
            font-size: 13px;
            margin-bottom: 38px;
        }

        .nav-label {
            color: #94a3b8;
            font-size: 11px;
            letter-spacing: 2px;
            margin: 22px 12px 12px;
        }

        .sidebar nav a {
            display: block;
            color: #dbe4f3;
            text-decoration: none;
            padding: 13px 15px;
            margin-bottom: 7px;
            border-radius: 10px;
        }

        .sidebar nav a:hover,
        .sidebar nav a[aria-current="page"] {
            background: #4f46e5;
            color: white;
        }

        .workspace { margin-left: 240px; }

        .topbar {
            min-height: 76px;
            padding: 18px 36px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
            background: white;
            border-bottom: 1px solid var(--border);
        }

        .topbar-title { font-weight: 600; }

        .profile {
            display: flex;
            align-items: center;
            gap: 10px;
            color: var(--muted);
            font-size: 14px;
        }

        .avatar {
            width: 36px;
            height: 36px;
            border-radius: 50%;
            display: grid;
            place-items: center;
            background: #eef2ff;
            color: var(--primary);
            font-weight: 700;
        }

        main {
            max-width: 1400px;
            padding: 36px;
            margin: auto;
        }

        h2 {
            margin: 0 0 10px;
            font-size: 28px;
            letter-spacing: -.7px;
        }

        .muted { color: var(--muted); }

        .page-heading {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 20px;
            flex-wrap: wrap;
            margin-bottom: 26px;
        }

        .page-heading p { margin: 0; }

        .card {
            background: white;
            border: 1px solid var(--border);
            border-radius: 16px;
            overflow: hidden;
            box-shadow: 0 8px 28px rgba(15,23,42,.04);
        }

        .card-header {
            padding: 20px 24px;
            font-weight: 600;
            border-bottom: 1px solid var(--border);
        }

        .table-scroll { overflow-x: auto; }

        table {
            width: 100%;
            border-collapse: collapse;
            text-align: left;
        }

        th {
            padding: 15px 24px;
            background: #f8fafc;
            color: var(--muted);
            font-size: 12px;
            text-transform: uppercase;
            letter-spacing: .5px;
        }

        td {
            padding: 19px 24px;
            border-top: 1px solid #eef2f7;
            font-size: 14px;
        }

        tbody tr:hover { background: #fafbff; }

        .category-name { font-weight: 600; }

        .image-path {
            display: inline-block;
            max-width: 220px;
            overflow-wrap: anywhere;
        }

        .badge {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            border-radius: 30px;
            padding: 6px 11px;
            font-size: 12px;
            font-weight: 600;
            white-space: nowrap;
        }

        .badge-active { background: #ecfdf5; color: #047857; }
        .badge-inactive { background: #f1f5f9; color: #475569; }

        .btn, button {
            display: inline-flex;
            justify-content: center;
            align-items: center;
            gap: 6px;
            padding: 11px 17px;
            border: 1px solid transparent;
            border-radius: 9px;
            font: inherit;
            font-size: 14px;
            font-weight: 600;
            text-decoration: none;
            cursor: pointer;
            background: var(--primary);
            color: white;
        }

        .btn:hover, button:hover { filter: brightness(.94); }

        .btn-edit {
            background: #eef2ff;
            color: #4338ca;
            padding: 7px 12px;
        }

        .btn-delete {
            background: #fff1f2;
            color: #be123c;
            padding: 7px 12px;
        }

        .actions { display: flex; gap: 8px; align-items: center; }
        .actions form { margin: 0; padding: 0; }

        .notice {
            padding: 14px 18px;
            border: 1px solid #c7d2fe;
            background: #eef2ff;
            color: #3730a3;
            border-radius: 10px;
            margin-bottom: 22px;
        }

        .empty-state { text-align: center; padding: 48px 20px; }

        main > form {
            max-width: 720px;
            padding: 28px;
            background: white;
            border: 1px solid var(--border);
            border-radius: 16px;
        }

        label {
            display: inline-block;
            margin-bottom: 8px;
            font-weight: 600;
            font-size: 14px;
        }

        input[type="text"], input[type="email"],
        input[type="password"], select, textarea {
            width: 100%;
            padding: 11px 13px;
            border: 1px solid #cbd5e1;
            border-radius: 8px;
            font: inherit;
            background: white;
            color: var(--text);
        }

        :focus-visible {
            outline: 3px solid #a5b4fc;
            outline-offset: 3px;
        }

        .footer {
            padding: 20px 36px;
            color: var(--muted);
            font-size: 12px;
        }

        @media (max-width: 800px) {
            .sidebar {
                position: static;
                width: 100%;
                padding: 20px;
            }

            .sidebar-caption, .nav-label { display: none; }
            .sidebar nav { display: flex; flex-wrap: wrap; gap: 6px; }
            .sidebar nav a { margin: 10px 0 0; padding: 10px; }
            .workspace { margin-left: 0; }
            main { padding: 22px 16px; }
            .topbar { padding: 16px; }
            th, td { padding: 14px; }
            h2 { font-size: 24px; }
        }
    </style>

    <sitemesh:write property="head" />
</head>
<body>
    <aside class="sidebar">
        <div class="brand">Category<span>Hub.</span></div>
        <div class="sidebar-caption">Không gian quản lý danh mục</div>

        <div class="nav-label">ĐIỀU HƯỚNG</div>

        <nav aria-label="Menu quản trị">
            <a href="${pageContext.request.contextPath}/admin">
                Tổng quan
            </a>

            <a href="${pageContext.request.contextPath}/admin/categories">
                Quản lý danh mục
            </a>

            <a href="${pageContext.request.contextPath}/admin/users">Quản lý người dùng</a>
            <a href="${pageContext.request.contextPath}/">
                Trang người dùng
            </a>
        </nav>
    </aside>

    <div class="workspace">
        <header class="topbar">
            <div class="topbar-title">Trang quản trị</div>

            <div class="profile">
                <form action="${pageContext.request.contextPath}/logout" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <button type="submit">Đăng xuất</button>
                </form>
                <div class="avatar" aria-hidden="true">A</div>
            </div>
        </header>

        <main>
            <sitemesh:write property="body" />
        </main>

        <footer class="footer">
            CategoryHub · Quản lý danh mục
        </footer>
    </div>

    <script>
        // Đánh dấu menu tương ứng với URL hiện tại
        const links = Array.from(document.querySelectorAll(".sidebar nav a"));
        const currentPath = window.location.pathname.replace(/\/$/, "");

        const matched = links
            .filter(link => {
                const path = new URL(link.href).pathname.replace(/\/$/, "");
                return currentPath === path ||
                       (path !== "" && currentPath.startsWith(path + "/"));
            })
            .sort((a, b) => b.pathname.length - a.pathname.length);

        if (matched.length > 0) {
            matched[0].setAttribute("aria-current", "page");
        }
    </script>
</body>
</html>
