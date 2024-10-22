직접 사용하려고 만든 앱이에요.
지하철에서 유튜브 보다가 하차해야 하는 역을 놓치는 일이 종종 있어서 제작했어요.
내가 하차해야 할 역을 등록하면 역에 도착하기 전에 알려줘요.

[![googleplay](https://img.shields.io/badge/googleplay-%414141.svg?style=for-the-badge&logo=googleplay&logoColor=white)](https://play.google.com/store/apps/details?id=com.jm.metrostationalert)

----------------------------------
- Compose
- MVVM
- Hilt
- DataStore
- Navigation
- Coroutine
- location
- Room
- TedPermission
- Gson
- Retrofit
- Admob

----------------------------------

# 시연
#### 검색 화면

<img src = "https://github.com/user-attachments/assets/11db063a-230e-4c20-8658-f7ddfddc3fdb" width = "200">
<img src = "https://github.com/user-attachments/assets/7bec7fc3-253c-48a8-b3cf-942b5d8b0ed7" width = "200">
<img src = "https://github.com/user-attachments/assets/2c29375d-9f6a-4200-881c-61a59c97977a" width = "200">

- Spinner를 통해 노선을 선택할 수 있습니다.
- 검색을 통해 검색어를 포함한 지하철 역을 찾을 수 있습니다.
- 하차 알림을 받고 싶은 역을 선택합니다.

#### 즐겨찾기 화면
<img src = "https://github.com/user-attachments/assets/464968f4-17f4-44b8-a7b6-5a8364650be8" width = "200">
<img src = "https://github.com/user-attachments/assets/36e11c88-3d56-4eee-a675-b1893d1c2942" width = "200">

- 하차 알림을 받을 역과, 지하철 도착 정보를 받을 역을 볼 수 있습니다. 지하철 도착 정보를 받을 역은 여러개 등록 가능합니다.

#### 설정 화면
<img src = "https://github.com/user-attachments/assets/6821f6ee-ee0e-4952-b2f6-8fcc1eb6efe9" width = "200">
<img src = "https://github.com/user-attachments/assets/049d80c2-b7ac-4f43-9f78-6141cec3274e" width = "200">

- 알림을 받을 메시지의 제목과 내용을 따로 수정할 수 있습니다.
- 도착 몇 km 전에 알림을 받을지 Slider로 설정할 수 있습니다.

#### 알림 도착
<img src = "https://github.com/user-attachments/assets/61046e13-dc16-4fba-8171-fc8524c2ed44" width = "200">

- 백그라운드에서 현재 위치와 목적지까지의 거리를 수시로 계산하며, 내가 설정한 거리 안으로 들어왔을 경우 알림을 보냅니다.
