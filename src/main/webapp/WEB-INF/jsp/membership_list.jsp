<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html lang="vi">
<head>
<meta charset="UTF-8">
<title>Thư viện ABC - Danh sách thẻ thành viên</title>
<script src="https://cdn.tailwindcss.com"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"
    rel="stylesheet" />
</head>
<body class="bg-gray-200">
    <jsp:include page="header.jsp" />

    <!-- Main Content -->
    <main class="p-4">
        <!-- Form tìm kiếm -->
        <div class="bg-gray-300 rounded">
            <div class="flex items-center px-5 py-2 border border-info rounded-t">
                <h2 class="text-lg font-bold">Tìm kiếm thẻ thành viên</h2>
            </div>
            <div class="flex items-center p-5 border border-info rounded-b">
                <form action="membership-card-list" method="GET" class="flex w-full">
                    <input class="flex-grow p-2 rounded bg-white" placeholder="Nhập mã thẻ thành viên" 
                           type="text" name="query" value="${query}" />
                    <button type="submit" class="bg-blue-600 text-white ml-1 p-3 rounded">
                        <i class="fas fa-search"></i>
                    </button>
                </form>
            </div>
        </div>

        <!-- Hiển thị kết quả tìm kiếm nếu có -->
        <c:if test="${not empty query}">
            <span class="block mt-4">Kết quả tìm kiếm cho: <b>${query}</b></span>
        </c:if>

        <!-- Phân trang -->
        <div class="flex items-center justify-center mb-4 gap-1">
            <!-- Nút "Trang trước" -->
            <c:if test="${currentPage > 1}">
                <a href="${pageContext.request.contextPath}/membership-card-list?currentPage=${currentPage - 1}&query=${query}"
                   class="p-2 bg-gray-100 rounded-full w-10 h-10 flex justify-center items-center">
                    <i class="fas fa-arrow-left"></i>
                </a>
            </c:if>

            <!-- Các số trang -->
            <c:forEach var="i" begin="1" end="${totalPages}">
                <a href="${pageContext.request.contextPath}/membership-card-list?currentPage=${i}&query=${query}"
                   class="p-2 rounded-full w-10 h-10 flex justify-center items-center
                  ${currentPage == i ? 'bg-blue-500 text-white' : 'bg-gray-100'}">
                    ${i}
                </a>
            </c:forEach>

            <!-- Nút "Trang sau" -->
            <c:if test="${currentPage < totalPages}">
                <a href="${pageContext.request.contextPath}/membership-card-list?currentPage=${currentPage + 1}&query=${query}"
                   class="p-2 bg-gray-100 rounded-full w-10 h-10 flex justify-center items-center">
                    <i class="fas fa-arrow-right"></i>
                </a>
            </c:if>
        </div>

        <!-- Bảng danh sách thẻ thành viên -->
        <div class="bg-white rounded-lg shadow overflow-hidden">
            <table class="min-w-full">
                <thead class="bg-gray-100">
                    <tr>
                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Mã thẻ thành viên
                        </th>
                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Trạng thái
                        </th>
                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Lịch sử
                        </th>
                    </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                    <c:forEach var="card" items="${cards}">
                        <tr>
                            <td class="px-6 py-4 whitespace-nowrap">
                                <div class="text-sm font-medium text-gray-900">${card.membershipCard}</div>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap">
                                <form method="POST" action="${pageContext.request.contextPath}/toggle-membership-status">
                                    <input value="${card.membershipCard}" name="membershipCard" hidden/>
                                    <button title="Đổi trạng thái?"
                                            class="px-3 py-1 rounded text-sm font-semibold
                                        ${card.prohibited == 1 ? 'bg-red-100 text-red-800 hover:bg-red-200' : 'bg-green-100 text-green-800 hover:bg-green-200'}">
                                        ${card.prohibited == 1 ? 'Bị cấm' : 'Hoạt động'}
                                    </button>
                                </form>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap">
                                <button onclick="showHistory('${card.membershipCard}')"
                                        class="px-3 py-1 rounded text-sm font-semibold bg-blue-100 text-blue-800 hover:bg-blue-200">
                                    Xem lịch sử
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </main>

    <!-- History Modal -->
    <div id="historyModal" class="fixed inset-0 bg-black bg-opacity-50 hidden flex items-center justify-center">
        <div class="bg-white rounded-lg w-3/4 max-h-[80vh] flex flex-col">
            <!-- Modal Header -->
            <div class="flex justify-between items-center p-4 border-b">
                <h3 class="text-lg font-semibold">Lịch sử mượn sách</h3>
                <button onclick="closeHistoryModal()" class="text-gray-500 hover:text-gray-700">
                    <i class="fas fa-times"></i>
                </button>
            </div>
            
            <!-- Modal Body with Scroll -->
            <div class="overflow-y-auto p-4">
                <table class="min-w-full">
                    <thead class="bg-gray-50">
                        <tr>
                            <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">ID</th>
                            <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Thời gian mượn</th>
                            <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Thời gian trả</th>
                            <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Sách</th>
                            <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Số lượng</th>
                        </tr>
                    </thead>
                    <tbody id="historyTableBody">
                        <!-- Will be populated by JavaScript -->
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <script>
        function showHistory(membershipCard) {
            fetch(`${pageContext.request.contextPath}/get-borrowing-history?membershipCard=` + membershipCard)
                .then(response => response.json())
                .then(response => {
                    if (response.data.length === 0) {
                        alert('Không có lịch sử mượn sách của thẻ này!');
                        return;
                    }

                    const tbody = document.getElementById('historyTableBody');
                    tbody.innerHTML = '';

                    response.data.forEach(request => {
                        request.books.forEach((bookRequest, index) => {
                            const row = document.createElement('tr');
                            row.className = 'border-t';

                            if (index === 0) {
                                row.innerHTML =
                                    '<td class="px-4 py-2" rowspan="' + request.books.length + '">' + request.borrowingRequestId + '</td>' +
                                    '<td class="px-4 py-2" rowspan="' + request.books.length + '">' + request.borrowingTime + '</td>' +
                                    '<td class="px-4 py-2" rowspan="' + request.books.length + '">' +
                                        (request.returnedTime ? request.returnedTime : 'Chưa trả') +
                                    '</td>';
                            }

                            row.innerHTML += '<td class="px-4 py-2">' + bookRequest.bookName + '</td>'
                                + '<td class="px-4 py-2">' + bookRequest.borrowedQuantity + '</td>';

                            tbody.appendChild(row);
                        });
                    });

                    document.getElementById('historyModal').classList.remove('hidden');
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Có lỗi xảy ra khi tải lịch sử');
                });
        }

        function closeHistoryModal() {
            document.getElementById('historyModal').classList.add('hidden');
        }

        // Close modal when clicking outside
        document.getElementById('historyModal').addEventListener('click', function(e) {
            if (e.target === this) {
                closeHistoryModal();
            }
        });
    </script>
</body>
</html>
