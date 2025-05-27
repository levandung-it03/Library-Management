<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thông tin thủ thư</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body class="bg-gray-100">
<jsp:include page="header.jsp"/>
<div class="flex items-center justify-center" style="margin-top: 40px">
    <div class="bg-white rounded-lg shadow-lg w-full max-w-6xl grid grid-cols-2 gap-6 p-6">
        <!-- BÊN TRÁI: Thông tin thủ thư -->
        <div>
            <h2 class="text-xl font-bold text-center mb-4">Thông tin cá nhân</h2>
            <form method="POST" action="${pageContext.request.contextPath}/update-fullname">
                <div class="mb-4">
                    <label class="block text-gray-700 font-medium">Mã thủ thư</label>
                    <input type="text" disabled value="${librarianId}" class="w-full p-2 border rounded bg-gray-100"/>
                </div>
                <div class="mb-4">
                    <label class="block text-gray-700 font-medium">Mã nhân viên</label>
                    <input type="text" disabled value="${employeeId}" class="w-full p-2 border rounded bg-gray-100"/>
                </div>
                <div class="mb-4">
                    <label class="block text-gray-700 font-medium">Email</label>
                    <input type="text" disabled value="${email}" class="w-full p-2 border rounded bg-gray-100"/>
                </div>
                <div class="mb-4">
                    <label class="block text-gray-700 font-medium">Họ và tên</label>
                    <input type="text" name="fullName" value="${fullName}" class="w-full p-2 border rounded"/>
                </div>
                <div class="text-right">
                    <button type="submit" id="updateFullNameBtn"
                            class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
                        Cập nhật tên
                    </button>
                </div>
            </form>
        </div>

        <!-- BÊN PHẢI: Đổi mật khẩu -->
        <div>
            <form method="post" action="change-password" id="changePasswordForm">
            <h2 class="text-xl font-bold text-center mb-4">Đổi mật khẩu</h2>
            <div class="mb-4">
                <label class="block text-gray-700 font-medium">Nhập mã OTP</label>
                <input type="text" name="otp" id="otpInput" class="w-full p-2 border rounded"/>
            </div>
            <div class="mb-4" style="margin-bottom: 10px">
                <button type="button" id="getOtpBtn"
                        class="w-full bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">
                    Lấy OTP xác nhận email
                </button>
            </div>
            <div class="mb-4" style="margin-bottom: 8px;text-align: center;">
                <i style="color: #555;">Hãy lấy mã OTP xác nhận trước khi đổi mật khẩu</i>
            </div>
                <input type="hidden" name="email" value="${email}"/>
                <div style="margin-bottom: 16px; position: relative;">
                    <label style="display: block; margin-bottom: 4px; font-weight: bold;">Mật khẩu mới</label>
                    <input type="password" id="newPassword" name="newPassword" required
                           style="width: 100%; padding: 8px 40px 8px 8px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;"/>
                    <button type="button" id="toggleNewPassword"
                            style="position: absolute; top: 68%; right: 10px; transform: translateY(-50%); background: none; border: none; cursor: pointer;">
                        <i class="fa-solid fa-eye" id="newPasswordIcon"></i>
                    </button>
                </div>
                <div style="margin-bottom: 16px; position: relative;">
                    <label style="display: block; margin-bottom: 4px; font-weight: bold;">Xác nhận mật khẩu</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required
                           style="width: 100%; padding: 8px 40px 8px 8px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;"/>
                    <button type="button" id="toggleConfirmPassword"
                            style="position: absolute; top: 68%; right: 10px; transform: translateY(-50%); background: none; border: none; cursor: pointer;">
                        <i class="fa-solid fa-eye" id="confirmPasswordIcon"></i>
                    </button>
                </div>
                <div class="text-right">
                    <button type="submit" class="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700"
                            id="changePasswordBtn">
                        Đổi mật khẩu
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<%@ page import="com.homework.library_management.enums.StatusMsgEnum" %>
<%@ page import="com.homework.library_management.enums.VerifyEmailOptions" %>
<script>
    const changePasswordForm = document.getElementById("changePasswordForm");
    const getOtpBtn = document.getElementById("getOtpBtn");
    const otpInput = document.getElementById("otpInput");
    const changePasswordBtn = document.getElementById("changePasswordBtn");
    const newPassword = document.querySelector("input[name=newPassword]");
    const confPassword = document.querySelector("input[name=confirmPassword]");
    const toggleNewPasswordBtn = document.getElementById("toggleNewPassword");
    const toggleConfirmPasswordBtn = document.getElementById("toggleConfirmPassword");
    const newPasswordIcon = document.getElementById("newPasswordIcon");
    const confirmPasswordIcon = document.getElementById("confirmPasswordIcon");

    toggleNewPasswordBtn.addEventListener("click", () => {
        const type = newPassword.type === "password" ? "text" : "password";
        newPassword.type = type;
        newPasswordIcon.classList.toggle("fa-eye");
        newPasswordIcon.classList.toggle("fa-eye-slash");
    });
    toggleConfirmPasswordBtn.addEventListener("click", () => {
        const type = confPassword.type === "password" ? "text" : "password";
        confPassword.type = type;
        confirmPasswordIcon.classList.toggle("fa-eye");
        confirmPasswordIcon.classList.toggle("fa-eye-slash");
    });
    changePasswordForm.addEventListener("submit", (e) => {
        e.preventDefault();
        if (confPassword.value !== newPassword.value) {
            alert("Mật khẩu không chính xác, hãy kiểm tra lại");
            return;
        }
        if (otpInput.value.trim().length > 6) {
            alert("OTP không hợp lệ, hãy kiểm tra lại");
            return;
        }
        e.target.submit();
    });
    getOtpBtn.addEventListener("click", async (e) => {
        e.preventDefault();

        const response = await fetch("${pageContext.request.contextPath}/verify-email", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                email: "${email}",
                option: "${VerifyEmailOptions.PRIVATE_CHANGE_PASSWORD.toString()}"
            })
        });

        if (response.ok) {
            const data = await response.json();

            getOtpBtn.disabled = true;
            getOtpBtn.classList.remove("bg-green-600", "hover:bg-green-700", "cursor-pointer");
            getOtpBtn.classList.add("bg-gray-400", "cursor-not-allowed", "hover:bg-gray-400");
            let countdown = data.otpAge;
            const originalText = getOtpBtn.textContent;

            const interval = setInterval(() => {
                if (countdown > 0) {
                    getOtpBtn.textContent = "OTP hết hạn trong " + countdown-- + "s";
                } else {
                    clearInterval(interval);
                    getOtpBtn.textContent = originalText;
                    getOtpBtn.classList.remove("bg-gray-400", "cursor-not-allowed", "hover:bg-gray-400");
                    getOtpBtn.classList.add("bg-green-600", "hover:bg-green-700", "cursor-pointer");
                    getOtpBtn.disabled = false;
                }
            }, 1000);

            alert(data["${StatusMsgEnum.SUCCESS.getMsg()}"]);
        } else {
            const error = await response.json();
            alert(error["${StatusMsgEnum.ERROR.getMsg()}"]);
        }
    });
</script>
</body>
</html>
