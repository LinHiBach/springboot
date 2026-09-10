<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác minh mã OTP</title>
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
            --border-color: #cbd5e1;
            --danger-bg: #fef2f2;
            --danger-border: #fecaca;
            --danger-text: #dc2626;
            --success-bg: #ecfdf5;
            --success-border: #a7f3d0;
            --success-text: #065f46;
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
            max-width: 460px;
            background: var(--card-bg);
            backdrop-filter: blur(16px);
            border-radius: 20px;
            padding: 40px;
            box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.35);
            border: 1px solid rgba(255, 255, 255, 0.2);
            text-align: center;
            animation: fadeIn 0.4s ease-out;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .otp-icon {
            width: 64px;
            height: 64px;
            border-radius: 50%;
            background: #e0e7ff;
            color: var(--primary);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 28px;
            margin: 0 auto 16px;
        }

        .auth-title {
            font-size: 24px;
            font-weight: 700;
            color: var(--text-main);
            letter-spacing: -0.5px;
        }

        .auth-subtitle {
            margin-top: 8px;
            color: var(--text-muted);
            font-size: 14px;
            line-height: 1.5;
        }

        .email-highlight {
            font-weight: 600;
            color: var(--primary);
        }

        .alert {
            padding: 12px 16px;
            border-radius: 10px;
            font-size: 13.5px;
            margin: 20px 0;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            text-align: left;
        }

        .alert-danger {
            background-color: var(--danger-bg);
            border: 1px solid var(--danger-border);
            color: var(--danger-text);
        }

        .alert-success {
            background-color: var(--success-bg);
            border: 1px solid var(--success-border);
            color: var(--success-text);
        }

        .otp-input-wrap {
            margin: 24px 0;
        }

        .otp-input {
            width: 100%;
            max-width: 280px;
            height: 56px;
            font-size: 28px;
            letter-spacing: 12px;
            text-align: center;
            font-weight: 700;
            color: #1e1b4b;
            border: 2px solid var(--border-color);
            border-radius: 12px;
            outline: none;
            transition: all 0.2s;
            background: #f8fafc;
        }

        .otp-input:focus {
            border-color: var(--primary);
            background: #ffffff;
            box-shadow: 0 0 0 4px rgba(79, 70, 229, 0.15);
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
        }

        .btn-submit:hover {
            background: var(--primary-hover);
            transform: translateY(-1px);
        }

        .resend-box {
            margin-top: 24px;
            padding-top: 20px;
            border-top: 1px solid #e2e8f0;
            font-size: 13.5px;
            color: var(--text-muted);
        }

        .btn-link {
            background: none;
            border: none;
            color: var(--primary);
            font-weight: 600;
            cursor: pointer;
            text-decoration: underline;
            padding: 0;
            font-size: 13.5px;
        }

        .auth-footer {
            margin-top: 20px;
            font-size: 13.5px;
            color: var(--text-muted);
        }

        .auth-footer a {
            color: var(--primary);
            text-decoration: none;
            font-weight: 600;
        }
    </style>
</head>
<body>

<div class="auth-card">
    <div class="otp-icon">✉</div>
    <h1 class="auth-title">Xác thực mã OTP</h1>
    <p class="auth-subtitle">
        Chúng tôi đã gửi mã xác nhận 6 số đến địa chỉ email:<br>
        <span class="email-highlight"><c:out value="${email}"/></span>
    </p>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger" role="alert">
            <span>⚠</span>
            <span><c:out value="${errorMessage}"/></span>
        </div>
    </c:if>

    <c:if test="${not empty message}">
        <div class="alert alert-success" role="alert">
            <span>✓</span>
            <span><c:out value="${message}"/></span>
        </div>
    </c:if>

    <c:url var="verifyUrl" value="/verify-otp"/>
    <form action="${verifyUrl}" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="email" value="${fn:escapeXml(email)}"/>

        <div class="otp-input-wrap">
            <input type="text" name="otp" class="otp-input"
                   placeholder="••••••" maxlength="6" pattern="[0-9]{6}"
                   required autofocus autocomplete="one-time-code"/>
        </div>

        <button type="submit" class="btn-submit">Xác nhận & Kích hoạt</button>
    </form>

    <div class="resend-box">
        Chưa nhận được mã hoặc mã đã hết hạn?
        <c:url var="resendUrl" value="/resend-otp"/>
        <form action="${resendUrl}" method="post" style="display:inline;">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="email" value="${fn:escapeXml(email)}"/>
            <button type="submit" class="btn-link">Gửi lại mã OTP</button>
        </form>
    </div>

    <div class="auth-footer">
        Muốn dùng email khác? <a href="${pageContext.request.contextPath}/register">Đăng ký lại</a>
    </div>
</div>

</body>
</html>
