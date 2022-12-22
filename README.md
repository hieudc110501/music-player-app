## Giới Thiệu
App nghe nhạc có thể nghe nhạc online hoặc offline. Người nghe vào app sẽ hiện ra trang chủ có tất cả các bài hát, chọn bài hát nghe sau đó ấn nút play để nghe, có thể tìm kiếm bài hát theo tên. Muốn thêm bài hát vào danh sách yêu thích thì click vào hình trái tim thì bài hát đó sẽ xuất hiện ở màn hình bài hát yêu thích. Có thể tải bài hát về máy sau đó bài hát sẽ xuất hiện ở màn hình offline.

## Công nghệ sử dụng
- Java, Firebase, Room

## Demo
- Trang chủ có 3 Fragment: Online, Offline, Favorite:

![image](https://user-images.githubusercontent.com/63246022/209189671-34c4d75d-c12e-4e38-b605-1700b2beafc1.png)


- Fragment Online: 
  + Load tất cả các bài hát từ firebase xuống
  + Click vào bài hát để nghe 
  + Click vào hình trái tim để lưu bài hát yêu thích
  + Click vào 3 dấu chấm để download bài hát về máy 
  
![image](https://user-images.githubusercontent.com/63246022/209189859-98c73a49-2757-427e-b0a2-6af2ae439d33.png)


- Fragment Offline: 
  + Xem các bài hát đã download về máy
  + Chức năng tương tự Fragment Online
  
![image](https://user-images.githubusercontent.com/63246022/209190149-b2da727a-17dc-442d-9c56-f4135fbe064a.png)


- Fragment Favorite:
  + Lưu trữ những bài hát yêu thích, có thể bỏ yêu thích bằng cách click hình trái tim.
  + Chức năng tương tự Fragment Online
 
![image](https://user-images.githubusercontent.com/63246022/209190245-379d4459-5225-42ad-955d-55caa17b9517.png)


- Thanh Search: 
  + Khi nhập vào kí tự thì hiện bài hát chứa kí tự 
  + Khi thanh search trống thì lại trả về danh sách ban đầu

![image](https://user-images.githubusercontent.com/63246022/209190527-1c187c21-890d-457e-b061-5b4cf58a47f2.png)


- Fragment Player: 
  + Hiện tên bài hát, ca sĩ, ảnh, lời bài hát, …
  + Click nút Play để nghe bài hát, Pause để dừng
  + Click nút trái tim để thêm bài hát yêu thích
  + Điều chỉnh bài hát bằng thanh SeekBar
  + Click nút Next để chuyển bài hát tiếp theo
  + Click nút Previous để quay lại bài hát trước
  + Click nút Repeat để nghe lại bài hát hiện tại
  + Click nút Back quay lại trang chủ

![image](https://user-images.githubusercontent.com/63246022/209190652-4f0d3d09-7075-4e94-9dce-1674de1f0412.png)


- Thanh Notification trên App: 
  + Hiển thị bài hát đang chạy
  + Cho phép dừng, tiếp tục, next, previous bài hát
  
![image](https://user-images.githubusercontent.com/63246022/209190742-6e3d0575-cd17-4ccf-8970-61ee70f09434.png)


- Thanh Notification trên điện thoại: 
  + Khi thoát app hoặc dùng app thì bài hát đang bật vẫn chạy, sẽ có thanh notification trên thiết bị đang dùng có thể dừng, tiếp tục, next, previous bài hát, hoặc tắt bài hát.

![image](https://user-images.githubusercontent.com/63246022/209190797-ba451da3-217e-4581-835a-3c0465c69b66.png)

