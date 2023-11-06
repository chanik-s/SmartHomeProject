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


1.로그인화면
 ![image](https://github.com/chanik-s/SmartHomeProject/assets/78005321/8b55d0b4-3902-41fb-b001-61eea5f14b14)


2.메인화면 
   ![image](https://github.com/chanik-s/SmartHomeProject/assets/78005321/7db8a549-0c5c-4539-bb78-844fa7c1bc08)



3. 센서 버튼 눌렀을경우(센서값 데이터 리스트, 각 센서마다 동일)
   ![image](https://github.com/chanik-s/SmartHomeProject/assets/78005321/6580de0e-fbf2-48b7-bf49-73bbb07f8f49)

4. 센서 그래프 정보(각 10개씩 시간대순으로 정렬,NEXT/PREV로 이동가능) 
   ![image](https://github.com/chanik-s/SmartHomeProject/assets/78005321/c8cad645-0ad1-44c5-bfda-50255ab06d7d)

5. 기기 제어
   앱 상에는 ON /OFF 버튼만 있다.
   ON을 누를시 라즈베리파이 웹서버에 명령을 내려 python파일을 실행시킨다.
   WIFI모듈이 달린 아두이노에 연결된 LED(모터가 될 수 있음)를 원격으로 제어 가능(ON/OFF)

6. 홈 캠 기능(내부)
  mjpg-streamer를 이용하여 웹뷰로 홈캠 스트리밍
  ![image](https://github.com/chanik-s/SmartHomeProject/assets/78005321/c1b70c03-812b-40d1-8b57-f622bb052472)


7. 로그인 위치(파이어베이스를 통한 실시간 위치정보(위도,경도) 저장 및 구글맵에 마커로 표시)
   (보안성 강화)
   ![image](https://github.com/chanik-s/SmartHomeProject/assets/78005321/964af260-4b0b-44b4-8ad0-cacbfd3a3c11)

8. 얼굴인식
   (보안성 강화)
   신원 미상자 5초 유지시 Firebase 스토리지에 이미지 저장 및 앱에서 확인 가능
   ![image](https://github.com/chanik-s/SmartHomeProject/assets/78005321/9b46b3f9-9c14-4ee6-86d9-4a63dc4e69d3)





