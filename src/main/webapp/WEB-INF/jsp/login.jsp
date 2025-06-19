<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng Nhập - Quản Lý Thư Viện</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap 4 -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
<div class="container">
    <div class="row justify-content-center align-items-center" style="height: 100vh;">
        <div class="col-md-6">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title text-center">Đăng Nhập</h5>

                    <form id="loginForm" action="authenticate" method="POST">
                        <div class="form-group">
                            <label for="email">Tài Khoản:</label>
                            <input type="text" class="form-control" id="email" name="email">
                        </div>

                        <div class="form-group position-relative">
                            <label for="password">Mật khẩu:</label>
                            <input type="password" class="form-control" id="password" name="password">
                            <span class="position-absolute" style="top: 38px; right: 15px; cursor: pointer;">
                                <i class="fa-solid fa-eye" id="togglePasswordIcon"></i>
                            </span>
                        </div>

                        <button type="submit" class="btn btn-primary btn-block">Đăng Nhập</button>
                    </form>

                </div>
            </div>
        </div>
    </div>
</div>

<%@ page import="com.homework.library_management.enums.StatusMsgEnum" %>

<script>
    // Ẩn/hiện mật khẩu
    const passwordInput = document.getElementById("password");
    const togglePasswordIcon = document.getElementById("togglePasswordIcon");

    togglePasswordIcon.addEventListener("click", () => {
        const type = passwordInput.type === "password" ? "text" : "password";
        passwordInput.type = type;
        togglePasswordIcon.classList.toggle("fa-eye");
        togglePasswordIcon.classList.toggle("fa-eye-slash");
    });

    // Hiển thị alert nếu có thông báo
    window.onload = function () {
        const STORAGE_SELECTED_BOOKS = "library_selected_books";
        const STORAGE_BORROW_MODE = "library_borrow_mode";
        const STORAGE_MEMBERSHIP_CARD = "library_membership_card";
        localStorage.removeItem(STORAGE_SELECTED_BOOKS);
        localStorage.removeItem(STORAGE_BORROW_MODE);
        localStorage.removeItem(STORAGE_MEMBERSHIP_CARD);

        const urlParams = new URLSearchParams(window.location.search);
        let hasMessage = false;

        const errMsg = urlParams.get("<%= StatusMsgEnum.ERROR.getMsg() %>");
        const sucMsg = urlParams.get("<%= StatusMsgEnum.SUCCESS.getMsg() %>");

        if (errMsg) {
            alert("❌ " + errMsg);
            hasMessage = true;
        }
        if (sucMsg) {
            alert("✅ " + sucMsg);
            hasMessage = true;
        }

        if (hasMessage) {
            history.replaceState(null, "", window.location.pathname);
        }
    };
</script>
</body>
</html>
