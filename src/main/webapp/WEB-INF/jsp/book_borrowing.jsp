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
	
	<div class="flex flex-col md:flex-row">
		<!-- Main Content -->
		<main class="p-4 md:w-3/4">
			<!-- Form tìm kiếm -->
			<div class="bg-gray-300 rounded">
				<div class="flex items-center px-5 py-2 border border-info rounded-t">
					<h2 class="text-lg font-bold">Tìm kiếm</h2>
				</div>
				<div class="flex items-center p-5 border border-info rounded-b">
					<form action="borrowing-book"
						method="GET" class="flex w-full">
						<input class="flex-grow p-2 rounded bg-white book-borrowing_search-box"
							   placeholder="Nội dung tìm kiếm" type="text" name="query" />
						<button type="submit" class="bg-blue-600 text-white ml-1 p-3 rounded book-borrowing_search-submit-btn">
							<i class="fas fa-search"></i>
						</button>
					</form>
				</div>
			</div>
			
			<c:if test="${not empty query}">
				<span style="display:block">Kết quả tìm kiếm cho: <b>${query}</b></span>
			</c:if>
			<!-- Hiển thị kết quả tìm kiếm -->
			<div class="flex items-center justify-center mb-4 gap-1">
				<!-- Nút "Trang trước" -->
				<c:if test="${currentPage > 1}">
					<a href="${pageContext.request.contextPath}/borrowing-book?currentPage=${currentPage - 1}&query=${query}"
						class="pagination-link p-2 bg-gray-100 rounded-full w-10 h-10 flex justify-center items-center">
						<i class="fas fa-arrow-left"></i>
					</a>
				</c:if>
			
				<!-- Hiển thị các số trang -->
				<c:forEach var="i" begin="1" end="${totalPages}">
					<a href="${pageContext.request.contextPath}/borrowing-book?currentPage=${i}&query=${query}"
					   class="pagination-link p-2 rounded-full w-10 h-10 flex justify-center items-center 
							  <c:if test='${currentPage == i}'> bg-blue-500 text-white </c:if> bg-gray-100'">
						${i}
					</a>
				</c:forEach>
	
			
				<!-- Nút "Trang sau" -->
				<c:if test="${currentPage < totalPages}">
					<a href="${pageContext.request.contextPath}/borrowing-book?currentPage=${currentPage + 1}&query=${query}"
						class="pagination-link p-2 bg-gray-100 rounded-full w-10 h-10 flex justify-center items-center">
						<i class="fas fa-arrow-right"></i>
					</a>
				</c:if>
			</div>

			<%-- Danh sách sách --%>
			<div id="bookList" class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
				<c:forEach var="book" items="${books}">
					<div class="block bg-white p-4 rounded shadow book-borrowing_book-item"
                        data-book-id="${book.bookId}"
                        data-book-name="${book.bookName}"
                        data-max-quantity="${book.availableQuantity}">
						<div class="flex justify-center">
							<img alt="Bìa sách ${book.bookName}" class="mb-4" height="200"
								src="https://www.win-rar.com/fileadmin/images/winrar-archive.png" width="150" />
						</div>
						<a class="block text-blue-700 mb-2 text-justify font-bold book-borrowing_book-item_book-name"
							href="detail-book?id=${book.bookId}" title="${book.bookName}">
							${book.bookName}
						</a>
						<p class="text-gray-700">Tác giả: ${book.authors}</p>
						
						<!-- Phần mượn sách (ẩn mặc định) -->
						<div class="borrow-controls hidden mt-3 border-t pt-2">
							<div class="flex items-center justify-between">
								<span>Số lượng mượn:</span>
								<div class="flex items-center">
									<button type="button" class="book-borrowing_book-item_decrease-book-btn bg-gray-200 px-2 rounded-l"
											onclick="decreaseQuantity('${book.bookId}')">-</button>
									<span class="quantity-display px-2 border-t border-b">0/${book.availableQuantity}</span>
									<button type="button" class="book-borrowing_book-item_increase-book-btn bg-gray-200 px-2 rounded-r"
											onclick="increaseQuantity('${book.bookId}')">+</button>
								</div>
							</div>
						</div>
					</div>
				</c:forEach>
			</div>
		</main>
		
		<!-- Right Column for Borrow/Return -->
		<div class="p-4 md:w-1/4">
			<div class="bg-white rounded shadow p-4 sticky top-4">
				<h2 class="text-xl font-bold mb-4 text-center">Quản lý mượn/trả</h2>
				
				<!-- Card Code Input -->
				<div class="mb-4">
					<label class="block text-gray-700 mb-2">Mã thẻ thư viện</label>
					<input id="membershipCardInput" class="w-full p-2 border border-gray-300 rounded" type="text" placeholder="Nhập mã thẻ (6 ký tự)" />
				</div>
				
				<!-- Action Buttons -->
				<div class="flex space-x-2 mb-4">
					<button id="borrowBtn" class="bg-blue-600 text-white py-2 px-4 rounded flex-1">Mượn</button>
					<button type="submit" id="returnBtn" class="bg-green-600 text-white py-2 px-4 rounded flex-1">Trả</button>
				</div>
				
				<!-- Selected Books Section -->
				<div id="selectedBooksSection" class="hidden">
					<h3 class="font-bold border-b pb-2 mb-2">Sách đã chọn</h3>
					<div id="selectedBooksList" class="max-h-60 overflow-y-auto mb-4">
						<!-- Selected books will be displayed here -->
						<p class="text-gray-500 italic">Chưa có sách nào được chọn</p>
					</div>
					
					<form id="borrowForm" action="create-borrowing-request" method="POST">
						<input type="hidden" id="membershipCardHidden" name="membershipCard" value="" />
	                	<select name="borrowedBooks" multiple hidden></select>
						<button type="button" id="cancelBorrowBtn" class="w-full bg-red-600 text-white py-2 px-4 rounded mb-2"
							onclick="cancelBorrowing()">Huỷ mượn sách</button>
						<button type="submit" class="w-full bg-blue-600 text-white py-2 px-4 rounded book-borrowing_create-borrowing-req-btn">
							Xác nhận mượn sách
						</button>
					</form>
				</div>
				
				<!-- Borrowed Books Section -->
				<div id="borrowedBooksSection" class="hidden">
					<h3 class="font-bold border-b pb-2 mb-2">Sách đã mượn</h3>
					<div id="borrowedBooksList" class="max-h-60 overflow-y-auto mb-4">
						<!-- Borrowed books will be displayed here -->
						<c:if test="${empty serializedBorrowingBooks}">
							<p class="text-gray-500 italic">Không có sách nào được mượn</p>
						</c:if>
					</div>
					
					<form id="returnForm" action="<%=request.getContextPath()%>/return-books" method="POST">
						<span id="borrowedBooksToReturn" style="display: none">${serializedBorrowingBooks}</span>
						<input type="text" id="returnMembershipCardHidden" name="membershipCard" value="" style="display: none"/>
						<button type="submit" class="w-full bg-green-600 text-white py-2 px-4 rounded book-borrowing_create-returning-req-btn">
							Xác nhận trả sách
						</button>
					</form>
				</div>
			</div>
		</div>
	</div>

	<script>
		// Borrow/Return functionality
		let selectedBooks = {};
		let borrowMode = false;
		let currentMembershipCard = "";
		
		// LocalStorage keys
		const STORAGE_SELECTED_BOOKS = "library_selected_books";
		const STORAGE_BORROW_MODE = "library_borrow_mode";
		const STORAGE_MEMBERSHIP_CARD = "library_membership_card";

		function clearBorrowingBooksDataInLocalStorage() {
			// Clear localStorage
			localStorage.removeItem(STORAGE_SELECTED_BOOKS);
			localStorage.removeItem(STORAGE_BORROW_MODE);
			localStorage.removeItem(STORAGE_MEMBERSHIP_CARD);
		}
		
		// Save data to localStorage
		function saveToLocalStorage() {
			localStorage.setItem(STORAGE_SELECTED_BOOKS, JSON.stringify(selectedBooks));
			localStorage.setItem(STORAGE_BORROW_MODE, JSON.stringify(borrowMode));
			localStorage.setItem(STORAGE_MEMBERSHIP_CARD, currentMembershipCard);
		}
		
		// Load data from localStorage
		function loadFromLocalStorage() {
			const storedBooks = localStorage.getItem(STORAGE_SELECTED_BOOKS);
			const storedBorrowMode = localStorage.getItem(STORAGE_BORROW_MODE);
			const storedMembershipCard = localStorage.getItem(STORAGE_MEMBERSHIP_CARD);
			
			if (storedBooks) {
				selectedBooks = JSON.parse(storedBooks);
			}
			
			if (storedBorrowMode) {
				borrowMode = JSON.parse(storedBorrowMode);
			}
			
			if (storedMembershipCard) {
				currentMembershipCard = storedMembershipCard;
				document.getElementById("membershipCardInput").value = storedMembershipCard;
			}
		}

		function getUrlParameters() {
			const extratedUrl = window.location.href.split("?");
			if (extratedUrl.length === 1)	return {};

			const result = {};
			const parameters = extratedUrl[1].split("&");
			parameters.forEach(parameterValuePair => {
				const extractedParameter = parameterValuePair.split("=");
				result[extractedParameter[0]] = extractedParameter[1];
			});
			return result;
		}
		
		// Load data from controller when return-books is activated
		function returnBooksPreparation() {
			const urlParameters = getUrlParameters();
			if (urlParameters.returningMembershipCard !== undefined) {
				// Clear data if the previous borrowing request is in progress (not done yet)
				clearBorrowingBooksDataInLocalStorage();

				// Pay-back card-code for input
				document.getElementById("membershipCardInput").value = urlParameters.returningMembershipCard;
				
				// Put borrowed-books to return
				const serializedBooks = document.getElementById("borrowedBooksToReturn").textContent;

				// Show borrowed books section
				const borrowedBooksSection = document.getElementById("borrowedBooksSection");
				borrowedBooksSection.classList.remove("hidden");
				const selectedBooksList = borrowedBooksSection.querySelector("div#borrowedBooksList");
				
				try {
					if (serializedBooks && serializedBooks.trim() !== '') {
						const booksData = JSON.parse(serializedBooks);
						if (Array.isArray(booksData) && booksData.length > 0) {
				booksData.forEach(bookDataObj => {
					const bookItem = document.createElement("div");
					bookItem.className = "py-2 border-b";
					bookItem.innerHTML = `<p>` + bookDataObj.bookName + ` - SL: ` + bookDataObj.quantity + `</p>`;
					selectedBooksList.appendChild(bookItem);
				});
						} else {
							selectedBooksList.innerHTML = '<p class="text-gray-500 italic">Không có sách nào được mượn</p>';
						}
					} else {
						selectedBooksList.innerHTML = '<p class="text-gray-500 italic">Không có sách nào được mượn</p>';
					}
				} catch (error) {
					console.error('Error parsing borrowed books data:', error);
					selectedBooksList.innerHTML = '<p class="text-gray-500 italic">Không có sách nào được mượn</p>';
				}

				// Put card-code into hidden block to make returning-request
				document.getElementById("returnMembershipCardHidden").value = urlParameters.returningMembershipCard;
			}
		}

		// Initialize the page
		function initializePage() {
			// Load saved data
			returnBooksPreparation();
			loadFromLocalStorage();
			
			// If we were in borrow mode, restore it
			if (borrowMode) {
				// Show borrow controls and retrieve books's quantity.
				document.querySelectorAll("div#bookList div.book-borrowing_book-item").forEach(bookTag => {
					// Show book controls
					bookTag.querySelector("div.borrow-controls").classList.remove("hidden");
					
					// Retore selected books's quantity
					storedQuantity = selectedBooks[bookTag.getAttribute("data-book-id")];
					if (storedQuantity) {
						const displayedQuantityElement = bookTag.querySelector("span.quantity-display");
						displayedQuantityElement.innerText = "" + storedQuantity.quantity + "/" + displayedQuantityElement.textContent.split("/")[1];
					}
				});
				
				// Show selected books section
				document.getElementById("selectedBooksSection").classList.remove("hidden");
				document.getElementById("borrowedBooksSection").classList.add("hidden");
				
				// Set card code in hidden field
				console.log(currentMembershipCard)
				document.getElementById("membershipCardHidden").value = currentMembershipCard;
				
				// Disable borrow button
				document.querySelector("button#borrowBtn").disabled = true;
			}
			
			// Update the selected books list
			updateSelectedBooksList();
			
			// Add event listeners to pagination links
			document.querySelectorAll(".pagination-link").forEach(link => {
				link.addEventListener("click", function(e) {
					// Save current state before navigating
					saveToLocalStorage();
				});
			});
		}
		
		// Clear localStorage and reset UI when canceling borrowing
		function cancelBorrowing() {
			clearBorrowingBooksDataInLocalStorage();

			// Reset variables
			selectedBooks = {};
			borrowMode = false;
			currentMembershipCard = "";
			
			// Refresh borrow controls
			document.querySelectorAll("div#bookList div.book-borrowing_book-item").forEach(bookTag => {
				bookTag.querySelector("div.borrow-controls").classList.add("hidden");

				const displayedQuantity = bookTag.querySelector(".quantity-display");
				displayedQuantity.innerText = "0/" + displayedQuantity.textContent.split("/")[1];
			});

			// Set card code in hidden field
			document.getElementById("membershipCardInput").value = "";

			// Hide selected books section
			const selectedBooksSection = document.getElementById("selectedBooksSection");
			selectedBooksSection.classList.add("hidden");
			selectedBooksSection.innerHTML = `<p class="text-gray-500 italic">Không có sách nào được mượn</p>`;

			// Enable borrow button
			document.querySelector("button#borrowBtn").disabled = false;
		}
		
		// Borrow button click
		document.getElementById("borrowBtn").addEventListener("click", () => {
            document.querySelector("button#borrowBtn").disabled = true;
			const membershipCard = document.getElementById("membershipCardInput").value.trim();
			if (!membershipCard) {
				alert("Vui lòng nhập mã thẻ thư viện");
				return;
			}
			
			// Set borrow mode and card code
			borrowMode = true;
			currentMembershipCard = membershipCard;
			
			// Show borrow controls for each book
			document.querySelectorAll(".borrow-controls").forEach(control => {
				control.classList.remove("hidden");
			});
			//Inquiring
			// Show selected books section
			document.getElementById("selectedBooksSection").classList.remove("hidden");
			document.getElementById("borrowedBooksSection").classList.add("hidden");
			
			// Set card code in hidden field
			document.getElementById("membershipCardHidden").value = membershipCard;
			
			// Reset selected books if card code changed
			if (membershipCard !== currentMembershipCard) {
				Object.keys(selectedBooks).forEach(key => delete selectedBooks[key]);
				updateSelectedBooksList();
			}
			
			// Save to localStorage
			saveToLocalStorage();
		});
		
		// Return button click
		document.getElementById("returnBtn").addEventListener("click", (e) => {
			const membershipCard = document.getElementById("membershipCardInput").value.trim();
			if (!membershipCard) {
				alert("Vui lòng nhập mã thẻ thư viện");
				return;
			}
			// Create form
			const form = document.createElement("form");
			form.id = "returnBookFormOfReturnBtn";
			form.action = "borrowing-book";
			form.method = "GET";

			// Create hidden input field
			const input = document.createElement("input");
			input.name = "returningMembershipCard";
			input.value = membershipCard;

			form.appendChild(input);
			document.body.appendChild(form); // Append form to body
			form.submit(); // Submit form
		});
		
		// Increase quantity function
		function increaseQuantity(bookId) {
			const bookElement = document.querySelector(`.book-borrowing_book-item[data-book-id="` + bookId + `"]`);
			const maxQuantity = parseInt(bookElement.getAttribute('data-max-quantity'));
			const quantityElement = bookElement.querySelector('span.quantity-display');
			const currentValue = parseInt(quantityElement.textContent.split('/')[0]);
			
			if (currentValue < maxQuantity) {
				const newValue = currentValue + 1;
				quantityElement.innerText = "" + newValue + "/" + maxQuantity;
				
				// Update selected books
				selectedBooks[bookId] = {
					quantity: newValue,
					name: bookElement.getAttribute('data-book-name')
				};
				
				// Save to localStorage
				saveToLocalStorage();
				updateSelectedBooksList();
			}
		}
		
		// Decrease quantity function
		function decreaseQuantity(bookId) {
			const bookElement = document.querySelector(`.book-borrowing_book-item[data-book-id="` + bookId + `"]`);
			const maxQuantity = parseInt(bookElement.getAttribute('data-max-quantity'));
			const quantityElement = bookElement.querySelector('span.quantity-display');
			const currentValue = parseInt(quantityElement.textContent.split('/')[0]);
			
			
			if (currentValue > 0) {
				const newValue = currentValue - 1;
				quantityElement.innerText = "" + newValue + "/" + maxQuantity;
				
				// Update selected books
				if (newValue === 0)
					delete selectedBooks[bookId];
				else
					selectedBooks[bookId] = {
						quantity: newValue,
						name: bookElement.getAttribute('data-book-name')
					};
				
				// Save to localStorage
				saveToLocalStorage();
				
				updateSelectedBooksList();
			}
		}
		
		// Update selected books list
		function updateSelectedBooksList(option) {
			const selectedBooksList = document.getElementById("selectedBooksList");
			const selectedBooksData = document.getElementById("selectedBooksData");
			
			// Clear current list
			selectedBooksList.innerHTML = "";
			
			// Create data object for form submission
			const formData = {};
			
			// Add each selected book to the list
			const selectedBooksCount = Object.keys(selectedBooks).length;
			if (selectedBooksCount > 0) {
				Object.keys(selectedBooks).forEach(bookId => {
					const book = selectedBooks[bookId];
					if (book.quantity > 0) {
						const bookItem = document.createElement("div");
						bookItem.className = "py-2 border-b";
						bookItem.innerHTML = `<p>` + book.name + ` - SL: ` + book.quantity + `</p>`;
						selectedBooksList.appendChild(bookItem);
						
						// Add to form data
						formData[bookId] = book.quantity;
					}
				});
			} else {
				// If no books selected, show message
				const emptyMessage = document.createElement("p");
				emptyMessage.className = "text-gray-500 italic";
				emptyMessage.textContent = "Chưa có sách nào được chọn";
				selectedBooksList.appendChild(emptyMessage);
			}
		}
		
		// Borrowing-books form submission handler
		document.getElementById("borrowForm").addEventListener("submit", function(e) {
			e.preventDefault();
			
			// Build input data to submit
			const hiddenMultiSelectTag = e.target.querySelector("select[name=borrowedBooks]");
			console.log(e.target);
			console.log(hiddenMultiSelectTag)
			console.log(Object.keys(selectedBooks).length)
			console.log(hiddenMultiSelectTag && Object.keys(selectedBooks).length !== 0)

			if (hiddenMultiSelectTag && Object.keys(selectedBooks).length !== 0) {
				hiddenMultiSelectTag.innerHTML = Object.entries(selectedBooks).reduce((acc, [key, value]) =>
					acc + '<option value=' + key + '_[separator]_' + value.quantity + ' selected></option>'
				,"");

				e.target.submit();
				cancelBorrowing();
			} else {
				alert("Không thể thực hiện thao tác trống");
			}
		});
		
		// Initialize the page when DOM is loaded
		document.addEventListener("DOMContentLoaded", initializePage);

		// Send request "return" with MembershipCard
		document.querySelector("form#returnForm").addEventListener("submit", e => {
			e.preventDefault();
			const form = e.target;
			form.querySelector("input#returnMembershipCardHidden").value
					= document.getElementById("membershipCardInput").value;
			form.submit();
		})
	</script>
</body>
</html>

