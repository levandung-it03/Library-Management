<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quên mật khẩu - Quản Lý Thư Viện</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>

<div class="container">
    <div class="row justify-content-center align-items-center" style="height: 100vh;">
        <div class="col-md-6">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title text-center">Quên mật khẩu</h5>

                    <!-- Form đăng nhập sử dụng method POST -->
                    <form id="forgotPasswordForm" action="forgot-password" method="POST">
                        <div class="form-group">
                            <label for="email">Tài Khoản:</label>
                            <input type="text" class="form-control" id="email" name="email">
                        </div>
                        <button type="button" class="btn btn-primary btn-block"
                                style="margin-bottom:10px"
                                onclick="sendOtpCodeForForgotPassword()">
                            Lấy OTP xác nhận
                        </button>
                        <div class="form-group">
                            <label for="otp">OTP Xác thực:</label>
                            <input type="text" class="form-control" id="otp" name="otp">
                        </div>
                        <button type="button" class="btn btn-primary btn-block" onclick="generateForgotPassword()">
                            Lấy lại mật khẩu
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ page import="com.homework.library_management.enums.StatusMsgEnum" %>
<%@ page import="com.homework.library_management.enums.VerifyEmailOptions" %>
<script>
    function sendOtpCodeForForgotPassword() {
        const emailInput = document.querySelector("#forgotPasswordForm input#email");
        const button = document.querySelector("#forgotPasswordForm button[onclick]");
        fetch("/verify-email", {
            method: "POST",
            headers: {
                "Content-type": "application/json",
            },
            body: JSON.stringify({
                email: emailInput.value,
                option: "PUBLIC_FORGOT_PASSWORD"
            })
        })
            .then(async response => {
                const data = await response.json();

                if (!response.ok) {
                    // Server trả về status 400/500
                    throw data;
                }

                const successMsg = data["<%= StatusMsgEnum.SUCCESS.getMsg() %>"];
                const otpAge = data["otpAge"]; // seconds

                alert(successMsg);

                let remainingTime = otpAge;
                button.disabled = true;
                button.classList.remove("btn-primary");
                button.classList.add("btn-secondary");
                button.textContent = "OTP hết hạn trong " + remainingTime + "s";

                otpTimerInterval = setInterval(() => {
                    remainingTime--;
                    if (remainingTime > 0) {
                        button.textContent = "OTP hết hạn trong " + remainingTime + "s";
                    } else {
                        clearInterval(otpTimerInterval);
                        button.disabled = false;
                        button.classList.remove("btn-secondary");
                        button.classList.add("btn-primary");
                        button.textContent = "Lấy OTP xác nhận";
                    }
                }, 1000);
            })
            .catch(error => {
                // Nếu error là JSON object (do throw ở trên), hiển thị thông báo
                if (error && error.ERROR) {
                    alert("❌ " + error.ERROR);
                } else {
                    console.error("Unexpected error:", error);
                    alert("❌ Có lỗi xảy ra, vui lòng thử lại sau.");
                }
            });
    }

    function generateForgotPassword() {
        const emailInput = document.querySelector("#forgotPasswordForm input#email");
        const otpInput = document.querySelector("#forgotPasswordForm input#otp");

        fetch("${pageContext.request.contextPath}/generate-password", {
            method: "POST",
            headers: {
                "Content-type": "application/json",
            },
            body: JSON.stringify({ email: emailInput.value, otp: otpInput.value })
        })
            .then(async response => {
                const data = await response.json();

                if (!response.ok) {
                    throw data;
                }

                const successMsg = data["<%= StatusMsgEnum.SUCCESS.getMsg() %>"];
                alert(successMsg);
                window.location.href = "${pageContext.request.contextPath}/login";
            })
            .catch(error => {
                if (error && error["<%= StatusMsgEnum.ERROR.getMsg() %>"]) {
                    alert("❌ " + error["<%= StatusMsgEnum.ERROR.getMsg() %>"]);
                } else {
                    console.error("Unexpected error:", error);
                    alert("❌ Có lỗi xảy ra, vui lòng thử lại sau.");
                }
            });
    }

    window.onload = function() {
        const urlParams = new URLSearchParams(window.location.search);
        let hasMessage = false;

        // Extract messages using StatusMsgEnum keys
        const errMsg = urlParams.get("<%= StatusMsgEnum.ERROR.getMsg() %>");
        const sucMsg = urlParams.get("<%= StatusMsgEnum.SUCCESS.getMsg() %>");

        // Show alerts if messages exist
        if (errMsg) {
            alert("❌ " + errMsg);
            hasMessage = true;
        } else if (sucMsg) {
            alert("✅ " + sucMsg);
            hasMessage = true;
        }

        // Remove messages from URL without reloading the page
        if (hasMessage) {
            history.replaceState(null, "", window.location.pathname);
        }
    };
</script>
</body>
</html>
