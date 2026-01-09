# Tính Năng & Luồng Hoạt Động Ứng Dụng LexiGo

**Dự án:** LexiGo - Ứng dụng học tiếng Anh toàn diện

**Ngày cập nhật:** 25/11/2025

## 1. Bối Cảnh & Giải Pháp Thực Tiễn (Context & Practical Solution)

Phần này phân tích lý do hình thành sản phẩm và giá trị cốt lõi mà LexiGo mang lại cho người dùng so với các giải pháp hiện có trên thị trường.

### 1.1. Thực trạng & Vấn đề (Pain Points)

Trong quá trình học tiếng Anh, người học hiện nay thường đối mặt với các rào cản sau:

- **Sự phân mảnh công cụ:** Người học thường phải cài đặt nhiều ứng dụng khác nhau (một app để tra từ điển, một app để học ngữ pháp, một app khác để luyện nghe), gây bất tiện và khó theo dõi lộ trình tổng thể.
- **Phương pháp học thụ động:** Nhiều ứng dụng cũ chỉ cung cấp bài tập trắc nghiệm khô khan, thiếu sự tương tác hai chiều, khiến người học nhanh chán và không áp dụng được vào thực tế.
- **Thiếu môi trường luyện tập:** Việc tìm kiếm người bản xứ hoặc gia sư để luyện giao tiếp (Speaking/Chat) thường tốn kém và khó sắp xếp thời gian.
- **Khó duy trì động lực:** Không có công cụ thống kê trực quan để người học nhìn thấy sự tiến bộ của mình mỗi ngày.

### 1.2. Giải pháp từ LexiGo (Our Solution)

LexiGo giải quyết các vấn đề trên thông qua một nền tảng di động "All-in-One":

- **Hệ sinh thái toàn diện:** Tích hợp chặt chẽ 4 kỹ năng (Nghe, Đọc, Từ vựng, Ngữ pháp) và Từ điển vào một ứng dụng duy nhất, giúp người học tiết kiệm thời gian và dung lượng bộ nhớ.
- **Ứng dụng Trí tuệ nhân tạo (AI):** Tính năng Chat AI đóng vai trò như một "Gia sư ảo" 24/7, cho phép người dùng hỏi đáp ngữ pháp, luyện hội thoại tự nhiên và nhận phản hồi tức thì.
- **Cá nhân hóa trải nghiệm:** Hệ thống tự động lưu trữ tiến độ và đưa ra thống kê chi tiết, giúp người học tự điều chỉnh lộ trình phù hợp với năng lực bản thân.
- **Tiếp cận dễ dàng:** Là ứng dụng di động (Mobile App), LexiGo cho phép học mọi lúc, mọi nơi, tận dụng các khoảng thời gian "chết" trong ngày để nâng cao trình độ.

## 2. Tổng Quan Công Nghệ (General Technology)

Ứng dụng được xây dựng dựa trên các tiêu chuẩn công nghệ di động hiện đại, đảm bảo hiệu năng và trải nghiệm người dùng mượt mà:

- **Kiến trúc hệ thống:** Mô hình Client-Server. Ứng dụng mobile (Client) giao tiếp với Máy chủ (Server) thông qua chuẩn RESTful API để trao đổi dữ liệu.
- **Xử lý đa phương tiện (Multimedia):** Tích hợp trình phát âm thanh (Audio Player) tối ưu hóa cho việc streaming và phát các file nghe offline.
- **Trí tuệ nhân tạo (AI Integration):** Tích hợp API Chatbot thông minh để xử lý ngôn ngữ tự nhiên, đóng vai trò trợ lý học tập ảo.
- **Lưu trữ cục bộ (Local Storage):** Sử dụng cơ chế lưu trữ bảo mật để quản lý Token đăng nhập (Session management) và caching dữ liệu bài học để tăng tốc độ tải.
- **Giao diện người dùng (UI/UX):** Thiết kế theo hướng hiện đại, tập trung vào trải nghiệm học tập (Learning Experience), hỗ trợ tương tác chạm, vuốt và phản hồi tức thì.

## 3. Chi Tiết Tính Năng & Luồng Hoạt Động

Dưới đây là mô tả chi tiết quy trình tương tác của người dùng với các chức năng cốt lõi.

### 3.1. Xác Thực & Tài Khoản (Authentication & Account)

### A. Đăng nhập & Đăng ký

- **Mục đích:** Quản lý định danh người dùng và đồng bộ dữ liệu học tập.
- **Luồng hoạt động:**
    1. Người dùng mở ứng dụng, màn hình chào mừng hiển thị.
    2. **Đăng nhập:**
        - Người dùng nhập Email và Mật khẩu.
        - Hệ thống gọi API xác thực.
        - *Thành công:* Lưu mã xác thực (token) an toàn, tải thông tin người dùng và chuyển vào màn hình chính.
        - *Thất bại:* Hiển thị thông báo lỗi cụ thể (sai mật khẩu, tài khoản không tồn tại).
    3. **Đăng ký:**
        - Người dùng nhập thông tin: Họ tên, Email, Mật khẩu, Xác nhận mật khẩu.
        - Hệ thống kiểm tra tính hợp lệ và gọi API tạo tài khoản.
        - *Thành công:* Thông báo tạo tài khoản hoàn tất và yêu cầu đăng nhập.

### B. Quản lý mật khẩu

- **Quên mật khẩu:** Người dùng nhập email đăng ký, hệ thống gửi hướng dẫn khôi phục qua email.
- **Đổi mật khẩu:** Trong phần cài đặt tài khoản, người dùng nhập mật khẩu cũ để xác thực và thiết lập mật khẩu mới.

### C. Hồ sơ người dùng (User Profile)

- **Luồng hoạt động:**
    1. Từ màn hình chính, truy cập khu vực cá nhân.
    2. Hệ thống hiển thị: Ảnh đại diện, Thông tin cơ bản, Cấp độ hiện tại và Biểu đồ thống kê tiến độ.
    3. **Cập nhật:** Người dùng chỉnh sửa thông tin cá nhân và lưu lại.
    4. **Thống kê:** Xem chi tiết lịch sử học tập, điểm số trung bình qua các kỹ năng.
    5. **Đăng xuất:** Xóa dữ liệu phiên làm việc hiện tại và trở về màn hình xác thực.

### 3.2. Các Chức Năng Học Tập (Learning Features)

### A. Từ Vựng (Vocabulary)

- **Luồng hoạt động:**
    1. **Chọn chủ đề:** Hệ thống hiển thị danh sách các nhóm từ vựng theo chủ đề (Ví dụ: Du lịch, Công sở).
    2. **Học từ:** Hiển thị thẻ học (Flashcard) bao gồm từ vựng, nghĩa, phiên âm, và ví dụ.
    3. **Kiểm tra (Quiz):**
        - Người dùng bắt đầu bài kiểm tra trắc nghiệm cho chủ đề đã chọn.
        - Hệ thống tải bộ câu hỏi ngẫu nhiên.
        - Người dùng chọn đáp án -> Hệ thống chấm điểm ngay lập tức (Real-time feedback).
        - Kết thúc: Hiển thị tổng điểm và cập nhật % hoàn thành chủ đề.

### B. Ngữ Pháp (Grammar)

- **Luồng hoạt động:**
    1. **Danh sách bài học:** Hiển thị lộ trình ngữ pháp từ cơ bản đến nâng cao.
    2. **Học lý thuyết:** Màn hình chi tiết cung cấp công thức, quy tắc, ví dụ minh họa và mẹo ghi nhớ (Tips).
    3. **Luyện tập:**
        - Người dùng kích hoạt bài tập thực hành.
        - Hệ thống tải các dạng bài: Trắc nghiệm, Điền từ vào chỗ trống, Đúng/Sai.
        - Sau khi nộp bài, hệ thống hiển thị giải thích chi tiết cho từng câu.

### C. Luyện Nghe (Listening)

- **Luồng hoạt động:**
    1. **Phân loại:** Người dùng chọn cấp độ phù hợp (Dễ / Trung bình / Khó).
    2. **Thực hiện bài nghe:**
        - Hệ thống chọn ngẫu nhiên bài nghe từ kho dữ liệu server.
        - Giao diện hiển thị trình phát Audio và đoạn văn bản bị khuyết từ (Script with blanks).
    3. **Tương tác:**
        - Người dùng nghe audio và điền từ còn thiếu.
        - Nhấn "Kiểm tra" để đối chiếu kết quả.
        - Hệ thống tự động chuyển sang câu tiếp theo hoặc hiển thị đáp án đúng nếu người dùng trả lời sai.

### D. Luyện Đọc (Reading)

- **Luồng hoạt động:**
    1. **Thư viện bài đọc:** Danh sách các bài đọc được phân trang để tối ưu hiệu năng.
    2. **Đọc hiểu:** Giao diện hiển thị văn bản đầy đủ, hỗ trợ cuộn mượt mà.
    3. **Kiểm tra đọc hiểu:**
        - Người dùng trả lời bộ câu hỏi trắc nghiệm liên quan đến nội dung văn bản.
        - Hỗ trợ điều hướng câu hỏi (Câu trước/Câu sau).
        - Nộp bài và nhận kết quả đánh giá mức độ hiểu văn bản.

### 3.3. Công Cụ Hỗ Trợ (Tools)

### A. Chat AI (Trợ lý ảo)

- **Công nghệ:** Sử dụng API Trí tuệ nhân tạo (AI/LLM).
- **Luồng hoạt động:**
    1. Người dùng khởi tạo hội thoại.
    2. Nhập câu hỏi (về ngữ pháp, từ vựng) hoặc luyện tập giao tiếp.
    3. Hệ thống gửi dữ liệu đến AI Server và trả về phản hồi tự nhiên.
    4. Hỗ trợ lưu trữ lịch sử chat cục bộ để xem lại.

### B. Từ điển (Dictionary)

- **Luồng hoạt động:**
    1. Người dùng nhập từ khóa vào thanh tìm kiếm.
    2. Hệ thống truy vấn dữ liệu từ API từ điển.
    3. **Hiển thị kết quả đa dạng:**
        - Nghĩa Tiếng Việt & Tiếng Anh.
        - Phiên âm chuẩn quốc tế (IPA).
        - Ví dụ ngữ cảnh.
        - Từ đồng nghĩa/trái nghĩa.
    4. **Audio:** Hỗ trợ phát âm từ vựng (chuẩn Anh-Anh/Anh-Mỹ).

### 3.4. Giao Diện Chính (Dashboard)

- **Chức năng trung tâm:**
    - Hiển thị tổng quan tiến độ học tập hàng ngày (Daily Goal).
    - Menu điều hướng trực quan dạng danh sách hoặc lưới (Grid) để truy cập nhanh các kỹ năng (Nghe, Nói, Đọc, Viết).
    - Lối tắt truy cập nhanh vào Hồ sơ cá nhân và Chat AI.