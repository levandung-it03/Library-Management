<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html lang="vi">
<head>
	<meta charset="UTF-8">
    <title>Thư viện ABC</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
</head>
<body class="bg-gray-200">
    <jsp:include page="header.jsp" />
    <main class="p-4">
        <h2 class="text-2xl font-bold mb-4">Bảng điều khiển</h2>
        <section class="bg-gray-300 p-4 rounded-lg">
            <h3 class="text-xl font-semibold mb-4">Thống kê nhanh</h3>
            <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
                <div class="bg-white p-4 rounded-lg shadow-md flex items-center justify-between">
                    <div>
                        <p class="text-3xl font-bold"><c:out value="${totalBooks}"/></p>
                        <p class="text-gray-600">Tổng số sách hiện có</p>
                    </div>
                    <i class="fas fa-book text-4xl text-blue-500"></i>
                </div>
                <div class="bg-white p-4 rounded-lg shadow-md flex items-center justify-between">
                    <div>
                        <p class="text-3xl font-bold"><c:out value="${availableBooks}"/></p>
                        <p class="text-gray-600">Sách sẵn sàng cho mượn</p>
                    </div>
                    <i class="fas fa-book text-4xl text-pink-500"></i>
                </div>
                <div class="bg-white p-4 rounded-lg shadow-md flex items-center justify-between">
                    <div>
                        <p class="text-3xl font-bold"><c:out value="${borrowedBooks == null ? 0 : borrowedBooks}"/></p>
                        <p class="text-gray-600">Sách đang cho mượn</p>
                    </div>
                    <i class="fas fa-book text-4xl text-yellow-500"></i>
                </div>
                <div class="bg-white p-4 rounded-lg shadow-md flex items-center justify-between">
                    <div>
                        <p class="text-3xl font-bold"><c:out value="${lockedBooks == null ? 0 : lockedBooks}"/></p>
                        <p class="text-gray-600">Sách nội bộ</p>
                    </div>
                    <i class="fas fa-book text-4xl text-green-500"></i>
                </div>
            </div>
        </section>
    </main>
</body>
</html>
