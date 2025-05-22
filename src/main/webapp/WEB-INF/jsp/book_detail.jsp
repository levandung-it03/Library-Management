<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title><c:out value="${book.bookName}" /></title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
</head>
<body class="bg-gray-200">
	<jsp:include page="header.jsp" />
    <main>
        <div class="p-4 flex justify-center">
            <div class="bg-white p-4 rounded shadow-md w-full max-w-4xl flex">
                <img id="bookImage" src="${book.imgUrl}" alt="Book cover" class="w-1/3" height="300" width="200" />
                <div class="ml-4">
                    <h2 id="bookTitle" class="text-blue-700 text-xl font-bold">
                        <c:out value="${book.bookName}" />
                    </h2>
                    <p>
                        <strong>Tác giả:</strong>
                        <span id="bookAuthor"><c:out value="${book.authors}" /></span>
                    </p>
                    <p>
                        <strong>Mô tả:</strong>
                        <span id="bookDescription"><c:out value="${book.description}" /></span>
                    </p>
                    <p>
                        <strong>Loại sách: </strong>
                        <span style="${bookStatus == '1' ? '' : 'color:red'}" id="bookAuthor">
                        	<c:out value="${bookStatus == '1' ? 'Cộng đồng' : 'Nội bộ'}" />
                        </span>
                    </p>
                    <p>
                        <strong>Số lượng có sẵn:</strong>
                        <span id="bookQuantity"><c:out value="${book.availableQuantity}" /></span>
                    </p>
	                <p>
	                    <strong>Thể loại:</strong>
	                    <ul id="bookGenres" class="list-disc pl-5">
	                        <c:forEach var="genre" items="${genresOfBook}">
	                            <li><c:out value="${genre.genreName}" /></li>
	                        </c:forEach>
	                    </ul>
	                </p>
                    <button class="mt-4 bg-blue-500 text-white px-4 py-2 rounded" onclick="openModal()">
                        Chỉnh sửa
                    </button>
                </div>
            </div>
        </div>
    </main>

    <!-- Modal chỉnh sửa -->
    <div id="editModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center hidden">
        <div class="bg-white p-6 rounded shadow-md w-full max-w-lg">
            <h2 class="text-xl font-bold mb-4">Chỉnh sửa thông tin sách</h2>
            <form id="editForm" method="post" action="<%=request.getContextPath()%>/update-book">
                <input type="hidden" name="bookId" value="${book.bookId}" />
                <div class="mb-4">
                    <label class="block text-gray-700">Tên sách</label>
                    <input name="bookName" type="text" class="w-full p-2 border" value="${book.bookName}" />
                </div>
                <div class="mb-4">
                    <label class="block text-gray-700">Tác giả</label>
                    <input name="authors" type="text" class="w-full p-2 border" value="${book.authors}" />
                </div>
                <div class="mb-4">
                    <label class="block text-gray-700">Mô tả</label>
                    <input name="description" type="text" class="w-full p-2 border" value="${book.description}" />
                </div>
                
                <input type="hidden" name="bookStatus" value="${bookStatus}" disabled>
			    <div class="mb-4">
			        <label class="block text-gray-700">Loại sách</label>
			        <select name="status" class="w-full p-2 border">
			            <option value="1">Cộng đồng (có thể mượn)</option>
			            <option value="0">Nội bộ (không thể mượn)</option>
			        </select>
			    </div>
			    
                <div class="mb-4">
                    <label class="block text-gray-700">Số lượng có sẵn</label>
                    <input name="availableQuantity" type="number" class="w-full p-2 border" value="${book.availableQuantity}" />
                </div>

                <!-- <div class="mb-4">
                    <label class="block text-gray-700">URL hình ảnh</label>
                    <input name="imgUrl" type="text" class="w-full p-2 border" value="${book.imgUrl}" />
                </div> -->
                <input type="hidden" name="buildIdGenres" value="${builtGenres}" disabled>
	            <div class="mb-4">
	                <label class="block text-gray-700">Thể loại (Giữ Ctrl + Click để chọn nhiều)</label>
	                <select class="w-full p-2 border border-gray-300" name="genres" multiple>
	                    <c:forEach var="genre" items="${allGenres}">
	                        <option value="${genre.genreId}">${genre.genreName}</option>
	                    </c:forEach>
	                </select>
	            </div>
                <div class="flex justify-end">
                    <button type="button" class="bg-gray-500 text-white px-4 py-2 rounded mr-2" onclick="closeModal()">
                        Hủy
                    </button>
                    <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded">
                        Lưu
                    </button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function openModal() {
            document.getElementById("editModal").classList.remove("hidden");
        }
        function closeModal() {
            document.getElementById("editModal").classList.add("hidden");
        }
        
     	// Select the "Loại sách" dropdown and set the selected option
        const bookStatus = document.querySelector("input[name='bookStatus']").value;
        const statusSelect = document.querySelector("select[name='status']");
        if (statusSelect) {
            statusSelect.value = bookStatus;
        }

        // Select the "Thể loại" multiple dropdown and set selected options
        const builtGenres = document.querySelector("input[name='buildIdGenres']").value.split(",");
        const genreSelect = document.querySelector("select[name='genres']");
        
        if (genreSelect) {
            for (let option of genreSelect.options) {
                if (builtGenres.includes(option.textContent.trim())) {
                    option.selected = true;
                }
            }
        }

    </script>
</body>
</html>
