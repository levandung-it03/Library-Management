<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.homework.library_management.enums.StatusMsgEnum" %>

<header class="bg-teal-700 p-4 flex items-center">
    <img alt="Library logo" class="mr-4" height="50" src="https://storage.googleapis.com/a1aa/image/RqiKCiMwfIGCH6vbwvxSF2JhXrKmd5KU27W3_6vAELo.jpg" width="50"/>
    <h1 class="text-white text-3xl font-bold">Thư viện ABC</h1>
</header>
<nav class="bg-blue-900 text-white p-2 flex justify-between items-center">
	<div class="flex space-x-4">
		<a class="text-lg font-bold px-3 py-2 rounded hover:bg-blue-700" href="home">Trang chủ</a>
		<a class="text-lg font-bold px-3 py-2 rounded hover:bg-blue-700" href="manage-book">Quản lí sách</a>
		<a class="text-lg font-bold px-3 py-2 rounded hover:bg-blue-700" href="borrowing-book">Mượn/trả</a>
		<a class="text-lg font-bold px-3 py-2 rounded hover:bg-blue-700" href="membership-card-list">Thành viên</a>
	</div>
	<div class="flex items-center space-x-2">
		<span><u><a href="${pageContext.request.contextPath}/user-info"><c:out value="${librarian}"/></a></u></span>
		<i class="fas fa-user"></i>
		<form action="log-out" method="POST">
			<button type="submit" class="text-lg font-bold px-3 py-2 rounded hover:bg-blue-700" style="margin-left: 20px;">Đăng xuất</button>
		</form>
	</div>
</nav>
<%@ page import="com.homework.library_management.enums.StatusMsgEnum" %>
<script>
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
