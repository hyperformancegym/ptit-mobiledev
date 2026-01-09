# Các Tính Năng Cốt Lõi Của Ứng Dụng LexiGo

Tài liệu này liệt kê các tính năng cốt lõi hiện có trong ứng dụng LexiGo và mô tả chi tiết luồng hoạt động của từng tính năng.

## 1. Xác Thực & Tài Khoản (Authentication & Account)

### A. Đăng nhập & Đăng ký (Login & Register)
*   **Màn hình:** `AuthActivity` (chứa `LoginFragment` và `RegisterFragment`).
*   **Luồng hoạt động:**
    1.  Người dùng mở ứng dụng, màn hình `AuthActivity` hiển thị.
    2.  **Đăng nhập:**
        *   Người dùng nhập Email và Mật khẩu tại tab "Đăng nhập".
        *   Nhấn nút "Đăng nhập".
        *   Hệ thống gọi API đăng nhập.
        *   Nếu thành công: Lưu token và thông tin user, chuyển hướng đến `HomeActivity`.
        *   Nếu thất bại: Hiển thị thông báo lỗi.
    3.  **Đăng ký:**
        *   Người dùng chuyển sang tab "Đăng ký".
        *   Nhập Họ tên, Email, Mật khẩu, Xác nhận mật khẩu.
        *   Nhấn nút "Đăng ký".
        *   Hệ thống gọi API đăng ký.
        *   Nếu thành công: Hiển thị thông báo và yêu cầu người dùng đăng nhập lại.

### B. Quản lý mật khẩu
*   **Quên mật khẩu:** Từ màn hình Đăng nhập -> `ForgotPasswordActivity`. Nhập email để nhận hướng dẫn.
*   **Đổi mật khẩu:** Từ màn hình Profile -> `ChangePasswordActivity`. Nhập mật khẩu cũ và mới để thay đổi.

### C. Hồ sơ người dùng (User Profile)
*   **Màn hình:** `ProfileActivity`.
*   **Luồng hoạt động:**
    1.  Từ `HomeActivity`, nhấn nút "Tài khoản".
    2.  Hiển thị thông tin: Avatar, Tên, Email, Cấp độ (Beginner/Intermediate/Advanced), Thống kê tổng quan (số bài đã học).
    3.  **Cập nhật thông tin:** Nhấn "Cập nhật thông tin" -> `UpdateProfileActivity`.
    4.  **Thống kê chi tiết:** Nhấn "Thống kê chi tiết" -> `DetailedStatisticsActivity` (xem biểu đồ, lịch sử học tập).
    5.  **Đăng xuất:** Nhấn "Đăng xuất" -> Xóa dữ liệu phiên làm việc -> Trở về `AuthActivity`.

## 2. Các Chức Năng Học Tập (Learning Features)

### A. Từ Vựng (Vocabulary)
*   **Luồng hoạt động:**
    1.  **Chọn chủ đề:** Từ `HomeActivity` -> `VocabTopicsActivity`. Hiển thị danh sách các chủ đề từ vựng.
    2.  **Học từ vựng:** Chọn một chủ đề -> `VocabLessonsActivity`. Hiển thị danh sách các bài học/từ vựng trong chủ đề đó.
    3.  **Làm bài kiểm tra (Quiz):**
        *   Từ `VocabTopicsActivity`, nhấn icon "Quiz" của một chủ đề.
        *   Mở `VocabQuizActivity`.
        *   Hệ thống tải danh sách câu hỏi trắc nghiệm.
        *   Người dùng chọn đáp án cho từng câu hỏi.
        *   Hệ thống kiểm tra đúng/sai ngay lập tức và cộng điểm.
        *   **Kết thúc:** Hiển thị popup kết quả (Số câu đúng, Điểm số) và cập nhật tiến độ học tập (`ProgressTracker`).

### B. Ngữ Pháp (Grammar)
*   **Luồng hoạt động:**
    1.  **Danh sách bài học:** Từ `HomeActivity` -> `GrammarLessonsActivity`. Hiển thị danh sách bài học ngữ pháp.
    2.  **Chi tiết bài học:** Chọn một bài học -> `GrammarLessonDetailActivity`.
        *   Hiển thị: Tiêu đề, Mô tả, Nội dung lý thuyết, Quy tắc, Ví dụ, Mẹo ghi nhớ.
    3.  **Làm bài tập:**
        *   Từ màn hình chi tiết, nhấn "Start Exercise".
        *   Mở `QuizActivity` (Unified Quiz).
        *   Hệ thống tải bài tập ngữ pháp (Trắc nghiệm, Điền từ, Đúng/Sai).
        *   Người dùng làm bài và nộp.
        *   **Kết thúc:** Hiển thị kết quả chi tiết và cập nhật tiến độ.

### C. Luyện Nghe (Listening)
*   **Luồng hoạt động:**
    1.  **Chọn cấp độ:** Từ `HomeActivity` -> `LevelSelectionActivity`. Người dùng chọn cấp độ: Beginner, Intermediate, hoặc Advanced.
    2.  **Làm bài tập nghe:** Chuyển đến `ListeningExerciseActivity`.
        *   Hệ thống tải danh sách bài nghe (Script) theo cấp độ.
        *   Trộn ngẫu nhiên và chọn ra 10 bài.
        *   **Giao diện:** Hiển thị đoạn văn bản có chỗ trống (Script with blank).
        *   **Thao tác:**
            *   Nhấn "Phát" để nghe audio.
            *   Nhập từ còn thiếu vào ô trống.
            *   Nhấn "Kiểm tra" (Submit).
        *   Hệ thống phản hồi Đúng/Sai và hiển thị đáp án đúng.
        *   Chuyển sang câu tiếp theo.
        *   **Kết thúc:** Hiển thị tổng kết điểm số và cập nhật tiến độ.

### D. Luyện Đọc (Reading)
*   **Luồng hoạt động:**
    1.  **Danh sách bài đọc:** Từ `HomeActivity` -> `ReadingPassagesActivity`. Hiển thị danh sách bài đọc (có phân trang).
    2.  **Đọc bài:** Chọn một bài -> `ReadingPassageDetailActivity`. Hiển thị nội dung bài đọc đầy đủ.
    3.  **Làm bài kiểm tra:**
        *   Nhấn "Start Quiz" -> `ReadingQuizActivity`.
        *   Hệ thống tải danh sách câu hỏi liên quan đến bài đọc.
        *   Người dùng trả lời lần lượt các câu hỏi trắc nghiệm.
        *   Có thể quay lại câu trước (Previous) hoặc đi tiếp (Next).
        *   Nhấn "Nộp bài" (Submit) khi hoàn thành.
        *   **Kết thúc:** Hiển thị kết quả chi tiết (Câu đúng/sai, đáp án) và cập nhật tiến độ.

## 3. Công Cụ Hỗ Trợ (Tools)

### A. Chat AI
*   **Màn hình:** `ChatActivity`.
*   **Luồng hoạt động:**
    1.  Từ `HomeActivity`, nhấn nút Floating Action Button (icon Chat).
    2.  Hệ thống khởi tạo phiên chat mới với AI.
    3.  Người dùng nhập tin nhắn và nhấn Gửi.
    4.  Hệ thống gửi tin nhắn đến API và hiển thị phản hồi từ AI.
    5.  Hỗ trợ lịch sử chat và tự động xử lý khi có phiên chat cũ chưa kết thúc.

### B. Từ điển (Dictionary)
*   **Màn hình:** `DictionaryActivity`.
*   **Luồng hoạt động:**
    1.  Từ `HomeActivity`, chọn mục "Tra cứu từ điển".
    2.  Nhập từ cần tra vào ô tìm kiếm.
    3.  Nhấn nút Tìm kiếm hoặc Enter.
    4.  Hệ thống gọi API tra từ.
    5.  **Hiển thị kết quả:**
        *   Từ vựng, Phiên âm, Loại từ.
        *   Nghĩa tiếng Việt.
        *   Định nghĩa chi tiết (tiếng Anh).
        *   Ví dụ minh họa.
        *   Từ đồng nghĩa.
    6.  **Phát âm:** Nhấn icon loa để nghe phát âm (nếu có audio).

## 4. Giao Diện Chính (Core UI)
*   **Màn hình:** `HomeActivity`.
*   **Chức năng:**
    *   Hiển thị tiến độ học tập tổng quan (Số bài đã học, Mục tiêu ngày).
    *   Menu danh sách các chức năng học tập (RecyclerView).
    *   Điều hướng nhanh đến Profile và Chat AI.
