<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký tài khoản người dùng</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary: #4f46e5;
            --primary-hover: #4338ca;
            --bg-gradient: linear-gradient(135deg, #0f172a 0%, #1e1b4b 50%, #312e81 100%);
            --card-bg: rgba(255, 255, 255, 0.96);
            --text-main: #0f172a;
            --text-muted: #64748b;
            --border-color: #e2e8f0;
            --danger-bg: #fef2f2;
            --danger-border: #fecaca;
            --danger-text: #dc2626;
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            font-family: 'Inter', system-ui, -apple-system, sans-serif;
        }

        body {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background: var(--bg-gradient);
            padding: 30px 20px;
        }

        .auth-card {
            width: 100%;
            max-width: 500px;
            background: var(--card-bg);
            backdrop-filter: blur(16px);
            border-radius: 20px;
            padding: 40px;
            box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.35);
            border: 1px solid rgba(255, 255, 255, 0.2);
            animation: fadeIn 0.4s ease-out;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .auth-header {
            text-align: center;
            margin-bottom: 26px;
        }

        .auth-badge {
            display: inline-block;
            padding: 5px 12px;
            background: #e0e7ff;
            color: var(--primary);
            border-radius: 9999px;
            font-size: 12px;
            font-weight: 600;
            margin-bottom: 10px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .auth-title {
            font-size: 26px;
            font-weight: 700;
            color: var(--text-main);
            letter-spacing: -0.5px;
        }

        .auth-subtitle {
            margin-top: 6px;
            color: var(--text-muted);
            font-size: 14px;
        }

        .alert {
            padding: 12px 16px;
            border-radius: 10px;
            font-size: 13.5px;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            gap: 10px;
            background-color: var(--danger-bg);
            border: 1px solid var(--danger-border);
            color: var(--danger-text);
        }

        .form-group {
            margin-bottom: 18px;
        }

        .form-group label {
            display: block;
            margin-bottom: 6px;
            font-size: 13px;
            font-weight: 600;
            color: #334155;
        }

        .form-control {
            width: 100%;
            padding: 11px 15px;
            border: 1.5px solid var(--border-color);
            border-radius: 10px;
            font-size: 14px;
            color: var(--text-main);
            transition: all 0.2s ease;
            background: #ffffff;
        }

        .form-control:focus {
            outline: none;
            border-color: var(--primary);
            box-shadow: 0 0 0 4px rgba(79, 70, 229, 0.15);
        }

        .field-error {
            color: var(--danger-text);
            font-size: 12px;
            margin-top: 5px;
            display: block;
            font-weight: 500;
        }

        .btn-submit {
            width: 100%;
            padding: 13px;
            background: var(--primary);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 15px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.2s ease;
            box-shadow: 0 4px 12px rgba(79, 70, 229, 0.3);
            margin-top: 12px;
        }

        .btn-submit:hover {
            background: var(--primary-hover);
            transform: translateY(-1px);
            box-shadow: 0 6px 16px rgba(79, 70, 229, 0.4);
        }

        .auth-footer {
            margin-top: 24px;
            text-align: center;
            font-size: 14px;
            color: var(--text-muted);
        }

        .auth-footer a {
            color: var(--primary);
            text-decoration: none;
            font-weight: 600;
        }

        .auth-footer a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="auth-card">
    <div class="auth-header">
        <span class="auth-badge">Tạo tài khoản</span>
        <h1 class="auth-title">Đăng ký thành viên</h1>
        <p class="auth-subtitle">Mã xác minh OTP sẽ được gửi đến email của bạn</p>
    </div>

    <!-- Thông báo lỗi chung từ Server nếu có -->
    <c:if test="${not empty errorMessage}">
        <div class="alert" role="alert">
            <span>⚠</span>
            <span><c:out value="${errorMessage}"/></span>
        </div>
    </c:if>

    <c:url var="registerUrl" value="/register"/>
    <form:form action="${registerUrl}" method="post" modelAttribute="user">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <div class="form-group">
            <label for="username">Tên đăng nhập <span style="color:#dc2626;">*</span></label>
            <form:input id="username" path="username" class="form-control"
                        placeholder="Ví dụ: nguyen_van_a" required="required" autofocus="autofocus"/>
            <form:errors path="username" cssClass="field-error"/>
        </div>

        <div class="form-group">
            <label for="fullName">Họ và tên <span style="color:#dc2626;">*</span></label>
            <form:input id="fullName" path="fullName" class="form-control"
                        placeholder="Ví dụ: Nguyễn Văn A" required="required"/>
            <form:errors path="fullName" cssClass="field-error"/>
        </div>

        <div class="form-group">
            <label for="email">Địa chỉ Email <span style="color:#dc2626;">*</span></label>
            <form:input id="email" path="email" type="email" class="form-control"
                        placeholder="name@example.com" required="required"/>
            <form:errors path="email" cssClass="field-error"/>
        </div>

        <div class="form-group">
            <label for="password">Mật khẩu <span style="color:#dc2626;">*</span></label>
            <form:password id="password" path="password" class="form-control"
                           placeholder="Ít nhất 8 ký tự" required="required" showPassword="false"/>
            <form:errors path="password" cssClass="field-error"/>
        </div>

        <div class="form-group">
            <label for="confirmPassword">Xác nhận mật khẩu <span style="color:#dc2626;">*</span></label>
            <form:password id="confirmPassword" path="confirmPassword" class="form-control"
                           placeholder="Nhập lại mật khẩu" required="required" showPassword="false"/>
            <form:errors path="confirmPassword" cssClass="field-error"/>
        </div>

        <button type="submit" class="btn-submit">Đăng ký tài khoản</button>
    </form:form>

    <div class="auth-footer">
        Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập tại đây</a>
    </div>
</div>

</body>
</html>
