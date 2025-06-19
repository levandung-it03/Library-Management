<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Thư viện ABC</title>
<script src="https://cdn.tailwindcss.com"></script>
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"
	rel="stylesheet" />
</head>
<body class="bg-gray-200">
	<jsp:include page="header.jsp" />

	<!-- Main Content -->
	<main class="p-4">
		<!-- Form tìm kiếm -->
		<div class="bg-gray-300 rounded">
			<div class="flex items-center px-5 py-2 border border-info rounded-t">
				<h2 class="text-lg font-bold">Tìm kiếm</h2>
			</div>
			<div class="flex items-center p-5 border border-info rounded-b">
				<form action="manage-book"
					method="GET" class="flex w-full">
					<input class="flex-grow p-2 rounded bg-white" placeholder="Nội dung tìm kiếm" type="text" name="query" />
					<button type="submit" class="bg-blue-600 text-white ml-1 p-3 rounded">
						<i class="fas fa-search"></i>
					</button>
				</form>
			</div>
		</div>

		<!-- Ẩn nút thêm sách khi có kết quả -->
		<button id="addBookBtn" class="mt-4 bg-blue-600 text-white py-2 px-5 rounded flex items-center">
			Thêm sách <i class="fas fa-plus-circle ml-2"> </i>
		</button>

        <!-- Hiển thị kết quả tìm kiếm nếu có -->
        <c:if test="${not empty query}">
            <span style="display:block">Kết quả tìm kiếm cho: <b>${query}</b></span>
        </c:if>

        <!-- Phân trang -->
        <div class="flex items-center justify-center mb-4 gap-1">
            <!-- Nút "Trang trước" -->
            <c:if test="${currentPage > 1}">
                <a href="${pageContext.request.contextPath}/manage-book?currentPage=${currentPage - 1}&query=${query}"
                   class="p-2 bg-gray-100 rounded-full w-10 h-10 flex justify-center items-center">
                    <i class="fas fa-arrow-left"></i>
                </a>
            </c:if>

            <!-- Các số trang -->
            <c:forEach var="i" begin="1" end="${totalPages}">
                <a href="${pageContext.request.contextPath}/manage-book?currentPage=${i}&query=${query}"
                	class="p-2 rounded-full w-10 h-10 flex justify-center items-center
                	${currentPage == i ? 'bg-blue-500 text-white' : 'bg-gray-100'}">${i}</a>
            </c:forEach>

            <!-- Nút "Trang sau" -->
            <c:if test="${currentPage < totalPages}">
                <a href="${pageContext.request.contextPath}/manage-book?currentPage=${currentPage + 1}&query=${query}"
                   class="p-2 bg-gray-100 rounded-full w-10 h-10 flex justify-center items-center">
                    <i class="fas fa-arrow-right"></i>
                </a>
            </c:if>
        </div>

        <%-- Danh sách sách --%>
		<div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4">
            <c:forEach var="book" items="${books}">
                <div class="block bg-white p-4 rounded shadow">
                    <div class="flex justify-center">
                        <img alt="Bìa sách ${book.bookName}" class="mb-4" height="200"
                             src="https://www.win-rar.com/fileadmin/images/winrar-archive.png" width="150" />
                    </div>
                    <a class="block text-blue-700 mb-2 text-justify font-bold"
                       href="detail-book?id=${book.bookId}" title="${book.bookName}">${book.bookName}
						</a>
                    <p class="text-gray-700">Tác giả: ${book.authors}</p>
                </div>
            </c:forEach>
		</div>
	</main>

	<!-- Modal -->
	<div id="modal" class="modal fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center hidden">
		<div class="bg-white p-6 rounded-lg w-1/2">
			<h2 class="text-2xl mb-4 text-center">Thêm sách mới</h2>
			<form id="addBookForm" action="<%=request.getContextPath()%>/add-book" method="POST">
				<div class="mb-4">
					<label class="block text-gray-700">Tên sách</label>
					<input
						class="w-full p-2 border border-gray-300" type="text" name="bookName"
						placeholder="Tên sách" />
				</div>
				<div class="mb-4">
					<label class="block text-gray-700">Tác giả</label> <input
						class="w-full p-2 border border-gray-300" type="text"
						name="authors" placeholder="Các tác giả" />
				</div>
				<div class="mb-4">
					<label class="block text-gray-700">Mô tả</label>
					<textarea class="w-full p-2 border border-gray-300"
						name="description" placeholder="Mô tả"></textarea>
				</div>
				<div class="mb-4">
					<label class="block text-gray-700">Số lượng</label> <input
						class="w-full p-2 border border-gray-300" type="number"
						name="availableQuantity" placeholder="Số lượng" />
				</div>

				<!-- <div class="mb-4">
					<label class="block text-gray-700">URL ảnh</label> <input
						class="w-full p-2 border border-gray-300" type="text" name="image"
						placeholder="URL ảnh" />
				</div> -->

	            <div class="mb-4">
	                <label class="block text-gray-700">Thể loại (Giữ Ctrl + Click để chọn nhiều)</label>
	                <select class="w-full p-2 border border-gray-300" name="genres" multiple>
	                    <c:forEach var="genre" items="${genres}">
	                        <option value="${genre.genreId}">${genre.genreName}</option>
	                    </c:forEach>
	                </select>
	            </div>

				<div class="flex justify-end">
					<button type="button" id="closeModalBtn"
						class="bg-gray-500 text-white p-2 mr-2 rounded">
						Hủy</button>
					<button type="submit" class="bg-blue-600 text-white p-2 rounded">
						Thêm sách</button>
				</div>
			</form>
		</div>
	</div>

	<script>
      document.getElementById("addBookBtn").addEventListener("click", () => {
        document.getElementById("modal").classList.remove("hidden");
      });

      document.getElementById("closeModalBtn").addEventListener("click", () => {
        document.getElementById("modal").classList.add("hidden");
      });
    </script>
</body>
</html>
