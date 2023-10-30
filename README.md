# SmartHomeProject
------------------------------------------------------------

졸업작품) 스마트홈 iot 어플리케이션(앱) 프로젝트

------------------------------------------------------------

created by 충북대학교 정보통신공학부 정찬익, 한만준

S/W - 안드로이드 스튜디오, Firebase, MariaDB, Apache, OpenCV

H/W - 아두이노Uno, 아두이노WemosD1-mini, 라즈베리파이

------------------------------------------------------------
□ 개발 목적
- 기존 스마트홈 앱 대비 편의 기능, 보안 기능 추가해보고자 함
- 라즈베리파이와 안드로이드를 활용한 SW, HW 구동 원리 이해 및 통신 프로토콜을 이용한 프로그램 개발 역량 증진 목적

□ 작품내용
- (라즈베리파이) 측정한 미세먼지 및 온습도 센서 데이터를 라즈베리파이의 MariaDB를 통해 데이터베이스에 저장하고 Apache 웹 서버에 json 포맷으로 저장함
- (라즈베리파이) 파이 카메라를 이용하여  홈 캠 스트리밍이 가능함
- (라즈베리파이) opencv를 이용하여 미확인 사용자 정보 수집 및 파이어베이스 스토리지에 이미지 파일 전송
- (라즈베리파이) 기기제어를 위한 중앙 허브 기능 수행
- (안드로이드) 웹뷰를 통한 홈 캠 스트리밍, 실시간 센서 데이터 표시, 센서 데이터값 그래프로 가시화, 얼굴인식 기능, 기기 제어 기능 등 여러 가지 기능 포함
- (안드로이드) 보안성 강화를 위해 로그인 기록을 파이어베이스 실시간 데이터베이스에 저장하고 구글맵에 로그인 위치 마커로 표시


![전체시스템구조도](https://github.com/chanik-s/SmartHomeProject/assets/78005321/8203ad18-62cf-4cb0-9957-f1e8af06de96)




시리얼 통신을 통해 얻어진 온습도/미세먼지 센서값을 라즈베리파이상에 MariaDB를 통해 DB화시킨다. +측정 시간 정보 포함

![mariadb데이터베이스구조](https://github.com/chanik-s/SmartHomeProject/assets/78005321/1c1e1568-d8c7-48fd-b17e-933d2b7fbb1a)


DB화된 센서값 데이터들을 Apache 웹 서버에 json포맷으로 모두 나타낸다. 
이는 안드로이드 앱과의 통신을 위한 것이며 앱에서는 json 파싱을 통해 데이터를 수집한다.

![connect_php내용물for앱과의통신](https://github.com/chanik-s/SmartHomeProject/assets/78005321/805ce96b-cb98-42eb-bef8-da55e8e1e3a3)










--안드로이드 앱

1.![image](https://github.com/chanik-s/SmartHomeProject/assets/78005321/fe865560-ee82-42df-81ea-56fee0c462ce)


2.![image](https://github.com/chanik-s/SmartHomeProject/assets/78005321/0c3c3045-8f17-4394-b707-d67eb11305e6)

3. ![image](https://github.com/chanik-s/SmartHomeProject/assets/78005321/2e77b253-009d-4039-82f2-1251b2f4553b)

4. ![image](https://github.com/chanik-s/SmartHomeProject/assets/78005321/21be653b-ff69-44b7-a635-05da9221d26f)


5.![image](https://github.com/chanik-s/SmartHomeProject/assets/78005321/deb78f4d-ae81-4615-ab72-e464800472bd)




