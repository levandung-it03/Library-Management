# NEEDED JOBS #
- Cần thử nghiệm: fake "null" value bằng cách bỏ DOM.element khỏi form.

# TESTCASES #
## 1. Weird request in server ##
- JSESSIONID không hợp lệ (gửi bởi IP Address khác -> Có khả năng bị đánh cắp)
- database đột nhiên bị ngắt: nhảy sang trang lỗi

## 2. Requests from Client ##
### Mượn sách(12/14) ###
```session```
- phiên đăng nhập hết hạn

```membershipCard```
- null/empty
- chưa trả sách -> không thể mượn
- mã thẻ không tồn tại(*)
- mã thẻ đã bị cấm thao tác(*)
- độ dài phải là 6

```borrowedBooks```
- null/empty
- ```borrowedBooks``` là một danh sách null (NullPointerExc).
- ```borrowedBooks``` chứa dữ liệu "<bookId>_<số lượng>" format không hợp lệ (NumberFormatExc).
- ```borrowedBooks``` chứa dữ liệu "<bookId>_<số lượng>" với bookId không tồn tại trong database.
- ```borrowedBooks``` chứa dữ liệu "<bookId>_<số lượng>" với bookId là sách nội bộ (fake bằng JS code bị chọc phá).
- ```borrowedBooks``` chứa dữ liệu "<bookId>_<số lượng>" với "số lượng" vượt quá số lượng sách trong database.
---
### Trả sách(6/7) ###
```session```
- phiên đăng nhập hết hạn

```membershipCard```
- null/empty
- mã thẻ không nợ sách
- mã thẻ không tồn tại(*)
- mã thẻ đã bị cấm thao tác(*)
- độ dài phải là 6
---
### Thêm sách(11/15) ###
```session```
- phiên đăng nhập hết hạn

```bookName```
- null/empty
- book_name bị trùng

```authors```
- null/empty

```availableQuantity```
- null
- string (need number)
- phải >= 1

```description```
- null/empty

```list<number> genres```
- null/empty
- ```list<string>``` (required ```list<number>```)
- ```list<id>``` không hợp lệ (không tồn tại, null)
---
### Cập nhật sách(15/19) ###
```session```
- phiên đăng nhập hết hạn

```bookId```
- null
- string (required number)
- id không tồn tại
- sách đang được mượn nên không thể cập nhật

```bookName```
- null/empty
- book_name được sửa trùng với tên của 1 cuốn khác.

```authors```
- null/empty

```availableQuantity```
- null
- string (need number)
- phải >= 1

```description```
- null/empty

```list<number> genres```
- null/empty
- ```list<string>``` (required ```list<number>```)
- ```list<id>``` không hợp lệ (không tồn tại, null)
