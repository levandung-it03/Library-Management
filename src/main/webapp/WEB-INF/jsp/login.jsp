<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Nhập - Quản Lý Thư Viện</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
	
    <div class="container">
        <div class="row justify-content-center align-items-center" style="height: 100vh;">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title text-center">Đăng Nhập</h5>

                        <!-- Form đăng nhập sử dụng method POST -->
                        <form id="loginForm" action="authenticate" method="POST">
                            <div class="form-group">
                                <label for="email">Tài Khoản:</label>
                                <input type="text" class="form-control" id="email" name="email">
                            </div>
                            <div class="form-group">
                                <label for="password">Mật Khẩu:</label>
                                <input type="password" class="form-control" id="password" name="password">
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
	    window.onload = function() {
			const STORAGE_SELECTED_BOOKS = "library_selected_books";
			const STORAGE_BORROW_MODE = "library_borrow_mode";
			const STORAGE_MEMBERSHIP_CARD = "library_membership_card";
			localStorage.removeItem(STORAGE_SELECTED_BOOKS);
			localStorage.removeItem(STORAGE_BORROW_MODE);
			localStorage.removeItem(STORAGE_MEMBERSHIP_CARD);
            
	        const urlParams = new URLSearchParams(window.location.search);
	        let hasMessage = false;
	
	        // Extract messages using StatusMsgEnum keys
	        const errMsg = urlParams.get("<%= StatusMsgEnum.ERROR.getMsg() %>");
	        const sucMsg = urlParams.get("<%= StatusMsgEnum.SUCCESS.getMsg() %>");
	
	        // Show alerts if messages exist
	        if (errMsg) {
	            alert("❌ " + errMsg);
	            hasMessage = true;
	        }
	        if (sucMsg) {
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
